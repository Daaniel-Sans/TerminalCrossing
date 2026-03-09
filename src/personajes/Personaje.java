package personajes;

import excepciones.EnergiaInsuficienteException;
import excepciones.NombreInvalidoException;
import java.util.Objects;

/**
 * Clase padre de todos los personajes del juego.
 * Es abstracta porque no tiene sentido crear un "Personaje genérico" —
 * en el juego solo existen jugadores (Pj) y NPCs (Npc).
 * Existe para compartir atributos y métodos comunes entre Pj y Npc.
 *
 * De ella heredan:
 * - {@code Pj} → el jugador que controla el usuario
 * - {@code Npc} → los personajes del pueblo que no se controlan
 */
public abstract class Personaje {

    protected String nombre;         // nombre del personaje
    protected String skin;           // emoji que representa al personaje en el mapa
    protected int x;                 // posición horizontal en el mapa
    protected int y;                 // posición vertical en el mapa
    protected int energiaActual;     // energía actual del personaje (0-100)
    protected double velocidad;      // velocidad de movimiento
    protected boolean estaActivo;    // true si el personaje puede actuar
    // EstadoPersonaje es un enum: ACTIVO, DORMIDO, AGOTADO o AUSENTE.
    // Se usa para saber en qué situación está el personaje en cada momento.
    protected EstadoPersonaje estado;

    /**
     * Constructor vacío. Crea un personaje genérico con valores por defecto.
     * Necesario para los tests y para las clases hijas que llamen a super().
     */
    public Personaje() {
        this.nombre = "Sin nombre";
        this.skin = "❓";
        this.x = 0;
        this.y = 0;
        this.energiaActual = 100;
        this.velocidad = 1.0;
        this.estaActivo = true;
        this.estado = EstadoPersonaje.ACTIVO;
    }

    /**
     * Constructor completo. Crea un personaje con todos sus datos.
     * La energía siempre empieza en 100 y el personaje siempre empieza activo.
     *
     * @param nombre    nombre del personaje, no puede estar vacío
     * @param skin      emoji que representa al personaje en el mapa
     * @param x         posición horizontal inicial en el mapa
     * @param y         posición vertical inicial en el mapa
     * @param velocidad velocidad de movimiento
     * @throws NombreInvalidoException si el nombre es nulo o está vacío
     */
    public Personaje(String nombre, String skin, int x, int y, double velocidad) throws NombreInvalidoException {
        // Validamos el nombre igual que en Objeto
        if (nombre == null || nombre.isBlank()) {
            throw new NombreInvalidoException("El nombre del personaje no puede estar vacío.");
        }
        this.nombre = nombre;
        this.skin = skin;
        this.x = x;
        this.y = y;
        this.energiaActual = 100;          // siempre empieza con energía máxima
        this.velocidad = velocidad;
        this.estaActivo = true;            // siempre empieza activo
        this.estado = EstadoPersonaje.ACTIVO;
    }

    // ── Getters y setters ──────────────────────────────────────────────────────

    public String getNombre()                { return nombre; }
    public void setNombre(String nombre)     { this.nombre = nombre; }
    public String getSkin()                  { return skin; }
    public void setSkin(String skin)         { this.skin = skin; }
    public int getX()                        { return x; }
    public void setX(int x)                 { this.x = x; }
    public int getY()                        { return y; }
    public void setY(int y)                 { this.y = y; }
    public int getEnergiaActual()            { return energiaActual; }
    public void setEnergiaActual(int e)      { this.energiaActual = e; }
    public double getVelocidad()             { return velocidad; }
    public void setVelocidad(double v)       { this.velocidad = v; }
    public boolean isEstaActivo()            { return estaActivo; }
    public void setEstaActivo(boolean b)     { this.estaActivo = b; }
    public EstadoPersonaje getEstado()       { return estado; }
    public void setEstado(EstadoPersonaje e) { this.estado = e; }

    // ── Métodos ────────────────────────────────────────────────────────────────

    /**
     * Resta energía al personaje. Si la energía llega a 0 lanza una excepción.
     * No devuelve nada (void) porque solo modifica el estado interno.
     * Es un método de la clase padre porque tanto Pj como Npc pueden agotarse.
     *
     * Hay dos comprobaciones:
     * - Si la energía llega a 0: lanza la excepción y cambia estado a AGOTADO
     * - Si la energía baja de 20: cambia estado a AGOTADO como aviso
     *
     * @param cantidad cantidad de energía a restar
     * @throws EnergiaInsuficienteException si la energía llega a 0
     */
    public void gastarEnergia(int cantidad) throws EnergiaInsuficienteException {
        if (this.energiaActual - cantidad <= 0) {
            this.energiaActual = 0;
            this.estado = EstadoPersonaje.AGOTADO;
            throw new EnergiaInsuficienteException("¡" + nombre + " se ha quedado sin energía!");
        }
        this.energiaActual -= cantidad;
        // Si la energía baja de 20 cambiamos el estado a AGOTADO como aviso
        if (this.energiaActual < 20) {
            this.estado = EstadoPersonaje.AGOTADO;
        }
    }

    /**
     * Devuelve una representación legible del personaje con sus datos principales.
     *
     * @return cadena con los datos del personaje
     */
    @Override
    public String toString() {
        return "Personaje{nombre='" + nombre + "', skin='" + skin +
                "', pos=(" + x + "," + y + "), energía=" + energiaActual +
                ", velocidad=" + velocidad + ", estado=" + estado + "}";
    }

    /**
     * Dos personajes son iguales si tienen el mismo nombre y posición.
     *
     * @param o objeto con el que comparar
     * @return true si son iguales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Personaje)) return false;
        Personaje p = (Personaje) o;
        return x == p.x && y == p.y && Objects.equals(nombre, p.nombre);
    }

    /**
     * Debe ser consistente con equals: personajes iguales tienen el mismo hashCode.
     *
     * @return código hash basado en nombre y posición
     */
    @Override
    public int hashCode() {
        return Objects.hash(nombre, x, y);
    }
}