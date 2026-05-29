package trabajoFinal.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trabajoFinal.enums.EstadoEspacioEnum;
import trabajoFinal.enums.TipoEspacioEnum;
import trabajoFinal.enums.TipoVehiculoEnum;
import trabajoFinal.modelo.Carro;
import trabajoFinal.modelo.EspacioParqueadero;
import trabajoFinal.modelo.Tarifa;
import trabajoFinal.service.ParkService;

import static org.junit.jupiter.api.Assertions.*;

public class ExcepcionesTest {

    private ParkService parqueadero;

    @BeforeEach
    public void setUp() {
        parqueadero = new ParkService("ParkUQ Test");
        parqueadero.agregarEspacio("C-01", TipoEspacioEnum.CARRO);
        parqueadero.configurarTarifa(TipoVehiculoEnum.CARRO, 3000.0, 0.0);
    }

    @Test
    public void registrarIngreso_placaDuplicada_debeLanzarExcepcion() throws Exception {
        parqueadero.registrarIngreso("AAA111", TipoVehiculoEnum.CARRO, "Juan Pérez", "111");

        Exception excepcion = assertThrows(
                Exception.class,
                () -> parqueadero.registrarIngreso("AAA111", TipoVehiculoEnum.CARRO, "Ana López", "222"));
        assertTrue(excepcion.getMessage().toLowerCase().contains("duplicada"));
    }

    @Test
    public void registrarIngreso_sinEspacios_debeLanzarExcepcion() throws Exception {
        // Llenar el único espacio disponible
        parqueadero.registrarIngreso("BBB222", TipoVehiculoEnum.CARRO, "Luis Torres", "333");

        Exception excepcion = assertThrows(
                Exception.class,
                () -> parqueadero.registrarIngreso("CCC333", TipoVehiculoEnum.CARRO, "María Gil", "444"));
        assertTrue(excepcion.getMessage().toLowerCase().contains("no hay espacio"));
    }

    @Test
    public void registrarSalida_placaNoExiste_debeLanzarExcepcion() {
        Exception excepcion = assertThrows(
                Exception.class,
                () -> parqueadero.registrarSalida("XYZ999"));
        assertTrue(excepcion.getMessage().toLowerCase().contains("no ingres"));
    }

    @Test
    public void excepcion_espacioFueraDeServicio_noPermiteIngreso() {
        // Poner el único espacio fuera de servicio
        parqueadero.deshabilitarEspacio("C-01");

        assertThrows(
                Exception.class,
                () -> parqueadero.registrarIngreso("DDD444", TipoVehiculoEnum.CARRO, "Rosa Mora", "555"));
    }
}
