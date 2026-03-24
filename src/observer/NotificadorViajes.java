package observer;

import model.Viaje;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Subject del OBSERVER PATTERN para notificaciones de viajes.
 *
 * Mantiene una lista de choferes suscritos (observadores) y los notifica
 * cuando se produce una nueva solicitud de viaje.
 * Los choferes pueden suscribirse o desuscribirse en cualquier momento.
 */
public class NotificadorViajes {

    private static final Logger logger = Logger.getLogger(NotificadorViajes.class.getName());

    private List<ObservadorViaje> observadores;

    public NotificadorViajes() {
        this.observadores = new ArrayList<>();
    }

    /**
     * Suscribe un chofer para recibir notificaciones de viajes.
     */
    public void suscribir(ObservadorViaje observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    /**
     * Desuscribe a un chofer (ej: cuando no está disponible).
     */
    public void desuscribir(ObservadorViaje observador) {
        observadores.remove(observador);
    }

    /**
     * Notifica a todos los observadores suscritos sobre un nuevo viaje solicitado.
     * Este método es el corazón del Observer Pattern: el Subject dispara el evento.
     */
    public void notificarTodos(Viaje viaje) {
        logger.info("  [Notificador] Enviando solicitud a " + observadores.size() + " chofer(es) en rango...");
        for (ObservadorViaje obs : observadores) {
            obs.notificarSolicitud(viaje);
        }
    }

    public int cantidadSuscriptos() {
        return observadores.size();
    }
}
