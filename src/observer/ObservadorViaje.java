package observer;

import model.Viaje;

/**
 * Interfaz del OBSERVER PATTERN para notificaciones de solicitudes de viaje.
 *
 * Define el contrato que deben implementar los observadores (Choferes).
 * Cuando un pasajero solicita un viaje, el Subject (NotificadorViajes)
 * llama a notificarSolicitud() en cada chofer suscripto.
 */
public interface ObservadorViaje {

    /**
     * Notifica al observador sobre una nueva solicitud de viaje.
     * @param viaje el viaje solicitado
     */
    void notificarSolicitud(Viaje viaje);
}
