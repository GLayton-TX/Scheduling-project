package SchedulingProgram.View_Controller;

import SchedulingProgram.DatabaseAccessObjects.*;
import SchedulingProgram.Model.Appointment;
import SchedulingProgram.Model.Contact;
import SchedulingProgram.Model.Customer;
import SchedulingProgram.Model.User;
import SchedulingProgram.Utilities.TimeHelper;
import SchedulingProgram.Utilities.Validate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AddAppointmentPage implements Initializable {
    @FXML
    ComboBox<User> userComboBox;
    @FXML
    Button clearDateTimeButton;
    @FXML
    ComboBox<LocalTime> endTimeComboBox;
    @FXML
    ComboBox<LocalTime> startTimeComboBox;
    @FXML
    DatePicker startDatePicker;
    @FXML
    DatePicker endDatePicker;
    @FXML
    TextField appointmentIdField;
    @FXML
    Button addAppointmentButton;
    @FXML
    TextField appointmentTypeField;
    @FXML
    TextField appointmentTitleField;
    @FXML
    TextField descriptionField;
    @FXML
    TextField appointmentLocationField;
    @FXML
    ComboBox<Contact>contactComboBox;
    @FXML
    ComboBox<Customer> customerComboBox;
    @FXML
    Button exitButton;
    @FXML
    Hyperlink goBackLink;


    /**
     * triggers enabling of the start time combo box once the start date has been selected
     */
    public void startDatePickerIsSelected() {
        startTimeComboBox.setDisable(false);
    }

    /**
     * triggers the enabling of the end date picker once a start time has been selected
     * disables all dates before the date selected as the start date as that would not be possible
     */
    public void startTimeComboBoxIsSelected() {
        endDatePicker.setDisable(false);
        endDatePicker.setDayCellFactory(datePicker -> new DateCell() {
            public void updateItem(LocalDate localDate, boolean empty) {
                super.updateItem(localDate, empty);
                setDisable(empty || localDate.isBefore(startDatePicker.getValue()));
            }
        });
        endDatePicker.setValue(startDatePicker.getValue());
    }

    /**
     *triggers the enabling of the end time combo box once an end date has been selected
     */
    public void endDatePickerIsSelected() {
        endTimeComboBox.setDisable(false);

    }

    /**
     * method takes the information entered on the page and passes it to a validation method before saving to database or prompting user for corrections
     * @throws IOException Exception based on user entering empty values
     */
    public void addAppointmentButtonClicked() throws IOException {
        if (startDatePicker.getValue() == null
                || endDatePicker.getValue() == null
                || startTimeComboBox.getValue() == null
                || endTimeComboBox.getValue() == null
                || customerComboBox.getValue() == null
                || userComboBox.getValue() == null
                || contactComboBox.getValue() == null
        ) {
            new Alert(Alert.AlertType.ERROR, "You must fill out all fields").showAndWait();
        }
        if (LocalDateTime.of(startDatePicker.getValue(),startTimeComboBox.getValue()).isAfter(LocalDateTime.of(endDatePicker.getValue(),endTimeComboBox.getValue())))
        {
            new Alert(Alert.AlertType.ERROR, "End of meeting must be after start of meeting").showAndWait();
        } else {
            Appointment validAppointment = Validate.validateAppointment(
                    Integer.parseInt(appointmentIdField.getText()),
                    appointmentTitleField.getText(),
                    descriptionField.getText(),
                    appointmentLocationField.getText(),
                    appointmentTypeField.getText(),
                    LocalDateTime.of(startDatePicker.getValue(), startTimeComboBox.getValue()),
                    LocalDateTime.of(endDatePicker.getValue(), endTimeComboBox.getValue()),
                    TimeHelper.zoneDateTimeToUtc(LocalDateTime.now()),
                    LogInScreen.getLoggedUser().getUserName(),
                    TimeHelper.zoneDateTimeToUtc(LocalDateTime.now()),
                    LogInScreen.getLoggedUser().getUserName(),
                    customerComboBox.getSelectionModel().getSelectedItem().getID(),
                    userComboBox.getSelectionModel().getSelectedItem().getId(),
                    contactComboBox.getSelectionModel().getSelectedItem().getContactId()
            );
            if (validAppointment != null) {
                AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
                if (appointmentDAO.addAppointment(validAppointment)) {
                    new Alert(Alert.AlertType.INFORMATION, "Appointment was added").showAndWait();
                    Parent root = FXMLLoader.load(getClass().getResource("/SchedulingProgram/View_controller/LandingPage.fxml"));
                    Stage window =  new Stage();
                    window.setScene(new Scene(root));
                    window.show();
                    Stage stage = (Stage) appointmentIdField.getScene().getWindow();
                    stage.close();
                }
            }
        }
    }

    /**
     * method to prompt user before exiting the program
     */
    public void exitButtonClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you wish to close the program?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) System.exit(0);
    }

    /**
     * method to return user to the previous page
     * @throws IOException Exception based on the fact the page to return to may not be found
     */
    public void goBackLinkClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/SchedulingProgram/View_controller/LandingPage.fxml"));
        Stage window =  new Stage();
        window.setScene(new Scene(root));
        window.show();
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * clears the information in the start date picker, start time, end date picker and end time boxes
     */
    public void clearDateTimeButtonClicked() {
        startDatePicker.setValue(null);
        startDatePicker.setDisable(false);
        startDatePicker.setDayCellFactory(datePicker -> new DateCell() {
            public void updateItem(LocalDate localDate, boolean empty) {
                super.updateItem(localDate, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || localDate.compareTo(today) < 0);
            }
        });
        startTimeComboBox.getItems().clear();
        startTimeComboBox.setDisable(true);
        startTimeComboBox.getItems().addAll(TimeHelper.getTimeBlocks());
        endDatePicker.setValue(null);
        endDatePicker.setDisable(true);
        endTimeComboBox.getItems().clear();
        endTimeComboBox.setDisable(true);
        endTimeComboBox.getItems().addAll(TimeHelper.getTimeBlocks());
    }

    /**
     * initializes data for the population of choice boxes
     *
     * Lambda function to set the date pickers date to not allow dates before the current date to be selected
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        endTimeComboBox.getItems().addAll(TimeHelper.getTimeBlocks());
        startTimeComboBox.getItems().addAll(TimeHelper.getTimeBlocks());
        appointmentIdField.setText(Integer.toString(AppointmentDAOImpl.getNextAppointmentId()));
        appointmentIdField.setDisable(true);
        ContactDAO contactDAO = new ContactDAOImpl();
        contactComboBox.getItems().addAll(contactDAO.getAllContacts());
        CustomerDAO customerDAO = new CustomerDAOImpl();
        customerComboBox.getItems().addAll(customerDAO.getAllCustomers());
        UserDAO userDAO = new UserDaoImpl();
        userComboBox.getItems().addAll(userDAO.getAllUsers());
        startTimeComboBox.setDisable(true);
        endTimeComboBox.setDisable(true);
        endDatePicker.setDisable(true);
       //Lambda #1
        startDatePicker.setDayCellFactory(datePicker -> new DateCell() {
            public void updateItem(LocalDate localDate, boolean empty) {
                super.updateItem(localDate, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || localDate.compareTo(today) < 0);
            }
        });

    }




}
