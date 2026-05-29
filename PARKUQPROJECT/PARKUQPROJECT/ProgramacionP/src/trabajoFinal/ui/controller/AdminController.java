package trabajoFinal.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import trabajoFinal.enums.EstadoEspacioEnum;
import trabajoFinal.enums.TipoEspacioEnum;
import trabajoFinal.enums.TipoUsuarioEnum;
import trabajoFinal.enums.TipoVehiculoEnum;
import trabajoFinal.modelo.Administrativo;
import trabajoFinal.modelo.Docente;
import trabajoFinal.modelo.EspacioParqueadero;
import trabajoFinal.modelo.Estudiante;
import trabajoFinal.modelo.Tarifa;
import trabajoFinal.modelo.Usuario;
import trabajoFinal.modelo.Visitante;
import trabajoFinal.service.ParkService;

public class AdminController {

    // Pestaña: Espacios
    @FXML
    private TextField campoCodigoEspacio;
    @FXML
    private ComboBox<String> comboTipoEspacio;
    @FXML
    private TextField campoCodigoModificar;
    @FXML
    private ComboBox<String> comboNuevoEstado;
    @FXML
    private Label etiquetaEspacios;
    @FXML
    private TableView<EspacioParqueadero> tablaEspacios;
    @FXML
    private TableColumn<EspacioParqueadero, String> colCodigo;
    @FXML
    private TableColumn<EspacioParqueadero, String> colTipoEspacio;
    @FXML
    private TableColumn<EspacioParqueadero, String> colEstado;

    // Pestaña: Tarifas
    @FXML
    private ComboBox<String> comboTipoVehiculo;
    @FXML
    private TextField campoNuevoValor;
    @FXML
    private Label etiquetaTarifas;
    @FXML
    private TableView<Tarifa> tablaTarifas;
    @FXML
    private TableColumn<Tarifa, String> colTarifaTipo;
    @FXML
    private TableColumn<Tarifa, Double> colValorHora;

    // Pestaña: Usuarios autorizados
    @FXML
    private TextField campoNombreUsuario;
    @FXML
    private TextField campoIdUsuario;
    @FXML
    private ComboBox<String> comboTipoUsuario;
    @FXML
    private Label etiquetaUsuarios;
    @FXML
    private TableView<Usuario> tablaUsuarios;
    @FXML
    private TableColumn<Usuario, String> colNombreUsuario;
    @FXML
    private TableColumn<Usuario, String> colIdUsuario;
    @FXML
    private TableColumn<Usuario, String> colTipoUsuario;
    @FXML
    private TableColumn<Usuario, String> colDescuento;

    private ParkService parqueadero;
    private Stage stage;

    public void inicializar() {
        comboTipoEspacio.setItems(FXCollections.observableArrayList("CARRO", "MOTO", "BICICLETA"));
        comboTipoEspacio.setValue("CARRO");

        comboNuevoEstado.setItems(FXCollections.observableArrayList("DISPONIBLE", "FUERA_DE_SERVICIO", "OCUPADO"));
        comboNuevoEstado.setValue("DISPONIBLE");

        comboTipoVehiculo.setItems(FXCollections.observableArrayList("CARRO", "MOTO", "BICICLETA"));
        comboTipoVehiculo.setValue("CARRO");

        comboTipoUsuario
                .setItems(FXCollections.observableArrayList("ESTUDIANTE", "DOCENTE", "ADMINISTRATIVO", "VISITANTE"));
        comboTipoUsuario.setValue("ESTUDIANTE");

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colTipoEspacio.setCellValueFactory(new PropertyValueFactory<>("tipoEspacio"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        colTarifaTipo.setCellValueFactory(new PropertyValueFactory<>("tipoVehiculo"));
        colValorHora.setCellValueFactory(new PropertyValueFactory<>("valorPorHora"));

        colNombreUsuario.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colIdUsuario.setCellValueFactory(new PropertyValueFactory<>("identificacion"));
        colTipoUsuario.setCellValueFactory(new PropertyValueFactory<>("tipoUsuario"));
        colDescuento.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().tieneDescuento() ? "Sí" : "No"));

        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener(
                (observable, anterior, seleccionado) -> {
                    if (seleccionado != null) {
                        campoNombreUsuario.setText(seleccionado.getNombre());
                        campoIdUsuario.setText(seleccionado.getIdentificacion());
                        comboTipoUsuario.setValue(seleccionado.getTipoUsuario());
                    }
                });

        actualizarTablas();
    }

