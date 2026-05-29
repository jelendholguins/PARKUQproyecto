package trabajoFinal.modelo;

import trabajoFinal.enums.EstadoEspacioEnum;
import trabajoFinal.enums.TipoEspacioEnum;

public class EspacioParqueadero {

    private String codigo;
    private TipoEspacioEnum tipoEspacio;
    private EstadoEspacioEnum estado;
    private Vehiculo vehiculoAsignado;

    public EspacioParqueadero(String codigo, TipoEspacioEnum tipoEspacio) {
        this.codigo = codigo;
        this.tipoEspacio = tipoEspacio;
        this.estado = EstadoEspacioEnum.DISPONIBLE;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public TipoEspacioEnum getTipoEspacio() {
        return tipoEspacio;
    }

    public void setTipoEspacio(TipoEspacioEnum tipoEspacio) {
        this.tipoEspacio = tipoEspacio;
    }

    public EstadoEspacioEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoEspacioEnum estado) {
        this.estado = estado;
    }

    public Vehiculo getVehiculoAsignado() {
        return vehiculoAsignado;
    }

    public void setVehiculoAsignado(Vehiculo vehiculoAsignado) {
        this.vehiculoAsignado = vehiculoAsignado;
    }

    public boolean estaDisponible() {
        return this.estado == EstadoEspacioEnum.DISPONIBLE;
    }
}
