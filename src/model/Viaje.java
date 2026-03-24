package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un viaje dentro del sistema Ubir.
 * Estados posibles: SOLICITADO, ACEPTADO, FINALIZADO.
 */
public class Viaje {

    private String origen;
    private String destino;
    private double monto;
    private EstadoViaje estado;
    private Pasajero pasajero;
    private Chofer chofer;
    private List<Chofer> chofersRechazantes = new ArrayList<>();

    public Viaje(String origen, String destino, double monto, Pasajero pasajero) {
        this.origen = origen;
        this.destino = destino;
        this.monto = monto;
        this.pasajero = pasajero;
        this.estado = EstadoViaje.SOLICITADO;
        this.chofer = null;
    }

    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public double getMonto() { return monto; }
    public EstadoViaje getEstado() { return estado; }
    public Pasajero getPasajero() { return pasajero; }
    public Chofer getChofer() { return chofer; }

    private void setEstado(EstadoViaje estado) { this.estado = estado; }

    public void aceptar(Chofer chofer) {
        if (estado != EstadoViaje.SOLICITADO) {
            throw new IllegalStateException("Solo se puede aceptar un viaje SOLICITADO. Estado actual: " + estado);
        }
        this.chofer = chofer;
        this.estado = EstadoViaje.ACEPTADO;
    }

    public void finalizar() {
        if (estado != EstadoViaje.ACEPTADO) {
            throw new IllegalStateException("Solo se puede finalizar un viaje ACEPTADO. Estado actual: " + estado);
        }
        this.estado = EstadoViaje.FINALIZADO;
    }

    public void registrarRechazo(Chofer chofer) {
        chofersRechazantes.add(chofer);
    }

    public boolean fueRechazadoPor(Chofer chofer) {
        return chofersRechazantes.contains(chofer);
    }

    @Override
    public String toString() {
        return "Viaje [" + origen + " -> " + destino + "] | Monto: $" +
               String.format("%.2f", monto) + " | Estado: " + estado;
    }
}
