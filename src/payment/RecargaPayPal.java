package payment;

import java.util.logging.Logger;

/**
 * Estrategia concreta del STRATEGY PATTERN: recarga mediante PayPal.
 *
 * Simula el procesamiento del pago a través de PayPal.
 * Intercambiable con RecargaTarjetaCredito sin modificar la clase Billetera.
 */
public class RecargaPayPal implements MetodoPago {

    private static final Logger logger = Logger.getLogger(RecargaPayPal.class.getName());

    private String cuentaPayPal;

    public RecargaPayPal(String cuentaPayPal) {
        this.cuentaPayPal = cuentaPayPal;
    }

    /**
     * Simula el cobro a través de PayPal.
     */
    @Override
    public boolean recargar(double monto) {
        logger.info("  [PayPal] Procesando pago de $" +
                String.format("%.2f", monto) + " desde la cuenta: " + cuentaPayPal);
        logger.info("  [PayPal] Transacción completada exitosamente.");
        return true;
    }
}
