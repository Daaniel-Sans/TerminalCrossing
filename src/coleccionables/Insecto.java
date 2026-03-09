package coleccionables;

import excepciones.NombreInvalidoException;

import java.util.Objects;

/**
 * Representa un insecto que el jugador puede capturar con una red.
 * Hereda de Objeto los atributos comunes (skin, nombre, precio, peso, esRecogible).
 * Contiene la lista estática {@code ESPECIES} con todos los insectos posibles del juego.
 */
public class Insecto extends Objeto {

    private int dificultad;
    private String descripcion;
    private double velocidadVuelo;
    private boolean esNocturno;

    // Lista de todas las especies posibles. Es 'static' porque pertenece
    // a la clase, no a cada objeto. Es 'final' porque no cambia nunca.
    //
    // Es una matriz String[][] (array de arrays) porque cada especie tiene
    // varios datos (skin, nombre, precio, dificultad, descripcion) y así
    // se pueden guardar todos juntos sin necesitar crear objetos Insecto
    // previamente. Cada fila es una especie, cada columna es un dato:
    //   [0] skin
    //   [1] nombre
    //   [2] precioVenta
    //   [3] dificultad
    //   [4] descripcion
    //
    // Para acceder a un dato concreto: ESPECIES[fila][columna]
    // Ejemplo: ESPECIES[1][1] → "Abeja"
    //          ESPECIES[1][2] → "500" (String, hay que convertir a int con Integer.parseInt)
    public static final String[][] ESPECIES = {
            {"🦋", "Mariposa Común", "100", "1", "Vaya mariposa más hermosa...¡Pa la saca!"},
            {"🐝", "Abeja",          "500", "2", "¡Esta abeja no me deja! ¡Pa la saca!"},
            {"🐞", "Mariquita",      "200", "1", "¡Una mariquita! Se come los pulgones y nunca te toca los melocotones"},
            {"🦂", "Escorpión",      "2000","3", "¡Illo illo un alacrán asesino! Lo cogeré por el pepino"}
    };

    /**
     * Constructor vacío. Crea un insecto genérico con valores por defecto.
     */
    public Insecto() {
        super();
        this.dificultad = 1;
        this.descripcion = "Sin descripción";
        this.velocidadVuelo = 1.0;
        this.esNocturno = false;
    }

    /**
     * Constructor completo. Crea un insecto con todos sus datos.
     *
     * @param skin        emoji que representa al insecto
     * @param nombre      nombre del insecto, no puede estar vacío
     * @param precioVenta precio en baules si se vendiera
     * @param dificultad  nivel de dificultad para capturarlo (1 fácil, 3 difícil)
     * @param descripcion texto gracioso que aparece al capturarlo
     * @throws NombreInvalidoException si el nombre es nulo o está vacío
     */
    public Insecto(String skin, String nombre, int precioVenta, int dificultad, String descripcion) throws NombreInvalidoException {
        super(skin, nombre, precioVenta);
        this.dificultad = dificultad;
        this.descripcion = descripcion;
        this.velocidadVuelo = 1.0; // valor por defecto, igual para todos los insectos
        this.esNocturno = false;   // por defecto los insectos son diurnos
    }

    // ── Getters y setters ──────────────────────────────────────────────────────

    public int getDificultad()               { return dificultad; }
    public void setDificultad(int d)         { this.dificultad = d; }
    public String getDescripcion()           { return descripcion; }
    public void setDescripcion(String d)     { this.descripcion = d; }
    public double getVelocidadVuelo()        { return velocidadVuelo; }
    public void setVelocidadVuelo(double v)  { this.velocidadVuelo = v; }
    public boolean isEsNocturno()            { return esNocturno; }
    public void setEsNocturno(boolean n)     { this.esNocturno = n; }

    // ── Métodos ────────────────────────────────────────────────────────────────

    /**
     * Devuelve una representación legible del insecto con sus datos principales.
     *
     * @return cadena con los datos del insecto
     */
    @Override
    public String toString() {
        return "Insecto{nombre='" + nombre + "', dificultad=" + dificultad +
                ", velocidad=" + velocidadVuelo + ", nocturno=" + esNocturno + "}";
    }

    /**
     * Dos insectos son iguales si tienen el mismo nombre y dificultad.
     *
     * @param o objeto con el que comparar
     * @return true si son iguales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Insecto)) return false;
        Insecto i = (Insecto) o;
        return super.equals(o) && dificultad == i.dificultad;
    }

    /**
     * Debe ser consistente con equals: insectos iguales tienen el mismo hashCode.
     *
     * @return código hash basado en el hashCode del padre y la dificultad
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dificultad);
    }
}