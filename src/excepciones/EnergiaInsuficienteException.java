package excepciones;

/**
 * Se lanza cuando un personaje intenta actuar sin energía suficiente.
 * Por ejemplo, cuando el jugador intenta moverse con energía 0.
 */
public class EnergiaInsuficienteException extends Exception {
    public EnergiaInsuficienteException(String mensaje) {
        super(mensaje);
    }
}