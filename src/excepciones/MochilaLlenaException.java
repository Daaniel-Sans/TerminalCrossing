package excepciones;

/**
 * Se lanza cuando se intenta guardar un objeto en una mochila que ya está llena.
 */
public class MochilaLlenaException extends Exception {
    public MochilaLlenaException(String mensaje) {
        super(mensaje);
    }
}