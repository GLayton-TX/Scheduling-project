package SchedulingProgram.View_Controller;

import SchedulingProgram.DatabaseAccessObjects.AppointmentDAO;
import SchedulingProgram.DatabaseAccessObjects.AppointmentDAOImpl;
import SchedulingProgram.Model.Appointment;
import SchedulingProgram.Model.Contact;
import SchedulingProgram.Model.Customer;
import SchedulingProgram.Utilities.TimeHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;

public class LandingPage implements Initializable {

    @FXML
    Hyperlink customersLink;
    @FXML
    ComboBox<Month> monthComboBox;
    @FXML
    TableColumn<Appointment,LocalDateTime> startTimeColumn;
    @FXML
    TableColumn<Appointment,LocalDateTime> endTimeColumn;
    @FXML
    TableColumn<Appointment,Integer> appointmentIdColumn;
    @FXML
    TableColumn<Appointment,String> appointmentTitleColumn;
    @FXML
    TableColumn<Appointment,String> appointmentDescriptionColumn;
    @FXML
    TableColumn<Appointment,String> appointmentLocationColumn;
    @FXML
    TableColumn<Contact,String> appointmentContactColumn;
    @FXML
    TableColumn<Appointment,String> appointmentTypeColumn;
    @FXML
    TableColumn<Customer,Integer> appointmentCustomerIdColumn;
    @FXML
    TableView<Appointment> appointmentTable;
    @FXML
    RadioButton allRadioButton;
    @FXML
    RadioButton monthRadioButton;
    @FXML
    RadioButton weekRadioButton;
    @FXML
    Button addAppointmentButton;
    @FXML
    Button modifyAppointmentButton;
    @FXML
    Button deleteAppointmentButton;
    @FXML
    Button exitButton;
    @FXML
    Button reportsButton;

