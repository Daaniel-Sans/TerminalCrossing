package coleccionables;

import excepciones.NombreInvalidoException;
import java.util.Objects;

/**
 * Representa un pez que el jugador puede pescar con una caña.
 * Hereda de Objeto los atributos comunes (skin, nombre, precio, peso, esRecogible).
 * Contiene la lista estática {@code ESPECIES} con todos los peces posibles del juego.
 */
public class Pez extends Objeto {

    private String tamañoSombra;  // tamaño de la sombra en el agua: "Pequeña", "Mediana" o "Grande"
    private String descripcion;
    private double pesoKg;        // peso del pez en kilogramos
    private boolean esRaro;       // true si precioVenta > 1000, se calcula automáticamente

    // Lista de todas las especies posibles. Misma estructura que en Insecto.
    // Cada fila es: {skin, nombre, precioVenta, tamañoSombra, descripcion}
    public static final String[][] ESPECIES = {
            {"🐟", "Salmonete",  "80",   "Pequeña", "El 33% es sal y lo demás: monete"},
            {"🐠", "Pez Payaso", "350",  "Pequeña", "¿Quillo el lunes voy a tu casa, te pito y bajas?"},
            {"🐡", "Pez Globo",  "600",  "Mediana", "Y asssssseeeee ¡¡¡PUMMM!!!"},
            {"🦈", "Tiburón",    "2500", "Grande",  "Tuuum tum... tuuuuuuum tum."}
    };

    /**
     * Constructor vacío. Crea un pez genérico con valores por defecto.
     */
    public Pez() {
        super();
        this.tamañoSombra = "Pequeña";
        this.descripcion = "Sin descripción";
        this.pesoKg = 0.5;
        this.esRaro = false;
    }

    /**
     * Constructor completo. Crea un pez con todos sus datos.
     * El atributo {@code esRaro} se calcula automáticamente:
     * si el precio supera 1000 baules, el pez se considera raro.
     *
     * @param skin         emoji que representa al pez
     * @param nombre       nombre del pez, no puede estar vacío
     * @param precioVenta  precio en baules si se vendiera
     * @param tamañoSombra tamaño de la sombra en el agua
     * @param descripcion  texto gracioso que aparece al pescarlo
     * @throws NombreInvalidoException si el nombre es nulo o está vacío
     */
    public Pez(String skin, String nombre, int precioVenta, String tamañoSombra, String descripcion) throws NombreInvalidoException {
        super(skin, nombre, precioVenta);
        this.tamañoSombra = tamañoSombra;
        this.descripcion = descripcion;
        this.pesoKg = 0.5;                    // valor por defecto igual para todos
        this.esRaro = precioVenta > 1000;     // se calcula automáticamente según el precio
    }

    // ── Getters y setters ──────────────────────────────────────────────────────

    public String getTamañoSombra()          { return tamañoSombra; }
    public void setTamañoSombra(String t)    { this.tamañoSombra = t; }
    public String getDescripcion()           { return descripcion; }
    public void setDescripcion(String d)     { this.descripcion = d; }
    public double getPesoKg()                { return pesoKg; }
    public void setPesoKg(double p)          { this.pesoKg = p; }
    public boolean isEsRaro()                { return esRaro; }
    public void setEsRaro(boolean r)         { this.esRaro = r; }

    // ── Métodos ────────────────────────────────────────────────────────────────

    /**
     * Devuelve una representación legible del pez con sus datos principales.
     *
     * @return cadena con los datos del pez
     */
    @Override
    public String toString() {
        return "Pez{nombre='" + nombre + "', tamaño='" + tamañoSombra +
                "', peso=" + pesoKg + "kg, raro=" + esRaro + "}";
    }

    /**
     * Dos peces son iguales si tienen el mismo nombre y tamaño de sombra.
     *
     * @param o objeto con el que comparar
     * @return true si son iguales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pez)) return false;
        Pez p = (Pez) o;
        return super.equals(o) && Objects.equals(tamañoSombra, p.tamañoSombra);
    }

    /**
     * Debe ser consistente con equals: peces iguales tienen el mismo hashCode.
     *
     * @return código hash basado en el hashCode del padre y el tamaño de sombra
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tamañoSombra);
    }
}