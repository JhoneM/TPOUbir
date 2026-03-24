package service;

import model.Chofer;
import model.Pasajero;
import model.Viaje;
import observer.NotificadorViajes;
import java.util.List;
import java.util.logging.Logger;

/**
 * Servicio encargado del ciclo de vida de los viajes:
 * solicitud, aceptación, rechazo y finalización con cobro.
 */
public class ViajeService {

    private static final Logger logger = Logger.getLogger(ViajeService.class.getName());
    private static final double COMISION_PLATAFORMA = 0.15;

    private final UsuarioService usuarioService;

    public ViajeService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * El pasajero solicita un viaje.
     * El sistema busca choferes disponibles dentro del rango de distancia
     * y los notifica usando el Observer Pattern.
     */
    public Viaje solicitarViaje(Pasajero pasajero, String origen, String destino,
                                 double monto, double rangoKm) {
        logger.info("Buscando choferes en un radio de " + rangoKm + " km...");

        List<Chofer> choferes = usuarioService.getChoferes();
        NotificadorViajes notificadorRango = new NotificadorViajes();
        for (Chofer chofer : choferes) {
            if (chofer.isDisponible() && chofer.getUbicacionKm() <= rangoKm) {
                notificadorRango.suscribir(chofer);
            }
        }

        if (notificadorRango.cantidadSuscriptos() == 0) {
            logger.warning("No hay choferes disponibles en el rango de " + rangoKm + " km.");
            return null;
        }

        Viaje viaje = new Viaje(origen, destino, monto, pasajero);
        notificadorRango.notificarTodos(viaje);
        return viaje;
    }

    /**
     * El chofer acepta el viaje notificado.
     */
    public boolean aceptarViaje(Chofer chofer, Viaje viaje) {
        try {
            viaje.aceptar(chofer);
            chofer.marcarOcupado();
            logger.info(chofer.getNombre() + " aceptó el viaje.");
            return true;
        } catch (IllegalStateException e) {
            logger.warning("El viaje ya no está disponible: " + e.getMessage());
            return false;
        }
    }

    /**
     * El chofer rechaza el viaje notificado.
     * El rechazo es individual — el viaje sigue SOLICITADO para otros choferes.
     */
    public void rechazarViaje(Chofer chofer, Viaje viaje) {
        viaje.registrarRechazo(chofer);
        logger.info(chofer.getNombre() + " rechazó el viaje.");
    }

    /**
     * Finaliza el viaje: cobra al pasajero y acredita al chofer menos la comisión del 15%.
     */
    public boolean finalizarViaje(Viaje viaje) {
        double monto = viaje.getMonto();
        double comision = monto * COMISION_PLATAFORMA;
        double montoChofer = monto - comision;

        boolean pagado = viaje.getPasajero().pagarViaje(monto);
        if (!pagado) {
            logger.warning("El pasajero no tiene saldo suficiente para el viaje.");
            return false;
        }

        viaje.getChofer().cobrarViaje(montoChofer);
        viaje.getChofer().marcarDisponible();
        viaje.finalizar();

        logger.info("Viaje finalizado | Total: $" + String.format("%.2f", monto)
                + " | Comisión (15%%): $" + String.format("%.2f", comision)
                + " | Acreditado al chofer: $" + String.format("%.2f", montoChofer));
        return true;
    }
}
