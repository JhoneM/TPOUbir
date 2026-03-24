package payment;

/**
 * Interfaz del STRATEGY PATTERN para métodos de recarga de billetera.
 *
 * Define el contrato que deben cumplir todas las estrategias de pago.
 * Permite cambiar el método de recarga en tiempo de ejecución sin
 * modificar la clase Billetera (principio Open/Closed).
 *
 * Implementaciones concretas: RecargaTarjetaCredito, RecargaPayPal.
 */
public interface MetodoPago {

    /**
     * Ejecuta la recarga con el monto indicado.
     * @param monto monto a cobrar/procesar
     * @return true si la operación fue exitosa, false si falló
     */
    boolean recargar(double monto);
}
