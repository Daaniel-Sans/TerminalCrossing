package controlador;

import coleccionables.Insecto;
import coleccionables.Pez;
import java.util.LinkedHashMap;

/**
 * Enciclopedia del juego. Registra los insectos y peces que el jugador
 * ha capturado al menos una vez.
 *
 * Usa LinkedHashMap para mantener el orden en que fueron capturados,
 * de forma que la enciclopedia muestra primero los que se capturaron antes.
 *
 * La clave es el nombre del animal y el valor es el objeto completo,
 * así podemos acceder a todos sus datos (skin, descripción...) para mostrarlos.
 */
public class Catalogo {

    // LinkedHashMap<clave, valor>:
    // clave → nombre del insecto o pez (ej: "Mariposa Común")
    // valor → el objeto completo con todos sus datos
    // LinkedHashMap mantiene el orden de inserción, a diferencia de HashMap
    private LinkedHashMap<String, Insecto> insectosRegistrados;
    private LinkedHashMap<String, Pez>     pecesRegistrados;

    /**
     * Constructor. Crea la enciclopedia con las dos listas vacías.
     * Se van rellenando conforme el jugador captura animales.
     */
    public Catalogo() {
        this.insectosRegistrados = new LinkedHashMap<>();
        this.pecesRegistrados    = new LinkedHashMap<>();
    }

    // ── Métodos de registro ────────────────────────────────────────────────────

    /**
     * Registra un insecto en la enciclopedia si es la primera vez que se captura.
     * Devuelve boolean para que el Controlador sepa si es un descubrimiento nuevo
     * y pueda mostrar un mensaje especial al jugador.
     *
     * @param insecto insecto a registrar
     * @return true si es la primera vez que se captura este insecto, false si ya estaba
     */
    public boolean registrarInsecto(Insecto insecto) {
        // containsKey comprueba si ya existe una entrada con ese nombre
        if (!insectosRegistrados.containsKey(insecto.getNombre())) {
            // put añade la entrada: clave = nombre, valor = objeto completo
            insectosRegistrados.put(insecto.getNombre(), insecto);
            return true;  // primera vez que se captura
        }
        return false; // ya estaba registrado
    }

    /**
     * Registra un pez en la enciclopedia si es la primera vez que se pesca.
     * Devuelve boolean para que el Controlador sepa si es un descubrimiento nuevo.
     *
     * @param pez pez a registrar
     * @return true si es la primera vez que se pesca este pez, false si ya estaba
     */
    public boolean registrarPez(Pez pez) {
        if (!pecesRegistrados.containsKey(pez.getNombre())) {
            pecesRegistrados.put(pez.getNombre(), pez);
            return true;
        }
        return false;
    }

    // ── Métodos de visualización ───────────────────────────────────────────────

    /**
     * Devuelve las líneas de la enciclopedia listas para mostrarlas
     * en el panel de mensajes del Controlador.
     * Devuelve String[] porque el Controlador espera un array de Strings
     * para inyectarlo directamente en el panel derecho del mapa.
     *
     * @return array de Strings con el contenido de la enciclopedia
     */
    public String[] getLineasCatalogo() {
        // Usamos una lista para ir añadiendo líneas dinámicamente
        // y al final la convertimos a array con toArray()
        java.util.List<String> lineas = new java.util.ArrayList<>();

        lineas.add("── 📖 ENCICLOPEDIA ──");

        // ── Insectos ──────────────────────────────────────────────────────────
        // Insecto.ESPECIES.length es el total de especies posibles
        lineas.add("🦋 Insectos (" + insectosRegistrados.size() + "/" + Insecto.ESPECIES.length + "):");
        if (insectosRegistrados.isEmpty()) {
            lineas.add("  Ninguno capturado aún.");
        } else {
            // Recorremos los valores del mapa (los objetos Insecto)
            for (Insecto i : insectosRegistrados.values()) {
                lineas.add(i.getSkin() + " " + i.getNombre());
                lineas.add("   " + i.getDescripcion());
            }
        }

        lineas.add("─────────────────────");

        // ── Peces ─────────────────────────────────────────────────────────────
        lineas.add("🐟 Peces (" + pecesRegistrados.size() + "/" + Pez.ESPECIES.length + "):");
        if (pecesRegistrados.isEmpty()) {
            lineas.add("  Ninguno pescado aún.");
        } else {
            for (Pez p : pecesRegistrados.values()) {
                lineas.add(p.getSkin() + " " + p.getNombre()
                        + "  [" + p.getTamañoSombra() + "]");
                lineas.add("   " + p.getDescripcion());
            }
        }

        lineas.add("─────────────────────");
        lineas.add("[ENTER] Cerrar");

        // toArray(new String[0]) convierte la lista a array de Strings
        return lineas.toArray(new String[0]);
    }
}