package SchedulingProgram.DatabaseAccessObjects;

import SchedulingProgram.Model.Appointment;
import SchedulingProgram.Utilities.SqlDbHelper;
import SchedulingProgram.View_Controller.LogInScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.sql.*;
import java.time.LocalDateTime;

public class AppointmentDAOImpl implements AppointmentDAO {
    private static Integer nextAppointmentId = 0;

    /**
     * returns a list of appointments based on month
     * @param month the month selected to get the appointments for
     * @return the list of appointments returned
     */
    @Override
    public ObservableList<Appointment> getAppointmentsByMonth(Integer month) {
        ObservableList<Appointment> data = FXCollections.observableArrayList();
        try {
            data = returnAppointmentList(SqlDbHelper.getObjectBySearch(
                    "appointments","month(Start)",month));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * returns a list of appointments based on the current week
     * @param begin the start of the current week
     * @param end the end of the current week
     * @return the list of the appointments returned
     */
    @Override
    public ObservableList<Appointment> getAppointmentsByWeek(Timestamp begin, Timestamp end) {
        ObservableList<Appointment> data = FXCollections.observableArrayList();
        try {
            data = returnAppointmentList(SqlDbHelper.getObjectByRange(begin,end));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * returns a list of all the appointments in the database
     * @return the list of appointments
     */
    @Override
    public ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> data = FXCollections.observableArrayList();
        try {
            data = returnAppointmentList(SqlDbHelper.getAllObjects("appointments"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * adds the appointment data to the database
     * @param newAppointment the appointment object passed to be added
     * @return boolean of whether the add was successful
     */
    @Override
    public boolean addAppointment(Appointment newAppointment) {
        boolean appointmentWasAdded = false;
        Connection connection = SqlDbHelper.getConnection();
        String query = "INSERT INTO appointments (Appointment_ID,Title,Description,Location,Type,Start,End,Create_Date,Created_By,Last_Update,Last_Updated_By,Customer_ID,User_ID,Contact_ID) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            SqlDbHelper.setPreparedStatement(connection,query);
            PreparedStatement preparedStatement = SqlDbHelper.getPreparedStatement();
            preparedStatement.setInt(1,newAppointment.getAppointmentId());
            preparedStatement.setString(2,newAppointment.getAppointmentTitle());
            preparedStatement.setString(3,newAppointment.getAppointmentDescription());
            preparedStatement.setString(4,newAppointment.getAppointmentLocation());
            preparedStatement.setString(5,newAppointment.getAppointmentType());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(newAppointment.getAppointmentStartTime()));
            preparedStatement.setTimestamp(7,Timestamp.valueOf(newAppointment.getAppointmentEndTime()));
            preparedStatement.setTimestamp(8,Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(9,LogInScreen.getLoggedUser().getUserName());
            preparedStatement.setTimestamp(10,Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(11,LogInScreen.getLoggedUser().getUserName());
            preparedStatement.setInt(12,newAppointment.getCustomerId());
            preparedStatement.setInt(13,newAppointment.getUserId());
            preparedStatement.setInt(14,newAppointment.getContactId());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Appointment will be added: Continue?");
            alert.showAndWait(); // too much?
            if (alert.getResult() == ButtonType.OK) {
                preparedStatement.execute();
                appointmentWasAdded = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentWasAdded;
    }

    /**
     * updates the appointment in the database
     * @param appointmentToBeModified the appointment object to be updated
     * @return boolean of whether the appointment update was successful
     */
    @Override
    public boolean modifyAppointment(Appointment appointmentToBeModified) {
        boolean appointmentWasModified = false;
        Connection connection = SqlDbHelper.getConnection();
        String query = "UPDATE appointments " +
                "SET Title = ?," +
                "Description = ?, " +
                "Location = ?, " +
                "Type = ?, " +
                "Start = ?, " +
                "End =?, " +
                "Last_Update =?, " +
                "Last_Updated_By = ?, " +
                "Customer_ID = ?, " +
                "User_ID =?, " +
                "Contact_ID = ? " +
                "WHERE Appointment_ID = ?";
        try {
            SqlDbHelper.setPreparedStatement(connection, query);
            PreparedStatement preparedStatement = SqlDbHelper.getPreparedStatement();
            preparedStatement.setString(1,appointmentToBeModified.getAppointmentTitle());
            preparedStatement.setString(2,appointmentToBeModified.getAppointmentDescription());
            preparedStatement.setString(3,appointmentToBeModified.getAppointmentLocation());
            preparedStatement.setString(4,appointmentToBeModified.getAppointmentType());
            preparedStatement.setTimestamp(5,Timestamp.valueOf(appointmentToBeModified.getAppointmentStartTime()));
            preparedStatement.setTimestamp(6,Timestamp.valueOf(appointmentToBeModified.getAppointmentEndTime()));
            preparedStatement.setTimestamp(7,Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(8, LogInScreen.getLoggedUser().getUserName());
            preparedStatement.setInt(9,appointmentToBeModified.getCustomerId());
            preparedStatement.setInt(10,appointmentToBeModified.getUserId());
            preparedStatement.setInt(11,appointmentToBeModified.getContactId());
            preparedStatement.setInt(12,appointmentToBeModified.getAppointmentId());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Appointment will be updated: Continue?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                preparedStatement.execute();
                appointmentWasModified = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentWasModified;
    }

    /**
     * deletes an appointment from the database
     * @param appointmentToBeDeleted the appointment object to be deleted
     * @return boolean of whether the appointment delete was successful
     */
    @Override
    public boolean deleteAppointment(Appointment appointmentToBeDeleted) {
        boolean appointmentWasDeleted = false;
        String query = "DELETE FROM appointments WHERE Appointment_ID = ?";
        if (SqlDbHelper.deleteObject(query, appointmentToBeDeleted.getAppointmentId()))
            appointmentWasDeleted = true;
        return appointmentWasDeleted;
    }

    /**
     * formats the data from the database into an appointment object to be used by the program
     * @param rs the result of the SQL query
     * @return a list of the appointment objects created
     * @throws SQLException Exception in case the query fails
     */
    public static ObservableList<Appointment> returnAppointmentList(ResultSet rs) throws SQLException {
        ObservableList<Appointment> list = FXCollections.observableArrayList();
        while (rs.next()) {
            Appointment appointment = new Appointment();
            appointment.setAppointmentId(rs.getInt("Appointment_ID"));
            if (appointment.getAppointmentId() > nextAppointmentId) nextAppointmentId = appointment.getAppointmentId();
            appointment.setAppointmentTitle(rs.getString("Title"));
            appointment.setAppointmentDescription(rs.getString("Description"));
            appointment.setAppointmentLocation(rs.getString("Location"));
            appointment.setAppointmentType(rs.getString("Type"));
            appointment.setAppointmentStartTime(rs.getTimestamp("Start").toLocalDateTime());
            appointment.setAppointmentEndTime(rs.getTimestamp("End").toLocalDateTime());
            appointment.setAppointmentCreatedOn(rs.getTimestamp("Create_Date").toLocalDateTime());
            appointment.setAppointmentCreatedBy(rs.getString("Created_By"));
            appointment.setAppointmentLastUpdatedOn(rs.getTimestamp("Last_Update").toLocalDateTime());
            appointment.setAppointmentLastUpdatedBy(rs.getString("Last_Updated_By"));
            appointment.setCustomerId(rs.getInt("Customer_ID"));
            appointment.setUserId(rs.getInt("User_ID"));
            appointment.setContactId(rs.getInt("Contact_ID"));
            list.add(appointment);
        }
        return list;
    }

    /**
     *  used to auto generate the appointment ID number
     * @return the next available appointment ID number
     */
    public static Integer getNextAppointmentId() {
        return nextAppointmentId + 1;
    }

}
