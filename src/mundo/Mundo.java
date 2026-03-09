package mundo;


import coleccionables.Objeto;
import coleccionables.Insecto;
import coleccionables.ArbolFrutal;


/**
 * Representa el mundo del juego: el mapa completo con todas sus celdas.
 * Se encarga de construir el mapa, dibujarlo en la terminal y actualizar
 * el estado de los árboles cada turno.
 *
 * El mapa se define como un array de Strings donde cada carácter representa
 * una celda:
 * <pre>
 *   '.' → hierba (atravesable)
 *   'T' → árbol frutal (no atravesable)
 *   'W' → agua (no atravesable, permite pescar)
 *   'R' → roca (no atravesable)
 *   'H' → casa (no atravesable)
 *   'I' → insecto aleatorio (atravesable)
 * </pre>
 */
public class Mundo {


    private int ancho;       // número de columnas del mapa
    private int alto;        // número de filas del mapa
    private Celda[][] mapa;  // matriz de celdas que forma el mapa completo


    // El diseño del pueblo se define como un array de Strings.
    // Cada String es una fila del mapa y cada carácter es una celda.
    // Es más fácil de leer y modificar que una matriz de números.
    private String[] diseñoPueblo = {
            "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT",
            "T.......T......I...................T",
            "T..H....T..........................T",
            "T.......T.......WWWWW..............T",
            "T...I...T......WWWWWWW.............T",
            "TTTT............WWWWW..............T",
            "T.......T..........................T",
            "T.......TTTTTT.....................T",
            "T............T....I................T",
            "T....R.......T....H................T",
            "T.......I....T.....................T",
            "T.......................TTTTTTTTTTTT",
            "T..................................T",
            "TTTTTTTTTT.........................T",
            "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT",
    };


    /**
     * Constructor. Crea el mundo calculando su tamaño a partir del diseño
     * y construyendo la matriz de celdas.
     * No tiene constructor vacío porque un mundo sin diseño no tiene sentido.
     */
    public Mundo() {
        this.alto = diseñoPueblo.length;          // número de filas
        this.ancho = diseñoPueblo[0].length();    // número de columnas (longitud de la primera fila)
        this.mapa = new Celda[alto][ancho];        // creamos la matriz vacía
        construirMundo();                          // la rellenamos con celdas
    }


    // ── Getters ────────────────────────────────────────────────────────────────


    public int getAncho() { return ancho; }
    public int getAlto()  { return alto; }


    // ── Métodos ────────────────────────────────────────────────────────────────


    /**
     * Construye el mapa recorriendo el diseño carácter a carácter.
     * Usa dos bucles anidados: el exterior recorre las filas (y) y el interior
     * recorre las columnas (x). Para cada posición crea la celda correspondiente.
     *
     * El bucle exterior avanza fila a fila. Para cada fila el bucle interior
     * recorre todas las columnas — como leer un libro línea a línea.
     *
     * No devuelve nada (void) porque solo inicializa el estado interno del mundo.
     */
    private void construirMundo() {
        // y recorre las filas (0 hasta alto-1)
        for (int y = 0; y < alto; y++) {
            String fila = diseñoPueblo[y];
            // x recorre las columnas de cada fila (0 hasta ancho-1)
            for (int x = 0; x < ancho; x++) {
                char tipo = fila.charAt(x); // carácter en la posición (x, y)
                mapa[y][x] = new Celda("."); // por defecto todas las celdas son hierba
                try {
                    switch (tipo) {
                        // Switch moderno con ->: más limpio que el clásico con : y break.
                        // Cada case hace una cosa sin necesitar break al final.
                        // Si un case necesita varias líneas se usan llaves {}.


                        // 'T' → árbol frutal. No reemplaza la celda, se coloca encima.
                        case 'T' -> mapa[y][x].setObjetoEncima(new ArbolFrutal("Árbol"));


                        // 'R' → roca. Se crea como Objeto directamente porque no
                        // necesita lógica especial, solo existe para bloquear el paso.
                        case 'R' -> mapa[y][x].setObjetoEncima(new Objeto("🪨", "Roca", 0));


                        // 'H' → casa. Igual que la roca pero con emoji de casa.
                        case 'H' -> mapa[y][x].setObjetoEncima(new Objeto("🏠", "Casa", 0));


                        // 'W' → agua. Reemplaza la celda entera porque el agua es
                        // un tipo de suelo, no un objeto que se coloca encima.
                        case 'W' -> mapa[y][x] = new Celda("🌊");


                        // 'I' → insecto aleatorio. Necesita varias líneas por eso usa {}.
                        case 'I' -> {
                            // Math.random() genera un decimal entre 0 y 1, ej: 0.73
                            // * ESPECIES.length lo multiplica por 4 → 2.92
                            // (int) lo convierte a entero redondeando hacia abajo → 2
                            int azar = (int) (Math.random() * Insecto.ESPECIES.length);


                            // Cogemos la fila de la matriz que corresponde a esa especie
                            // Si azar es 2: {"🐞", "Mariquita", "200", "1", "descripcion"}
                            String[] datos = Insecto.ESPECIES[azar];


                            // Creamos el insecto con esos datos.
                            // Integer.parseInt() convierte "200" (String) a 200 (int)
                            // porque en la matriz todos los datos son Strings pero el
                            // constructor de Insecto espera int para precio y dificultad.
                            mapa[y][x].setObjetoEncima(new Insecto(
                                    datos[0],                   // skin
                                    datos[1],                   // nombre
                                    Integer.parseInt(datos[2]), // precio
                                    Integer.parseInt(datos[3]), // dificultad
                                    datos[4]));                 // descripcion
                        }
                    }
                } catch (excepciones.NombreInvalidoException e) {
                    System.err.println("Error al construir mundo: " + e.getMessage());
                }
            }
        }
    }


