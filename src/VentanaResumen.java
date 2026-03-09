import javax.swing.*;
import java.awt.*;
import mundo.Mundo;
import mundo.Celda;
import personajes.Pj;
import controlador.Controlador;
import excepciones.NombreInvalidoException;

/**
 * Ventana de resumen antes de empezar a jugar.
 * Muestra el personaje elegido y el nombre leído del JSON,
 * y al pulsar el botón lanza el juego en la terminal.
 */
public class VentanaResumen extends JFrame {

    /**
     * Constructor. Crea la ventana con el resumen del personaje.
     *
     * @param nombre nombre del jugador introducido en VentanaInicio
     * @param skin   emoji del personaje elegido en VentanaInicio
     */
    public VentanaResumen(String nombre, String skin) {
        setTitle("¡Listo para jugar!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 220);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);

        // Emoji grande del personaje elegido
        JLabel lblSkin = new JLabel(skin, SwingConstants.CENTER);
        lblSkin.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        c.gridy = 0;
        panel.add(lblSkin, c);

        // Leemos el JSON para mostrar el nombre guardado
        // Esto verifica que el guardado en VentanaInicio funcionó correctamente
        String[] guardado = ConfiguracionJSON.leer();
        String nombreMostrado = (guardado != null) ? guardado[0] : nombre;
        JLabel lblNombre = new JLabel("¡Hola, " + nombreMostrado + "!", SwingConstants.CENTER);
        lblNombre.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        c.gridy = 1;
        panel.add(lblNombre, c);

        // Botón para lanzar el juego
        JButton btnJugar = new JButton("▶ ¡A jugar!");
        c.gridy = 2;
        panel.add(btnJugar, c);

        btnJugar.addActionListener(e -> {
            dispose(); // cerramos la ventana
            lanzarJuego(nombre, skin);
        });

        add(panel);
    }

    /**
     * Crea el mundo, el jugador y lanza el controlador del juego.
     * Se llama al pulsar el botón "¡A jugar!".
     *
     * @param nombre nombre del jugador
     * @param skin   emoji del personaje
     */
    private void lanzarJuego(String nombre, String skin) {
        Mundo miMundo = new Mundo();
        Pj jugador;
        try {
            jugador = new Pj(nombre, skin, 10, 10);
        } catch (NombreInvalidoException e) {
            System.err.println("Error al crear personaje: " + e.getMessage());
            return;
        }
        // Colocamos al jugador en su celda inicial
        Celda inicio = miMundo.getCelda(jugador.getX(), jugador.getY());
        if (inicio != null) inicio.setPersonajeEncima(jugador);
        // Esparcimos los objetos por el mapa
        Main.esparcirEntidades(miMundo);
        // Lanzamos el bucle principal del juego
        new Controlador(miMundo, jugador).iniciarJuego();
    }
}