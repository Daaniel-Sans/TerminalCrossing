package controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import mundo.Mundo;
import mundo.Celda;
import personajes.Pj;
import coleccionables.*;
import excepciones.HerramientaRotaException;
import excepciones.MochilaLlenaException;
import excepciones.NombreInvalidoException;

/**
 * Cerebro del juego. Conecta todos los componentes y gestiona el bucle principal.
 *
 * Se encarga de:
 * - Leer la entrada del teclado y decidir qué hacer
 * - Mover al jugador por el mapa
 * - Gestionar las interacciones con objetos del mundo (F)
 * - Lanzar los minijuegos de pesca y captura de insectos
 * - Dibujar el mapa con el HUD y los mensajes en cada turno
 */
public class Controlador {

    private final Mundo mundo;
    private final Pj jugador;
    private boolean juegoEnMarcha;
    private final Scanner scanner;
    private final Catalogo catalogo;
    private final InventarioControlador inventario;

    // Historial de mensajes que aparecen en el panel derecho.
    // MAX_MENSAJES limita cuántos se muestran a la vez para no desbordar el panel.
    private final List<String> mensajes = new ArrayList<>();
    private static final int MAX_MENSAJES = 20;

    // Códigos ANSI para colorear el texto en la terminal.
    // Son constantes static final porque nunca cambian y se usan en toda la clase.
    private static final String RESET  = "\u001B[0m";
    private static final String BOLD   = "\u001B[1m";
    private static final String CYAN   = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN  = "\u001B[32m";
    private static final String BLUE   = "\u001B[34m";

    /**
     * Constructor. Recibe el mundo y el jugador ya creados desde Main.
     * Crea el Scanner, el Catálogo y el InventarioControlador internamente.
     *
     * @param mundo   el mundo del juego con el mapa completo
     * @param jugador el jugador que controla el usuario
     */
    public Controlador(Mundo mundo, Pj jugador) {
        this.mundo = mundo;
        this.jugador = jugador;
        this.juegoEnMarcha = true;
        this.scanner = new Scanner(System.in);
        this.catalogo = new Catalogo();
        // Le pasamos 'this' para que InventarioControlador pueda llamar a
        // mensaje(), redibujar() y getMensajes() del Controlador principal
        this.inventario = new InventarioControlador(scanner, jugador, mundo, this);
    }

    // ── Bucle principal ────────────────────────────────────────────────────────

    /**
     * Inicia y mantiene el bucle principal del juego.
     * En cada iteración: dibuja el mapa, lee la entrada y procesa la acción.
     * El bucle termina cuando el jugador pulsa Q o se queda sin energía.
     */
    public void iniciarJuego() {
        mensaje("¡Bienvenido a Terminal Crossing!");
        mensaje("Usa WASD para moverte.");

        while (juegoEnMarcha) {
            redibujar();
            System.out.print("\n> ");
            String input = scanner.nextLine().toUpperCase().trim();
            mensajes.clear(); // limpiamos los mensajes del turno anterior

            // Switch moderno: cada tecla ejecuta una acción distinta
            switch (input) {
                case "Q" -> juegoEnMarcha = false;       // salir del juego
                case "I" -> inventario.abrirMochila();   // abrir mochila
                case "E" -> intentarComer();             // comer una fruta
                case "F" -> intentarInteractuar();       // interactuar con lo de alrededor
                case "C" -> abrirCatalogo();             // abrir enciclopedia
                default  -> procesarMovimiento(input);   // mover al jugador
            }
        }

        // Limpiamos la pantalla al salir
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("👋 ¡Hasta pronto!");
    }

    // ── Redibujo ───────────────────────────────────────────────────────────────

    /**
     * Redibuja el mapa con el HUD y los mensajes actuales.
     * Se llama en cada turno y cada vez que el estado del juego cambia.
     */
    public void redibujar() {
        mundo.dibujarMapa(getMensajesArray(), getHudLineas());
    }

