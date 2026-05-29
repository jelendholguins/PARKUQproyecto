package trabajoFinal.modelo;

public abstract class Usuario {
    private String nombre;
    private String identificacion;

    public Usuario(String nombre, String identificacion) {
        this.nombre = nombre;
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public abstract String getTipoUsuario();

    public abstract boolean tieneDescuento();
}
