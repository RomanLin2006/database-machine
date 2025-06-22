package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.services.RepairManager;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Загрузка данных из базы данных...");
        RepairManager.loadAllRepairs();
        System.out.println("Данные успешно загружены в контейнер.");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/views/MainView.fxml")));

        primaryStage.setTitle("Система управления ремонтами");
        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}