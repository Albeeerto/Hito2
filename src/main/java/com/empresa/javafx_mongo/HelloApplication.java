package com.empresa.javafx_mongo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {


    @Override
    public void start(Stage primaryStage) throws IOException {
        // Cargar el archivo FXML de la pantalla de selección
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/empresa/javafx_mongo/seleccion.fxml"));
        Parent root = fxmlLoader.load();

        // Configurar la escena de la pantalla de selección
        Scene scene = new Scene(root);

        primaryStage.setTitle("Selección de Inicio de Sesión o Registro");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