    /**
     * Devuelve la celda en la posición (x, y) del mapa.
     * Comprueba primero que la posición esté dentro de los límites porque
     * si no Java lanzaría un ArrayIndexOutOfBoundsException — el equivalente
     * a intentar leer la página 200 de un libro de 100 páginas.
     *
     * Fíjate que se accede como mapa[y][x] y no mapa[x][y] — en las matrices
     * Java primero va la fila (y) y luego la columna (x).
     *
     * Devuelve Celda porque el Controlador necesita la celda para operar con ella.
     * Devuelve null si la posición está fuera del mapa — el Controlador lo
     * interpreta como "no puedes ir ahí".
     *
     * @param x posición horizontal
     * @param y posición vertical
     * @return la celda en esa posición, o null si está fuera del mapa
     */
    public Celda getCelda(int x, int y) {
        if (x >= 0 && x < ancho && y >= 0 && y < alto) return mapa[y][x];
        return null;
    }


    // Ancho visual fijo que ocupa cada fila del mapa en la terminal.
    // Se calcula una vez y se reutiliza para alinear el panel derecho.
    private int anchoVisualMapa = -1;


    /**
     * Dibuja el mapa en la terminal con un panel lateral de información.
     * El panel izquierdo muestra el mapa y el derecho el HUD y los mensajes.
     *
     * Usa "\033[H" para mover el cursor al inicio sin borrar la pantalla,
     * lo que evita el parpadeo que produciría limpiar y redibujar todo.
     *
     * Usa StringBuilder en vez de concatenar Strings con + porque es más
     * eficiente — concatenar con + crea un objeto nuevo en memoria cada vez,
     * StringBuilder acumula todo y lo convierte a String al final.
     *
     * @param mensajes  mensajes del juego que aparecen en el panel derecho
     * @param hudLineas líneas del HUD con las estadísticas del jugador
     */
    public void dibujarMapa(String[] mensajes, String[] hudLineas) {
        // Calculamos el ancho visual de una fila del mapa la primera vez
        if (anchoVisualMapa < 0) {
            anchoVisualMapa = calcularAnchoVisualFila(0);
        }


        // Movemos el cursor al inicio sin borrar la pantalla (evita parpadeo)
        System.out.print("\033[H");
        StringBuilder sb = new StringBuilder();


        for (int y = 0; y < alto; y++) {
            StringBuilder fila = new StringBuilder();


            // 1. Dibujamos cada celda de la fila
            for (int x = 0; x < ancho; x++) {
                fila.append(mapa[y][x].obtenerCaracterADibujar());
            }


            // 2. Compensamos el ancho visual para alinear el panel derecho.
            // Los emojis ocupan 2 columnas en la terminal en vez de 1,
            // por eso necesitamos calcular el ancho real y añadir espacios.
            int anchoFilaActual = anchoVisual(fila.toString());
            int padding = Math.max(0, anchoVisualMapa - anchoFilaActual);
            fila.append(" ".repeat(padding));


            // 3. Separador azul entre el mapa y el panel derecho
            fila.append(" \u001B[34m│\u001B[0m ");


            // 4. Panel derecho: primero el HUD, luego los mensajes
            String lineaDerecha = "";
            if (y < hudLineas.length) {
                lineaDerecha = hudLineas[y];
            } else {
                int idx = y - hudLineas.length;
                if (idx >= 0 && idx < mensajes.length) {
                    lineaDerecha = mensajes[idx];
                }
            }
            fila.append(padDerecha(lineaDerecha, 46));
            fila.append("\n");
            sb.append(fila);
        }


        System.out.print(sb);
        System.out.flush();
    }


