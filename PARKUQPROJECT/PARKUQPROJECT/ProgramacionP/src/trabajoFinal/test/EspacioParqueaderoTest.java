package trabajoFinal.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trabajoFinal.enums.EstadoEspacioEnum;
import trabajoFinal.enums.TipoEspacioEnum;
import trabajoFinal.modelo.Carro;
import trabajoFinal.modelo.EspacioParqueadero;

import static org.junit.jupiter.api.Assertions.*;

public class EspacioParqueaderoTest {

    private EspacioParqueadero espacio;

    @BeforeEach
    public void setUp() {
        espacio = new EspacioParqueadero("C-01", TipoEspacioEnum.CARRO);
    }

    @Test
    public void espacio_estadoInicialDebeSerDisponible() {
        assertEquals(EstadoEspacioEnum.DISPONIBLE, espacio.getEstado());
        assertTrue(espacio.estaDisponible());
        assertNull(espacio.getVehiculoAsignado());
    }

    @Test
    public void asignarVehiculo_debeOcuparElEspacio() {
        Carro carro = new Carro("ABC123", "Juan Pérez", "111222333");
        espacio.setEstado(EstadoEspacioEnum.OCUPADO);
        espacio.setVehiculoAsignado(carro);

        assertEquals(EstadoEspacioEnum.OCUPADO, espacio.getEstado());
        assertFalse(espacio.estaDisponible());
        assertEquals(carro, espacio.getVehiculoAsignado());
    }

    @Test
    public void liberarEspacio_debeDevolverEstadoDisponible() {
        Carro carro = new Carro("DEF456", "Ana López", "444555666");
        espacio.setEstado(EstadoEspacioEnum.OCUPADO);
        espacio.setVehiculoAsignado(carro);
        espacio.setEstado(EstadoEspacioEnum.DISPONIBLE);
        espacio.setVehiculoAsignado(null);

        assertEquals(EstadoEspacioEnum.DISPONIBLE, espacio.getEstado());
        assertTrue(espacio.estaDisponible());
        assertNull(espacio.getVehiculoAsignado());
    }

    @Test
    public void espacio_debeTenerCodigoYTipoCorrecto() {
        assertEquals("C-01", espacio.getCodigo());
        assertEquals(TipoEspacioEnum.CARRO, espacio.getTipoEspacio());
    }

    @Test
    public void espacio_fuera_de_servicio_no_estaDisponible() {
        espacio.setEstado(EstadoEspacioEnum.FUERA_DE_SERVICIO);

        assertFalse(espacio.estaDisponible());
    }
}
