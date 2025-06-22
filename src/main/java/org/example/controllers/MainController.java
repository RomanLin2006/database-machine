package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.example.containers.RepairsContainer;
import org.example.models.Repair;
import org.example.models.User;
import org.example.services.RepairManager;

public class MainController {
    @FXML private TableView<Repair> repairsTable;
    @FXML private TableColumn<Repair, String> clientNameColumn;
    @FXML private TableColumn<Repair, String> brandColumn;
    @FXML private TableColumn<Repair, Integer> yearColumn;
    @FXML private TableColumn<Repair, String> repairTypeColumn;
    @FXML private TableColumn<Repair, String> startDateColumn;
    @FXML private TableColumn<Repair, Integer> durationDaysColumn;
    @FXML private TableColumn<Repair, Double> costColumn;
    @FXML private TableColumn<Repair, String> contactInfoColumn;
    @FXML private TableColumn<Repair, String> countryColumn;
    @FXML private TableColumn<Repair, Integer> machineIdColumn;
    @FXML private HBox adminButtonsBox;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    public void initialize() {
        // Загрузка данных из БД
        RepairManager.loadAllRepairs();
        // Привязка столбцов к полям модели
        clientNameColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, String>, javafx.beans.value.ObservableValue<String>>() {
            @Override
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Repair, String> data) {
                return new javafx.beans.property.SimpleStringProperty(data.getValue().getClientName()); }
        });
        brandColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, String>, javafx.beans.value.ObservableValue<String>>() {
            @Override
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Repair, String> data) {
                return new javafx.beans.property.SimpleStringProperty(data.getValue().getBrand()); }
        });
        yearColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, Integer>, javafx.beans.value.ObservableValue<Integer>>() {
            @Override
            public javafx.beans.value.ObservableValue<Integer> call(TableColumn.CellDataFeatures<Repair, Integer> data) {
                return new javafx.beans.property.SimpleIntegerProperty(data.getValue().getYear()).asObject(); }
        });
        repairTypeColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, String>, javafx.beans.value.ObservableValue<String>>() {
            @Override
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Repair, String> data) {
                return new javafx.beans.property.SimpleStringProperty(data.getValue().getRepairType()); }
        });
        startDateColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, String>, javafx.beans.value.ObservableValue<String>>() {
            @Override
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Repair, String> data) {
                return new javafx.beans.property.SimpleStringProperty(data.getValue().getStartDate()); }
        });
        durationDaysColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, Integer>, javafx.beans.value.ObservableValue<Integer>>() {
            @Override
            public javafx.beans.value.ObservableValue<Integer> call(TableColumn.CellDataFeatures<Repair, Integer> data) {
                return new javafx.beans.property.SimpleIntegerProperty(data.getValue().getDurationDays()).asObject(); }
        });
        costColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, Double>, javafx.beans.value.ObservableValue<Double>>() {
            @Override
            public javafx.beans.value.ObservableValue<Double> call(TableColumn.CellDataFeatures<Repair, Double> data) {
                return new javafx.beans.property.SimpleDoubleProperty(data.getValue().getCost()).asObject(); }
        });
        contactInfoColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, String>, javafx.beans.value.ObservableValue<String>>() {
            @Override
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Repair, String> data) {
                return new javafx.beans.property.SimpleStringProperty(data.getValue().getContactInfo()); }
        });
        countryColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, String>, javafx.beans.value.ObservableValue<String>>() {
            @Override
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Repair, String> data) {
                return new javafx.beans.property.SimpleStringProperty(data.getValue().getCountry()); }
        });
        machineIdColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, Integer>, javafx.beans.value.ObservableValue<Integer>>() {
            @Override
            public javafx.beans.value.ObservableValue<Integer> call(TableColumn.CellDataFeatures<Repair, Integer> data) {
                return new javafx.beans.property.SimpleIntegerProperty(data.getValue().getMachineId()).asObject(); }
        });
        repairsTable.getItems().setAll(RepairsContainer.getInstance().getRepairs());
        adminButtonsBox.setVisible(false);

        clientNameColumn.setMinWidth(180);
        contactInfoColumn.setMinWidth(180);
    }

    public void showAdminButtons() {
        if (currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            adminButtonsBox.setVisible(true);
        }
    }
}
