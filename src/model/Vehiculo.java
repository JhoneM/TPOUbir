package model;

/**
 * Vehículo registrado por un chofer en la plataforma Ubir.
 * Campos obligatorios: patente, fecha de VTV, marca y modelo.
 */
public class Vehiculo {

    private String patente;
    private String fechaVTV;
    private String marca;
    private String modelo;

    public Vehiculo(String patente, String fechaVTV, String marca, String modelo) {
        this.patente = patente;
        this.fechaVTV = fechaVTV;
        this.marca = marca;
        this.modelo = modelo;
    }

    public String getPatente() { return patente; }
    public String getFechaVTV() { return fechaVTV; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }

    @Override
    public String toString() {
        return marca + " " + modelo + " (Patente: " + patente + ", VTV: " + fechaVTV + ")";
    }
}
