package SchedulingProgram.View_Controller;

import SchedulingProgram.DatabaseAccessObjects.AppointmentDAO;
import SchedulingProgram.DatabaseAccessObjects.AppointmentDAOImpl;
import SchedulingProgram.DatabaseAccessObjects.UserDAO;
import SchedulingProgram.DatabaseAccessObjects.UserDaoImpl;
import SchedulingProgram.Model.Appointment;
import SchedulingProgram.Model.User;
import SchedulingProgram.Utilities.ReportHelper;
import SchedulingProgram.Utilities.TimeHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class LogInScreen implements Initializable {

    private static User loggedUser;
    @FXML
    TextField userNameTextField;
    @FXML
    TextField passwordTextField;
    @FXML
    Button logInButton;
    @FXML
    Label localRegionLabel;
    @FXML
    Label userNameLabel;
    @FXML
    Label passwordLabel;
    @FXML
    Label localRegion;

    public boolean isFrench = Locale.getDefault().getLanguage().equals("fr");

    /**
     * method to check entered data against the database to allow or disallow access to the program
     * @throws IOException the page this directs to may not be found
     */
    public void logInButtonClicked() throws IOException {
        String username = userNameTextField.getText();
        boolean noUpcoming = false;
        UserDAO temp = new UserDaoImpl();
        for (User user : temp.getAllUsers()) {
            if (username.equals(user.getUserName()) && passwordTextField.getText().equals(user.getPassword())) {
                ReportHelper.loginRecorder(LocalDateTime.now(ZoneId.systemDefault()),true);
                if (isFrench) {
                    new Alert(Alert.AlertType.INFORMATION, "Connexion réussie").showAndWait();
                } else {
                    new Alert(Alert.AlertType.INFORMATION,"Login successful").showAndWait();
                    }
                loggedUser = user;
                AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
                for (Appointment appointment : appointmentDAO.getAllAppointments()) {
                    if (appointment.getUserId().equals(loggedUser.getId())) {
                        if ((TimeHelper.utcToZoneDateTime(appointment.getAppointmentStartTime())).isAfter(LocalDateTime.now(ZoneId.systemDefault()))
                                && (TimeHelper.utcToZoneDateTime(appointment.getAppointmentStartTime())).isBefore(LocalDateTime.now(ZoneId.systemDefault()).plusMinutes(15))) {
                                    noUpcoming = false;
                                    new Alert(Alert.AlertType.INFORMATION, "You have an appointment scheduled to start within 15 Min: \n"
                                    + "Appointment ID: " + appointment.getAppointmentId()
                                    + "\nStart: " + TimeHelper.utcToZoneDateTime(appointment.getAppointmentStartTime()).format(TimeHelper.dateTimeFormatter)
                                    + " " + ZoneId.systemDefault().getDisplayName(TextStyle.SHORT_STANDALONE, Locale.ROOT)).showAndWait();
                        } else {
                            noUpcoming = true;
                        }
                    }
                }
                if (noUpcoming) {
                    new Alert(Alert.AlertType.INFORMATION, "You have no appointments scheduled to start soon").showAndWait();
                }
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/SchedulingProgram/View_controller/LandingPage.fxml")));
                Stage window =  new Stage();
                window.setScene(new Scene(root));
                window.show();
                Stage stage = (Stage) logInButton.getScene().getWindow();
                stage.close();
            } else {
                ReportHelper.loginRecorder(LocalDateTime.now(ZoneId.systemDefault()),false);
                if (isFrench) {
                    new Alert(Alert.AlertType.ERROR, "Nom d'utilisateur et mot de passe ne correspondent pas").showAndWait();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Username and Password do not match").showAndWait();
                }
            }
            break;
        }
    }

    /**
     * get the data of the user currently logging in
     * @return the user object that logged in
     */
    public static User getLoggedUser() {
        return loggedUser;
    }

    /**
     * sets the labels to french if the system default language is french
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        localRegion.setText((ZoneId.systemDefault().getId()));
        if (isFrench) {
            userNameLabel.setText("Nom d'utilisateur");
            userNameTextField.setPromptText("Nom d'utilisateur");
            passwordLabel.setText("Mot de passe");
            passwordTextField.setPromptText("Mot de passe");
            localRegionLabel.setText("Région locale");
            logInButton.setText("Connexion");
        }
    }


}
