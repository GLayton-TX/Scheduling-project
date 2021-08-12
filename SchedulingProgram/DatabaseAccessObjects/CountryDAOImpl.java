package SchedulingProgram.DatabaseAccessObjects;

import SchedulingProgram.Model.Country;
import SchedulingProgram.Utilities.SqlDbHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;

public class CountryDAOImpl implements CountryDAO{

    /**
     * returns a list of countries who's ID matches the search
     * @param id the ID to search for
     * @return list of countries who match the search
     */
    @Override
    public ObservableList<Country> getCountryByID(Integer id) {
        ObservableList<Country> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM countries WHERE Country_ID = ?";
        try {
            data = returnCountriesList(SqlDbHelper.getObjectByID(query,id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * returns a list of countries who's name matches the search
     * @param name the name to search for
     * @return list of countries who match the search
     */
    @Override
    public ObservableList<Country> getCountryByName(String name) {
        ObservableList<Country> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM countries WHERE Country = ?";
        try {
            data = returnCountriesList(SqlDbHelper.getObjectByName(query,name));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * returns all the countries in the database
     * @return list of all country objects
     */
    @Override
    public ObservableList<Country> getAllCountries() {
        ObservableList<Country> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM countries";
        try (Statement statement = SqlDbHelper.getConnection().createStatement()) {
            data = returnCountriesList(statement.executeQuery(query));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * adds a country object to the database
     * @param newCountry the country object to be added
     * @return boolean whether the add was successful
     */
    @Override
    public boolean addCountry(Country newCountry) {
        boolean countryWasAdded = false;
        Connection connection = SqlDbHelper.getConnection();
        String query = "INSERT INTO users (User_Name,Created_By,Last_Updated_By) " +
                "VALUES (?,?,?)";
        try {
            SqlDbHelper.setPreparedStatement(connection,query);
            PreparedStatement preparedStatement = SqlDbHelper.getPreparedStatement();
            preparedStatement.setString(1,newCountry.getName());
            preparedStatement.setString(2,newCountry.getCreatedBy()); //todo use logged in user?
            preparedStatement.setString(3,newCountry.getLastUpdateBy());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Country will be added: Continue?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                preparedStatement.execute();
                countryWasAdded = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryWasAdded;
    }

    /**
     * updates a country object to the database
     * @param countryToUpdate the country object to be updated
     * @return boolean whether the update was successful
     */
    @Override
    public void updateCountry(Country countryToUpdate) {
        Connection connection = SqlDbHelper.getConnection();
        String query = "UPDATE countries " +
                "SET Country = ?," +
                "Last_Update =?, " +
                "Last_Updated_By =? " +
                "WHERE Country_ID = ?";
        try {
            SqlDbHelper.setPreparedStatement(connection, query);
            PreparedStatement preparedStatement = SqlDbHelper.getPreparedStatement();
            preparedStatement.setString(1,countryToUpdate.getName());
            preparedStatement.setTimestamp(3,countryToUpdate.getLastUpdate());
            preparedStatement.setString(3,countryToUpdate.getLastUpdateBy());
            preparedStatement.setInt(4,countryToUpdate.getId());
            preparedStatement.execute(); // confirmation?
        } catch (SQLException e) {
            e.printStackTrace();
        } new Alert(Alert.AlertType.INFORMATION,"Country " + " was updated").showAndWait();
    }

    /**
     * deletes a country from the database
     * @param countryToBeDeleted the country to be deleted
     * @return boolean whether the delete was successful
     */
    @Override
    public boolean deleteCountry(Country countryToBeDeleted) {
        boolean countryWasDeleted = false;
        Connection connection = SqlDbHelper.getConnection();
        String query = "DELETE FROM countries WHERE Country_ID = ?";
        try {
            SqlDbHelper.setPreparedStatement(connection,query);
            PreparedStatement preparedStatement = SqlDbHelper.getPreparedStatement();
            preparedStatement.setInt(1,countryToBeDeleted.getId());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Country: " + "will be deleted: Continue?"); //todo get user name
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                preparedStatement.execute();
                countryWasDeleted = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryWasDeleted;
    }

    /**
     * formats the data from the database into an country object to be used by the program
     * @param rs the result of the SQL query
     * @return a list of the country objects created
     * @throws SQLException Exception in case the query fails
     */
    public static ObservableList<Country> returnCountriesList(ResultSet rs) throws SQLException {
        ObservableList<Country> list = FXCollections.observableArrayList();
        while (rs.next()) {
            Country country = new Country();
            country.setId(rs.getInt("Country_ID"));
            country.setName(rs.getString("Country"));
            country.setCreatedOn(rs.getTimestamp("Create_Date"));
            country.setCreatedBy(rs.getString("Created_By"));
            country.setLastUpdate(rs.getTimestamp("Last_Update"));
            country.setLastUpdateBy(rs.getString("Last_updated_By"));
            list.add(country);
        }
        return list;
    }



}
