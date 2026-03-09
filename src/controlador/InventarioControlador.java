package controlador;

import coleccionables.Mochila;
import coleccionables.Objeto;
import coleccionables.Herramienta;
import mundo.Celda;
import mundo.Mundo;
import personajes.Pj;

import java.util.List;
import java.util.Scanner;

/**
 * Gestiona la interfaz de la mochila del jugador.
 * Se encarga de mostrar el contenido, paginar las líneas si no caben
 * en el panel, equipar herramientas y soltar objetos al suelo.
 *
 * Recibe referencias al jugador, el mundo y el Controlador principal
 * porque necesita acceder a todos ellos para funcionar.
 */
public class InventarioControlador {

    // Referencias a los objetos principales del juego.
    // Son 'final' porque nunca cambian una vez asignados en el constructor.
    private final Scanner scanner;       // para leer la entrada del jugador
    private final Pj jugador;            // el jugador cuya mochila gestionamos
    private final Mundo mundo;           // el mundo, para soltar objetos al suelo
    private final Controlador controlador; // el controlador principal, para dibujar y mostrar mensajes

    /**
     * Constructor. Recibe todas las referencias que necesita para funcionar.
     *
     * @param scanner     lector de entrada del teclado
     * @param jugador     el jugador cuya mochila se gestiona
     * @param mundo       el mundo del juego
     * @param controlador el controlador principal
     */
    public InventarioControlador(Scanner scanner, Pj jugador, Mundo mundo, Controlador controlador) {
        this.scanner = scanner;
        this.jugador = jugador;
        this.mundo = mundo;
        this.controlador = controlador;
    }

    // ── Métodos ────────────────────────────────────────────────────────────────

    /**
     * Abre la mochila y muestra su contenido en el panel de mensajes.
     * Gestiona la paginación si hay más líneas de las que caben en el panel.
     *
     * El bucle principal espera la entrada del jugador:
     * - Número → selecciona un objeto para equipar o soltar
     * - G      → guarda la herramienta en mano en la mochila
     * - N/P    → página siguiente/anterior
     * - Q      → cierra la mochila
     */
    public void abrirMochila() {
        // Si el jugador no tiene mochila no podemos abrirla
        if (!jugador.tieneMochila()) {
            controlador.mensaje("❌ No tienes mochila equipada.");
            return;
        }

        Mochila mochila = jugador.getMochilaEquipada();
        int filasHud = 9; // filas que ocupa el HUD en el panel derecho
        // hueco: líneas disponibles para mostrar el inventario
        int hueco = Math.max(1, controlador.getMundo().getAlto() - filasHud - 4);
        int pag = 0; // página actual, empieza en 0

        // Bucle principal: se repite hasta que el jugador pulsa Q o ENTER
        while (true) {

            // ── Construimos todas las líneas del inventario ────────────────────
            List<String> todasLineas = new java.util.ArrayList<>();
            todasLineas.add("── 🎒 MOCHILA ──");
            todasLineas.add("Espacio: " + mochila.getTotalObjetos() + "/" + mochila.getCapacidadMaxima());
            todasLineas.add("─────────────────");

            if (mochila.getContenido().isEmpty()) {
                todasLineas.add("  (vacía)");
            } else {
                // Recorremos los grupos de la mochila y mostramos cada uno
                // con su número, skin, nombre y cantidad
                List<String> grupos = mochila.getNombresGrupos();
                for (int i = 0; i < grupos.size(); i++) {
                    String nombre  = grupos.get(i);
                    int cantidad   = mochila.getContenido().get(nombre).size();
                    String skin    = mochila.getContenido().get(nombre).get(0).getSkin();
                    todasLineas.add("[" + (i + 1) + "] " + skin + " " + nombre + " x" + cantidad);
                }
            }

            todasLineas.add("─────────────────");
            // Mostramos la herramienta en mano o "(vacía)" si no hay ninguna
            String mano = jugador.tieneHerramienta()
                    ? jugador.getHerramientaEnMano().getSkin() + " " + jugador.getHerramientaEnMano().getNombre()
                    : "(vacía)";
            todasLineas.add("✋ Mano: " + mano);
            if (jugador.tieneHerramienta()) todasLineas.add("[G] Guardar herramienta");

            // ── Paginación ────────────────────────────────────────────────────
            // Calculamos cuántas páginas hay y ajustamos la página actual
            // por si al guardar/soltar algo la lista se ha quedado más corta
            int totalPags = Math.max(1, (int) Math.ceil((double) todasLineas.size() / hueco));
            pag = Math.min(pag, totalPags - 1);

            // Inyectamos solo las líneas de la página actual en el panel
            controlador.getMensajes().clear();
            int desde = pag * hueco;
            int hasta = Math.min(desde + hueco, todasLineas.size());
            for (int i = desde; i < hasta; i++) {
                controlador.mensaje(todasLineas.get(i));
            }

            // Mostramos los controles de paginación o el botón de cerrar
            if (totalPags > 1) {
                controlador.mensaje("N sig  P ant  Q cerrar  (" + (pag + 1) + "/" + totalPags + ")");
            } else {
                controlador.mensaje("[Q] Cerrar mochila");
            }

            controlador.redibujar();
            System.out.print("\n> ");
            String input = scanner.nextLine().trim().toUpperCase();

            // ── Procesamos la entrada ─────────────────────────────────────────
            if (input.isEmpty() || input.equals("Q")) break; // cerramos la mochila
            if (input.equals("N") && pag < totalPags - 1) { pag++; continue; } // página siguiente
            if (input.equals("P") && pag > 0)             { pag--; continue; } // página anterior

            // [G] → guarda la herramienta en mano en la mochila
            if (input.equals("G") && jugador.tieneHerramienta()) {
                Herramienta h = jugador.getHerramientaEnMano();
                try {
                    if (mochila.guardar(h)) {
                        jugador.setHerramientaEnMano(null); // vaciamos la mano
                        controlador.getMensajes().clear();
                        controlador.mensaje("✋ " + h.getNombre() + " guardada en la mochila.");
                    }
                } catch (excepciones.MochilaLlenaException e) {
                    controlador.mensaje("❌ " + e.getMessage());
                }
                continue;
            }

            // Número → seleccionamos un objeto del inventario
            try {
                int opcion = Integer.parseInt(input) - 1; // restamos 1 porque la lista empieza en 0
                List<String> grupos = mochila.getNombresGrupos();
                if (opcion >= 0 && opcion < grupos.size()) {
                    manejarSoltar(mochila, grupos.get(opcion));
                }
            } catch (NumberFormatException e) {
                // Entrada inválida (no es un número ni un comando) → ignoramos
            }
        }

        controlador.getMensajes().clear();
    }

