package trabajoFinal.test;

import org.junit.jupiter.api.Test;
import trabajoFinal.enums.EstadoVehiculoEnum;
import trabajoFinal.enums.TipoVehiculoEnum;
import trabajoFinal.modelo.Bicicleta;
import trabajoFinal.modelo.Carro;
import trabajoFinal.modelo.Moto;

import static org.junit.jupiter.api.Assertions.*;

public class VehiculoTest {

    @Test
    public void carro_debeCrearseConTipoCorrecto() {
        Carro carro = new Carro("ABC123", "Juan Pérez", "1098765432");

        assertEquals("ABC123", carro.getPlaca());
        assertEquals(TipoVehiculoEnum.CARRO, carro.getTipoVehiculo());
        assertEquals("Juan Pérez", carro.getNombreConductor());
        assertEquals("CARRO", carro.getTipoDescripcion());
    }

    @Test
    public void placa_debeAlmacenarseEnMayusculas() {
        Carro carro = new Carro("abc123", "Ana López", "123456789");

        assertEquals("ABC123", carro.getPlaca());
    }

    @Test
    public void motocicleta_debeCrearseConCilindraje() {
        Moto moto = new Moto("ZXY987", "Carlos Ruiz", "987654321", 150);
        assertEquals("ZXY987", moto.getPlaca());
        assertEquals(150, moto.getCilindraje());
        assertEquals("MOTO", moto.getTipoDescripcion());
    }

    @Test
    public void bicicleta_debeCrearseConTipoCorrecto() {
        Bicicleta bici = new Bicicleta("BCI001", "María García", "555444333");

        assertEquals(TipoVehiculoEnum.BICICLETA, bici.getTipoVehiculo());
        assertEquals("BICICLETA", bici.getTipoDescripcion());
    }

    @Test
    public void vehiculo_estadoInicialDebeSerDentro() {
        Carro carro = new Carro("DEF456", "Luis Torres", "111222333");

        assertEquals(EstadoVehiculoEnum.DENTRO, carro.getEstado());
    }

    @Test
    public void vehiculo_debePermitirCambiarEstado() {
        Carro carro = new Carro("GHI789", "Rosa Mora", "444555666");
        carro.setEstado(EstadoVehiculoEnum.SALIO);

        assertEquals(EstadoVehiculoEnum.SALIO, carro.getEstado());
    }
}
