package SchedulingProgram.Utilities;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ReportHelper {

    /**
     * records the login attempts timestamp in UTC and whether or not the login was successful
     * @param attemptInstant the timestamp at UTC for the attempt
     * @param loginSucceeded boolean of whether the login was successful or not
     * @throws IOException
     */
    public static void loginRecorder(LocalDateTime attemptInstant, boolean loginSucceeded) throws IOException {
        String filename = "src/SchedulingProgram/Files/login_activity.txt", item;
        FileWriter fileWriter = new FileWriter(filename,true);
        PrintWriter outputFile = new PrintWriter(fileWriter);
        String logInWas = "Unsuccessful";
        if (loginSucceeded) logInWas = "Successful";
        item = "Login attempt occurred at : " + TimeHelper.zoneDateTimeToUtc(attemptInstant).format(TimeHelper.dateTimeFormatter) + " UTC | Login was: " + logInWas;
        outputFile.println(item);
        outputFile.close();
    }

    /**
     * creates a report that shows the total number of appointments per month
     * @return the string built from a custom format for each month that has an appointment in it
     * @throws SQLException
     */
    public static String getAppointmentTotalsByMonth() throws SQLException {
        StringBuilder monthlyAppointmentCounts = new StringBuilder();
        String query = "SELECT MONTHNAME(start) AS 'Month', COUNT(*) AS 'Number of appointments' FROM appointments GROUP BY MONTHNAME(start);";
        SqlDbHelper.setPreparedStatement(SqlDbHelper.getConnection(),query);
        PreparedStatement preparedStatement = SqlDbHelper.getPreparedStatement();
        preparedStatement.execute();
        ResultSet data = preparedStatement.getResultSet();
        while (data.next()) {
            monthlyAppointmentCounts.append("Month: ").append(data.getString("Month"))
                    .append("\nNumber of appointments: ").append(data.getInt("Number of appointments"))
                    .append("\n\n");
        }
        return monthlyAppointmentCounts.toString();
    }

    /**
     * creates a report that shows the total number of appointments by type of appointment
     * @return the string built from a custom format to display each type that has an appointment and its count
     * @throws SQLException
     */
    public static String getAppointmentTotalsByType() throws SQLException {
        StringBuilder typeAppointmentCounts = new StringBuilder();
        String query = "SELECT Type AS 'Type', COUNT(*) AS 'Number of appointments' FROM appointments GROUP BY Type;";
        SqlDbHelper.setPreparedStatement(SqlDbHelper.getConnection(),query);
        PreparedStatement preparedStatement = SqlDbHelper.getPreparedStatement();
        preparedStatement.execute();
        ResultSet data = preparedStatement.getResultSet();
        while (data.next()) {
            typeAppointmentCounts.append("Type: ").append(data.getString("Type"))
                    .append("\nNumber of appointments: ").append(data.getInt("Number of appointments"))
                    .append("\n\n"); //todo test
        }
        return typeAppointmentCounts.toString();
    }

    /**
     * creates a report to show the total number of appointments per customer
     * @return the string built from a custom format to display the number of appointments per customer
     * @throws SQLException
     */
    public static String getAppointmentTotalsByCustomer() throws SQLException {
        StringBuilder customerAppointmentCounts = new StringBuilder();
        String query = "SELECT appointments.Customer_ID AS 'Customer', customers.Customer_Name AS 'Name', COUNT(*) AS 'Number of appointments' FROM appointments JOIN customers ON appointments.Customer_ID=customers.Customer_ID GROUP BY appointments.Customer_ID;";
        SqlDbHelper.setPreparedStatement(SqlDbHelper.getConnection(),query);
        PreparedStatement preparedStatement = SqlDbHelper.getPreparedStatement();
        preparedStatement.execute();
        ResultSet data = preparedStatement.getResultSet();
        while (data.next()) {
            customerAppointmentCounts.append("Customer number: ").append(data.getInt("Customer"))
                    .append("\nName: ").append(data.getString("Name"))
                    .append("\nNumber of appointments: ").append(data.getInt("Number of appointments"))
                    .append("\n\n"); //todo test
        }
        return customerAppointmentCounts.toString();
    }

}
