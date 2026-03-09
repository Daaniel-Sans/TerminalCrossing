package mundo;

import coleccionables.Objeto;
import coleccionables.ArbolFrutal;

/**
 * Representa una casilla del mapa del juego.
 * Cada celda sabe qué tipo de suelo es, qué objeto hay encima
 * y qué personaje está pisando esa casilla.
 *
 * El mapa completo es una matriz de celdas (Celda[][]) en la clase Mundo.
 */
public class Celda {

    private String tipoSuelo;              // tipo de suelo: "." hierba, "🌊" agua
    private Objeto objetoEncima;           // objeto en esta casilla, null si no hay ninguno
    private personajes.Pj personajeEncima; // jugador en esta casilla, null si no hay ninguno

    // Códigos ANSI para colorear el texto en la terminal
    private final String VERDE = "\u001B[32m";
    private final String RESET = "\u001B[0m";

    /**
     * Crea una celda con el tipo de suelo indicado.
     * Al crearse no tiene ningún objeto ni personaje encima.
     *
     * @param tipoSuelo tipo de suelo de la celda ("." para hierba, "🌊" para agua)
     */
    public Celda(String tipoSuelo) {
        this.tipoSuelo = tipoSuelo;
        this.objetoEncima = null;
        this.personajeEncima = null;
    }

    // ── Getters y setters ──────────────────────────────────────────────────────

    public String getTipoSuelo()                        { return tipoSuelo; }
    public Objeto getObjetoEncima()                     { return objetoEncima; }
    public void setObjetoEncima(Objeto o)               { this.objetoEncima = o; }
    public personajes.Pj getPersonajeEncima()           { return personajeEncima; }
    public void setPersonajeEncima(personajes.Pj p)     { this.personajeEncima = p; }

    // ── Métodos ────────────────────────────────────────────────────────────────

    /**
     * Devuelve el carácter que debe dibujarse en esta celda en el mapa.
     * La prioridad es: primero el personaje, luego el objeto, luego el suelo.
     * Devuelve String porque el método dibujarMapa() de Mundo necesita el texto.
     *
     * @return texto que representa esta celda en la terminal
     */
    public String obtenerCaracterADibujar() {
        // Prioridad 1: si hay un personaje, mostramos su skin
        if (personajeEncima != null) {
            return personajeEncima.getSkin() + " ";
        }
        // Prioridad 2: si hay un objeto, mostramos su skin
        if (objetoEncima != null) {
            return objetoEncima.getSkin() + " ";
        }
        // Prioridad 3: mostramos el suelo
        // La hierba "." se colorea de verde con códigos ANSI
        if (tipoSuelo.equals(".")) {
            return VERDE + tipoSuelo + RESET + "  ";
        }
        return tipoSuelo + ".";
    }

    /**
     * Comprueba si el jugador puede moverse a esta celda.
     * El agua nunca es atravesable.
     * Los árboles, rocas y casas tampoco son atravesables porque son objetos grandes.
     * Frutas, insectos, herramientas y mochilas sí son atravesables —
     * el jugador puede pisarlos pero no los recoge automáticamente (eso lo hace F).
     * Devuelve boolean porque el Controlador necesita saber si puede mover al jugador.
     *
     * @return true si el jugador puede moverse a esta celda
     */
    public boolean esAtravesable() {
        // El agua nunca se puede atravesar
        if (this.tipoSuelo.contains("🌊")) return false;

        // Si hay un objeto encima comprobamos si es un objeto grande que bloquea
        if (this.objetoEncima != null) {
            String nombre = this.objetoEncima.getNombre().toLowerCase();
            if (nombre.contains("árbol") || nombre.contains("roca") || nombre.contains("casa")) {
                return false;
            }
            // Objetos pequeños (frutas, insectos, herramientas): se puede pasar
            return true;
        }
        return true;
    }

    /**
     * Comprueba si esta celda es agua.
     * Se usa en el Controlador para saber si el jugador puede pescar.
     * Devuelve boolean porque el Controlador necesita saberlo para lanzar el minijuego.
     *
     * @return true si el suelo de esta celda es agua
     */
    public boolean esAgua() {
        return this.tipoSuelo.contains("🌊");
    }
}