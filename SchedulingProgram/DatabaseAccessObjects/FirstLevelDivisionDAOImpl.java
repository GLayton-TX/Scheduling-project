package SchedulingProgram.DatabaseAccessObjects;

import SchedulingProgram.Model.FirstLevelDivision;
import SchedulingProgram.Utilities.SqlDbHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;

public class FirstLevelDivisionDAOImpl implements FirstLevelDivisionDAO{

    /**
     * returns a list of divisions who's ID matches the search
     * @param id the ID to search for
     * @return list of countries who match the search
     */
    @Override
    public ObservableList<FirstLevelDivision> getDivisionByID(Integer id) {
        ObservableList<FirstLevelDivision> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        try {
            data = returnDivisionsList(SqlDbHelper.getObjectByID(query,id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * returns a list of divisions who's name matches the search
     * @param name the name to search for
     * @return list of divisions who match the search
     */
    @Override
    public ObservableList<FirstLevelDivision> getDivisionByName(String name) {
        ObservableList<FirstLevelDivision> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM first_level_divisions WHERE Division = ?";
        try {
            data = returnDivisionsList(SqlDbHelper.getObjectByName(query,name));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * returns a list of divisions who's country ID matches the search
     * @param countryId the ID to search for
     * @return list of divisions who match the search
     */
    @Override
    public ObservableList<FirstLevelDivision> getDivisionsByCountry(Integer countryId) {
        ObservableList<FirstLevelDivision> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = ?";
        try {
            data = returnDivisionsList(SqlDbHelper.getObjectByID(query,countryId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * returns all the divisons in the database
     * @return list of all divisions objects
     */
    @Override
    public ObservableList<FirstLevelDivision> getAllDivisions() {
        ObservableList<FirstLevelDivision> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM first_level_divisions";
        try (Statement statement = SqlDbHelper.getConnection().createStatement()) {
            data = returnDivisionsList(statement.executeQuery(query));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * adds a division object to the database
     * @param newDivision the division object to be added
     * @return boolean whether the add was successful
     */
    @Override
    public boolean addDivision(FirstLevelDivision newDivision) {
        boolean divisionWasAdded = false;
        Connection connection = SqlDbHelper.getConnection();
        String query = "INSERT INTO first_level_divisions (Division,Created_By,Last_Updated_By,COUNTRY_ID) " +
                "VALUES (?,?,?,?)";
        try {
            SqlDbHelper.setPreparedStatement(connection,query);
            PreparedStatement preparedStatement = SqlDbHelper.getPreparedStatement();
            preparedStatement.setString(1,newDivision.getName());
            preparedStatement.setString(2,newDivision.getCreatedBy());
            preparedStatement.setString(3,newDivision.getLastUpdatedBy());
            preparedStatement.setInt(4,newDivision.getCountryId());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Division will be added: Continue?");
            alert.showAndWait(); // too much?
            if (alert.getResult() == ButtonType.OK) {
                preparedStatement.execute();
                divisionWasAdded = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return divisionWasAdded;
    }

    /**
     * updates a division object to the database
     * @param divisionToUpdate the division object to be updated
     * @return boolean whether the update was successful
     */
    @Override
    public void updateDivision(FirstLevelDivision divisionToUpdate) {
        Connection connection = SqlDbHelper.getConnection();
        String query = "UPDATE first_level_divisions " +
                "SET Division = ?," +
                "Last_Update =?, " +
                "Last_Updated_By =? " +
                "COUNTRY_ID = ?" +
                "WHERE Division_ID = ?";
        try {
            SqlDbHelper.setPreparedStatement(connection, query);
            PreparedStatement preparedStatement = SqlDbHelper.getPreparedStatement();
            preparedStatement.setString(1,divisionToUpdate.getName());
            preparedStatement.setTimestamp(2,divisionToUpdate.getLastUpdate());
            preparedStatement.setString(3,divisionToUpdate.getLastUpdatedBy());
            preparedStatement.setInt(4,divisionToUpdate.getCountryId());
            preparedStatement.setInt(5,divisionToUpdate.getId());
            preparedStatement.execute(); // confirmation?
        } catch (SQLException e) {
            e.printStackTrace();
        } new Alert(Alert.AlertType.INFORMATION,"Division " + " was updated").showAndWait();
    }

    /**
     * deletes a division from the database
     * @param divisionToBeDeleted the division to be deleted
     * @return boolean whether the delete was successful
     */
    @Override
    public boolean deleteDivision(FirstLevelDivision divisionToBeDeleted) {
        boolean divisionWasDeleted = false;
        String query = "DELETE FROM first_level_divisions WHERE Division_ID = ?";
        if (SqlDbHelper.deleteObject(query, divisionToBeDeleted.getId()))
            divisionWasDeleted = true;
        return divisionWasDeleted;
    }

    /**
     * formats the data from the database into a division object to be used by the program
     * @param rs the result of the SQL query
     * @return a list of the division objects created
     * @throws SQLException Exception in case the query fails
     */
    public static ObservableList<FirstLevelDivision> returnDivisionsList(ResultSet rs) throws SQLException {
        ObservableList<FirstLevelDivision> list = FXCollections.observableArrayList();
        while (rs.next()) {
            FirstLevelDivision division = new FirstLevelDivision();
            division.setId(rs.getInt("Division_ID"));
            division.setName(rs.getString("Division"));
            division.setCreateDate(rs.getTimestamp("Create_Date"));
            division.setCreatedBy(rs.getString("Created_By"));
            division.setLastUpdate(rs.getTimestamp("Last_Update"));
            division.setLastUpdatedBy(rs.getString("Last_updated_By"));
            division.setCountryId(rs.getInt("COUNTRY_ID"));
            list.add(division);
        }
        return list;
    }
}
