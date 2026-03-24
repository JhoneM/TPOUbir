package service;

import model.Chofer;
import model.Vehiculo;
import java.util.logging.Logger;

/**
 * Servicio encargado del registro de vehículos.
 */
public class VehiculoService {

    private static final Logger logger = Logger.getLogger(VehiculoService.class.getName());

    /**
     * Registra el vehículo del chofer. Todos los campos son obligatorios.
     */
    public boolean registrarVehiculo(Chofer chofer, String patente, String fechaVTV,
                                      String marca, String modelo) {
        if (patente == null || fechaVTV == null || marca == null || modelo == null) {
            logger.warning("Error: todos los datos del vehículo son obligatorios.");
            return false;
        }
        Vehiculo vehiculo = new Vehiculo(patente, fechaVTV, marca, modelo);
        chofer.setVehiculo(vehiculo);
        logger.info("Vehículo registrado para " + chofer.getNombre() + ": " + vehiculo);
        return true;
    }
}
