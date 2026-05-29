package trabajoFinal.excepcion;

public class NoEspacioDisponibleException extends ParkUQException {

    public NoEspacioDisponibleException(String tipoVehiculo) {
        super("No se encuentran espacios libres para el tipo de vehiculo:" + tipoVehiculo);
    }
}
