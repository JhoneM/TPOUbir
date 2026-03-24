package payment;

import java.util.logging.Logger;

/**
 * Estrategia concreta del STRATEGY PATTERN: recarga mediante tarjeta de crédito.
 *
 * Simula el procesamiento del pago con tarjeta de crédito.
 * Al implementar MetodoPago, puede ser intercambiada por otra estrategia
 * sin modificar la clase Billetera.
 */
public class RecargaTarjetaCredito implements MetodoPago {

    private static final Logger logger = Logger.getLogger(RecargaTarjetaCredito.class.getName());

    private String numeroTarjeta;
    private String titular;

    public RecargaTarjetaCredito(String numeroTarjeta, String titular) {
        this.numeroTarjeta = numeroTarjeta;
        this.titular = titular;
    }

    /**
     * Simula el cobro a la tarjeta de crédito.
     */
    @Override
    public boolean recargar(double monto) {
        String tarjetaOculta = "**** **** **** " + numeroTarjeta.substring(Math.max(0, numeroTarjeta.length() - 4));
        logger.info("  [Tarjeta de Crédito] Procesando cobro de $" +
                String.format("%.2f", monto) + " a " + tarjetaOculta + " (Titular: " + titular + ")");
        logger.info("  [Tarjeta de Crédito] Pago aprobado.");
        return true;
    }
}
