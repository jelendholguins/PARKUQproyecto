package trabajoFinal.excepcion;

public class VehiculoNoEncontradoException extends ParkUQException {

    public VehiculoNoEncontradoException(String placa) {
        super("No hay vehiculo activo registrado con esta placa: " + placa);
    }
}
