package trabajoFinal.modelo;

public class Administrativo extends Usuario {
    public Administrativo(String nombre, String identificacion) {
        super(nombre, identificacion);
    }

    @Override
    public String getTipoUsuario() {
        return "Administrativo";
    }

    @Override
    public boolean tieneDescuento() {
        return true;
    }
}
