package model;

import observer.ObservadorViaje;
import java.util.logging.Logger;

/**
 * Chofer: usuario que ofrece viajes en la plataforma Ubir.
 *
 * Implementa la interfaz ObservadorViaje como parte del OBSERVER PATTERN:
 * cuando un pasajero solicita un viaje, el sistema notifica a todos los
 * choferes disponibles en rango; cada chofer decide si acepta o no.
 *
 * Requiere licencia de conducir válida para registrarse.
 */
public class Chofer extends Usuario implements ObservadorViaje {

    private static final Logger logger = Logger.getLogger(Chofer.class.getName());

    private String licenciaConducir;
    private Vehiculo vehiculo;
    private double ubicacionKm;      // distancia en km desde un punto de referencia
    private boolean disponible;
    private Viaje viajePendiente;    // último viaje notificado (sin responder aún)

    public Chofer(String nombre, String email, String contrasenia, String licenciaConducir, double ubicacionKm) {
        super(nombre, email, contrasenia);
        this.licenciaConducir = licenciaConducir;
        this.ubicacionKm = ubicacionKm;
        this.disponible = true;
        this.vehiculo = null;
        this.viajePendiente = null;
    }

    /**
     * Recibe notificación de una nueva solicitud de viaje (Observer Pattern).
     * El chofer queda informado y puede aceptar o rechazar.
     */
    @Override
    public void notificarSolicitud(Viaje viaje) {
        this.viajePendiente = viaje;
        logger.info("  [Notificación] " + nombre + " recibió solicitud: " + viaje);
    }

    public String getLicenciaConducir() { return licenciaConducir; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public double getUbicacionKm() { return ubicacionKm; }
    public boolean isDisponible() { return disponible; }
    public Viaje getViajePendiente() { return viajePendiente; }

    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }

    public void marcarOcupado() {
        this.disponible = false;
    }

    public void marcarDisponible() {
        this.disponible = true;
    }

    @Override
    public String toString() {
        return "[Chofer] " + super.toString() + " | Licencia: " + licenciaConducir;
    }
}
