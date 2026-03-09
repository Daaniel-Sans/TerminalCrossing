package personajes;

/**
 * Define los estados posibles de un personaje en el juego.
 * Se usa para saber en qué situación está el personaje en cada momento.
 * Es un enum porque los estados son fijos y no pueden ser otros.
 */
public enum EstadoPersonaje {
    ACTIVO,   // el personaje está despierto y puede actuar
    DORMIDO,  // el personaje está durmiendo
    AGOTADO,  // el personaje se ha quedado sin energía
    AUSENTE   // el personaje no está disponible
}