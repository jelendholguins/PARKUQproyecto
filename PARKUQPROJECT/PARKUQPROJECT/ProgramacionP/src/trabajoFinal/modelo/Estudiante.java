package trabajoFinal.modelo;

public class Estudiante extends Usuario {
    public Estudiante(String nombre, String identificacion) {
        super(nombre, identificacion);
    }

    @Override
    public String getTipoUsuario() {
        return "Estudiante";
    }

    @Override
    public boolean tieneDescuento() {
        return true;
    }
}
