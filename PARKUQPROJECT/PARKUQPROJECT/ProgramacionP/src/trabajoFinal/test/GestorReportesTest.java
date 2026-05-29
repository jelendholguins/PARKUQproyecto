package trabajoFinal.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trabajoFinal.enums.TipoEspacioEnum;
import trabajoFinal.enums.TipoVehiculoEnum;
import trabajoFinal.modelo.Carro;
import trabajoFinal.modelo.EspacioParqueadero;
import trabajoFinal.modelo.Tarifa;
import trabajoFinal.service.GestorReportService;
import trabajoFinal.service.ParkService;

import static org.junit.jupiter.api.Assertions.*;

public class GestorReportesTest {

    private ParkService parqueadero;
    private GestorReportService gestor;

    @BeforeEach
    public void setUp() {
        parqueadero = new ParkService("ParkUQ Test");
        parqueadero.agregarEspacio("C-01", TipoEspacioEnum.CARRO);
        parqueadero.agregarEspacio("C-02", TipoEspacioEnum.CARRO);
        parqueadero.configurarTarifa(TipoVehiculoEnum.CARRO, 3000.0, 0.0);
        gestor = new GestorReportService(parqueadero);
    }

    @Test
    public void calcularIngresosTotales_sinHistorial_debeRetornarCero() {
        assertEquals(0.0, gestor.calcularIngresosTotales(), 0.01);
    }

    @Test
    public void calcularTiempoPromedio_sinHistorial_debeRetornarCero() {
        assertEquals(0.0, gestor.calcularTiempoPromedio(), 0.01);
    }

    @Test
    public void calcularIngresosTotales_conRegistros_debeSumarCorrectamente() throws Exception {
        parqueadero.registrarIngreso("AAA111", TipoVehiculoEnum.CARRO, "Juan Pérez", "111");
        parqueadero.registrarSalida("AAA111");

        parqueadero.registrarIngreso("BBB222", TipoVehiculoEnum.CARRO, "Ana López", "222");
        parqueadero.registrarSalida("BBB222");

        // Ambos estuvieron menos de 1 hora → tarifa mínima × 2
        double ingresos = gestor.calcularIngresosTotales();
        assertEquals(6000.0, ingresos, 0.01);
    }

    @Test
    public void generarResumenDia_debeIncluirDatosDelDia() throws Exception {
        parqueadero.registrarIngreso("CCC333", TipoVehiculoEnum.CARRO, "Rosa Mora", "333");
        parqueadero.registrarSalida("CCC333");

        String resumen = gestor.generarResumenDia();

        assertTrue(resumen.contains("1"));
        assertTrue(resumen.contains("ParkUQ"));
    }

    @Test
    public void listarVehiculosSobreTiempo_ceroMinutos_debeRetornarActivos() throws Exception {
        parqueadero.registrarIngreso("DDD444", TipoVehiculoEnum.CARRO, "Luis Torres", "444");

        // Con límite de 0 minutos, cualquier vehículo activo supera el tiempo
        int cantidad = gestor.listarVehiculosSobreTiempo(0).size();
        assertEquals(1, cantidad);
    }
}
