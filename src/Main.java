import model.*;
import payment.*;
import service.SistemaUbir;

/**
 * Demo del sistema Ubir.
 *
 * Demuestra el uso de los dos patrones de diseño implementados:
 *   - STRATEGY PATTERN: recarga de billetera con tarjeta de crédito y PayPal
 *   - OBSERVER PATTERN: notificación a choferes al solicitar un viaje
 *
 * Flujo completo:
 *   1. Registro de pasajero y dos choferes
 *   2. Recarga de billetera con tarjeta y con PayPal
 *   3. Registro de vehículos
 *   4. Solicitud de viaje (choferes son notificados automáticamente)
 *   5. Chofer 1 rechaza, Chofer 2 acepta
 *   6. Finalización con cobro y acreditación (menos 15% de comisión)
 *   7. Resumen de saldos
 */
public class Main {

    public static void main(String[] args) {

        SistemaUbir sistema = new SistemaUbir();

        separador("REGISTRO DE USUARIOS");

        Pasajero ana = sistema.registrarPasajero("Ana García", "ana@email.com", "pass123");
        Chofer carlos = sistema.registrarChofer("Carlos López", "carlos@email.com", "pass456",
                "LIC-001-ARG", 3.5);   // a 3.5 km del punto de referencia
        Chofer marta = sistema.registrarChofer("Marta Ruiz", "marta@email.com", "pass789",
                "LIC-002-ARG", 7.0);   // a 7.0 km

        separador("REGISTRO DE VEHÍCULOS");

        sistema.registrarVehiculo(carlos, "ABC123", "2025-06-30", "Toyota", "Corolla");
        sistema.registrarVehiculo(marta,  "XYZ789", "2025-12-15", "Honda",  "Civic");

        separador("RECARGA DE BILLETERA (Strategy Pattern)");

        System.out.println(">> Recargando $500 con Tarjeta de Crédito:");
        MetodoPago tarjeta = new RecargaTarjetaCredito("4111111111111111", "Ana García");
        ana.getBilletera().recargar(500.0, tarjeta);

        System.out.println();
        System.out.println(">> Recargando $300 con PayPal:");
        MetodoPago paypal = new RecargaPayPal("ana.garcia@paypal.com");
        ana.getBilletera().recargar(300.0, paypal);

        System.out.println("\nSaldo actual de Ana: " + ana.getBilletera());

        separador("SOLICITUD DE VIAJE (Observer Pattern)");

        System.out.println(">> Ana solicita viaje: Casa -> Aeropuerto | Rango: 5 km");
        Viaje viaje = sistema.solicitarViaje(ana, "Casa de Ana", "Aeropuerto", 150.0, 5.0);

        separador("CHOFER RECHAZA");

        if (viaje != null) {
            sistema.rechazarViaje(carlos, viaje);
        }

        separador("NUEVA SOLICITUD CON RANGO AMPLIADO");

        System.out.println(">> Ana solicita nuevamente con rango de 10 km:");
        Viaje viaje2 = sistema.solicitarViaje(ana, "Casa de Ana", "Aeropuerto", 150.0, 10.0);

        separador("CHOFER ACEPTA Y VIAJE FINALIZA");

        if (viaje2 != null) {
            // Marta acepta
            sistema.aceptarViaje(marta, viaje2);
            System.out.println();
            sistema.finalizarViaje(viaje2);
        }

        separador("SALDOS FINALES");

        System.out.println("Ana    → " + ana.getBilletera());
        System.out.println("Carlos → " + carlos.getBilletera());
        System.out.println("Marta  → " + marta.getBilletera());

        separador("FIN DE LA DEMO");
    }

    private static void separador(String titulo) {
        System.out.println("\n========== " + titulo + " ==========");
    }
}
