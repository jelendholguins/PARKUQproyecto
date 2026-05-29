package trabajoFinal.modelo;

import trabajoFinal.enums.TipoVehiculoEnum;
import java.time.LocalDateTime;

public class Carro extends Vehiculo {

    public Carro(String placa, String nombreConductor, String identificacionConductor, LocalDateTime horaIngreso,
            String espacioAsignado) {
        super(placa, nombreConductor, identificacionConductor, horaIngreso, espacioAsignado);
    }

    public Carro(String placa, String nombreConductor, String identificacionConductor) {
        super(placa, nombreConductor, identificacionConductor, LocalDateTime.now(), "");
    }

    @Override
    public TipoVehiculoEnum getTipoVehiculo() {
        return TipoVehiculoEnum.CARRO;
    }
}
