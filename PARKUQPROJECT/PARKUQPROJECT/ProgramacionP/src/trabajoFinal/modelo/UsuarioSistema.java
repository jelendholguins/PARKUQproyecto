package trabajoFinal.modelo;

import trabajoFinal.enums.RolSistemaEnum;

public class UsuarioSistema {

    private String nombreUsuario;
    private String contrasena;
    private RolSistemaEnum rol;

    public UsuarioSistema(String nombreUsuario, String contrasena, RolSistemaEnum rol) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public boolean autenticar(String usuario, String pass) {
        return this.nombreUsuario.equals(usuario) && this.contrasena.equals(pass);
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public RolSistemaEnum getRol() {
        return rol;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setRol(RolSistemaEnum rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return nombreUsuario + " [" + rol + "]";
    }
}
