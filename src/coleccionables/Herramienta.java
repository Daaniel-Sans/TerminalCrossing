package coleccionables;

import excepciones.HerramientaRotaException;
import excepciones.NombreInvalidoException;

import java.util.Objects;

/**
 * Representa una herramienta que el jugador puede equipar y usar.
 * Hereda de Objeto los atributos comunes (skin, nombre, precio, peso, esRecogible).
 *
 * Cada herramienta tiene un tipo definido por el enum {@code TipoHerramienta}:
 * RED para capturar insectos, CAÑA para pescar, PALA para cavar.
 * Usar un enum en vez de un String evita errores tipográficos.
 */
public class Herramienta extends Objeto {

    private TipoHerramienta tipo;  // enum: RED, CAÑA, PALA o DESCONOCIDO
    private int durabilidad;       // usos que le quedan antes de romperse
    private double eficiencia;     // multiplicador de efectividad
    private boolean estaReparada;  // false cuando durabilidad llega a 0

    /**
     * Constructor vacío. Crea una herramienta genérica con valores por defecto.
     */
    public Herramienta() {
        super();
        this.tipo = TipoHerramienta.DESCONOCIDO;
        this.durabilidad = 10;
        this.eficiencia = 1.0;
        this.estaReparada = true;
    }

    /**
     * Constructor completo. Crea una herramienta con todos sus datos.
     * {@code eficiencia} y {@code estaReparada} se inicializan con valores
     * por defecto porque toda herramienta nueva empieza igual.
     *
     * @param skin        emoji que representa la herramienta
     * @param nombre      nombre de la herramienta, no puede estar vacío
     * @param tipo        tipo de herramienta según el enum TipoHerramienta
     * @param durabilidad número de usos antes de romperse
     * @throws NombreInvalidoException si el nombre es nulo o está vacío
     */
    public Herramienta(String skin, String nombre, TipoHerramienta tipo, int durabilidad) throws NombreInvalidoException {
        super(skin, nombre, 0);
        this.tipo = tipo;
        this.durabilidad = durabilidad;
        this.eficiencia = 1.0;     // valor por defecto igual para todas
        this.estaReparada = true;  // toda herramienta nueva está reparada
    }

    // ── Getters y setters ──────────────────────────────────────────────────────

    public TipoHerramienta getTipo()          { return tipo; }
    public void setTipo(TipoHerramienta t)    { this.tipo = t; }
    public int getDurabilidad()               { return durabilidad; }
    public void setDurabilidad(int d)         { this.durabilidad = d; }
    public double getEficiencia()             { return eficiencia; }
    public void setEficiencia(double e)       { this.eficiencia = e; }
    public boolean isEstaReparada()           { return estaReparada; }
    public void setEstaReparada(boolean r)    { this.estaReparada = r; }

    // ── Métodos ────────────────────────────────────────────────────────────────

    /**
     * Usa la herramienta reduciendo su durabilidad en 1.
     * No devuelve nada (void) porque solo ejecuta una acción,
     * nadie necesita una respuesta al llamarlo.
     *
     * Hay dos comprobaciones de durabilidad:
     * - Antes de usar: si ya estaba rota, lanza la excepción y para.
     * - Después de usar: si se acaba de romper, la marca como rota y avisa.
     *
     * @throws HerramientaRotaException si la herramienta ya está rota (durabilidad = 0)
     */
    public void usar() throws HerramientaRotaException {
        // Si la durabilidad ya era 0 antes de usarla, lanzamos el error y paramos
        if (durabilidad <= 0) {
            throw new HerramientaRotaException("La " + getNombre() + " está rota y no se puede usar.");
        }
        this.durabilidad--; // le quitamos 1 uso
        // Si después de restar 1 la durabilidad llega a 0, se acaba de romper
        if (durabilidad <= 0) {
            this.estaReparada = false;
            System.out.println("¡Oh no! Tu " + getNombre() + " se ha roto.");
        }
    }

    /**
     * Comprueba si esta herramienta es de un tipo concreto.
     * Devuelve boolean porque el Controlador necesita la respuesta
     * para decidir si dejar al jugador pescar o capturar insectos.
     *
     * Ejemplo de uso en el juego:
     * <pre>
     *     if (!jugador.tieneHerramientaDeTipo("CAÑA")) {
     *         mensaje("Necesitas una caña para pescar.");
     *     }
     * </pre>
     *
     * @param tipoStr nombre del tipo a comparar (ej: "RED", "CAÑA")
     * @return true si el tipo de esta herramienta coincide con tipoStr
     */
    public boolean esDeTipo(String tipoStr) {
        // name() convierte el enum a String: TipoHerramienta.CAÑA → "CAÑA"
        // equalsIgnoreCase compara ignorando mayúsculas: "caña" == "CAÑA" == "Caña"
        return this.tipo.name().equalsIgnoreCase(tipoStr);
    }

    /**
     * Devuelve una representación legible de la herramienta con sus datos principales.
     *
     * @return cadena con los datos de la herramienta
     */
    @Override
    public String toString() {
        return "Herramienta{nombre='" + nombre + "', tipo=" + tipo +
                ", durabilidad=" + durabilidad + ", eficiencia=" + eficiencia + "}";
    }

    /**
     * Dos herramientas son iguales si tienen el mismo nombre y tipo.
     *
     * @param o objeto con el que comparar
     * @return true si son iguales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Herramienta)) return false;
        Herramienta h = (Herramienta) o;
        return super.equals(o) && tipo == h.tipo;
    }

    /**
     * Debe ser consistente con equals: herramientas iguales tienen el mismo hashCode.
     *
     * @return código hash basado en el hashCode del padre y el tipo
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tipo);
    }
}