    /**
     * Construye las líneas del HUD con las estadísticas del jugador.
     * Incluye la barra de energía, la herramienta en mano, la mochila
     * y los controles disponibles.
     * Devuelve String[] porque dibujarMapa() espera un array de Strings.
     *
     * @return array de Strings con el HUD del jugador
     */
    private String[] getHudLineas() {
        // Herramienta en mano o "Vacía" si no lleva ninguna
        String mano = jugador.tieneHerramienta()
                ? jugador.getHerramientaEnMano().getSkin() + " " + jugador.getHerramientaEnMano().getNombre()
                : "Vacía";

        // Mochila equipada con ocupación o "Sin mochila"
        String mochila = jugador.tieneMochila()
                ? "🎒 " + jugador.getMochilaEquipada().getTotalObjetos()
                + "/" + jugador.getMochilaEquipada().getCapacidadMaxima()
                : "Sin mochila";

        // Barra de energía visual: █ por cada 10 de energía, ░ por el resto
        int e = jugador.getEnergiaActual();
        String barra = GREEN + "[" + "█".repeat(e / 10) + "░".repeat(10 - e / 10) + "]" + RESET;

        return new String[]{
                BOLD + CYAN + "┌─ " + jugador.getNombre() + " ─┐" + RESET,
                YELLOW + "⚡ " + barra + " " + e + "/100" + RESET,
                "✋ " + mano,
                mochila,
                BLUE + "├─────────────────┤" + RESET,
                GREEN + "WASD Mover  F Interactuar" + RESET,
                GREEN + "E Comer  I Mochila  C Catálogo" + RESET,
                GREEN + "Q Salir" + RESET,
                BLUE + "├─────────────────┤" + RESET,
        };
    }

    /**
     * Devuelve los últimos MAX_MENSAJES mensajes para mostrarlos en el panel.
     * Si hay más mensajes que el máximo solo muestra los más recientes.
     *
     * @return array de Strings con los mensajes visibles
     */
    private String[] getMensajesArray() {
        int desde = Math.max(0, mensajes.size() - MAX_MENSAJES);
        List<String> visibles = mensajes.subList(desde, mensajes.size());
        return visibles.toArray(new String[0]);
    }

    // ── Mensajes y getters públicos ────────────────────────────────────────────

    /**
     * Añade un mensaje al panel derecho del mapa.
     * Lo usan InventarioControlador y otros métodos para comunicarse con el jugador.
     *
     * @param texto mensaje a mostrar
     */
    public void mensaje(String texto) {
        mensajes.add(texto);
    }

    /**
     * Devuelve la lista de mensajes.
     * La usa InventarioControlador para limpiarla con mensajes.clear().
     *
     * @return lista de mensajes activos
     */
    public List<String> getMensajes() {
        return mensajes;
    }

    /**
     * Devuelve el mundo del juego.
     * Lo usa InventarioControlador para calcular el espacio disponible en el panel.
     *
     * @return el mundo del juego
     */
    public Mundo getMundo() {
        return mundo;
    }

    // ── Movimiento ─────────────────────────────────────────────────────────────

    /**
     * Procesa una tecla de movimiento (W/A/S/D) y mueve al jugador si es posible.
     * Si la celda destino no es atravesable muestra un mensaje de error.
     * Cada movimiento gasta 1 de energía. Si llega a 0 el juego termina.
     *
     * @param tecla tecla pulsada (W/A/S/D)
     */
    private void procesarMovimiento(String tecla) {
        // Calculamos la nueva posición según la tecla
        int nx = jugador.getX(), ny = jugador.getY();
        switch (tecla) {
            case "W" -> ny--; // arriba
            case "S" -> ny++; // abajo
            case "A" -> nx--; // izquierda
            case "D" -> nx++; // derecha
            default  -> { mensaje("❓ Tecla desconocida."); return; }
        }

        Celda destino = mundo.getCelda(nx, ny);
        if (destino != null && destino.esAtravesable()) {
            // Quitamos al jugador de la celda actual y lo ponemos en la nueva
            mundo.getCelda(jugador.getX(), jugador.getY()).setPersonajeEncima(null);
            jugador.setX(nx);
            jugador.setY(ny);
            destino.setPersonajeEncima(jugador);
        } else {
            mensaje("❌ No puedes ir ahí.");
        }

        // Actualizamos los árboles después de cada movimiento
        mundo.actualizarArboles();

        // Restamos energía y comprobamos si el jugador se ha quedado sin ella
        jugador.setEnergiaActual(jugador.getEnergiaActual() - 1);
        if (jugador.getEnergiaActual() <= 0) {
            jugador.setEnergiaActual(0);
            redibujar();
            System.out.println("\n💀 ¡Te has quedado sin energía! Game over.");
            System.out.println("Pulsa ENTER para salir...");
            scanner.nextLine();
            juegoEnMarcha = false;
        }
    }

