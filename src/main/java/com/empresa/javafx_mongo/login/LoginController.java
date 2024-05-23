package com.empresa.javafx_mongo.login;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import org.mindrot.jbcrypt.BCrypt;
import com.empresa.javafx_mongo.login.CaptchaGenerator;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField captchaField;

    @FXML
    private ImageView captchaImageView;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink registerLink;

    private ConexionMysql conexion;

    private CaptchaGenerator captchaGenerator;
    private String captchaText;

    public LoginController() {
        conexion = new ConexionMysql();
        captchaGenerator = new CaptchaGenerator();
    }

    @FXML
    private void initialize() {
        generateNewCaptcha();
    }

    @FXML
    private void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String captchaInput = captchaField.getText();

        if (username.isEmpty() || password.isEmpty() || captchaInput.isEmpty()) {
            showAlert("Error", "Por favor, complete todos los campos.");
        } else if (!captchaInput.equals(captchaText)) {
            showAlert("Error", "El texto CAPTCHA no coincide.");
            generateNewCaptcha(); // Generar un nuevo CAPTCHA si el anterior es incorrecto
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/empresa/javafx_mongo/registro.fxml"));
            Parent root = loader.load();

            // Mostrar la ventana registro.fxml
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual de inicio de sesión (opcional, si deseas mantener la ventana de login abierta, puedes comentar esta línea)
            Stage currentStage = (Stage) registerLink.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir la ventana de registro.");
        }
    }

    public boolean validateLogin(String username, String password) {
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
        return BCrypt.checkpw(plainPassword, hashedPassword);
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

    private void generateNewCaptcha() {
        captchaText = captchaGenerator.createCaptchaText();
        try {
            InputStream captchaImageStream = captchaGenerator.createCaptchaImage(captchaText);
            Image captchaImage = new Image(captchaImageStream);
            captchaImageView.setImage(captchaImage);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo generar la imagen CAPTCHA.");
        }
    }
}
