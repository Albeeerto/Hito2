package com.empresa.javafx_mongo.test;


import com.empresa.javafx_mongo.login.LoginController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginControllerValidationTest {

    private LoginController loginController;

    @BeforeEach
    void setUp() {
        loginController = new LoginController();
    }

    @Test
    void testValidateLoginEmptyUsername() {
        String username = "";
        String password = "password123";
        assertFalse(loginController.validateLogin(username, password));
    }

    @Test
    void testValidateLoginEmptyPassword() {
        String username = "user";
        String password = "";
        assertFalse(loginController.validateLogin(username, password));
    }
}