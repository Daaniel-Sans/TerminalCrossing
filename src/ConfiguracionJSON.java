import org.json.JSONObject;
import java.io.*;
import java.nio.file.*;

/**
 * Gestiona el guardado y la lectura del personaje en formato JSON.
 * Usa la librería org.json para crear y parsear el archivo.
 *
 * Los métodos son 'static' porque no necesitan crear un objeto de esta clase
 * para funcionar — se llaman directamente como ConfiguracionJSON.guardar(...).
 *
 * El archivo se guarda en saves/personaje.json. La carpeta 'saves' se crea
 * automáticamente si no existe gracias a Files.createDirectories().
 */
public class ConfiguracionJSON {

    // Ruta del archivo JSON donde se guarda el personaje.
    // Es 'static final' porque es la misma para todos y nunca cambia.
    private static final String RUTA = "saves/personaje.json";

    /**
     * Guarda el nombre y el skin del jugador en el archivo JSON.
     * Si la carpeta 'saves' no existe la crea automáticamente.
     * No devuelve nada (void) porque solo escribe en disco.
     *
     * El archivo JSON resultante tiene este formato:
     * <pre>
     * {
     *   "nombre": "Alex",
     *   "skin": "🦝"
     * }
     * </pre>
     *
     * @param nombre nombre del jugador a guardar
     * @param skin   emoji del personaje a guardar
     */
    public static void guardar(String nombre, String skin) {
        try {
            // Creamos la carpeta 'saves' si no existe
            // createDirectories no lanza error si ya existe
            Files.createDirectories(Paths.get("saves"));

            // Creamos el objeto JSON y le añadimos los datos
            JSONObject json = new JSONObject();
            json.put("nombre", nombre);
            json.put("skin", skin);

            // Escribimos el JSON en el archivo con indentación de 2 espacios
            BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA));
            writer.write(json.toString(2)); // toString(2) añade indentación para que sea legible
            writer.close();
        } catch (IOException e) {
            System.err.println("[ERROR] No se pudo guardar el JSON: " + e.getMessage());
        }
    }

    /**
     * Lee el nombre y el skin del jugador desde el archivo JSON.
     * Devuelve String[] porque necesitamos devolver dos valores a la vez.
     * Devuelve null si el archivo no existe o hay algún error al leerlo.
     *
     * @return array con [nombre, skin], o null si no se pudo leer
     */
    public static String[] leer() {
        try {
            // Leemos todo el contenido del archivo como String
            String contenido = new String(Files.readAllBytes(Paths.get(RUTA)));
            // Parseamos el String como JSON y extraemos los valores
            JSONObject json = new JSONObject(contenido);
            return new String[]{ json.getString("nombre"), json.getString("skin") };
        } catch (Exception e) {
            // Si el archivo no existe o está corrupto devolvemos null
            // VentanaResumen lo maneja mostrando el nombre original
            return null;
        }
    }
}