package personajes;

import excepciones.NombreInvalidoException;
import java.util.Objects;

/**
 * Representa un personaje del pueblo que no controla el usuario.
 * Hereda de Personaje los atributos comunes (nombre, skin, x, y, energía, estado).
 *
 * Los NPCs tienen una profesión y un diálogo fijo. No se mueven ni gastan energía,
 * solo pueden hablar con el jugador cuando este se acerca.
 */
public class Npc extends Personaje {

    private String profesion;        // qué hace en el pueblo (ej: "Tendero", "Jardinera")
    private String dialogo;          // lo que dice cuando hablas con él
    private boolean tiendaAbierta;   // true si su tienda está abierta
    private double precioDescuento;  // descuento que aplica en su tienda (0.0 = sin descuento)

    /**
     * Constructor vacío. Crea un NPC genérico con valores por defecto.
     */
    public Npc() {
        super();
        this.profesion = "Vecino";
        this.dialogo = "...";
        this.tiendaAbierta = false;
        this.precioDescuento = 0.0;
    }

    /**
     * Constructor completo. Crea un NPC con todos sus datos.
     * La velocidad se fija en 0.8 porque los NPCs se mueven más despacio
     * que el jugador.
     *
     * @param nombre    nombre del NPC, no puede estar vacío
     * @param skin      emoji que representa al NPC en el mapa
     * @param x         posición horizontal inicial en el mapa
     * @param y         posición vertical inicial en el mapa
     * @param profesion profesión del NPC en el pueblo
     * @param dialogo   texto que dice el NPC cuando hablas con él
     * @throws NombreInvalidoException si el nombre es nulo o está vacío
     */
    public Npc(String nombre, String skin, int x, int y, String profesion, String dialogo) throws NombreInvalidoException {
        super(nombre, skin, x, y, 0.8); // velocidad 0.8, más lento que el jugador
        this.profesion = profesion;
        this.dialogo = dialogo;
        this.tiendaAbierta = false;  // la tienda empieza cerrada
        this.precioDescuento = 0.0;  // sin descuento por defecto
    }

    // ── Getters y setters ──────────────────────────────────────────────────────

    public String getProfesion()                { return profesion; }
    public void setProfesion(String p)          { this.profesion = p; }
    public String getDialogo()                  { return dialogo; }
    public void setDialogo(String d)            { this.dialogo = d; }
    public boolean isTiendaAbierta()            { return tiendaAbierta; }
    public void setTiendaAbierta(boolean t)     { this.tiendaAbierta = t; }
    public double getPrecioDescuento()          { return precioDescuento; }
    public void setPrecioDescuento(double d)    { this.precioDescuento = d; }

    // ── Métodos ────────────────────────────────────────────────────────────────

    /**
     * El NPC dice su diálogo si está activo.
     * Devuelve String porque el Controlador necesita el texto para mostrarlo en pantalla.
     * Si el NPC no está activo devuelve un mensaje alternativo.
     *
     * Accede directamente a {@code estaActivo} y {@code nombre} sin getter
     * porque son protected en Personaje y la hija puede usarlos directamente.
     *
     * @return el diálogo del NPC o un mensaje de que no quiere hablar
     */
    public String hablar() {
        // Si el NPC no está activo no quiere hablar
        if (!estaActivo) {
            return nombre + " no quiere hablar ahora mismo.";
        }
        // Las \" son comillas escapadas — es la forma de poner comillas dentro de un String
        return nombre + " dice: \"" + dialogo + "\"";
    }

    /**
     * Devuelve una representación legible del NPC con sus datos principales.
     *
     * @return cadena con los datos del NPC
     */
    @Override
    public String toString() {
        return "Npc{nombre='" + nombre + "', profesion='" + profesion +
                "', tiendaAbierta=" + tiendaAbierta + ", descuento=" + precioDescuento + "}";
    }

    /**
     * Dos NPCs son iguales si tienen el mismo nombre y profesión.
     *
     * @param o objeto con el que comparar
     * @return true si son iguales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Npc)) return false;
        Npc npc = (Npc) o;
        return super.equals(o) && Objects.equals(profesion, npc.profesion);
    }

    /**
     * Debe ser consistente con equals: NPCs iguales tienen el mismo hashCode.
     *
     * @return código hash basado en el hashCode del padre y la profesión
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), profesion);
    }
}