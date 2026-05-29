package trabajoFinal.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trabajoFinal.enums.TipoEspacioEnum;
import trabajoFinal.enums.TipoVehiculoEnum;
import trabajoFinal.modelo.Carro;
import trabajoFinal.modelo.Tarifa;
import trabajoFinal.service.ParkService;

import static org.junit.jupiter.api.Assertions.*;

public class ParqueaderoTest {

    private ParkService parqueadero;

    @BeforeEach
    public void setUp() {
        parqueadero = new ParkService("Test Park");
        parqueadero.agregarEspacio("C-01", TipoEspacioEnum.CARRO);
        parqueadero.configurarTarifa(TipoVehiculoEnum.CARRO, 3000, 0.2); // 20% descuento
        parqueadero.registrarUsuario("Juan", "123", "Estudiante");
    }

    @Test
    public void testIngresoSinEspacioLanzaExcepcion() {
        assertDoesNotThrow(() -> {
            parqueadero.registrarIngreso("AAA-111", TipoVehiculoEnum.CARRO, "Piloto", "000");
        });

        Exception exception = assertThrows(Exception.class, () -> {
            parqueadero.registrarIngreso("BBB-222", TipoVehiculoEnum.CARRO, "Otro", "999");
        });

        assertTrue(exception.getMessage().contains("no hay espacios disponibles"));
    }

    @Test
    public void testIngresoPlacaDuplicadaLanzaExcepcion() {
        parqueadero.agregarEspacio("C-02", TipoEspacioEnum.CARRO);

        assertDoesNotThrow(() -> {
            parqueadero.registrarIngreso("AAA-111", TipoVehiculoEnum.CARRO, "Piloto", "000");
        });

        Exception exception = assertThrows(Exception.class, () -> {
            parqueadero.registrarIngreso("AAA-111", TipoVehiculoEnum.CARRO, "Otro", "999");
        });

        assertTrue(exception.getMessage().contains("placas duplicadas activas"));
    }

    @Test
    public void testSalidaSinIngresoLanzaExcepcion() {
        Exception exception = assertThrows(Exception.class, () -> {
            parqueadero.registrarSalida("ZZZ-999");
        });

        assertTrue(exception.getMessage().contains("vehículo que no ingresó"));
    }

    @Test
    public void testCalculoTarifaConDescuento() throws Exception {
        parqueadero.registrarIngreso("AAA-111", TipoVehiculoEnum.CARRO, "Juan", "123");

        double total = parqueadero.registrarSalida("AAA-111");
        assertEquals(2400.0, total, 0.01);
    }
}
