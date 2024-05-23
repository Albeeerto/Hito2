package com.empresa.javafx_mongo.login;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

public class RegistroController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button registerButton;

    private ConexionMysql conexion;

    public RegistroController() {
        conexion = new ConexionMysql();
    }

    @FXML
    private void initialize() {
        // Puedes realizar inicializaciones adicionales aquí
    }

    @FXML
    private void handleRegisterButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "Por favor, completa todos los campos.");
        } else if (!password.equals(confirmPassword)) {
            showAlert("Error", "Las contraseñas no coinciden.");
        } else {
            // Encriptar la contraseña antes de almacenarla en la base de datos
            String hashedPassword = hashPassword(password);
            if (registerUser(username, hashedPassword)) {
                showAlert("Registro exitoso", "Usuario registrado correctamente.");
                closeWindowAndOpenLogin();
            } else {
                showAlert("Error", "No se pudo registrar el usuario.");
            }
        }
    }

    private boolean registerUser(String username, String password) {
        String query = "INSERT INTO usuarios (username, password) VALUES (?, ?)";
        try (Connection conn = conexion.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            showAlert("Error", "Error al registrar el usuario.");
            e.printStackTrace();
            return false;
        }
    }

    private void closeWindowAndOpenLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/empresa/javafx_mongo/Seleccion.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual de registro
            Stage currentStage = (Stage) registerButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "No se pudo abrir la ventana de selección.");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
