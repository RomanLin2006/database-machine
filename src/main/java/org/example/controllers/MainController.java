package org.example.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.containers.RepairsContainer;
import org.example.Main;
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

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    public void initialize() {
        RepairManager.loadAllRepairs();
        clientNameColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, String>, javafx.beans.value.ObservableValue<String>>() {
            @Override
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Repair, String> data) {
                return new javafx.beans.property.SimpleStringProperty(data.getValue().getClientName());
            }
        });
        brandColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, String>, javafx.beans.value.ObservableValue<String>>() {
            @Override
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Repair, String> data) {
                return new javafx.beans.property.SimpleStringProperty(data.getValue().getBrand());
            }
        });
        yearColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, Integer>, javafx.beans.value.ObservableValue<Integer>>() {
            @Override
            public javafx.beans.value.ObservableValue<Integer> call(TableColumn.CellDataFeatures<Repair, Integer> data) {
                return new javafx.beans.property.SimpleIntegerProperty(data.getValue().getYear()).asObject();
            }
        });
        repairTypeColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, String>, javafx.beans.value.ObservableValue<String>>() {
            @Override
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Repair, String> data) {
                return new javafx.beans.property.SimpleStringProperty(data.getValue().getRepairType());
            }
        });
        startDateColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, String>, javafx.beans.value.ObservableValue<String>>() {
            @Override
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Repair, String> data) {
                return new javafx.beans.property.SimpleStringProperty(data.getValue().getStartDate());
            }
        });
        durationDaysColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, Integer>, javafx.beans.value.ObservableValue<Integer>>() {
            @Override
            public javafx.beans.value.ObservableValue<Integer> call(TableColumn.CellDataFeatures<Repair, Integer> data) {
                return new javafx.beans.property.SimpleIntegerProperty(data.getValue().getDurationDays()).asObject();
            }
        });
        costColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, Double>, javafx.beans.value.ObservableValue<Double>>() {
            @Override
            public javafx.beans.value.ObservableValue<Double> call(TableColumn.CellDataFeatures<Repair, Double> data) {
                return new javafx.beans.property.SimpleDoubleProperty(data.getValue().getCost()).asObject();
            }
        });
        contactInfoColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, String>, javafx.beans.value.ObservableValue<String>>() {
            @Override
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Repair, String> data) {
                return new javafx.beans.property.SimpleStringProperty(data.getValue().getContactInfo());
            }
        });
        countryColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, String>, javafx.beans.value.ObservableValue<String>>() {
            @Override
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Repair, String> data) {
                return new javafx.beans.property.SimpleStringProperty(data.getValue().getCountry());
            }
        });
        machineIdColumn.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Repair, Integer>, javafx.beans.value.ObservableValue<Integer>>() {
            @Override
            public javafx.beans.value.ObservableValue<Integer> call(TableColumn.CellDataFeatures<Repair, Integer> data) {
                return new javafx.beans.property.SimpleIntegerProperty(data.getValue().getMachineId()).asObject();
            }
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

    @FXML
    private void handleAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/views/RepairView.fxml"));
            Parent root = loader.load();
            RepairController controller = loader.getController();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Добавить ремонт");
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
            if (controller.isSaved()) {
                Repair newRepair = controller.getResult();
                if (RepairManager.addRepair(newRepair)) {
                    showAlert("Успех", "Ремонт успешно добавлен!");
                    RepairManager.loadAllRepairs();
                    repairsTable.getItems().setAll(RepairsContainer.getInstance().getRepairs());
                } else {
                    showAlert("Ошибка", "Не удалось добавить ремонт.");
                }
            }
        } catch (Exception e) {
            showAlert("Ошибка", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEdit() {
        Repair selected = repairsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите запись для редактирования.");
            return;
        }
        try {
            int oldMachineId = selected.getMachineId();
            String oldClientName = selected.getClientName();
            String oldBrand = selected.getBrand();
            int oldYear = selected.getYear();
            String oldCountry = selected.getCountry();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/views/RepairView.fxml"));
            Parent root = loader.load();
            RepairController controller = loader.getController();
            controller.setRepairInfo(selected);
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Изменить ремонт");
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
            if (controller.isSaved()) {
                Repair updated = controller.getResult();
                if (RepairManager.updateRepair(updated, oldMachineId, oldClientName, oldBrand, oldYear, oldCountry)) {
                    showAlert("Успех", "Ремонт успешно обновлён!");
                    RepairManager.loadAllRepairs();
                    repairsTable.getItems().setAll(RepairsContainer.getInstance().getRepairs());
                } else {
                    showAlert("Ошибка", "Не удалось обновить ремонт.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Repair selected = repairsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите запись для удаления.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Удалить выбранный ремонт?", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Подтверждение удаления");
        confirm.setHeaderText(null);
        confirm.showAndWait();
        if (confirm.getResult() == ButtonType.YES) {
            if (RepairManager.deleteRepair(selected.getRepairId())) {
                showAlert("Успех", "Ремонт успешно удалён!");
                RepairManager.loadAllRepairs();
                repairsTable.getItems().setAll(RepairsContainer.getInstance().getRepairs());
            } else {
                showAlert("Ошибка", "Не удалось удалить ремонт.");
            }
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleLogout() {
        Stage stage = (Stage) repairsTable.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new Main().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @FXML
    private void handleEditAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/views/AuthDialog.fxml"));
            Parent root = loader.load();
            org.example.controllers.AuthController controller = loader.getController();
            controller.setMode(org.example.controllers.AuthController.Mode.EDIT);
            controller.setUser(currentUser);
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Изменить данные пользователя");
            dialogStage.setScene(new Scene(root, 400, 250));
            dialogStage.showAndWait();
            if (controller.isSaved()) {
                boolean success = org.example.services.AuthManager.updateUser(currentUser.getId(), controller.getNewLogin(), controller.getNewPassword());
                if (success) {
                    showAlert("Успех", "Данные успешно обновлены!");
                    currentUser = new org.example.models.User(currentUser.getId(), controller.getNewLogin(), currentUser.getRole());
                } else {
                    showAlert("Ошибка", "Не удалось обновить данные пользователя");
                }
            }
        } catch (Exception e) {
            showAlert("Ошибка", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}