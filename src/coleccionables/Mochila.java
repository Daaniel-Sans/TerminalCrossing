package coleccionables;

import excepciones.MochilaLlenaException;
import excepciones.NombreInvalidoException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * Representa la mochila del jugador donde guarda los objetos que recoge.
 * Hereda de Objeto los atributos comunes (skin, nombre, precio, peso, esRecogible).
 *
 * Los objetos se agrupan por nombre usando un LinkedHashMap:
 * cada clave es el nombre del objeto y el valor es una lista con todos
 * los objetos de ese tipo. Así 3 manzanas ocupan una sola entrada con una lista de 3.
 *
 * Se usa LinkedHashMap en vez de HashMap para mantener el orden de inserción,
 * es decir, los objetos aparecen en el orden en que fueron guardados.
 */
public class Mochila extends Objeto {

    private int capacidadMaxima;   // número máximo de objetos que caben
    private String material;       // material de la mochila
    private double pesoPropio;     // peso de la mochila vacía en kilogramos
    private boolean estaEquipada;  // true si el jugador la lleva puesta

    // El contenido es un LinkedHashMap donde:
    // - La CLAVE es el nombre del objeto (ej: "Manzana")
    // - El VALOR es una lista con todos los objetos de ese tipo
    // Ejemplo de cómo se ve internamente:
    //   "Manzana"  → [Fruta, Fruta, Fruta]
    //   "Mariposa" → [Insecto]
    //   "Tiburón"  → [Pez, Pez]
    private LinkedHashMap<String, List<Objeto>> contenido;

    /**
     * Constructor vacío. Crea una mochila genérica con valores por defecto.
     * Inicializa el contenido como un mapa vacío — si no se inicializara
     * y se intentara guardar algo, Java lanzaría un error porque sería null.
     */
    public Mochila() {
        super();
        this.capacidadMaxima = 10;
        this.material = "Tela";
        this.pesoPropio = 0.3;
        this.estaEquipada = false;
        this.contenido = new LinkedHashMap<>(); // mapa vacío, listo para usar
    }

    /**
     * Constructor completo. Crea una mochila con todos sus datos.
     * Solo recibe skin, nombre y capacidad porque son los únicos datos
     * que varían de una mochila a otra. El material, peso y estado
     * son siempre iguales para todas las mochilas.
     *
     * @param skin      emoji que representa la mochila
     * @param nombre    nombre de la mochila, no puede estar vacío
     * @param capacidad número máximo de objetos que caben
     * @throws NombreInvalidoException si el nombre es nulo o está vacío
     */
    public Mochila(String skin, String nombre, int capacidad) throws NombreInvalidoException {
        super(skin, nombre, 0);
        this.capacidadMaxima = capacidad;
        this.material = "Tela";          // valor por defecto igual para todas
        this.pesoPropio = 0.3;           // valor por defecto igual para todas
        this.estaEquipada = false;       // toda mochila nueva empieza sin equipar
        this.contenido = new LinkedHashMap<>(); // mapa vacío, listo para usar
    }

    // ── Getters y setters ──────────────────────────────────────────────────────

    public int getCapacidadMaxima()            { return capacidadMaxima; }
    public void setCapacidadMaxima(int c)      { this.capacidadMaxima = c; }
    public String getMaterial()                { return material; }
    public void setMaterial(String m)          { this.material = m; }
    public double getPesoPropio()              { return pesoPropio; }
    public void setPesoPropio(double p)        { this.pesoPropio = p; }
    public boolean isEstaEquipada()            { return estaEquipada; }
    public void setEstaEquipada(boolean e)     { this.estaEquipada = e; }
    public LinkedHashMap<String, List<Objeto>> getContenido() { return contenido; }

    // ── Métodos ────────────────────────────────────────────────────────────────

    /**
     * Devuelve el número total de objetos guardados en la mochila.
     * Recorre todos los grupos del HashMap y suma el tamaño de cada lista.
     * Devuelve int porque el Controlador necesita saber cuántos objetos hay.
     *
     * Ejemplo: si la mochila tiene
     *   "Manzana"  → [Fruta, Fruta, Fruta]  → 3
     *   "Mariposa" → [Insecto]               → 1
     * el total sería 4.
     *
     * @return número total de objetos en la mochila
     */
    public int getTotalObjetos() {
        int total = 0;
        // contenido.values() devuelve todas las listas del mapa sin las claves.
        // Para cada lista sumamos cuántos objetos tiene.
        for (List<Objeto> lista : contenido.values()) {
            total += lista.size();
        }
        return total;
    }

    /**
     * Comprueba si hay espacio disponible en la mochila.
     * Devuelve boolean porque otros métodos necesitan saber si pueden guardar.
     *
     * @return true si el total de objetos es menor que la capacidad máxima
     */
    public boolean hayEspacio() {
        return getTotalObjetos() < capacidadMaxima;
    }

