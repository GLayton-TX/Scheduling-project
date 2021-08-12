package SchedulingProgram.DatabaseAccessObjects;

import SchedulingProgram.Model.User;
import SchedulingProgram.Utilities.SqlDbHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;

public class UserDaoImpl implements UserDAO {

    /**
     * returns a list of users who's ID matches the search
     * @param id the ID to search for
     * @return list of users who match the search
     */
    @Override
    public ObservableList<User> getUserByID(Integer id) {
        ObservableList<User> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM users WHERE User_ID = ?";
        try {
            data = returnUsersList(SqlDbHelper.getObjectByID(query,id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * returns a list of users who's name matches the search
     * @param name the name to search for
     * @return list of users who match the search
     */
    @Override
    public ObservableList<User> getUserByName(String name) {
        ObservableList<User> data = FXCollections.observableArrayList();
        //String query = "SELECT * FROM users WHERE User_Name = ?";
        try {
            data = returnUsersList(SqlDbHelper.getObjectBySearch(
                    "users","User_Name",name));
            //data = returnUsersList(SqlDbHelper.getObjectByName(query,name));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * returns all the users in the database
     * @return list of all user objects
     */
    @Override
    public ObservableList<User> getAllUsers() {
        ObservableList<User> data = FXCollections.observableArrayList();
        try {
            data = returnUsersList(SqlDbHelper.getAllObjects("users"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * adds a user object to the database
     * @param newUser the user object to be added
     * @return boolean whether the add was successful
     */
    @Override
    public boolean addUser(User newUser) {
        boolean userWasAdded = false;
        Connection connection = SqlDbHelper.getConnection();
        String query = "INSERT INTO users (User_Name,Password,Created_By,Last_Updated_By) " +
                "VALUES (?,?,?,?)";
        try {
            SqlDbHelper.setPreparedStatement(connection,query);
            PreparedStatement preparedStatement = SqlDbHelper.getPreparedStatement();
            preparedStatement.setString(1,newUser.getUserName());
            preparedStatement.setString(2,newUser.getPassword());
            preparedStatement.setString(3,newUser.getCreatedBy()); //todo use logged in user?
            preparedStatement.setString(4,newUser.getLastUpdateBy());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"User will be added: Continue?");
            alert.showAndWait(); // too much?
            if (alert.getResult() == ButtonType.OK) {
                preparedStatement.execute();
                userWasAdded = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userWasAdded;
    }

    /**
     * updates a user object to the database
     * @param userToUpdate the user object to be updated
     * @return boolean whether the update was successful
     */
    @Override
    public void updateUser(User userToUpdate) {
        Connection connection = SqlDbHelper.getConnection();
        String query = "UPDATE users " +
                "SET User_Name = ?," +
                "Password = ?, " +
                "Last_Update =?, " +
                "Last_Updated_By =? " +
                "WHERE User_ID = ?";
        try {
            SqlDbHelper.setPreparedStatement(connection, query);
            PreparedStatement preparedStatement = SqlDbHelper.getPreparedStatement();
            preparedStatement.setString(1,userToUpdate.getUserName());
            preparedStatement.setString(2,userToUpdate.getPassword());
            preparedStatement.setTimestamp(3,userToUpdate.getLastUpdate());
            preparedStatement.setString(4,userToUpdate.getLastUpdateBy());
            preparedStatement.setInt(5,userToUpdate.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } new Alert(Alert.AlertType.INFORMATION,"User " + " was updated").showAndWait();
    }

    /**
     * deletes a user from the database
     * @param userToBeDeleted the user to be deleted
     * @return boolean whether the delete was successful
     */
    @Override
    public boolean deleteUser(User userToBeDeleted) {
        boolean userWasDeleted = false;
        String query = "DELETE FROM users WHERE User_ID = ?";
        if (SqlDbHelper.deleteObject(query, userToBeDeleted.getId()))
            userWasDeleted = true;
        return userWasDeleted;
    }

    /**
     * formats the data from the database into a user object to be used by the program
     * @param rs the result of the SQL query
     * @return a list of the user objects created
     * @throws SQLException Exception in case the query fails
     */
    public static ObservableList<User> returnUsersList(ResultSet rs) throws SQLException {
        ObservableList<User> list = FXCollections.observableArrayList();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("User_ID"));
            user.setUserName(rs.getString("User_Name"));
            user.setPassword(rs.getString("Password"));
            user.setCreatedOn(rs.getTimestamp("Create_Date"));
            user.setCreatedBy(rs.getString("Created_By"));
            user.setLastUpdate(rs.getTimestamp("Last_Update"));
            user.setLastUpdateBy(rs.getString("Last_updated_By"));
            list.add(user);
        }
        return list;
    }




}
