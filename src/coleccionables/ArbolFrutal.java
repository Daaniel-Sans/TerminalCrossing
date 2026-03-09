package coleccionables;

import excepciones.NombreInvalidoException;

import java.util.Objects;

/**
 * Representa un árbol frutal que aparece en el mapa del juego.
 * Hereda de Objeto los atributos comunes (skin, nombre, precio, peso, esRecogible).
 *
 * El árbol tiene un ciclo de vida: empieza con fruta, el jugador lo sacude
 * y se queda sin fruta, y después de {@code TIEMPO_RECARGA} turnos vuelve a tenerla.
 */
public class
ArbolFrutal extends Objeto {

    private boolean tieneFruta;              // si el árbol tiene fruta ahora mismo
    private int turnosParaRegenerar;         // turnos que faltan para volver a tener fruta
    private double alturaMetros;             // altura del árbol en metros
    private String tipoDeFruta;              // tipo de fruta que da el árbol

    // Constante: número de turnos que tarda el árbol en regenerar su fruta.
    // Es 'final' porque ese valor nunca debe cambiar durante la ejecución.
    private final int TIEMPO_RECARGA = 10;

    // Emojis que representan al árbol según su estado.
    // No son parámetros porque todos los árboles usan los mismos.
    private String skinConFruta = "🌳";
    private String skinVacio    = "🪵";

    /**
     * Constructor vacío. Crea un árbol genérico con valores por defecto.
     */
    public ArbolFrutal() {
        super();
        this.tieneFruta = true;
        this.turnosParaRegenerar = 0;
        this.alturaMetros = 3.0;
        this.tipoDeFruta = "Manzana";
    }

    /**
     * Constructor completo. Crea un árbol con el nombre indicado.
     * El resto de atributos se inicializan con valores por defecto porque
     * todos los árboles empiezan igual: con fruta, altura 3m y tipo Manzana.
     * El skin y el precio son fijos para todos los árboles.
     *
     * @param nombre nombre del árbol, no puede estar vacío
     * @throws NombreInvalidoException si el nombre es nulo o está vacío
     */
    public ArbolFrutal(String nombre) throws NombreInvalidoException {
        super("🌳", nombre, 0); // skin y precio fijos para todos los árboles
        this.tieneFruta = true;
        this.turnosParaRegenerar = 0;
        this.alturaMetros = 3.0;
        this.tipoDeFruta = "Manzana";
    }

    // ── Getters y setters ──────────────────────────────────────────────────────

    public boolean isTieneFruta()                { return tieneFruta; }
    public void setTieneFruta(boolean t)         { this.tieneFruta = t; }
    public int getTurnosParaRegenerar()          { return turnosParaRegenerar; }
    public void setTurnosParaRegenerar(int t)    { this.turnosParaRegenerar = t; }
    public double getAlturaMetros()              { return alturaMetros; }
    public void setAlturaMetros(double a)        { this.alturaMetros = a; }
    public String getTipoDeFruta()               { return tipoDeFruta; }
    public void setTipoDeFruta(String t)         { this.tipoDeFruta = t; }

    // ── Métodos ────────────────────────────────────────────────────────────────

    /**
     * Sacude el árbol quitándole la fruta y activando el contador de regeneración.
     * Cambia el skin a árbol vacío para que se vea en el mapa.
     * No devuelve nada (void) porque solo ejecuta una acción.
     */
    public void sacudir() {
        this.tieneFruta = false;
        this.skin = skinVacio;               // cambiamos el emoji en el mapa
        this.turnosParaRegenerar = TIEMPO_RECARGA; // activamos el contador
    }

    /**
     * Actualiza el contador de regeneración del árbol.
     * Se llama automáticamente cada vez que el jugador se mueve.
     * Cuando el contador llega a 0 el árbol vuelve a tener fruta.
     * No devuelve nada (void) porque solo actualiza el estado interno.
     */
    public void actualizarRegeneracion() {
        // Solo actualizamos si el árbol no tiene fruta
        if (!tieneFruta) {
            turnosParaRegenerar--;
            // Si el contador ha llegado a 0, el árbol vuelve a tener fruta
            if (turnosParaRegenerar <= 0) {
                tieneFruta = true;
                this.skin = skinConFruta; // restauramos el emoji en el mapa
            }
        }
    }

    /**
     * Devuelve una representación legible del árbol con sus datos principales.
     *
     * @return cadena con los datos del árbol
     */
    @Override
    public String toString() {
        return "ArbolFrutal{nombre='" + nombre + "', tieneFruta=" + tieneFruta +
                ", altura=" + alturaMetros + "m, fruta='" + tipoDeFruta + "'}";
    }

    /**
     * Dos árboles son iguales si tienen el mismo nombre y el mismo estado de fruta.
     *
     * @param o objeto con el que comparar
     * @return true si son iguales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArbolFrutal)) return false;
        ArbolFrutal a = (ArbolFrutal) o;
        return super.equals(o) && tieneFruta == a.tieneFruta;
    }

    /**
     * Debe ser consistente con equals: árboles iguales tienen el mismo hashCode.
     *
     * @return código hash basado en el hashCode del padre y el estado de fruta
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tieneFruta);
    }
}