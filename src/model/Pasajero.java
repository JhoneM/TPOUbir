package model;

/**
 * Pasajero: usuario que solicita viajes en la plataforma Ubir.
 */
public class Pasajero extends Usuario {

    public Pasajero(String nombre, String email, String contrasenia) {
        super(nombre, email, contrasenia);
    }

    @Override
    public String toString() {
        return "[Pasajero] " + super.toString();
    }
}
