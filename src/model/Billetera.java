package model;

import payment.MetodoPago;
import java.util.logging.Logger;

/**
 * Billetera virtual del usuario.
 *
 * Utiliza el STRATEGY PATTERN para los métodos de recarga:
 * el método recargar() recibe un MetodoPago (estrategia) como parámetro,
 * delegando la lógica de cobro a la implementación concreta.
 * Esto permite agregar nuevos métodos de pago sin modificar esta clase.
 */
public class Billetera {

    private static final Logger logger = Logger.getLogger(Billetera.class.getName());

    private double saldo;

    public Billetera() {
        this.saldo = 0.0;
    }

    /**
     * Recarga la billetera usando el método de pago recibido (Strategy Pattern).
     * @param monto monto a recargar
     * @param metodo estrategia de pago concreta (tarjeta, PayPal, etc.)
     */
    public boolean recargar(double monto, MetodoPago metodo) {
        boolean exitoso = metodo.recargar(monto);
        if (exitoso) {
            saldo += monto;
            logger.info("  Recarga exitosa. Saldo actual: $" + String.format("%.2f", saldo));
        } else {
            logger.info("  Error al procesar el pago. Saldo sin cambios.");
        }
        return exitoso;
    }

    /**
     * Debita un monto de la billetera (pago de viaje).
     */
    public boolean debitar(double monto) {
        if (saldo >= monto) {
            saldo -= monto;
            return true;
        }
        logger.info("  Saldo insuficiente. Saldo: $" + String.format("%.2f", saldo));
        return false;
    }

    /**
     * Acredita un monto en la billetera (cobro de viaje al chofer).
     */
    public void acreditar(double monto) {
        saldo += monto;
    }

    public double getSaldo() { return saldo; }

    @Override
    public String toString() {
        return "Saldo: $" + String.format("%.2f", saldo);
    }
}
