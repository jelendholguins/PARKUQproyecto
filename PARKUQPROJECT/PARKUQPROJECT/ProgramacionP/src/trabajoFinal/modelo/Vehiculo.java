package trabajoFinal.modelo;

import trabajoFinal.enums.EstadoVehiculoEnum;
import trabajoFinal.enums.TipoVehiculoEnum;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Vehiculo {

    private String placa;
    private String nombreConductor;
    private String identificacionConductor;
    private LocalDateTime horaIngreso;
    private String espacioAsignado;
    private EstadoVehiculoEnum estado;

    public Vehiculo(String placa, String nombreConductor, String identificacionConductor, LocalDateTime horaIngreso,
            String espacioAsignado) {
        this.placa = placa.toUpperCase();
        this.nombreConductor = nombreConductor;
        this.identificacionConductor = identificacionConductor;
        this.horaIngreso = horaIngreso;
        this.espacioAsignado = espacioAsignado;
        this.estado = EstadoVehiculoEnum.DENTRO;
    }

    public abstract TipoVehiculoEnum getTipoVehiculo();

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa.toUpperCase();
    }

    public String getNombreConductor() {
        return nombreConductor;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }

    public String getIdentificacionConductor() {
        return identificacionConductor;
    }

    public void setIdentificacionConductor(String identificacionConductor) {
        this.identificacionConductor = identificacionConductor;
    }

    public LocalDateTime getHoraIngreso() {
        return horaIngreso;
    }

    public void setHoraIngreso(LocalDateTime horaIngreso) {
        this.horaIngreso = horaIngreso;
    }

    public String getEspacioAsignado() {
        return espacioAsignado;
    }

    public void setEspacioAsignado(String espacioAsignado) {
        this.espacioAsignado = espacioAsignado;
    }

    public EstadoVehiculoEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoVehiculoEnum estado) {
        this.estado = estado;
    }

    // Método de soporte para la UI
    public String getTipoDescripcion() {
        return getTipoVehiculo().name();
    }

    // Método de soporte para la UI — columna "Hora Ingreso"
    public String getHoraIngresoFormateada() {
        if (horaIngreso == null) return "";
        return horaIngreso.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