    /**
     * Calcula el ancho visual de un String en columnas de terminal.
     * No podemos usar String.length() porque los emojis cuentan como
     * 1 carácter pero ocupan 2 columnas. Este método cuenta columnas reales.
     *
     * Usa codePointAt() en vez de charAt() porque algunos emojis ocupan
     * 2 posiciones internas en Java (son pares surrogate).
     *
     * @param s texto cuyo ancho visual queremos calcular
     * @return número de columnas que ocupa el texto en la terminal
     */
    private int anchoVisual(String s) {
        // Eliminamos los códigos ANSI antes de calcular el ancho
        // porque esos no ocupan espacio visual en la terminal
        String limpio = s.replaceAll("\u001B\\[[;\\d]*m", "");
        int ancho = 0;
        for (int i = 0; i < limpio.length(); ) {
            int cp = limpio.codePointAt(i);
            ancho += anchoCodePoint(cp);
            i += Character.charCount(cp); // avanza 1 o 2 posiciones según el carácter
        }
        return ancho;
    }


    /**
     * Devuelve el ancho visual de un carácter Unicode en columnas de terminal.
     * Los emojis y caracteres CJK (chino, japonés, coreano) ocupan 2 columnas,
     * el resto ocupa 1.
     *
     * Cada carácter Unicode tiene un número identificador llamado codepoint.
     * Los rangos en hexadecimal:
     * - 0x1F000 en adelante → emojis modernos (🌳, 🦋, 🐟...)
     * - 0x2600-0x27BF       → símbolos varios (☀️, ♠️...)
     * - 0x2E80-0x9FFF       → caracteres chinos, japoneses y coreanos
     *
     * @param cp codepoint del carácter
     * @return 2 si es de doble ancho, 1 si no
     */
    private int anchoCodePoint(int cp) {
        if (cp >= 0x1F000) return 2;
        if (cp >= 0x2600 && cp <= 0x27BF) return 2;
        if (cp >= 0x2E80 && cp <= 0x9FFF) return 2;
        if (cp >= 0xF900 && cp <= 0xFAFF) return 2;
        if (cp >= 0xFE30 && cp <= 0xFE4F) return 2;
        return 1;
    }


    /**
     * Calcula el ancho visual de una fila completa del mapa.
     * Se llama una sola vez y el resultado se guarda en anchoVisualMapa
     * para no recalcularlo en cada redibujado.
     *
     * @param fila índice de la fila a calcular
     * @return ancho visual de esa fila en columnas de terminal
     */
    private int calcularAnchoVisualFila(int fila) {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < ancho; x++) {
            sb.append(mapa[fila][x].obtenerCaracterADibujar());
        }
        return anchoVisual(sb.toString());
    }


    /**
     * Rellena un texto con espacios hasta alcanzar una longitud visual fija.
     * Se usa para que todas las líneas del panel derecho tengan el mismo ancho
     * aunque tengan distinto contenido.
     *
     * @param texto    texto a rellenar
     * @param longitud longitud visual deseada en columnas
     * @return texto rellenado con espacios hasta la longitud indicada
     */
    private String padDerecha(String texto, int longitud) {
        int visible = anchoVisual(texto);
        if (visible >= longitud) return texto;
        return texto + " ".repeat(longitud - visible);
    }


    /**
     * Actualiza el contador de regeneración de todos los árboles del mapa.
     * Se llama cada vez que el jugador se mueve.
     *
     * Usa pattern matching de Java moderno: "instanceof ArbolFrutal arbol"
     * hace dos cosas a la vez — comprueba si el objeto es un ArbolFrutal
     * y lo asigna a la variable arbol. Sin pattern matching habría que escribir:
     * if (obj instanceof ArbolFrutal) { ArbolFrutal arbol = (ArbolFrutal) obj; ... }
     *
     * No devuelve nada (void) porque solo actualiza el estado interno del mundo.
     */
    public void actualizarArboles() {
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                Objeto obj = mapa[y][x].getObjetoEncima();
                // Pattern matching: comprueba si es ArbolFrutal y lo asigna a 'arbol'
                if (obj instanceof ArbolFrutal arbol) {
                    arbol.actualizarRegeneracion();
                }
            }
        }
    }
}
