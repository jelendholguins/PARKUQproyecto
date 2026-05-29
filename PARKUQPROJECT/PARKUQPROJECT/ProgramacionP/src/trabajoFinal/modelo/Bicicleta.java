package trabajoFinal.modelo;

import trabajoFinal.enums.TipoVehiculoEnum;
import java.time.LocalDateTime;

public class Bicicleta extends Vehiculo {

    public Bicicleta(String placa, String nombreConductor, String identificacionConductor, LocalDateTime horaIngreso,
            String espacioAsignado) {
        super(placa, nombreConductor, identificacionConductor, horaIngreso, espacioAsignado);
    }

    public Bicicleta(String placa, String nombreConductor, String identificacionConductor) {
        super(placa, nombreConductor, identificacionConductor, LocalDateTime.now(), "");
    }

    @Override
    public TipoVehiculoEnum getTipoVehiculo() {
        return TipoVehiculoEnum.BICICLETA;
    }
}