    /**
     * Muestra las opciones para un objeto seleccionado: equipar o soltar al suelo.
     * Solo aparece la opción de equipar si el objeto es una herramienta.
     * Si el jugador ya tenía una herramienta equipada la devuelve a la mochila.
     *
     * @param mochila      la mochila del jugador
     * @param nombreObjeto nombre del objeto seleccionado
     */
    private void manejarSoltar(Mochila mochila, String nombreObjeto) {
        Objeto obj = mochila.getContenido().get(nombreObjeto).get(0);
        boolean esHerramienta = obj instanceof Herramienta; // comprueba si es herramienta

        controlador.getMensajes().clear();
        controlador.mensaje("── " + obj.getSkin() + " " + nombreObjeto + " ──");
        if (esHerramienta) controlador.mensaje("[1] Equipar en mano");
        controlador.mensaje("[2] Soltar al suelo");
        controlador.mensaje("[ENTER] Cancelar");

        controlador.redibujar();
        System.out.print("\n> ");
        String input = scanner.nextLine().trim();
        controlador.getMensajes().clear();

        // [1] → equipar herramienta en mano
        if (esHerramienta && input.equals("1")) {
            Objeto sacado = mochila.sacar(nombreObjeto);
            // Si ya tenía herramienta la devolvemos a la mochila antes de equipar la nueva
            if (jugador.tieneHerramienta()) {
                try {
                    mochila.guardar(jugador.getHerramientaEnMano());
                } catch (excepciones.MochilaLlenaException e) {
                    controlador.mensaje("❌ " + e.getMessage());
                }
                controlador.mensaje("↩️  " + jugador.getHerramientaEnMano().getNombre() + " devuelta.");
            }
            jugador.setHerramientaEnMano((Herramienta) sacado);
            controlador.mensaje("✋ Equipado: " + sacado.getSkin() + " " + sacado.getNombre());

            // [2] → soltar objeto al suelo en la posición actual del jugador
        } else if (input.equals("2")) {
            Celda celda = mundo.getCelda(jugador.getX(), jugador.getY());
            // Solo podemos soltar si la celda existe y no hay ya un objeto encima
            if (celda == null || celda.getObjetoEncima() != null) {
                controlador.mensaje("❌ No hay sitio en el suelo aquí.");
                return;
            }
            Objeto sacado = mochila.sacar(nombreObjeto);
            celda.setObjetoEncima(sacado);
            controlador.mensaje("📦 " + sacado.getSkin() + " " + sacado.getNombre() + " en el suelo.");
        }
    }
}