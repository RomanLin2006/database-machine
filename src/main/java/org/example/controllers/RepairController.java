package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import org.example.models.Repair;
import org.example.services.RepairManager;
import java.util.List;
import javafx.scene.control.Alert;

public class RepairController {
    @FXML private TextField clientField;
    @FXML private TextField brandField;
    @FXML private TextField yearField;
    @FXML private TextField repairTypeField;
    @FXML private TextField startDateField;
    @FXML private TextField durationDaysField;
    @FXML private TextField costField;
    @FXML private TextField contactInfoField;
    @FXML private TextField countryField;
    @FXML private ComboBox<Repair> machineComboBox;
    @FXML private CheckBox newMachineCheckBox;

    private Repair repairInfo;
    private boolean saved = false;

    public void setRepairInfo(Repair repairInfo) {
        this.repairInfo = repairInfo;
        if (repairInfo != null) {
            clientField.setText(repairInfo.getClientName());
            contactInfoField.setText(repairInfo.getContactInfo());
            brandField.setText(repairInfo.getBrand());
            yearField.setText(String.valueOf(repairInfo.getYear()));
            countryField.setText(repairInfo.getCountry());
            repairTypeField.setText(repairInfo.getRepairType());
            startDateField.setText(repairInfo.getStartDate());
            durationDaysField.setText(String.valueOf(repairInfo.getDurationDays()));
            costField.setText(String.valueOf(repairInfo.getCost()));
            // Скрыть ComboBox и CheckBox при редактировании
            if (machineComboBox != null) machineComboBox.setVisible(false);
            if (newMachineCheckBox != null) newMachineCheckBox.setVisible(false);
        }
    }

    public boolean isSaved() {
        return saved;
    }

    @FXML
    private void handleSave() {
        // Валидация и сохранение данных
        saved = true;
        Stage stage = (Stage) clientField.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    @FXML
    private void handleCancel() {
        saved = false;
        Stage stage = (Stage) clientField.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    public void initialize() {
        List<Repair> machines = RepairManager.getAllMachinesForCurrentClient();
        machineComboBox.getItems().setAll(machines);
        machineComboBox.setDisable(false);
        newMachineCheckBox.setSelected(false);
        newMachineCheckBox.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                boolean isNew = newMachineCheckBox.isSelected();
                machineComboBox.setDisable(isNew);
                clientField.setDisable(!isNew);
                brandField.setDisable(!isNew);
                yearField.setDisable(!isNew);
                countryField.setDisable(!isNew);
                contactInfoField.setDisable(!isNew);
                if (isNew) {
                    clientField.clear();
                    brandField.clear();
                    yearField.clear();
                    countryField.clear();
                    contactInfoField.clear();
                }
            }
        });
        machineComboBox.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                Repair selected = machineComboBox.getValue();
                if (selected != null) {
                    clientField.clear();
                    brandField.clear();
                    yearField.clear();
                    countryField.clear();
                    contactInfoField.clear();
                    clientField.setDisable(true);
                    brandField.setDisable(true);
                    yearField.setDisable(true);
                    countryField.setDisable(true);
                    contactInfoField.setDisable(true);
                }
            }
        });
    }

    public Repair getResult() {
        // Режим редактирования: ComboBox и CheckBox скрыты
        if ((machineComboBox == null || !machineComboBox.isVisible()) && (newMachineCheckBox == null || !newMachineCheckBox.isVisible())) {
            return new Repair(
                    repairInfo != null ? repairInfo.getRepairId() : 0,
                    clientField.getText(),
                    repairInfo != null ? repairInfo.getMachineId() : 0,
                    brandField.getText(),
                    Integer.parseInt(yearField.getText()),
                    repairTypeField.getText(),
                    startDateField.getText(),
                    Integer.parseInt(durationDaysField.getText()),
                    Double.parseDouble(costField.getText()),
                    contactInfoField.getText(),
                    countryField.getText()
            );
        }
        // Режим добавления
        if (newMachineCheckBox.isSelected()) {
            return new Repair(
                    repairInfo != null ? repairInfo.getRepairId() : 0,
                    clientField.getText(), 0,
                    brandField.getText(),
                    Integer.parseInt(yearField.getText()),
                    repairTypeField.getText(),
                    startDateField.getText(),
                    Integer.parseInt(durationDaysField.getText()),
                    Double.parseDouble(costField.getText()),
                    contactInfoField.getText(),
                    countryField.getText()
            );
        } else {
            Repair selected = machineComboBox.getValue();
            if (selected == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Не выбран станок!");
                alert.showAndWait();
                return null;
            }
            return new Repair(
                    repairInfo != null ? repairInfo.getRepairId() : 0,
                    selected.getClientName(),
                    selected.getMachineId(),
                    selected.getBrand(),
                    selected.getYear(),
                    repairTypeField.getText(),
                    startDateField.getText(),
                    Integer.parseInt(durationDaysField.getText()),
                    Double.parseDouble(costField.getText()),
                    selected.getContactInfo(),
                    selected.getCountry()
            );
        }
    }
}