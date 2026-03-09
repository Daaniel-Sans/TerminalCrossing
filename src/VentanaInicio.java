import javax.swing.*;
import java.awt.*;

/**
 * Ventana de inicio del juego. Muestra un formulario para que el jugador
 * introduzca su nombre y elija su personaje antes de empezar.
 *
 * Usa Java Swing para crear la interfaz gráfica.
 * GridBagLayout permite colocar los componentes en una cuadrícula flexible.
 */
public class VentanaInicio extends JFrame {

    // Emojis disponibles como skin del jugador
    private final String[] SKINS = {"🦝", "🐶", "🐸", "🐷", "😺"};

    /**
     * Constructor. Crea y muestra la ventana de inicio.
     * Al confirmar guarda los datos en JSON y abre VentanaResumen.
     */
    public VentanaInicio() {
        setTitle("Terminal Crossing — Crear personaje");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 260);
        setLocationRelativeTo(null); // centra la ventana en la pantalla
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titulo = new JLabel("🏡 Bienvenido a Terminal Crossing", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        panel.add(titulo, c);

        // Campo de nombre
        c.gridwidth = 1; c.gridy = 1; c.gridx = 0;
        panel.add(new JLabel("Tu nombre:"), c);
        JTextField campoNombre = new JTextField(15);
        c.gridx = 1;
        panel.add(campoNombre, c);

        // Selector de personaje
        c.gridy = 2; c.gridx = 0;
        panel.add(new JLabel("Tu personaje:"), c);
        JComboBox<String> comboPj = new JComboBox<>(SKINS);
        comboPj.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        c.gridx = 1;
        panel.add(comboPj, c);

        // Botón de confirmar
        JButton btnConfirmar = new JButton("¡Empezar!");
        c.gridy = 3; c.gridx = 0; c.gridwidth = 2;
        panel.add(btnConfirmar, c);

        // Acción al pulsar el botón
        btnConfirmar.addActionListener(e -> {
            String nombre = campoNombre.getText().trim();
            // Validamos que el nombre no esté vacío
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, escribe tu nombre.", "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String skin = (String) comboPj.getSelectedItem();
            dispose(); // cerramos esta ventana
            ConfiguracionJSON.guardar(nombre, skin); // guardamos en JSON
            new VentanaResumen(nombre, skin).setVisible(true); // abrimos la siguiente ventana
        });

        add(panel);
        setVisible(true);
    }
}