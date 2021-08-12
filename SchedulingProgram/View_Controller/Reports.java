package SchedulingProgram.View_Controller;

import SchedulingProgram.Utilities.ReportHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Reports {
    @FXML
    TextArea reportField;
    @FXML
    Button goBackButton;
    @FXML
    Button getCountByMonth;
    @FXML
    Button getCountByType;
    @FXML
    Button getCountsByCustomer;

    /**
     * method to return user to the previous page
     * @throws IOException Exception based on the fact the page to return to may not be found
     */
    public void goBackButtonClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/SchedulingProgram/View_controller/LandingPage.fxml"));
        Stage window =  new Stage();
        window.setScene(new Scene(root));
        window.show();
        Stage stage = (Stage) reportField.getScene().getWindow();
        stage.close();
    }

    /**
     * populates the report field with the selected report data
     * @throws SQLException Exception for if there is an issue with the SQL query
     */
    public void getCountByMonthClicked() throws SQLException {
        reportField.clear();
        reportField.setText(ReportHelper.getAppointmentTotalsByMonth());
    }

    /**
     * populates the report field with the selected report data
     * @throws SQLException Exception for if there is an issue with the SQL query
     */
    public void getCountByTypeClicked() throws SQLException {
        reportField.clear();
        reportField.setText(ReportHelper.getAppointmentTotalsByType());
    }

    /**
     * populates the report field with the selected report data
     * custom report created per the assessment instructions
     * @throws SQLException Exception for if there is an issue with the SQL query
     */
    public void getCountByCustomerClicked() throws  SQLException {
        reportField.clear();
        reportField.setText(ReportHelper.getAppointmentTotalsByCustomer());
    }


}
