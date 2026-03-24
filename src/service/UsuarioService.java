package service;

import model.Chofer;
import model.Pasajero;
import model.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Servicio encargado del registro y autenticación de usuarios.
 */
public class UsuarioService {

    private static final Logger logger = Logger.getLogger(UsuarioService.class.getName());

    private List<Usuario> usuarios;
    private List<Chofer> choferes;

    public UsuarioService() {
        this.usuarios = new ArrayList<>();
        this.choferes = new ArrayList<>();
    }

    /**
     * Registra un nuevo pasajero en el sistema.
     */
    public Pasajero registrarPasajero(String nombre, String email, String contrasenia) {
        if (buscarUsuarioPorEmail(email) != null) {
            logger.warning("Ya existe un usuario con el email " + email);
            return null;
        }
        Pasajero pasajero = new Pasajero(nombre, email, contrasenia);
        usuarios.add(pasajero);
        logger.info("Pasajero registrado: " + pasajero);
        return pasajero;
    }

    /**
     * Registra un nuevo chofer en el sistema.
     * La licencia de conducir es obligatoria.
     */
    public Chofer registrarChofer(String nombre, String email, String contrasenia,
                                   String licencia, double ubicacionKm) {
        if (licencia == null || licencia.trim().isEmpty()) {
            logger.warning("El chofer debe tener licencia de conducir válida.");
            return null;
        }
        if (buscarUsuarioPorEmail(email) != null) {
            logger.warning("Ya existe un usuario con el email " + email);
            return null;
        }
        Chofer chofer = new Chofer(nombre, email, contrasenia, licencia, ubicacionKm);
        usuarios.add(chofer);
        choferes.add(chofer);
        logger.info("Chofer registrado: " + chofer);
        return chofer;
    }

    public Usuario login(String email, String contrasenia) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        if (usuario != null && usuario.verificarContrasenia(contrasenia)) {
            logger.info("Login exitoso: " + usuario);
            return usuario;
        }
        logger.warning("Credenciales incorrectas para: " + email);
        return null;
    }

    List<Chofer> getChoferes() {
        return choferes;
    }

    Usuario buscarUsuarioPorEmail(String email) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) return u;
        }
        return null;
    }
}