    /**
     * redirects the user to the add appointment page
     * @throws IOException possibility the page is not found to open
     */
    public void addAppointmentButtonClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/SchedulingProgram/View_controller/AddAppointmentPage.fxml"));
        Stage window =  new Stage();
        window.setScene(new Scene(root));
        window.show();
        Stage stage = (Stage) monthComboBox.getScene().getWindow();
        stage.close();
    }

    /**
     * redirects the user to the modify appointment page
     * @throws IOException possibility the page is not found to open
     */
    public void modifyAppointmentButtonClicked() throws IOException {
        if (!appointmentTable.getSelectionModel().isEmpty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SchedulingProgram/View_controller/ModifyAppointmentPage.fxml"));
            Parent root = loader.load();
            Stage window = new Stage();
            window.setScene(new Scene(root));
            window.show();
            Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            ModifyAppointmentPage controller = loader.getController();
            controller.displaySelectedAppointment(selectedAppointment);
            Stage stage = (Stage) appointmentTable.getScene().getWindow();
            stage.close();
        } else {
            new Alert(Alert.AlertType.ERROR,"You must select an Appointment to modify").showAndWait();
        }
    }

    /**
     * deletes the appointment selected from the table
     * displays an error if no selection is made
     */
    public void deleteAppointmentButtonClicked() {
        AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (!appointmentTable.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.CONFIRMATION, "Do you wish to delete " + selectedAppointment.getAppointmentTitle()).showAndWait();

            if (appointmentDAO.deleteAppointment(selectedAppointment))
                new Alert(Alert.AlertType.INFORMATION, "Appointment ID #" + selectedAppointment.getAppointmentId() + " " + selectedAppointment.getAppointmentType() + " was deleted").showAndWait();
        } else {
            new Alert(Alert.AlertType.ERROR,"You must select an Appointment to delete.").showAndWait();
        }
        appointmentTable.setItems(appointmentDAO.getAllAppointments());
    }

    /**
     * displays all the appointments from the database
     */
    public void allRadioButtonSelected (){
        AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
        appointmentTable.setItems(appointmentDAO.getAllAppointments());
        monthComboBox.getSelectionModel().clearSelection();
        monthComboBox.setDisable(true);
    }

    /**
     * displays the appointments in the current week
     */
    public void weekRadioButtonSelected (){
        LocalDateTime today = LocalDateTime.now();
        while (today.getDayOfWeek() != DayOfWeek.SUNDAY) today = today.minusDays(1);
        Timestamp begin = Timestamp.valueOf(today);
        Timestamp end = Timestamp.valueOf(today.plusWeeks(1));
        if (weekRadioButton.isSelected()) {
            AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
            appointmentTable.setItems(appointmentDAO.getAppointmentsByWeek(begin,end));
            monthComboBox.getSelectionModel().clearSelection();
            monthComboBox.setDisable(true);
        }

    }
    /**
     * displays all the appointments per a selected month, beginning month pre selected as current month
     */
    public void monthRadioButtonSelected () {
        ObservableList<Month> months = FXCollections.observableArrayList();
        Collections.addAll(months, Month.values());
        monthComboBox.setItems(months);
        monthComboBox.setValue(LocalDateTime.now().getMonth());
        monthComboBox.setDisable(false);
    }
    /**
     * populates the table based on the selection made in the month combo box
     */
    public void monthSelected (){
        if (monthRadioButton.isSelected()) {
            AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
            Integer month = (monthComboBox.getSelectionModel().getSelectedItem()).getValue();
            appointmentTable.setItems(appointmentDAO.getAppointmentsByMonth(month));
        }
    }

    /**
     * redirects the user to the customers page
     * @throws IOException possibility the page is not found to open
     */
    public void customersLinkClicked () throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/SchedulingProgram/View_controller/CustomersPage.fxml"));
        Stage window =  new Stage();
        window.setScene(new Scene(root));
        window.show();
        Stage stage = (Stage) appointmentTable.getScene().getWindow();
        stage.close();
    }

    /**
     * redirects the user to the reports page
     * @throws IOException possibility the page is not found to open
     */
    public void reportsButtonClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/SchedulingProgram/View_controller/Reports.fxml"));
        Stage window =  new Stage();
        window.setScene(new Scene(root));
        window.show();
        Stage stage = (Stage) appointmentTable.getScene().getWindow();
        stage.close();
    }

    /**
     * This method is called whenever the exitButton is pressed.
     * It launches a confirmation dialog.
     * if the user confirms then this method exits the running program.
     */
    public void exitButtonClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you wish to close the program?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) System.exit(0);
    }

    /**
     * method used to pre-populate the various items on the page
     *
     * Lambda function used to display a more readable timestamp in the system time zone and displaying the time zone name
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
        appointmentTable.setItems(appointmentDAO.getAllAppointments());
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentStartTime"));
        //Lambda 2
        startTimeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime localDateTime, boolean empty) {
                super.updateItem(localDateTime, empty);
                if (localDateTime == null || empty) {
                    setText(null);
                } else {
                    setText((TimeHelper.dateTimeFormatter.format(TimeHelper.utcToZoneDateTime(localDateTime))) + " " + ZoneId.systemDefault().getDisplayName(TextStyle.SHORT_STANDALONE, Locale.ROOT));
                }
            }
        });
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentEndTime"));
        endTimeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime localDateTime, boolean empty) {
                super.updateItem(localDateTime, empty);
                if (localDateTime == null || empty) {
                    setText(null);
                } else {
                    setText(TimeHelper.dateTimeFormatter.format(TimeHelper.utcToZoneDateTime(localDateTime)) + " " + ZoneId.systemDefault().getDisplayName(TextStyle.SHORT_STANDALONE, Locale.ROOT));
                }
            }
        });
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        appointmentContactColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        ToggleGroup toggleGroup = new ToggleGroup();
        this.allRadioButton.setToggleGroup(toggleGroup);
        this.allRadioButton.setSelected(true);
        this.weekRadioButton.setToggleGroup(toggleGroup);
        this.monthRadioButton.setToggleGroup(toggleGroup);

    }
}
