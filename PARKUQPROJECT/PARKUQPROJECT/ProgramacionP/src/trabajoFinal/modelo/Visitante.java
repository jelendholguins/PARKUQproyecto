package trabajoFinal.modelo;

public class Visitante extends Usuario {
    public Visitante(String nombre, String identificacion) {
        super(nombre, identificacion);
    }

    @Override
    public String getTipoUsuario() {
        return "Visitante";
    }

    @Override
    public boolean tieneDescuento() {
        return false;
    }
}