    /**
     * Guarda un objeto en la mochila agrupándolo por nombre.
     * Usa computeIfAbsent para simplificar la lógica de agrupación:
     * si ya existe una lista para ese nombre la usa, si no la crea.
     * Devuelve boolean para confirmar que se guardó correctamente.
     *
     * Sin computeIfAbsent habría que escribir:
     * <pre>
     *     if (!contenido.containsKey(obj.getNombre())) {
     *         contenido.put(obj.getNombre(), new ArrayList&lt;&gt;());
     *     }
     *     contenido.get(obj.getNombre()).add(obj);
     * </pre>
     *
     * @param obj objeto a guardar
     * @return true si se guardó correctamente
     * @throws MochilaLlenaException si la mochila está llena
     */
    public boolean guardar(Objeto obj) throws MochilaLlenaException {
        if (!hayEspacio()) {
            throw new MochilaLlenaException("La mochila está llena. Capacidad máxima: " + capacidadMaxima);
        }
        // computeIfAbsent: si la clave no existe crea una lista nueva,
        // si ya existe devuelve la lista que había. Luego añade el objeto.
        contenido.computeIfAbsent(obj.getNombre(), k -> new ArrayList<>()).add(obj);
        System.out.println("🎒 " + obj.getSkin() + " " + obj.getNombre() + " guardado en la mochila.");
        return true;
    }

    /**
     * Comprueba si hay algún objeto con ese nombre en la mochila.
     * Comprueba dos cosas: que la clave exista Y que la lista no esté vacía,
     * porque podría darse el caso de que la clave exista con una lista vacía.
     * Devuelve boolean porque el Controlador necesita saber si puede sacar algo.
     *
     * @param nombre nombre del objeto a buscar
     * @return true si existe al menos un objeto con ese nombre
     */
    public boolean contiene(String nombre) {
        // containsKey → ¿existe esa clave en el mapa?
        // !isEmpty    → ¿la lista tiene al menos un objeto?
        // Las dos condiciones deben ser true con &&
        return contenido.containsKey(nombre) && !contenido.get(nombre).isEmpty();
    }

    /**
     * Saca y devuelve un objeto de la mochila por su nombre.
     * Elimina el último objeto de la lista porque es la operación más
     * eficiente en un ArrayList — no requiere reorganizar los elementos.
     * Si era el último objeto de ese tipo elimina también la entrada del mapa.
     * Devuelve Objeto porque el Controlador necesita el objeto para usarlo.
     *
     * @param nombre nombre del objeto a sacar
     * @return el objeto sacado, o null si no había ninguno con ese nombre
     */
    public Objeto sacar(String nombre) {
        List<Objeto> lista = contenido.get(nombre);
        if (lista == null || lista.isEmpty()) return null;
        // remove(lista.size() - 1) elimina y devuelve el último elemento
        Objeto obj = lista.remove(lista.size() - 1);
        // Si la lista quedó vacía eliminamos también la entrada del mapa
        if (lista.isEmpty()) contenido.remove(nombre);
        return obj;
    }

    /**
     * Devuelve los nombres de todos los grupos de objetos en la mochila.
     * Se usa en el Controlador para mostrar el contenido en pantalla.
     * Devuelve una copia de la lista para que nadie pueda modificar
     * el contenido real de la mochila desde fuera.
     *
     * @return lista con los nombres de los grupos (ej: ["Manzana", "Mariposa"])
     */
    public List<String> getNombresGrupos() {
        // keySet() devuelve las claves del mapa: ["Manzana", "Mariposa", "Tiburón"]
        // new ArrayList<>() crea una copia para proteger el contenido original
        return new ArrayList<>(contenido.keySet());
    }

    /**
     * Devuelve una representación legible de la mochila con sus datos principales.
     *
     * @return cadena con los datos de la mochila
     */
    @Override
    public String toString() {
        return "Mochila{nombre='" + nombre + "', capacidad=" + getTotalObjetos() +
                "/" + capacidadMaxima + ", material='" + material + "', peso=" + pesoPropio + "}";
    }

    /**
     * Dos mochilas son iguales si tienen el mismo nombre y capacidad máxima.
     *
     * @param o objeto con el que comparar
     * @return true si son iguales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mochila)) return false;
        Mochila m = (Mochila) o;
        return super.equals(o) && capacidadMaxima == m.capacidadMaxima;
    }

    /**
     * Debe ser consistente con equals: mochilas iguales tienen el mismo hashCode.
     *
     * @return código hash basado en el hashCode del padre y la capacidad máxima
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), capacidadMaxima);
    }
}