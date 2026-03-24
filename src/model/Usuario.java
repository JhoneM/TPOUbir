package model;

/**
 * Clase base para todos los usuarios del sistema Ubir.
 * Pasajero y Chofer extienden esta clase.
 */
public abstract class Usuario {

    protected String nombre;
    protected String email;
    protected String contrasenia;
    protected Billetera billetera;

    public Usuario(String nombre, String email, String contrasenia) {
        this.nombre = nombre;
        this.email = email;
        this.contrasenia = contrasenia;
        this.billetera = new Billetera();
    }

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public Billetera getBilletera() { return billetera; }

    public boolean verificarContrasenia(String contrasenia) {
        return this.contrasenia.equals(contrasenia);
    }

    public boolean pagarViaje(double monto) {
        return billetera.debitar(monto);
    }

    public void cobrarViaje(double monto) {
        billetera.acreditar(monto);
    }

    @Override
    public String toString() {
        return nombre + " (" + email + ")";
    }
}
