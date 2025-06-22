package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.services.RepairManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Загрузка данных из базы данных...");
        RepairManager.loadAllRepairs();
        System.out.println("Данные успешно загружены в контейнер.");
        primaryStage.setTitle("Система управления ремонтами");
    }

    public static void main(String[] args) {
        launch(args);
    }
}