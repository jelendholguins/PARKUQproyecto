package trabajoFinal.excepcion;

public class PlacaDuplicadaException extends ParkUQException {

    public PlacaDuplicadaException(String placa) {
        super("El vehículo con placa " + placa + " ya se encuentra dentro del parqueadero");
    }
}
