package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.models.User;
import org.example.services.AuthManager;
import java.util.function.Consumer;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class AuthController {
    public enum Mode {LOGIN, REGISTER, EDIT}

    private Mode mode;
    private User user;
    private Runnable onRegister;
    private boolean saved = false;
    private Consumer<User> onLoginSuccess;

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label loginLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private Button mainActionButton;
    @FXML
    private Button secondaryActionButton;
    @FXML
    private Button cancelButton;

    public void setMode(Mode mode) { // Установка режима (вход/регистр/эдит)
        this.mode = mode;
        updateInterface();
    }

    private void updateInterface() { // Обновление интерфейса
        switch (mode) {
            case LOGIN:
                loginLabel.setText("Логин:");
                passwordLabel.setText("Пароль:");
                confirmPasswordLabel.setVisible(false);
                confirmPasswordField.setVisible(false);
                mainActionButton.setText("Войти");
                mainActionButton.setVisible(true);
                secondaryActionButton.setText("Регистрация");
                secondaryActionButton.setVisible(true);
                cancelButton.setVisible(false);
                break;
            case REGISTER:
                loginLabel.setText("Логин:");
                passwordLabel.setText("Пароль:");
                confirmPasswordLabel.setText("Повторите пароль:");
                confirmPasswordLabel.setVisible(true);
                confirmPasswordField.setVisible(true);
                mainActionButton.setText("Зарегистрироваться");
                mainActionButton.setVisible(true);
                secondaryActionButton.setVisible(false);
                cancelButton.setVisible(true);
                break;
            case EDIT:
                loginLabel.setText("Новый логин:");
                passwordLabel.setText("Новый пароль:");
                confirmPasswordLabel.setText("Повторите пароль:");
                confirmPasswordLabel.setVisible(true);
                confirmPasswordField.setVisible(true);
                mainActionButton.setText("Сохранить");
                mainActionButton.setVisible(true);
                secondaryActionButton.setVisible(false);
                cancelButton.setVisible(true);
                break;
        }
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            loginField.setText(user.getLogin());
        }
    }

    // Обработчики успешного входа и регистрации
    public void setOnLoginSuccess(Consumer<User> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }

    public void setOnRegisterSuccess(Runnable onRegister) {
        this.onRegister = onRegister;
    }

    public String getNewLogin() { return loginField.getText(); }
    public String getNewPassword() { return passwordField.getText(); }

    @FXML
    private void handleAction() { // Обработка действия (вход/регистр/эдит)
        switch (mode) {
            case LOGIN:
                handleLogin();
                break;
            case REGISTER:
                handleRegister();
                break;
            case EDIT:
                handleEdit();
                break;
        }
    }

    private void handleLogin() { // Вход
        String login = loginField.getText();
        String password = passwordField.getText();
        try {
            User user = AuthManager.login(login, password);
            if (user != null) {
                if (onLoginSuccess != null) onLoginSuccess.accept(user);
                closeStage();
            } else {
                showAlert("Ошибка", "Неверный логин или пароль");
            }
        } catch (Exception e) {
            showAlert("Ошибка", e.getMessage());
        }
    }

    private void handleRegister() { // Регитсрация
        String login = loginField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if (!password.equals(confirmPassword)) {
            showAlert("Ошибка", "Пароли не совпадают");
            return;
        }
        try {
            boolean success = AuthManager.register(login, password);
            if (success) {
                showAlert("Успех", "Регистрация прошла успешно!");
                closeStage();
            } else {
                showAlert("Ошибка", "Логин уже занят");
            }
        } catch (Exception e) {
            showAlert("Ошибка", e.getMessage());
        }
    }

    private void handleEdit() { // Изменение данных
        String login = loginField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if (!password.equals(confirmPassword)) {
            showAlert("Ошибка", "Пароли не совпадают");
            return;
        }
        if (login.isEmpty() || password.isEmpty()) {
            showAlert("Ошибка", "Логин и пароль не должны быть пустыми");
            return;
        }
        saved = true;
        closeStage();
    }

    @FXML
    private void handleCancel() {
        if (mode == Mode.REGISTER || mode == Mode.EDIT) {
            closeStage();
        }
    }

    @FXML
    private void handleSecondaryAction() {
        if (mode == Mode.LOGIN) {
            try {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/org/example/views/AuthDialog.fxml"));
                javafx.scene.Parent root = loader.load();
                AuthController controller = loader.getController();
                controller.setMode(Mode.REGISTER);
                javafx.stage.Stage dialogStage = new javafx.stage.Stage();
                dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
                dialogStage.setTitle("Регистрация");
                dialogStage.setScene(new javafx.scene.Scene(root, 400, 350));
                dialogStage.setResizable(false);
                dialogStage.showAndWait();
            } catch (Exception e) {
                showAlert("Ошибка", e.getMessage());
            }
        }
    }

    private void closeStage() { // Закрытие окна
        Stage stage = (Stage) loginField.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    // Показ всплывающих сообщений об ошибках/успехе
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
