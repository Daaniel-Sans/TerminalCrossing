import mundo.Mundo;
import mundo.Celda;
import coleccionables.*;
import personajes.*;
import excepciones.*;

/**
 * Punto de entrada del programa.
 * Hace dos cosas:
 * 1. Crea objetos de todas las clases para probar que funcionan (requisito UD1)
 * 2. Lanza la ventana de inicio del juego
 */
public class Main {

    public static void main(String[] args) {

        try {
            // ── Probamos todas las clases creando 2 objetos de cada una ───────
            // Esto cumple el requisito de instanciar al menos 2 objetos por clase

            // Frutas
            Fruta manzana = new Fruta("🍎", "Manzana", 20);
            Fruta pera    = new Fruta("🍐", "Pera",    15);
            System.out.println(manzana); // toString personalizado
            System.out.println(pera);

            // Insectos
            Insecto mariposa  = new Insecto("🦋", "Mariposa Común", 100, 1, "Vaya mariposa más hermosa...");
            Insecto escorpion = new Insecto("🦂", "Escorpión",     2000, 3, "Un alacrán asesino.");
            System.out.println(mariposa);
            System.out.println(escorpion);

            // Peces
            Pez pezComun = new Pez("🐟", "Pez Común",  80,   "Pequeña", "Un pececillo sin historia...");
            Pez tiburon  = new Pez("🦈", "Tiburón",    2500, "Grande",  "Tiene dientes, tiene fama...");
            System.out.println(pezComun);
            System.out.println(tiburon);

            // Herramientas — usan el enum TipoHerramienta
            Herramienta red  = new Herramienta("🏸", "Red de mariposas", TipoHerramienta.RED,  10);
            Herramienta caña = new Herramienta("🎣", "Caña de pescar",   TipoHerramienta.CAÑA, 10);
            System.out.println(red);
            System.out.println(caña);

            // Árboles frutales
            ArbolFrutal manzano = new ArbolFrutal("Manzano");
            ArbolFrutal peral   = new ArbolFrutal("Peral");
            System.out.println(manzano);
            System.out.println(peral);

            // Mochilas
            Mochila mochilaGrande = new Mochila("🎒", "Mochila Grande", 15);
            Mochila mochilaSmall  = new Mochila("👜", "Bolsita",         5);
            System.out.println(mochilaGrande);
            System.out.println(mochilaSmall);

            // Jugadores — heredan de Personaje
            Pj jugador1 = new Pj("Alex",  "🦝", 10, 10);
            Pj jugador2 = new Pj("Bella", "🐸", 5,  5);
            System.out.println(jugador1);
            System.out.println(jugador2);

            // NPCs — heredan de Personaje
            Npc tendero = new Npc("Tom Nook", "🦝", 15, 15, "Tendero",   "¡Bienvenido a mi tienda!");
            Npc vecina  = new Npc("Isabela",  "🌺", 20, 10, "Jardinera", "¿Has visto mis flores?");
            System.out.println(tendero);
            System.out.println(vecina);

            // ── Probamos las 4 excepciones personalizadas ─────────────────────
            // Cada excepción se lanza y captura aquí para cumplir el requisito UD4

            // MochilaLlenaException: mochila de capacidad 1, intentamos meter 2 objetos
            Mochila mochitaTest = new Mochila("🎒", "Mini mochila", 1);
            try {
                mochitaTest.guardar(manzana);
                mochitaTest.guardar(pera); // esta lanza MochilaLlenaException
            } catch (MochilaLlenaException e) {
                System.out.println("[CAPTURADA] MochilaLlenaException: " + e.getMessage());
            }

            // EnergiaInsuficienteException: jugador con energía 1 intenta moverse
            try {
                jugador1.setEnergiaActual(1);
                jugador1.mover(); // esta lanza EnergiaInsuficienteException
            } catch (EnergiaInsuficienteException e) {
                System.out.println("[CAPTURADA] EnergiaInsuficienteException: " + e.getMessage());
            }

            // HerramientaRotaException: herramienta con durabilidad 0 intenta usarse
            try {
                Herramienta palaNueva = new Herramienta("⛏️", "Pala", TipoHerramienta.PALA, 0);
                palaNueva.usar(); // esta lanza HerramientaRotaException
            } catch (HerramientaRotaException e) {
                System.out.println("[CAPTURADA] HerramientaRotaException: " + e.getMessage());
            }

            // NombreInvalidoException: fruta con nombre vacío
            try {
                Fruta frutaInvalida = new Fruta("", "", 10); // nombre vacío lanza la excepción
            } catch (NombreInvalidoException e) {
                System.out.println("[CAPTURADA] NombreInvalidoException: " + e.getMessage());
            }

        } catch (NombreInvalidoException e) {
            System.err.println("Error al crear objetos: " + e.getMessage());
        }

        // ── Lanzamos el juego con la ventana de inicio ─────────────────────────
        new VentanaInicio();
    }

    /**
     * Esparce entidades por el mapa al inicio del juego.
     * Coloca árboles en posiciones aleatorias y herramientas en posiciones fijas.
     * Es public static para que VentanaResumen pueda llamarlo sin crear un Main.
     *
     * @param mundo el mundo donde colocar las entidades
     */
    public static void esparcirEntidades(Mundo mundo) {
        try {
            // Colocamos 10 árboles en posiciones aleatorias
            for (int i = 0; i < 10; i++) {
                colocarAleatorio(mundo, new ArbolFrutal("Manzano"));
            }
            // Colocamos la mochila en una posición aleatoria
            colocarAleatorio(mundo, new Mochila("🎒", "Mochila de tela", 10));
            // Colocamos las herramientas en posiciones fijas conocidas
            colocarEnPosicion(mundo, new Herramienta("🏸", "Red de mariposas", TipoHerramienta.RED,  10), 3, 6);
            colocarEnPosicion(mundo, new Herramienta("🎣", "Caña de pescar",   TipoHerramienta.CAÑA, 10), 5, 6);
        } catch (NombreInvalidoException e) {
            System.err.println("Error al esparcir entidades: " + e.getMessage());
        }
    }

    /**
     * Coloca un objeto en una posición concreta del mapa si está libre.
     *
     * @param mundo   el mundo donde colocar el objeto
     * @param objeto  el objeto a colocar
     * @param x       posición horizontal
     * @param y       posición vertical
     */
    private static void colocarEnPosicion(Mundo mundo, coleccionables.Objeto objeto, int x, int y) {
        Celda c = mundo.getCelda(x, y);
        if (c != null && c.getObjetoEncima() == null && c.getPersonajeEncima() == null) {
            c.setObjetoEncima(objeto);
        }
    }

    /**
     * Coloca un objeto en una posición aleatoria libre del mapa.
     * Intenta hasta 200 veces encontrar una celda libre antes de rendirse.
     *
     * @param mundo  el mundo donde colocar el objeto
     * @param objeto el objeto a colocar
     */
    private static void colocarAleatorio(Mundo mundo, Object objeto) {
        boolean colocado = false;
        int intentos = 0;
        // Bucle while: seguimos intentando hasta colocar el objeto o agotar intentos
        while (!colocado && intentos < 200) {
            int rx = (int) (Math.random() * mundo.getAncho());
            int ry = (int) (Math.random() * mundo.getAlto());
            Celda c = mundo.getCelda(rx, ry);
            if (c != null && c.esAtravesable() && c.getObjetoEncima() == null && c.getPersonajeEncima() == null) {
                if (objeto instanceof coleccionables.Objeto obj) {
                    c.setObjetoEncima(obj);
                    colocado = true;
                }
            }
            intentos++;
        }
    }
}