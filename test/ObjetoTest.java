import coleccionables.Objeto;
import excepciones.NombreInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ObjetoTest {

    private Objeto obj1;
    private Objeto obj2;

    @BeforeEach
    void setUp() throws NombreInvalidoException {
        obj1 = new Objeto("🪨", "Roca", 0);
        obj2 = new Objeto("🪨", "Roca", 0);
    }

    // TEST 1 - POSITIVO: equals devuelve true para objetos iguales
    @Test
    void testEqualsObjetosIguales() {
        assertEquals(obj1, obj2);
    }

    // TEST 2 - POSITIVO: hashCode es igual para objetos iguales
    @Test
    void testHashCodeIgualParaObjetosIguales() {
        assertEquals(obj1.hashCode(), obj2.hashCode());
    }

    // TEST 3 - POSITIVO: toString contiene el nombre
    @Test
    void testToStringContieneNombre() {
        assertTrue(obj1.toString().contains("Roca"));
    }

    // TEST 4 - POSITIVO: getters devuelven los valores del constructor
    @Test
    void testGettersDevolverValoresCorrectos() throws NombreInvalidoException {
        Objeto obj = new Objeto("🍎", "Manzana", 100);
        assertEquals("Manzana", obj.getNombre());
        assertEquals(100, obj.getPrecioVenta());
    }

    // TEST 5 - NEGATIVO: crear Objeto con nombre vacío lanza NombreInvalidoException
    @Test
    void testCrearObjetoConNombreVacioLanzaExcepcion() {
        assertThrows(NombreInvalidoException.class, () -> new Objeto("🍎", "", 0));
    }

    // TEST 6 - NEGATIVO: crear Objeto con nombre null lanza NombreInvalidoException
    @Test
    void testCrearObjetoConNombreNuloLanzaExcepcion() {
        assertThrows(NombreInvalidoException.class, () -> new Objeto("🍎", null, 0));
    }
}