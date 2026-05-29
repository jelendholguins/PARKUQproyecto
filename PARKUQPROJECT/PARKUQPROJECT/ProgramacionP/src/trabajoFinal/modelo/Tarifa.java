package trabajoFinal.modelo;

import trabajoFinal.enums.TipoVehiculoEnum;

public class Tarifa {

    private TipoVehiculoEnum tipoVehiculo;
    private double valorPorHora;
    private double descuento;

    public Tarifa(TipoVehiculoEnum tipoVehiculo, double valorPorHora, double descuento) {
        this.tipoVehiculo = tipoVehiculo;
        this.valorPorHora = valorPorHora;
        this.descuento = descuento;
    }

    public TipoVehiculoEnum getTipoVehiculo() {
        return tipoVehiculo;
    }

    public double getValorPorHora() {
        return valorPorHora;
    }

    public double getDescuento() {
        return descuento;
    }

    public double calcularValor(double horas, boolean tieneDescuento) {
        double subtotal = horas * valorPorHora;
        if (tieneDescuento) {
            return subtotal * (1 - descuento);
        }
        return subtotal;
    }
}
