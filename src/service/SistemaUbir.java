package service;

import model.Chofer;
import model.Pasajero;
import model.Usuario;
import model.Viaje;

/**
 * Facade del sistema Ubir.
 *
 * Punto de entrada único que delega cada operación al servicio correspondiente:
 * - UsuarioService: registro y autenticación
 * - VehiculoService: gestión de vehículos
 * - ViajeService: ciclo de vida de los viajes
 *
 * Permite que los clientes (Main, UI) interactúen con el sistema
 * sin conocer la estructura interna de servicios.
 */
public class SistemaUbir {

    private final UsuarioService usuarioService;
    private final VehiculoService vehiculoService;
    private final ViajeService viajeService;

    public SistemaUbir() {
        this.usuarioService = new UsuarioService();
        this.vehiculoService = new VehiculoService();
        this.viajeService = new ViajeService(usuarioService);
    }

    public Pasajero registrarPasajero(String nombre, String email, String contrasenia) {
        return usuarioService.registrarPasajero(nombre, email, contrasenia);
    }

    public Chofer registrarChofer(String nombre, String email, String contrasenia,
                                   String licencia, double ubicacionKm) {
        return usuarioService.registrarChofer(nombre, email, contrasenia, licencia, ubicacionKm);
    }

    public Usuario login(String email, String contrasenia) {
        return usuarioService.login(email, contrasenia);
    }

    public boolean registrarVehiculo(Chofer chofer, String patente, String fechaVTV,
                                      String marca, String modelo) {
        return vehiculoService.registrarVehiculo(chofer, patente, fechaVTV, marca, modelo);
    }

    public Viaje solicitarViaje(Pasajero pasajero, String origen, String destino,
                                 double monto, double rangoKm) {
        return viajeService.solicitarViaje(pasajero, origen, destino, monto, rangoKm);
    }

    public boolean aceptarViaje(Chofer chofer, Viaje viaje) {
        return viajeService.aceptarViaje(chofer, viaje);
    }

    public void rechazarViaje(Chofer chofer, Viaje viaje) {
        viajeService.rechazarViaje(chofer, viaje);
    }

    public boolean finalizarViaje(Viaje viaje) {
        return viajeService.finalizarViaje(viaje);
    }
}
