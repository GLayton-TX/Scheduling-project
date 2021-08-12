package SchedulingProgram.View_Controller;

import SchedulingProgram.DatabaseAccessObjects.AppointmentDAO;
import SchedulingProgram.DatabaseAccessObjects.AppointmentDAOImpl;
import SchedulingProgram.DatabaseAccessObjects.CustomerDAO;
import SchedulingProgram.DatabaseAccessObjects.CustomerDAOImpl;
import SchedulingProgram.Model.Customer;
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
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomersPage implements Initializable {



    @FXML
    TableColumn <Customer, Integer> customerIdColumn;
    @FXML
    TableColumn <Customer, String> customerNameColumn;
    @FXML
    TableColumn <Customer, String> customerAddressColumn;
    @FXML
    TableColumn <Customer, Integer> firstLevelDivisionColumn;
    @FXML
    TableColumn <Customer, String> postalCodeColumn;
    @FXML
    TableColumn <Customer, String> customerPhoneColumn;
    @FXML
    TableView <Customer> customerTable;
    @FXML
    Button addCustomerButton;
    @FXML
    Button modifyCustomerButton;
    @FXML
    Button deleteCustomerButton;
    @FXML
    Button exitButton;
    @FXML
    Hyperlink appointmentsLink;

    /**
     * opens the add a customer page
     * @throws IOException possibility the page is not found to open
     */
    public void addCustomerButtonClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/SchedulingProgram/View_controller/AddCustomerPage.fxml"));
        Stage window =  new Stage();
        window.setScene(new Scene(root));
        window.show();
        Stage stage = (Stage) customerTable.getScene().getWindow();
        stage.close();
    }

    /**
     * opens the modify customer page that will be populated with data from the selected customer
     * @throws IOException possibility the page is not found to open
     */
    public void modifyCustomerButtonClicked() throws IOException, SQLException {
        if (!customerTable.getSelectionModel().isEmpty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SchedulingProgram/View_controller/ModifyCustomerPage.fxml"));
            Parent root = loader.load();
            Stage window = new Stage();
            window.setScene(new Scene(root));
            window.show();
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

            ModifyCustomerPage controller = loader.getController();
            controller.displaySelectedCustomer(selectedCustomer);
            Stage stage = (Stage) customerTable.getScene().getWindow();
            stage.close();
        } else {
            new Alert(Alert.AlertType.ERROR,"You must select a Customer to modify").showAndWait();
        }
    }

    /**
     * method to delete the customer selected in the table from the database
     * confirms before deletion
     * error message if pressed with no selection made
     */
    public void deleteCustomerButtonClicked() {
        CustomerDAO customerDAO = new CustomerDAOImpl();
        AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (!customerTable.getSelectionModel().isEmpty()) {
            Alert alert =  new Alert(Alert.AlertType.CONFIRMATION, "All appointments with " +  selectedCustomer.getName() + " will also be deleted: Continue?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                appointmentDAO.getAllAppointments().stream().filter(appointment -> appointment.getCustomerId().equals(selectedCustomer.getID())).forEach(appointmentDAO::deleteAppointment);
                if (customerDAO.deleteCustomer(selectedCustomer))
                    new Alert(Alert.AlertType.INFORMATION, selectedCustomer.getName() + " was deleted").showAndWait();
            }
        } else {
            new Alert(Alert.AlertType.ERROR,"You must select a Customer to delete.").showAndWait();
        }
        customerTable.setItems(customerDAO.getAllCustomers());
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
     * method to return user to the appointments page
     * @throws IOException Exception based on the fact the page to return to may not be found
     */
    public void appointmentsLinkClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/SchedulingProgram/View_controller/LandingPage.fxml"));
        Stage window =  new Stage();
        window.setScene(new Scene(root));
        window.show();
        Stage stage = (Stage) customerTable.getScene().getWindow();
        stage.close();
    }

    /**
     * initializes cells in the table to display the correct information from the database
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        CustomerDAO customerDAO = new CustomerDAOImpl();
        customerTable.setItems(customerDAO.getAllCustomers());
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        firstLevelDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionID"));

    }



}
