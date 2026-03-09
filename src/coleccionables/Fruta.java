package coleccionables;

import excepciones.NombreInvalidoException;
import java.util.Objects;

/**
 * Representa una fruta que el jugador puede recoger y comer para recuperar energía.
 * Hereda de Objeto los atributos comunes (skin, nombre, precio, peso, esRecogible).
 */
public class Fruta extends Objeto {

    private int energiaQueRecupera;
    private String temporada;
    private double dulzura;
    private boolean esFresca;

    /**
     * Constructor vacío. Crea una fruta con valores por defecto.
     */
    public Fruta() {
        super();
        this.energiaQueRecupera = 10;
        this.temporada = "Todo el año";
        this.dulzura = 5.0;
        this.esFresca = true;
    }

    /**
     * Constructor completo. Crea una fruta con todos sus datos.
     *
     * @param skin    emoji que representa la fruta
     * @param nombre  nombre de la fruta, no puede estar vacío
     * @param energia energía que recupera el jugador al comerla
     * @throws NombreInvalidoException si el nombre es nulo o está vacío
     */
    public Fruta(String skin, String nombre, int energia) throws NombreInvalidoException {
        super(skin, nombre, 0);
        this.energiaQueRecupera = energia;
        this.temporada = "Todo el año";
        this.dulzura = 5.0;
        this.esFresca = true;
    }

    // ── Getters y setters ──────────────────────────────────────────────────────

    public int getEnergiaQueRecupera()           { return energiaQueRecupera; }
    public void setEnergiaQueRecupera(int e)     { this.energiaQueRecupera = e; }
    public String getTemporada()                 { return temporada; }
    public void setTemporada(String t)           { this.temporada = t; }
    public double getDulzura()                   { return dulzura; }
    public void setDulzura(double d)             { this.dulzura = d; }
    public boolean isEsFresca()                  { return esFresca; }
    public void setEsFresca(boolean f)           { this.esFresca = f; }

    // ── Métodos ────────────────────────────────────────────────────────────────

    /**
     * Devuelve una representación legible de la fruta con sus datos principales.
     *
     * @return cadena con los datos de la fruta
     */
    @Override
    public String toString() {
        return "Fruta{nombre='" + nombre + "', energía=" + energiaQueRecupera +
                ", temporada='" + temporada + "', dulzura=" + dulzura +
                ", fresca=" + esFresca + "}";
    }

    /**
     * Dos frutas son iguales si tienen el mismo nombre y la misma energía.
     *
     * @param o objeto con el que comparar
     * @return true si son iguales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fruta)) return false;
        Fruta f = (Fruta) o;
        return super.equals(o) && energiaQueRecupera == f.energiaQueRecupera;
    }

    /**
     * Debe ser consistente con equals: frutas iguales tienen el mismo hashCode.
     *
     * @return código hash basado en el hashCode del padre y la energía
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), energiaQueRecupera);
    }
}