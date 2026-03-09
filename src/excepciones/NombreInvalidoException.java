package excepciones;

/**
 * Se lanza cuando se intenta crear un objeto o personaje con nombre vacío o nulo.
 * Hereda de Exception para que Java la reconozca como un error y permita
 * usarla con throw y catch.
 */
public class NombreInvalidoException extends Exception {

    /**
     * @param mensaje descripción del error que se mostrará cuando se lance
     */
    // super(mensaje) pasa el mensaje a la clase padre (Exception), que es
    // quien sabe guardarlo y mostrarlo. Sin esta línea el mensaje se perdería.
    // → Se lanza en Personaje(constructor) y Objeto(constructor), se captura en Main y Controlador
    public NombreInvalidoException(String mensaje) {
        super(mensaje);
    }
}