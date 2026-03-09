import personajes.Pj;
import personajes.EstadoPersonaje;
import excepciones.EnergiaInsuficienteException;
import excepciones.NombreInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PjTest {

    private Pj jugador;

    @BeforeEach
    void setUp() throws NombreInvalidoException {
        jugador = new Pj("Alex", "🦝", 10, 10);
    }

    // TEST 1 - POSITIVO: comer suma energía
    @Test
    void testComerSumaEnergia() {
        jugador.setEnergiaActual(50);
        jugador.comer(20);
        assertEquals(70, jugador.getEnergiaActual());
    }

    // TEST 2 - POSITIVO: comer no supera el máximo (100)
    @Test
    void testComerNoSuperaMaximo() {
        jugador.setEnergiaActual(90);
        jugador.comer(50);
        assertEquals(100, jugador.getEnergiaActual());
    }

    // TEST 3 - POSITIVO: estado inicial es ACTIVO
    @Test
    void testEstadoInicialActivo() {
        assertEquals(EstadoPersonaje.ACTIVO, jugador.getEstado());
    }

    // TEST 4 - POSITIVO: no tiene mochila al crearse
    @Test
    void testSinMochilaAlCrearse() {
        assertFalse(jugador.tieneMochila());
    }

    // TEST 5 - NEGATIVO: mover sin energía lanza EnergiaInsuficienteException
    @Test
    void testMoverSinEnergiaLanzaExcepcion() {
        jugador.setEnergiaActual(1);
        assertThrows(EnergiaInsuficienteException.class, () -> jugador.mover());
    }

    // TEST 6 - NEGATIVO: crear Pj con nombre vacío lanza NombreInvalidoException
    @Test
    void testCrearPjConNombreVacioLanzaExcepcion() {
        assertThrows(NombreInvalidoException.class, () -> new Pj("", "🦝", 0, 0));
    }
}