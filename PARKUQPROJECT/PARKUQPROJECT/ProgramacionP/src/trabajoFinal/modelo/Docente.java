package trabajoFinal.modelo;

public class Docente extends Usuario {
    public Docente(String nombre, String identificacion) {
        super(nombre, identificacion);
    }

    @Override
    public String getTipoUsuario() {
        return "Docente";
    }

    @Override
    public boolean tieneDescuento() {
        return true;
    }
}
