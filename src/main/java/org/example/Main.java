package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.controllers.AuthController;
import org.example.controllers.MainController;
import org.example.models.User;

public class Main extends Application {
    private Stage primaryStage;
    private User currentUser;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        showLoginView();
    }

    private void showLoginView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/views/AuthView.fxml"));
        Parent root = loader.load();
        AuthController controller = loader.getController();
        controller.setMode(AuthController.Mode.LOGIN);
        controller.setOnLoginSuccess(new java.util.function.Consumer<User>() {
            @Override
            public void accept(User user) {
                Main.this.currentUser = user;
                try {
                    showMainView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Stage stage = (Stage) root.getScene().getWindow();
                if (stage != null) {
                    stage.close();
                }
            }
        });
        primaryStage.setTitle("Авторизация");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void showMainView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/views/MainView.fxml"));
        Parent root = loader.load();
        MainController mainController = loader.getController();
        mainController.setCurrentUser(currentUser); // <-- передайте пользователя
        mainController.showAdminButtons(); // если нужно
        primaryStage.setTitle("Главное окно");
        primaryStage.setScene(new Scene(root, 1135, 600));
        primaryStage.setResizable(true);
        primaryStage.setX(120);
        primaryStage.setY(80);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}