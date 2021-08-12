package SchedulingProgram.View_Controller;

import SchedulingProgram.DatabaseAccessObjects.*;
import SchedulingProgram.Model.Country;
import SchedulingProgram.Model.Customer;
import SchedulingProgram.Model.FirstLevelDivision;
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
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class AddCustomerPage implements Initializable {
    @FXML
    Button addCustomerButton;
    @FXML
    TextField customerIdField;
    @FXML
    TextField customerNameField;
    @FXML
    TextField customerPhoneField;
    @FXML
    TextField customerStreetAddressField;
    @FXML
    TextField customerPostalCodeField;
    @FXML
    ComboBox<Country>customerCountryComboBox;
    @FXML
    ComboBox<FirstLevelDivision> customerDivisionComboBox;
    @FXML
    Button exitButton;
    @FXML
    Hyperlink goBackLink;

    /**
     * method takes the information entered on the page and passes it to a validation method before saving to database or prompting user for corrections
     * @throws IOException Exception based on user entering empty values
     */
    public void addCustomerButtonClicked() throws IOException {
        if (customerNameField.getText().isEmpty()
            || customerPhoneField.getText().isEmpty()
                || customerStreetAddressField.getText().isEmpty()
                || customerDivisionComboBox.getSelectionModel().isEmpty()
                || customerPostalCodeField.getText().isEmpty()
                || customerDivisionComboBox.getValue() == null
        ) {
            new Alert(Alert.AlertType.ERROR, "You must fill out all fields").showAndWait();
        } else {
            Customer validCustomer = Validate.validateCustomer(
            Integer.parseInt(customerIdField.getText()),
                    customerNameField.getText(),
                    customerStreetAddressField.getText(),
                    customerPostalCodeField.getText(),
                    customerPhoneField.getText(),
                    LocalDateTime.now(),
                    LogInScreen.getLoggedUser().getUserName(),
                    LocalDateTime.now(),
                    LogInScreen.getLoggedUser().getUserName(),
                    customerDivisionComboBox.getSelectionModel().getSelectedItem().getId()
            );
            if (validCustomer != null) {
                CustomerDAO customerDAO = new CustomerDAOImpl();
                if (customerDAO.addCustomer(validCustomer)) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer was added").showAndWait();
                }
                Parent root = FXMLLoader.load(getClass().getResource("/SchedulingProgram/View_controller/CustomersPage.fxml"));
                Stage window = new Stage();
                window.setScene(new Scene(root));
                window.show();
                Stage stage = (Stage) addCustomerButton.getScene().getWindow();
                stage.close();
            }
        }
    }

    /**
     * triggers population of the division combo box based on the value of the country selected
     */
    public void countryComboBoxSelected() {
        customerDivisionComboBox.getItems().clear();
        customerDivisionComboBox.setDisable(false);
        FirstLevelDivisionDAO firstLevelDivisionDAO = new FirstLevelDivisionDAOImpl();
        customerDivisionComboBox.getItems().addAll(firstLevelDivisionDAO.getDivisionsByCountry(customerCountryComboBox.getSelectionModel().getSelectedItem().getId()));
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
        Parent root = FXMLLoader.load(getClass().getResource("/SchedulingProgram/View_controller/CustomersPage.fxml"));
        Stage window =  new Stage();
        window.setScene(new Scene(root));
        window.show();
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * initializes data for choice boxes
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerIdField.setText(Integer.toString(CustomerDAOImpl.getNextCustomerId()));
        customerIdField.setDisable(true);
        CountryDAO countryDAO = new CountryDAOImpl();
        customerCountryComboBox.getItems().addAll(countryDAO.getAllCountries());
        customerDivisionComboBox.setDisable(true);

    }


}