    // ── Interacción (tecla F) ──────────────────────────────────────────────────

    /**
     * Comprueba las cuatro celdas adyacentes al jugador y actúa según lo que haya.
     * La prioridad es: agua (pesca) → insecto → mochila → árbol → herramienta → objeto genérico.
     * Si no hay nada cerca muestra un mensaje de error.
     */
    private void intentarInteractuar() {
        boolean accionRealizada = false;
        // Las cuatro posiciones adyacentes: arriba, abajo, izquierda, derecha
        int[][] adyacentes = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        for (int[] pos : adyacentes) {
            int cx = jugador.getX() + pos[0];
            int cy = jugador.getY() + pos[1];
            Celda celda = mundo.getCelda(cx, cy);
            if (celda == null) continue;

            // Agua → minijuego de pesca
            if (celda.esAgua()) {
                minijuegoPesca();
                accionRealizada = true;
                break;
            }

            Objeto algo = celda.getObjetoEncima();
            if (algo == null) continue;

            // Insecto → minijuego de captura
            if (algo instanceof Insecto insecto) {
                minijuegoCaptura(insecto, celda);
                accionRealizada = true;
                break;
            }

            // Mochila → equiparla si no llevamos ninguna
            if (algo instanceof Mochila mochila) {
                if (!jugador.tieneMochila()) {
                    jugador.equiparMochila(mochila);
                    celda.setObjetoEncima(null);
                    mensaje("✨ ¡Mochila equipada!");
                } else {
                    mensaje("❌ Ya llevas una mochila.");
                }
                accionRealizada = true;
                break;
            }

            // Árbol frutal → sacudirlo para obtener fruta
            if (algo instanceof ArbolFrutal arbol) {
                if (arbol.isTieneFruta()) {
                    mensaje("🌳 ¡Sacudes el árbol!");
                    arbol.sacudir();
                    try {
                        guardarEnMochila(new Fruta("🍎", "Manzana", 20));
                    } catch (NombreInvalidoException ex) {
                        mensaje("❌ Error al crear fruta: " + ex.getMessage());
                    }
                } else {
                    mensaje("🌳 El árbol no tiene fruta aún.");
                }
                accionRealizada = true;
                break;
            }

            // Herramienta → equipar en mano o guardar en mochila
            if (algo instanceof Herramienta herramienta) {
                redibujar();
                mensaje("🔧 " + herramienta.getSkin() + " " + herramienta.getNombre());
                mensaje("[1] Equipar  [2] Guardar  [ENTER] Cancelar");
                redibujar();
                System.out.print("\n> ");
                String op = scanner.nextLine().trim();
                mensajes.clear();
                if (op.equals("1")) {
                    // Si ya llevaba herramienta la guardamos en la mochila antes
                    if (jugador.tieneHerramienta()) guardarEnMochila(jugador.getHerramientaEnMano());
                    jugador.setHerramientaEnMano(herramienta);
                    celda.setObjetoEncima(null);
                    mensaje("✋ Equipado: " + herramienta.getNombre());
                } else if (op.equals("2")) {
                    if (guardarEnMochila(herramienta)) celda.setObjetoEncima(null);
                }
                accionRealizada = true;
                break;
            }

            // Objeto genérico → guardarlo en la mochila
            redibujar();
            mensaje("📦 " + algo.getSkin() + " " + algo.getNombre());
            mensaje("[1] Guardar en mochila  [ENTER] Cancelar");
            redibujar();
            System.out.print("\n> ");
            String op = scanner.nextLine().trim();
            mensajes.clear();
            if (op.equals("1")) {
                if (guardarEnMochila(algo)) celda.setObjetoEncima(null);
            }
            accionRealizada = true;
            break;
        }

        if (!accionRealizada) mensaje("❌ Nada cerca con lo que interactuar.");
    }

    // ── Minijuego: captura de insecto ──────────────────────────────────────────

