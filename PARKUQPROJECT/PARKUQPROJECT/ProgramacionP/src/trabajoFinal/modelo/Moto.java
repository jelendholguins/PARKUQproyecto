package trabajoFinal.modelo;

import trabajoFinal.enums.TipoVehiculoEnum;
import java.time.LocalDateTime;

public class Moto extends Vehiculo {

    private int cilindraje;

    public Moto(String placa, String nombreConductor, String identificacionConductor, LocalDateTime horaIngreso,
            String espacioAsignado, int cilindraje) {
        super(placa, nombreConductor, identificacionConductor, horaIngreso, espacioAsignado);
        this.cilindraje = cilindraje;
    }

    public Moto(String placa, String nombreConductor, String identificacionConductor, int cilindraje) {
        super(placa, nombreConductor, identificacionConductor, LocalDateTime.now(), "");
        this.cilindraje = cilindraje;
    }

    @Override
    public TipoVehiculoEnum getTipoVehiculo() {
        return TipoVehiculoEnum.MOTO;
    }

    public int getCilindraje() {
        return cilindraje;
    }

    public void setCilindraje(int cilindraje) {
        this.cilindraje = cilindraje;
    }
}
