package personajes;

import coleccionables.Herramienta;
import coleccionables.Mochila;
import excepciones.EnergiaInsuficienteException;
import excepciones.NombreInvalidoException;
import java.util.Objects;

/**
 * Representa al jugador que controla el usuario.
 * Hereda de Personaje los atributos comunes (nombre, skin, x, y, energía, estado).
 *
 * Añade la herramienta en mano y la mochila equipada, que pueden ser null
 * si el jugador no lleva ninguna todavía.
 */
public class Pj extends Personaje {

    private Herramienta herramientaEnMano; // herramienta equipada, null si no lleva ninguna
    private Mochila mochilaEquipada;       // mochila equipada, null si no lleva ninguna
    private int baulesMoney;               // dinero del jugador en baules
    private double horasJugadas;           // tiempo total jugado

    // Energía máxima que puede tener el jugador
    private static final int ENERGIA_MAX = 100;

    /**
     * Constructor vacío. Crea un jugador genérico con valores por defecto.
     */
    public Pj() {
        super();
        this.herramientaEnMano = null;  // empieza sin herramienta
        this.mochilaEquipada = null;    // empieza sin mochila
        this.baulesMoney = 0;
        this.horasJugadas = 0.0;
    }

    /**
     * Constructor completo. Crea un jugador con nombre, skin y posición inicial.
     * La energía empieza en 80 en vez de 100 para que el juego tenga algo de desafío
     * desde el principio.
     *
     * @param nombre nombre del jugador, no puede estar vacío
     * @param skin   emoji que representa al jugador en el mapa
     * @param x      posición horizontal inicial en el mapa
     * @param y      posición vertical inicial en el mapa
     * @throws NombreInvalidoException si el nombre es nulo o está vacío
     */
    public Pj(String nombre, String skin, int x, int y) throws NombreInvalidoException {
        super(nombre, skin, x, y, 1.0); // velocidad 1.0 por defecto
        this.energiaActual = 80;        // empieza con 80 de energía, no 100
        this.herramientaEnMano = null;
        this.mochilaEquipada = null;
        this.baulesMoney = 0;
        this.horasJugadas = 0.0;
    }

    // ── Getters y setters ──────────────────────────────────────────────────────

    public Herramienta getHerramientaEnMano()        { return herramientaEnMano; }
    public void setHerramientaEnMano(Herramienta h)  { this.herramientaEnMano = h; }
    public Mochila getMochilaEquipada()              { return mochilaEquipada; }
    public void setMochilaEquipada(Mochila m)        { this.mochilaEquipada = m; }
    public int getBaulesMoney()                      { return baulesMoney; }
    public void setBaulesMoney(int b)                { this.baulesMoney = b; }
    public double getHorasJugadas()                  { return horasJugadas; }
    public void setHorasJugadas(double h)            { this.horasJugadas = h; }

    // ── Métodos ────────────────────────────────────────────────────────────────

    /**
     * El jugador come una fruta recuperando energía.
     * No puede superar la energía máxima (100).
     * Si estaba agotado y recupera más de 20 de energía vuelve a estar activo.
     * No devuelve nada (void) porque solo modifica el estado interno.
     *
     * @param cantidad energía que recupera la fruta
     */
    public void comer(int cantidad) {
        this.energiaActual += cantidad;
        // No puede superar el máximo
        if (this.energiaActual > ENERGIA_MAX) {
            this.energiaActual = ENERGIA_MAX;
        }
        // Si estaba agotado y ahora tiene más de 20 vuelve a estar activo
        if (this.estado == EstadoPersonaje.AGOTADO && this.energiaActual > 20) {
            this.estado = EstadoPersonaje.ACTIVO;
        }
    }

    /**
     * El jugador se mueve gastando energía.
     * Delega el gasto de energía al método gastarEnergia() del padre,
     * que es quien lanza la excepción si la energía llega a 0.
     * También suma tiempo jugado.
     * No devuelve nada (void) porque solo modifica el estado interno.
     *
     * @throws EnergiaInsuficienteException si la energía llega a 0
     */
    public void mover() throws EnergiaInsuficienteException {
        // Delegamos al padre — él sabe cómo gestionar la energía y lanzar la excepción
        gastarEnergia(2);
        this.horasJugadas += 0.01; // cada movimiento suma un poco de tiempo jugado
    }

    /**
     * Comprueba si el jugador lleva una mochila equipada.
     * Devuelve boolean porque el Controlador necesita saberlo antes de guardar objetos.
     *
     * @return true si lleva mochila equipada
     */
    public boolean tieneMochila() {
        return mochilaEquipada != null;
    }

    /**
     * Equipa una mochila al jugador.
     * No devuelve nada (void) porque solo asigna la mochila.
     *
     * @param m mochila a equipar
     */
    public void equiparMochila(Mochila m) {
        this.mochilaEquipada = m;
    }

    /**
     * Comprueba si el jugador lleva una herramienta en mano.
     * Devuelve boolean porque el Controlador necesita saberlo antes de usar herramientas.
     *
     * @return true si lleva herramienta en mano
     */
    public boolean tieneHerramienta() {
        return herramientaEnMano != null;
    }

    /**
     * Comprueba si el jugador lleva una herramienta de un tipo concreto.
     * Primero comprueba que lleve herramienta y luego delega en esDeTipo()
     * de Herramienta para comprobar el tipo.
     * Devuelve boolean porque el Controlador necesita saberlo antes de pescar
     * o capturar insectos.
     *
     * @param tipo nombre del tipo a comprobar (ej: "RED", "CAÑA")
     * @return true si lleva una herramienta del tipo indicado
     */
    public boolean tieneHerramientaDeTipo(String tipo) {
        // Primero comprobamos que haya herramienta, luego el tipo
        // Si herramientaEnMano fuera null y llamáramos a esDeTipo() directamente
        // Java lanzaría un NullPointerException
        return herramientaEnMano != null && herramientaEnMano.esDeTipo(tipo);
    }

    /**
     * Devuelve una representación legible del jugador con sus datos principales.
     *
     * @return cadena con los datos del jugador
     */
    @Override
    public String toString() {
        return "Pj{nombre='" + nombre + "', skin='" + skin +
                "', pos=(" + x + "," + y + "), energía=" + energiaActual +
                ", dinero=" + baulesMoney + ", horas=" + horasJugadas + "}";
    }

    /**
     * Dos jugadores son iguales si tienen el mismo nombre, posición y mochila.
     *
     * @param o objeto con el que comparar
     * @return true si son iguales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pj)) return false;
        Pj pj = (Pj) o;
        return super.equals(o) && Objects.equals(mochilaEquipada, pj.mochilaEquipada);
    }

    /**
     * Debe ser consistente con equals: jugadores iguales tienen el mismo hashCode.
     *
     * @return código hash basado en el hashCode del padre y el dinero
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), baulesMoney);
    }
}