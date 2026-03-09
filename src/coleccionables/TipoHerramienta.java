package coleccionables;

/**
 * Define los tipos posibles de herramienta en el juego.
 *
 * Usar un enum en vez de un String evita errores tipográficos y limita
 * los valores a los definidos aquí. Por ejemplo, es imposible escribir
 * {@code TipoHerramienta.REDD} por error — Java lo detectaría al compilar.
 *
 * Se usa en la clase {@code Herramienta} para definir qué tipo es cada una,
 * y en {@code Pj} para comprobar si el jugador lleva la herramienta correcta
 * antes de pescar o capturar insectos.
 */
public enum TipoHerramienta {
    RED,         // para capturar insectos
    CAÑA,        // para pescar
    PALA,        // para cavar
    DESCONOCIDO  // valor por defecto cuando no se especifica tipo
}