package trabajoFinal.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import trabajoFinal.enums.RolSistemaEnum;
import trabajoFinal.enums.TipoEspacioEnum;
import trabajoFinal.enums.TipoVehiculoEnum;
import trabajoFinal.modelo.UsuarioSistema;
import trabajoFinal.service.ParkService;
import trabajoFinal.ui.controller.LoginController;

import java.io.IOException;

public class AppParkUQ extends Application {

    private static ParkService parqueadero;

    @Override
    public void start(Stage stage) throws IOException {
        parqueadero = inicializarParqueadero();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("vista/login.fxml"));
        Scene escena = new Scene(loader.load(), 400, 300);

        LoginController controlador = loader.getController();
        controlador.setParqueadero(parqueadero);
        controlador.setStage(stage);

        stage.setTitle("ParkUQ — Sistema de Gestión de Parqueadero");
        stage.setScene(escena);
        stage.setResizable(false);
        stage.show();
    }

    private ParkService inicializarParqueadero() {
        ParkService nuevoParqueadero = new ParkService("Parqueadero Central UQ");

        // Espacios para carros
        for (int i = 1; i <= 10; i++) {
            nuevoParqueadero.agregarEspacio("C-" + String.format("%02d", i), TipoEspacioEnum.CARRO);
        }
        // Espacios para motocicletas
        for (int i = 1; i <= 10; i++) {
            nuevoParqueadero.agregarEspacio("M-" + String.format("%02d", i), TipoEspacioEnum.MOTO);
        }
        // Espacios para bicicletas
        for (int i = 1; i <= 5; i++) {
            nuevoParqueadero.agregarEspacio("B-" + String.format("%02d", i), TipoEspacioEnum.BICICLETA);
        }

        // Tarifas por defecto (por hora)
        nuevoParqueadero.configurarTarifa(TipoVehiculoEnum.CARRO, 3000.0, 0.0);
        nuevoParqueadero.configurarTarifa(TipoVehiculoEnum.MOTO, 2000.0, 0.0);
        nuevoParqueadero.configurarTarifa(TipoVehiculoEnum.BICICLETA, 500.0, 0.0);

        // Usuarios del sistema
        nuevoParqueadero.agregarUsuarioSistema(new UsuarioSistema("operador", "1234", RolSistemaEnum.OPERADOR));
        nuevoParqueadero.agregarUsuarioSistema(new UsuarioSistema("admin", "admin", RolSistemaEnum.ADMINISTRADOR));

        return nuevoParqueadero;
    }

    public static ParkService getParqueadero() {
        return parqueadero;
    }

    public static void main(String[] args) {
        launch(args);
    }
}