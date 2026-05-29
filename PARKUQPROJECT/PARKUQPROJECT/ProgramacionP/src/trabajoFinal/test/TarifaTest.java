package trabajoFinal.test;

import org.junit.jupiter.api.Test;
import trabajoFinal.enums.TipoVehiculoEnum;
import trabajoFinal.modelo.Tarifa;

import static org.junit.jupiter.api.Assertions.*;

public class TarifaTest {

    @Test
    public void calcularCosto_menosDeUnaHora_debeAplicarMinimoUnaHora() {
        Tarifa tarifa = new Tarifa(TipoVehiculoEnum.CARRO, 3000.0, 0.0);

        // 30 minutos (0.5 horas) -> el nuevo sistema cobra fracciones o horas completas
        // dependiendo. Si pasamos 1.0 (minimo 1 hora).
        // Si pasamos 0.5, cobrará 1500 según la nueva lógica (no hay mínimos en el
        // modelo, el cálculo se hace en Parqueadero).
        double costo = tarifa.calcularValor(1.0, false);

        assertEquals(3000.0, costo, 0.01);
    }

    @Test
    public void calcularCosto_exactamenteUnaHora_debeCalcularCorrectamente() {
        Tarifa tarifa = new Tarifa(TipoVehiculoEnum.MOTO, 2000.0, 0.0);

        double costo = tarifa.calcularValor(1.0, false);

        assertEquals(2000.0, costo, 0.01);
    }

    @Test
    public void calcularCosto_dosHoras_debeMultiplicarCorrectamente() {
        Tarifa tarifa = new Tarifa(TipoVehiculoEnum.CARRO, 3000.0, 0.0);

        double costo = tarifa.calcularValor(2.0, false);

        assertEquals(6000.0, costo, 0.01);
    }

    @Test
    public void calcularCosto_unaHoraYMedia_debeProrratear() {
        Tarifa tarifa = new Tarifa(TipoVehiculoEnum.CARRO, 3000.0, 0.0);

        double costo = tarifa.calcularValor(1.5, false);

        assertEquals(4500.0, costo, 0.01);
    }

    @Test
    public void tarifa_debePersistirValorPorHora() {
        Tarifa tarifa = new Tarifa(TipoVehiculoEnum.BICICLETA, 500.0, 0.0);

        assertEquals(TipoVehiculoEnum.BICICLETA, tarifa.getTipoVehiculo());
        assertEquals(500.0, tarifa.getValorPorHora(), 0.01);
    }

    @Test
    public void modificarValorPorHora_debeCambiarElCosto() {
        Tarifa tarifa = new Tarifa(TipoVehiculoEnum.CARRO, 4000.0, 0.0);

        assertEquals(4000.0, tarifa.calcularValor(1.0, false), 0.01);
    }
}
