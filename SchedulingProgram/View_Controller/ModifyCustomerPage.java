package SchedulingProgram.View_Controller;

import SchedulingProgram.DatabaseAccessObjects.*;
import SchedulingProgram.Model.Country;
import SchedulingProgram.Model.Customer;
import SchedulingProgram.Model.FirstLevelDivision;
import SchedulingProgram.Utilities.SqlDbHelper;
import SchedulingProgram.Utilities.Validate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ModifyCustomerPage {
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
    ComboBox<Country> customerCountryComboBox;
    @FXML
    ComboBox<FirstLevelDivision> customerDivisionComboBox;
    @FXML
    Button exitButton;
    @FXML
    Hyperlink goBackLink;
    private static LocalDateTime createdOn;
    private static String createdBy;

    /**
     * method takes the information entered on the page and passes it to a validation method before saving to database or prompting user for corrections
     * @throws IOException Exception based on user entering empty values
     */
    public void modifyCustomerButtonClicked() throws IOException {
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
                    createdOn,
                    createdBy,
                    LocalDateTime.now(),
                    LogInScreen.getLoggedUser().getUserName(),
                    customerDivisionComboBox.getSelectionModel().getSelectedItem().getId()
            );
            if (validCustomer != null) {
                CustomerDAO customerDAO = new CustomerDAOImpl();
                if (customerDAO.modifyCustomer(validCustomer)) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer was Updated").showAndWait();
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
     * method to prompt user before exiting the program
     */
    public void exitButtonClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to close the program?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) System.exit(0);
    }

    /**
     * method to return user to the previous page
     * @throws IOException Exception based on the fact the page to return to may not be found
     */
    public void goBackLinkClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/SchedulingProgram/View_controller/CustomersPage.fxml"));
        Stage window = new Stage();
        window.setScene(new Scene(root));
        window.show();
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * method to display the information from the existing customer to be modified
     * pulls the data about the country from the division table as they are not saved on the same table
     * @param passedCustomer the selected customer from the previous page and table
     */
    public void displaySelectedCustomer(Customer passedCustomer) throws SQLException {
        CountryDAO countryDAO = new CountryDAOImpl();
        FirstLevelDivisionDAO firstLevelDivisionDAO = new FirstLevelDivisionDAOImpl();
        customerIdField.setText(Integer.toString(passedCustomer.getID()));
        customerIdField.setDisable(true);
        customerNameField.setText(passedCustomer.getName());
        customerPhoneField.setText(passedCustomer.getPhone());
        customerStreetAddressField.setText(passedCustomer.getAddress());
        createdOn = passedCustomer.getCreatedOn();
        createdBy = passedCustomer.getCreatedBy();
        FirstLevelDivision division = new FirstLevelDivision();
        String query = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        ResultSet rs = SqlDbHelper.getObjectByID(query, passedCustomer.getDivisionID());
        while (rs.next()) {
            division.setId(rs.getInt("Division_ID"));
            division.setName(rs.getString("Division"));
            division.setCreateDate(rs.getTimestamp("Create_Date"));
            division.setCreatedBy(rs.getString("Created_By"));
            division.setLastUpdate(rs.getTimestamp("Last_Update"));
            division.setLastUpdatedBy(rs.getString("Last_updated_By"));
            division.setCountryId(rs.getInt("COUNTRY_ID"));
        }
        customerCountryComboBox.getItems().addAll(countryDAO.getAllCountries());
            for (Country country : countryDAO.getAllCountries()) {
                if (country.getId().equals(division.getCountryId())) {
                    customerCountryComboBox.setValue(country);
                    break;
                }
            }
            customerDivisionComboBox.getItems().addAll(firstLevelDivisionDAO.getAllDivisions());
            for (FirstLevelDivision firstLevelDivision : firstLevelDivisionDAO.getAllDivisions()) {
                if (firstLevelDivision.getId().equals(passedCustomer.getDivisionID())) {
                    customerDivisionComboBox.setValue(firstLevelDivision);
                    break;
                }
            }
            customerPostalCodeField.setText(passedCustomer.getPostalCode());
        }

    /**
     * populates the data in the division combo box based on the country selected
     */
    public void countryComboBoxSelected () {
        customerDivisionComboBox.getItems().clear();
        customerDivisionComboBox.setDisable(false);
        FirstLevelDivisionDAO firstLevelDivisionDAO = new FirstLevelDivisionDAOImpl();
        customerDivisionComboBox.getItems().addAll(firstLevelDivisionDAO.getDivisionsByCountry(customerCountryComboBox.getSelectionModel().getSelectedItem().getId()));
    }




    }