    @FXML
    public void agregarEspacio() {
        String codigo = campoCodigoEspacio.getText().trim().toUpperCase();
        String tipoTexto = comboTipoEspacio.getValue();

        if (codigo.isEmpty()) {
            etiquetaEspacios.setText("Ingrese el código del espacio.");
            return;
        }

        TipoEspacioEnum tipo = TipoEspacioEnum.valueOf(tipoTexto);
        parqueadero.agregarEspacio(codigo, tipo);
        etiquetaEspacios.setText("Espacio " + codigo + " agregado correctamente.");
        campoCodigoEspacio.clear();
        actualizarTablas();
    }

    @FXML
    public void modificarEstadoEspacio() {
        String codigo = campoCodigoModificar.getText().trim().toUpperCase();
        String estadoTexto = comboNuevoEstado.getValue();

        if (codigo.isEmpty()) {
            etiquetaEspacios.setText("Ingrese el código del espacio a modificar.");
            return;
        }

        EstadoEspacioEnum estado = EstadoEspacioEnum.valueOf(estadoTexto);
        if (estado == EstadoEspacioEnum.FUERA_DE_SERVICIO) {
            parqueadero.deshabilitarEspacio(codigo);
        } else {
            for (EspacioParqueadero espacio : parqueadero.getEspacios()) {
                if (espacio.getCodigo().equalsIgnoreCase(codigo)) {
                    espacio.setEstado(estado);
                }
            }
        }

        etiquetaEspacios.setText("Estado del espacio " + codigo + " actualizado a " + estadoTexto + ".");
        campoCodigoModificar.clear();
        actualizarTablas();
    }

    @FXML
    public void modificarTarifa() {
        String tipoTexto = comboTipoVehiculo.getValue();
        String valorStr = campoNuevoValor.getText().trim();

        if (valorStr.isEmpty()) {
            etiquetaTarifas.setText("Ingrese el nuevo valor por hora.");
            return;
        }

        try {
            double nuevoValor = Double.parseDouble(valorStr);
            TipoVehiculoEnum tipo = TipoVehiculoEnum.valueOf(tipoTexto);
            parqueadero.configurarTarifa(tipo, nuevoValor, 0.0);
            etiquetaTarifas.setText(
                    "Tarifa para " + tipoTexto + " actualizada a $" + String.format("%.0f", nuevoValor) + "/hora.");
            campoNuevoValor.clear();
            actualizarTablas();

        } catch (NumberFormatException e) {
            etiquetaTarifas.setText("El valor debe ser un número.");
        }
    }

    @FXML
    public void agregarUsuarioAutorizado() {
        String nombre = campoNombreUsuario.getText().trim();
        String id = campoIdUsuario.getText().trim();
        String tipoTexto = comboTipoUsuario.getValue();

        if (nombre.isEmpty() || id.isEmpty()) {
            etiquetaUsuarios.setText("Complete el nombre y la identificación.");
            return;
        }

        parqueadero.registrarUsuario(nombre, id, tipoTexto);
        etiquetaUsuarios.setText("Usuario " + nombre + " registrado.");
        campoNombreUsuario.clear();
        campoIdUsuario.clear();
        actualizarTablas();
    }

    @FXML
    public void eliminarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            etiquetaUsuarios.setText("Seleccione un usuario de la tabla para eliminarlo.");
            return;
        }
        parqueadero.getUsuarios()
                .removeIf(usuario -> usuario.getIdentificacion().equals(seleccionado.getIdentificacion()));
        etiquetaUsuarios.setText("Usuario " + seleccionado.getNombre() + " eliminado.");
        campoNombreUsuario.clear();
        campoIdUsuario.clear();
        actualizarTablas();
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

    private void actualizarTablas() {
        tablaEspacios.setItems(FXCollections.observableArrayList(parqueadero.getEspacios()));
        tablaTarifas.setItems(FXCollections.observableArrayList(parqueadero.getTarifas()));
        tablaUsuarios.setItems(FXCollections.observableArrayList(parqueadero.getUsuarios()));
    }

    public void setParqueadero(ParkService parqueadero) {
        this.parqueadero = parqueadero;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
