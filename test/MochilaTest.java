import coleccionables.Fruta;
import coleccionables.Mochila;
import excepciones.MochilaLlenaException;
import excepciones.NombreInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MochilaTest {

    private Mochila mochila;
    private Fruta manzana;
    private Fruta pera;

    @BeforeEach
    void setUp() throws NombreInvalidoException {
        mochila = new Mochila("🎒", "Mochila test", 2);
        manzana = new Fruta("🍎", "Manzana", 20);
        pera    = new Fruta("🍐", "Pera", 15);
    }

    // TEST 1 - POSITIVO: guardar un objeto correctamente
    @Test
    void testGuardarObjetoCorrectamente() throws MochilaLlenaException {
        boolean resultado = mochila.guardar(manzana);
        assertTrue(resultado);
        assertEquals(1, mochila.getTotalObjetos());
    }

    // TEST 2 - POSITIVO: contiene devuelve true tras guardar
    @Test
    void testContieneTrasDeguardar() throws MochilaLlenaException {
        mochila.guardar(manzana);
        assertTrue(mochila.contiene("Manzana"));
    }

    // TEST 3 - POSITIVO: sacar un objeto reduce el total
    @Test
    void testSacarReduceTotal() throws MochilaLlenaException {
        mochila.guardar(manzana);
        mochila.sacar("Manzana");
        assertEquals(0, mochila.getTotalObjetos());
    }

    // TEST 4 - POSITIVO: hayEspacio devuelve false cuando está llena
    @Test
    void testHayEspacioFalseCuandoLlena() throws MochilaLlenaException {
        mochila.guardar(manzana);
        mochila.guardar(pera);
        assertFalse(mochila.hayEspacio());
    }

    // TEST 5 - NEGATIVO: guardar cuando está llena lanza MochilaLlenaException
    @Test
    void testGuardarCuandoLlenaLanzaExcepcion() throws MochilaLlenaException, NombreInvalidoException {
        mochila.guardar(manzana);
        mochila.guardar(pera);
        Fruta extra = new Fruta("🍊", "Naranja", 10);
        assertThrows(MochilaLlenaException.class, () -> mochila.guardar(extra));
    }
}