    /**
     * Minijuego de captura de insecto. El jugador tiene 2.5 segundos para pulsar X.
     * Requiere tener una red en mano y mochila con espacio.
     * Si captura al insecto lo registra en el catálogo y lo guarda en la mochila.
     *
     * @param insecto el insecto a capturar
     * @param celda   la celda donde está el insecto
     */
    private void minijuegoCaptura(Insecto insecto, Celda celda) {
        if (!jugador.tieneHerramientaDeTipo("RED")) {
            mensaje("🦋 Necesitas una RED para atrapar al " + insecto.getNombre() + ".");
            return;
        }
        if (!jugador.tieneMochila()) {
            mensaje("🦋 Necesitas una mochila para guardar el " + insecto.getNombre() + ".");
            return;
        }
        if (!jugador.getMochilaEquipada().hayEspacio()) {
            mensaje("🦋 La mochila está llena.");
            return;
        }

        mensaje(insecto.getSkin() + " ¡UN " + insecto.getNombre().toUpperCase() + "!");
        mensaje("⚠️  Pulsa X + ENTER rápido (2.5 seg)...");
        redibujar();
        System.out.print("\n> ");

        // Medimos el tiempo que tarda el jugador en responder
        long t1 = System.currentTimeMillis();
        String res = scanner.nextLine().toUpperCase();
        long t2 = System.currentTimeMillis();

        mensajes.clear();
        if (res.equals("X") && (t2 - t1) < 2500) {
            // Captura exitosa
            mensaje("✅ ¡Capturado! " + insecto.getSkin() + " " + insecto.getNombre());
            mensaje("📖 " + insecto.getDescripcion());
            try {
                jugador.getHerramientaEnMano().usar(); // gastamos durabilidad de la red
            } catch (HerramientaRotaException e) {
                mensaje("❌ " + e.getMessage());
            }
            // Registramos en el catálogo y avisamos si es especie nueva
            if (catalogo.registrarInsecto(insecto)) {
                mensaje("✨ ¡Nueva especie! " + insecto.getNombre() + " en tu enciclopedia.");
            } else {
                mensaje("📖 Ya conocías el " + insecto.getNombre() + ".");
            }
            guardarEnMochila(insecto);
            celda.setObjetoEncima(null);
        } else {
            // Captura fallida
            mensaje("💨 ¡Se escapó el " + insecto.getNombre() + "!");
            celda.setObjetoEncima(null);
        }
    }

    // ── Minijuego: pesca ───────────────────────────────────────────────────────

    /**
     * Minijuego de pesca. Espera un tiempo aleatorio y el jugador tiene 2 segundos
     * para pulsar 0 cuando pica algo.
     * Requiere tener una caña en mano y mochila con espacio.
     * Si pesca un pez lo registra en el catálogo y lo guarda en la mochila.
     */
    private void minijuegoPesca() {
        if (!jugador.tieneHerramientaDeTipo("CAÑA")) {
            mensaje("🎣 Necesitas una CAÑA DE PESCAR.");
            return;
        }
        if (!jugador.tieneMochila()) {
            mensaje("🎣 Necesitas una mochila para guardar el pez.");
            return;
        }
        if (!jugador.getMochilaEquipada().hayEspacio()) {
            mensaje("🎣 La mochila está llena.");
            return;
        }

        mensaje("🎣 Lanzas el sedal al agua...");
        redibujar();

        // Esperamos un tiempo aleatorio entre 1.5 y 4 segundos antes de que pique
        int espera = 1500 + (int) (Math.random() * 2500);
        try { Thread.sleep(espera); } catch (Exception e) {}

        mensaje("🌊 ¡ALGO PICA! Pulsa 0 + ENTER (2 seg)...");
        redibujar();
        System.out.print("\n> ");

        long t1 = System.currentTimeMillis();
        String res = scanner.nextLine().toUpperCase();
        long t2 = System.currentTimeMillis();

        mensajes.clear();
        if (res.equals("0") && (t2 - t1) < 2000) {
            // Pesca exitosa
            Pez pez = generarPezAleatorio();
            mensaje("🐟 ¡Has pescado un " + pez.getNombre() + "!");
            mensaje("📖 " + pez.getDescripcion());
            try {
                jugador.getHerramientaEnMano().usar(); // gastamos durabilidad de la caña
            } catch (HerramientaRotaException e) {
                mensaje("❌ " + e.getMessage());
            }
            // Registramos en el catálogo y avisamos si es especie nueva
            if (catalogo.registrarPez(pez)) {
                mensaje("✨ ¡Nueva especie! " + pez.getNombre() + " en tu enciclopedia.");
            } else {
                mensaje("📖 Ya conocías el " + pez.getNombre() + ".");
            }
            guardarEnMochila(pez);
        } else {
            // Pesca fallida
            mensaje("💨 Se escapó el pez...");
        }
    }

