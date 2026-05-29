package trabajoFinal.service;

import trabajoFinal.enums.EstadoEspacioEnum;
import trabajoFinal.enums.EstadoVehiculoEnum;
import trabajoFinal.enums.TipoEspacioEnum;
import trabajoFinal.enums.TipoVehiculoEnum;
import trabajoFinal.modelo.Bicicleta;
import trabajoFinal.modelo.Carro;
import trabajoFinal.modelo.EspacioParqueadero;
import trabajoFinal.modelo.Moto;
import trabajoFinal.modelo.RegistroSalida;
import trabajoFinal.modelo.Tarifa;
import trabajoFinal.modelo.Usuario;
import trabajoFinal.modelo.Administrativo;
import trabajoFinal.modelo.Docente;
import trabajoFinal.modelo.Estudiante;
import trabajoFinal.modelo.Visitante;
import trabajoFinal.modelo.UsuarioSistema;
import trabajoFinal.modelo.Vehiculo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class ParkService {

    private String nombre;
    private ArrayList<EspacioParqueadero> espacios;
    private ArrayList<Vehiculo> vehiculos;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Tarifa> tarifas;

    // Colecciones paralelas necesarias para UI / Login
    private ArrayList<RegistroSalida> historialDia;
    private ArrayList<UsuarioSistema> usuariosSistema;

    public ParkService(String nombre) {
        this.nombre = nombre;
        this.espacios = new ArrayList<>();
        this.vehiculos = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.tarifas = new ArrayList<>();
        this.historialDia = new ArrayList<>();
        this.usuariosSistema = new ArrayList<>();
    }

    public void registrarIngreso(String placa, TipoVehiculoEnum tipo, String conductor, String identificacion)
            throws Exception {
        // Validación 1: Placa duplicada
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getPlaca().equalsIgnoreCase(placa) && vehiculo.getEstado() == EstadoVehiculoEnum.DENTRO) {
                throw new Exception("Error: Registro de placas duplicadas activas dentro del parqueadero.");
            }
        }

        // Validación 2: Espacio disponible
        TipoEspacioEnum tipoEspacio = TipoEspacioEnum.valueOf(tipo.name());
        EspacioParqueadero espacio = buscarEspacioDisponible(tipoEspacio);
        if (espacio == null) {
            throw new Exception("Error: Ingresar vehículo cuando no hay espacios disponibles.");
        }

        Vehiculo vehiculo;
        LocalDateTime ahora = LocalDateTime.now();
        if (tipo == TipoVehiculoEnum.MOTO) {
            vehiculo = new Moto(placa, conductor, identificacion, ahora, espacio.getCodigo(), 0);
        } else if (tipo == TipoVehiculoEnum.BICICLETA) {
            vehiculo = new Bicicleta(placa, conductor, identificacion, ahora, espacio.getCodigo());
        } else {
            vehiculo = new Carro(placa, conductor, identificacion, ahora, espacio.getCodigo());
        }

        espacio.setEstado(EstadoEspacioEnum.OCUPADO);
        espacio.setVehiculoAsignado(vehiculo);
        vehiculos.add(vehiculo);
    }

    public double registrarSalida(String placa) throws Exception {
        // Validación 3: Vehículo no existe o no está dentro
        Vehiculo vehiculo = buscarVehiculo(placa);
        if (vehiculo == null || vehiculo.getEstado() != EstadoVehiculoEnum.DENTRO) {
            throw new Exception("Error: Intentar registrar salida de un vehículo que no ingresó.");
        }

        double horas = calcularHoras(vehiculo.getHoraIngreso());

        boolean tieneDescuento = false;
        for (Usuario usuario : usuarios) {
            if (usuario.getIdentificacion().equals(vehiculo.getIdentificacionConductor())) {
                tieneDescuento = usuario.tieneDescuento();
                break;
            }
        }

        Tarifa tarifaAplicar = null;
        for (Tarifa tarifa : tarifas) {
            if (tarifa.getTipoVehiculo() == vehiculo.getTipoVehiculo()) {
                tarifaAplicar = tarifa;
                break;
            }
        }

        if (tarifaAplicar == null) {
            tarifaAplicar = new Tarifa(vehiculo.getTipoVehiculo(), 0, 0); // fallback
        }

        double total = tarifaAplicar.calcularValor(horas, tieneDescuento);

        // Actualizar estados
        for (EspacioParqueadero espacio : espacios) {
            if (espacio.getCodigo().equalsIgnoreCase(vehiculo.getEspacioAsignado())) {
                espacio.setEstado(EstadoEspacioEnum.DISPONIBLE);
                espacio.setVehiculoAsignado(null);
                break;
            }
        }
        vehiculo.setEstado(EstadoVehiculoEnum.SALIO);

        // Registrar en historial para la UI
        historialDia.add(new RegistroSalida(vehiculo, vehiculo.getHoraIngreso(), LocalDateTime.now(),
                (long) (horas * 60), tarifaAplicar.getValorPorHora() * horas, tarifaAplicar.getDescuento(), total));

        return total;
    }

    public void consultarVehiculosDentro() {
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getEstado() == EstadoVehiculoEnum.DENTRO) {
                System.out.println(vehiculo.getPlaca() + " - " + vehiculo.getTipoVehiculo());
            }
        }
    }

    public void consultarEspaciosDisponibles() {
        for (EspacioParqueadero espacio : espacios) {
            if (espacio.estaDisponible()) {
                System.out.println("Espacio: " + espacio.getCodigo() + " - Tipo: " + espacio.getTipoEspacio());
            }
        }
    }

    public void generarReporte() {
        System.out.println("=== Reporte de Parqueadero ===");
        System.out.println("Vehículos históricos: " + historialDia.size());
    }

    public void agregarEspacio(String codigo, TipoEspacioEnum tipo) {
        espacios.add(new EspacioParqueadero(codigo, tipo));
    }

    public void deshabilitarEspacio(String codigo) {
        for (EspacioParqueadero espacio : espacios) {
            if (espacio.getCodigo().equalsIgnoreCase(codigo)) {
                espacio.setEstado(EstadoEspacioEnum.FUERA_DE_SERVICIO);
            }
        }
    }

    public void configurarTarifa(TipoVehiculoEnum tipo, double valor, double descuento) {
        for (Tarifa tarifa : tarifas) {
            if (tarifa.getTipoVehiculo() == tipo) {
                tarifas.remove(tarifa);
                break;
            }
        }
        tarifas.add(new Tarifa(tipo, valor, descuento));
    }

    public void registrarUsuario(String nombre, String id, String tipo) {
        Usuario nuevoUsuario;
        switch (tipo.toUpperCase()) {
            case "ESTUDIANTE" -> nuevoUsuario = new Estudiante(nombre, id);
            case "DOCENTE"    -> nuevoUsuario = new Docente(nombre, id);
            case "ADMINISTRATIVO" -> nuevoUsuario = new Administrativo(nombre, id);
            default          -> nuevoUsuario = new Visitante(nombre, id);
        }
        usuarios.add(nuevoUsuario);
    }

    private EspacioParqueadero buscarEspacioDisponible(TipoEspacioEnum tipo) {
        for (EspacioParqueadero espacio : espacios) {
            if (espacio.getTipoEspacio() == tipo && espacio.estaDisponible()) {
                return espacio;
            }
        }
        return null;
    }

    private Vehiculo buscarVehiculo(String placa) {
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getPlaca().equalsIgnoreCase(placa)) {
                return vehiculo;
            }
        }
        return null;
    }

    private double calcularHoras(LocalDateTime horaIngreso) {
        long minutos = ChronoUnit.MINUTES.between(horaIngreso, LocalDateTime.now());
        if (minutos == 0)
            return 1.0;
        return minutos / 60.0;
    }

    // GETTERS PARA UI Y AUTH

    public String getNombre() {
        return nombre;
    }

    public ArrayList<EspacioParqueadero> getEspacios() {
        return espacios;
    }

    public ArrayList<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public ArrayList<Tarifa> getTarifas() {
        return tarifas;
    }

    public ArrayList<RegistroSalida> getHistorialDia() {
        return historialDia;
    }

    public ArrayList<UsuarioSistema> getUsuariosSistema() {
        return usuariosSistema;
    }

    public void agregarUsuarioSistema(UsuarioSistema usuarioNuevo) {
        usuariosSistema.add(usuarioNuevo);
    }

    public UsuarioSistema autenticarUsuarioSistema(String nombreUsuario, String contrasena) {
        for (UsuarioSistema usuario : usuariosSistema) {
            if (usuario.autenticar(nombreUsuario, contrasena)) {
                return usuario;
            }
        }
        return null;
    }

    public ArrayList<Vehiculo> consultarVehiculosActivos() {
        ArrayList<Vehiculo> activos = new ArrayList<>();
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getEstado() == EstadoVehiculoEnum.DENTRO) {
                activos.add(vehiculo);
            }
        }
        return activos;
    }

    public ArrayList<EspacioParqueadero> obtenerEspaciosDisponiblesList() {
        ArrayList<EspacioParqueadero> disponibles = new ArrayList<>();
        for (EspacioParqueadero espacio : espacios) {
            if (espacio.estaDisponible()) {
                disponibles.add(espacio);
            }
        }
        return disponibles;
    }
}