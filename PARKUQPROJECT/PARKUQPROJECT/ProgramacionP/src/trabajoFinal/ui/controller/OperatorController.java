package trabajoFinal.ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import trabajoFinal.enums.EstadoVehiculoEnum;
import trabajoFinal.enums.TipoVehiculoEnum;
import trabajoFinal.excepcion.ParkUQException;
import trabajoFinal.modelo.Bicicleta;
import trabajoFinal.modelo.Carro;
import trabajoFinal.modelo.EspacioParqueadero;
import trabajoFinal.modelo.Moto;
import trabajoFinal.modelo.RegistroSalida;
import trabajoFinal.modelo.Vehiculo;
import trabajoFinal.service.GestorReportService;
import trabajoFinal.service.ParkService;

public class OperatorController {

    // Pestaña: Ingreso
    @FXML
    private TextField campoPlacaIngreso;
    @FXML
    private ComboBox<String> combotipoVehiculo;
    @FXML
    private TextField campoNombreConductor;
    @FXML
    private TextField campoIdConductor;
    @FXML
    private TextField campoCilindraje;
    @FXML
    private Label etiquetaResultadoIngreso;
    @FXML
    private ComboBox<String> comboTipoUsuario;
    // Pestaña: Salida
    @FXML
    private TextField campoPlacaSalida;
    @FXML
    private TextArea areaRecibo;

    // Pestaña: Consultas
    @FXML
    private TableView<Vehiculo> tablaVehiculos;
    @FXML
    private TableColumn<Vehiculo, String> colPlaca;
    @FXML
    private TableColumn<Vehiculo, String> colTipo;
    @FXML
    private TableColumn<Vehiculo, String> colConductor;
    @FXML
    private TableColumn<Vehiculo, String> colEspacio;
    @FXML
    private TableColumn<Vehiculo, String> colHoraIngreso;
    @FXML
    private Label etiquetaEspaciosDisponibles;

    // Pestaña: Reportes
    @FXML
    private TextArea areaReporte;

    private ParkService parqueadero;
    private Stage stage;
    private GestorReportService gestorReportService;

    public void inicializar() {
        gestorReportService = new GestorReportService(parqueadero);
        combotipoVehiculo.setItems(FXCollections.observableArrayList("CARRO", "MOTO", "BICICLETA"));
        combotipoVehiculo.setValue("CARRO");

        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoDescripcion"));
        colConductor.setCellValueFactory(new PropertyValueFactory<>("nombreConductor"));
        colEspacio.setCellValueFactory(new PropertyValueFactory<>("espacioAsignado"));
        colHoraIngreso.setCellValueFactory(new PropertyValueFactory<>("horaIngresoFormateada"));
        comboTipoUsuario.setItems(FXCollections.observableArrayList(
                "ESTUDIANTE", "DOCENTE", "ADMINISTRATIVO", "VISITANTE"));
        comboTipoUsuario.setValue("VISITANTE");
        actualizarConsultas();
    }

    @FXML
    public void registrarIngreso() {
        String placa = campoPlacaIngreso.getText().trim().toUpperCase();
        String nombre = campoNombreConductor.getText().trim();
        String id = campoIdConductor.getText().trim();
        String tipoTexto = combotipoVehiculo.getValue();
        String tipoUsuario = comboTipoUsuario.getValue();

        if (placa.isEmpty() || nombre.isEmpty() || id.isEmpty()) {
            etiquetaResultadoIngreso.setText("Complete todos los campos obligatorios.");
            return;
        }

        try {
            TipoVehiculoEnum tipoEnum = TipoVehiculoEnum.valueOf(tipoTexto);
            parqueadero.registrarIngreso(placa, tipoEnum, nombre, id);

            // Buscar el vehículo para obtener su espacio y mostrarlo en la UI
            String codigoEspacio = "";
            for (Vehiculo vehiculo : parqueadero.getVehiculos()) {
                if (vehiculo.getPlaca().equalsIgnoreCase(placa) && vehiculo.getEstado() == EstadoVehiculoEnum.DENTRO) {
                    codigoEspacio = vehiculo.getEspacioAsignado();
                    break;
                }
            }
            etiquetaResultadoIngreso.setText("Ingreso registrado. Espacio: " + codigoEspacio + " | Usuario: " + tipoUsuario);
        } catch (Exception e) {
            etiquetaResultadoIngreso.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void registrarSalida() {
        String placa = campoPlacaSalida.getText().trim().toUpperCase();

        if (placa.isEmpty()) {
            areaRecibo.setText("Ingrese la placa del vehículo.");
            return;
        }

        try {
            double totalCobrado = parqueadero.registrarSalida(placa);

            // Obtener el último registro del historial para generar recibo
            if (!parqueadero.getHistorialDia().isEmpty()) {
                RegistroSalida registro = parqueadero.getHistorialDia().get(parqueadero.getHistorialDia().size() - 1);
                areaRecibo.setText(registro.generarRecibo());
            } else {
                areaRecibo.setText("Salida registrada. Total: $" + totalCobrado);
            }

            campoPlacaSalida.clear();
            actualizarConsultas();

        } catch (Exception e) {
            areaRecibo.setText(e.getMessage());
        }
    }

    @FXML
    public void actualizarConsultas() {
        ObservableList<Vehiculo> vehiculos = FXCollections.observableArrayList(
                parqueadero.consultarVehiculosActivos());
        tablaVehiculos.setItems(vehiculos);

        int disponibles = parqueadero.obtenerEspaciosDisponiblesList().size();
        int total = parqueadero.getEspacios().size();
        etiquetaEspaciosDisponibles.setText("Espacios disponibles: " + disponibles + " / " + total);
    }

    @FXML
    public void generarReporte() {
        areaReporte.setText(gestorReportService.generarResumenDia());
    }

    private void limpiarCamposIngreso() {
        campoPlacaIngreso.clear();
        campoNombreConductor.clear();
        campoIdConductor.clear();
        campoCilindraje.clear();
        combotipoVehiculo.setValue("CARRO");
        comboTipoUsuario.setValue("VISITANTE");
    }

    @FXML
    public void cerrarSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../vista/login.fxml"));
            Scene escena = new Scene(loader.load(), 400, 300);
            LoginController controlador = loader.getController();
            controlador.setParqueadero(parqueadero);
            controlador.setStage(stage);
            stage.setScene(escena);
            stage.setTitle("ParkUQ — Sistema de Gestión de Parqueadero");
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setParqueadero(ParkService parqueadero) {
        this.parqueadero = parqueadero;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