    /**
     * Genera un pez aleatorio eligiendo una especie al azar de Pez.ESPECIES.
     * Devuelve Pez porque minijuegoPesca() necesita el objeto para guardarlo.
     *
     * @return un pez aleatorio
     */
    private Pez generarPezAleatorio() {
        String[] datos = Pez.ESPECIES[(int) (Math.random() * Pez.ESPECIES.length)];
        try {
            return new Pez(datos[0], datos[1], Integer.parseInt(datos[2]), datos[3], datos[4]);
        } catch (NombreInvalidoException e) {
            // RuntimeException porque este error no debería ocurrir nunca —
            // los datos de ESPECIES son siempre válidos
            throw new RuntimeException("Error al generar pez: " + e.getMessage());
        }
    }

    // ── Comer ──────────────────────────────────────────────────────────────────

    /**
     * Busca la primera fruta en la mochila y se la come el jugador.
     * Si no hay frutas muestra un mensaje de error.
     */
    private void intentarComer() {
        if (jugador.tieneMochila()) {
            Mochila m = jugador.getMochilaEquipada();
            // Recorremos los grupos de la mochila buscando una fruta
            for (String nombre : m.getNombresGrupos()) {
                Objeto obj = m.getContenido().get(nombre).get(0);
                if (obj instanceof Fruta fruta) {
                    m.sacar(nombre); // la sacamos de la mochila
                    jugador.comer(fruta.getEnergiaQueRecupera()); // recuperamos energía
                    mensaje("😋 Comiste una " + fruta.getNombre() + ". ¡Energía recuperada!");
                    return;
                }
            }
        }
        mensaje("❌ No tienes comida en la mochila.");
    }

    // ── Utilidades ─────────────────────────────────────────────────────────────

    /**
     * Intenta guardar un objeto en la mochila del jugador.
     * Muestra mensajes de error si no hay mochila o está llena.
     * Devuelve boolean para que el método que lo llama sepa si tuvo éxito,
     * por ejemplo para eliminar el objeto del suelo solo si se guardó bien.
     *
     * @param obj objeto a guardar
     * @return true si se guardó correctamente
     */
    private boolean guardarEnMochila(Objeto obj) {
        if (jugador.tieneMochila() && jugador.getMochilaEquipada().hayEspacio()) {
            try {
                jugador.getMochilaEquipada().guardar(obj);
            } catch (MochilaLlenaException e) {
                mensaje("❌ " + e.getMessage());
                return false;
            }
            mensaje("🎒 " + obj.getSkin() + " " + obj.getNombre() + " guardado.");
            return true;
        } else if (!jugador.tieneMochila()) {
            mensaje("❌ No tienes mochila.");
        } else {
            mensaje("❌ La mochila está llena.");
        }
        return false;
    }

    /**
     * Abre la enciclopedia y la muestra paginada en el panel de mensajes.
     * Funciona igual que abrirMochila() en InventarioControlador.
     */
    private void abrirCatalogo() {
        String[] lineas = catalogo.getLineasCatalogo();
        int filasHud  = getHudLineas().length;
        int hueco     = Math.max(1, mundo.getAlto() - filasHud - 1);
        int totalPags = (int) Math.ceil((double) lineas.length / hueco);
        int pag       = 0;

        while (true) {
            mensajes.clear();
            int desde = pag * hueco;
            int hasta = Math.min(desde + hueco, lineas.length);
            for (int i = desde; i < hasta; i++) {
                mensajes.add(lineas[i]);
            }
            if (totalPags > 1) {
                mensajes.add("N sig  P ant  Q cerrar  ("
                        + (pag + 1) + "/" + totalPags + ")");
            } else {
                mensajes.add("[Q] Cerrar catálogo");
            }
            redibujar();
            System.out.print("\n> ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.isEmpty() || input.equals("Q")) break;
            if (input.equals("N") && pag < totalPags - 1) pag++;
            if (input.equals("P") && pag > 0) pag--;
        }
        mensajes.clear();
    }
}