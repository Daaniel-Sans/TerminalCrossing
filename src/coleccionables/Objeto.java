package coleccionables;

import excepciones.NombreInvalidoException;
import java.util.Objects;

/**
 * Representa cualquier objeto que puede existir en el mapa del juego.
 * Es la clase padre de Fruta, Insecto, Pez, Herramienta, ArbolFrutal y Mochila.
 *
 * Los atributos son 'protected' para que las clases hijas puedan acceder
 * directamente a ellos sin necesitar un getter. Con 'private' no podrían.
 */
public class Objeto {

    protected String skin;
    protected String nombre;
    protected int precioVenta;    // en baules, moneda del juego
    protected double peso;        // en kilogramos
    protected boolean esRecogible;

    /**
     * Constructor vacío. Crea un objeto genérico con valores por defecto.
     * Necesario para los tests y para crear objetos sin datos iniciales.
     */
    public Objeto() {
        this.skin = "📦";
        this.nombre = "Objeto desconocido";
        this.precioVenta = 0;
        this.peso = 0.0;
        this.esRecogible = true;
    }

    /**
     * Constructor completo. Crea un objeto con todos sus datos.
     *
     * @param skin        emoji que representa al objeto en el mapa
     * @param nombre      nombre del objeto, no puede estar vacío
     * @param precioVenta precio en baules si se vendiera
     * @throws NombreInvalidoException si el nombre es nulo o está vacío
     */
    public Objeto(String skin, String nombre, int precioVenta) throws NombreInvalidoException {
        // Validamos el nombre antes de asignarlo.
        // Si está vacío o es nulo, lanzamos el error personalizado.
        if (nombre == null || nombre.isBlank()) {
            throw new NombreInvalidoException("El nombre del objeto no puede estar vacío.");
        }
        this.skin = skin;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.peso = 0.5;         // siempre 0.5 por defecto, igual para todos los objetos
        this.esRecogible = true; // siempre true por defecto, todos son recogibles
    }

    // ── Getters y setters ──────────────────────────────────────────────────────

    public String getSkin()                 { return skin; }
    public void setSkin(String skin)        { this.skin = skin; }
    public String getNombre()               { return nombre; }
    public void setNombre(String nombre)    { this.nombre = nombre; }
    public int getPrecioVenta()             { return precioVenta; }
    public void setPrecioVenta(int p)       { this.precioVenta = p; }
    public double getPeso()                 { return peso; }
    public void setPeso(double peso)        { this.peso = peso; }
    public boolean isEsRecogible()          { return esRecogible; }
    public void setEsRecogible(boolean r)   { this.esRecogible = r; }

    // ── Métodos ────────────────────────────────────────────────────────────────

    /**
     * Devuelve una representación legible del objeto con sus datos principales.
     *
     * @return cadena con los datos del objeto
     */
    @Override
    public String toString() {
        return "Objeto{skin='" + skin + "', nombre='" + nombre +
                "', precio=" + precioVenta + ", peso=" + peso +
                ", recogible=" + esRecogible + "}";
    }

    /**
     * Dos objetos son iguales si tienen el mismo nombre y precio de venta.
     *
     * @param o objeto con el que comparar
     * @return true si son iguales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Objeto)) return false;
        Objeto obj = (Objeto) o;
        return precioVenta == obj.precioVenta && Objects.equals(nombre, obj.nombre);
    }

    /**
     * Debe ser consistente con equals: objetos iguales tienen el mismo hashCode.
     *
     * @return código hash basado en nombre y precio
     */
    @Override
    public int hashCode() {
        return Objects.hash(nombre, precioVenta);
    }
}