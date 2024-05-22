package com.empresa.javafx_mongo.login;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink registerLink;

    private ConexionMysql conexion;

    public LoginController() {
        conexion = new ConexionMysql();
    }

    @FXML
    private void initialize() {
        // Puedes realizar inicializaciones adicionales aquí
    }

    @FXML
    private void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Por favor, ingresa un nombre de usuario y una contraseña.");
        } else {
            if (validateLogin(username, password)) {
                openHelloView();
            } else {
                showAlert("Error", "Nombre de usuario o contraseña incorrectos.");
            }
        }
    }

    @FXML
    private void handleRegisterLinkAction() {
        // Aquí puedes agregar la lógica para abrir el formulario de registro
        System.out.println("Abrir formulario de registro");
    }

    private boolean validateLogin(String username, String password) {
        String query = "SELECT * FROM usuarios WHERE username = ?";
        try {
            Connection conn = conexion.getConnection();
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                // Comparar la contraseña encriptada almacenada en la base de datos con la proporcionada por el usuario
                String storedPassword = result.getString("password");
                return verifyPassword(password, storedPassword);
            } else {
                return false; // El usuario no existe en la base de datos
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        // Implementa la lógica para verificar la contraseña encriptada aquí (por ejemplo, usando bcrypt)
        return plainPassword.equals(hashedPassword); // Aquí se está comparando la contraseña sin encriptar con la almacenada en la base de datos
    }

    private void openHelloView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/empresa/javafx_mongo/hello-view.fxml"));
            Parent root = loader.load();

            // Mostrar la ventana hello-view.fxml
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual de inicio de sesión
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir la ventana hello-view.");
        }
    }
}
