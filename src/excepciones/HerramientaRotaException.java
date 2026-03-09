package excepciones;

/**
 * Se lanza cuando se intenta usar una herramienta con durabilidad 0.
 */
public class HerramientaRotaException extends Exception {
    public HerramientaRotaException(String mensaje) {
        super(mensaje);
    }
}