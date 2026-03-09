import coleccionables.Herramienta;
import coleccionables.TipoHerramienta;
import excepciones.HerramientaRotaException;
import excepciones.NombreInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HerramientaTest {

    private Herramienta red;
    private Herramienta herramientaRota;

    @BeforeEach
    void setUp() throws NombreInvalidoException {
        red             = new Herramienta("🏸", "Red de mariposas", TipoHerramienta.RED, 3);
        herramientaRota = new Herramienta("⛏️", "Pala rota",        TipoHerramienta.PALA, 0);
    }

    // TEST 1 - POSITIVO: usar reduce durabilidad
    @Test
    void testUsarReduceDurabilidad() throws HerramientaRotaException {
        red.usar();
        assertEquals(2, red.getDurabilidad());
    }

    // TEST 2 - POSITIVO: esDeTipo devuelve true para el tipo correcto
    @Test
    void testEsDeTipoCorrecto() {
        assertTrue(red.esDeTipo("RED"));
    }

    // TEST 3 - POSITIVO: esDeTipo devuelve false para tipo incorrecto
    @Test
    void testEsDeTipoIncorrecto() {
        assertFalse(red.esDeTipo("CAÑA"));
    }

    // TEST 4 - POSITIVO: getDurabilidad inicial es la del constructor
    @Test
    void testDurabilidadInicial() {
        assertEquals(3, red.getDurabilidad());
    }

    // TEST 5 - NEGATIVO: usar una herramienta con durabilidad 0 lanza excepción
    @Test
    void testUsarHerramientaRotaLanzaExcepcion() {
        assertThrows(HerramientaRotaException.class, () -> herramientaRota.usar());
    }
}