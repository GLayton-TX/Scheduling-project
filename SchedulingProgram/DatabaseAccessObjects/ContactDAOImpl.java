package SchedulingProgram.DatabaseAccessObjects;

import SchedulingProgram.Model.Contact;
import SchedulingProgram.Utilities.SqlDbHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;

public class ContactDAOImpl implements ContactDAO {

    /**
     * return the contact associated with an ID
     * @param id the ID number used to search
     * @return the list of contacts that match the search
     */
    @Override
    public ObservableList<Contact> getContactByID(Integer id) {
        ObservableList<Contact> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM contacts WHERE Contact_ID = ?";
        try {
            data = returnContactsList(SqlDbHelper.getObjectByID(query,id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * return the contact associated with a name
     * @param name the ID number used to search
     * @return the list of contacts that match the search
     */
    @Override
    public ObservableList<Contact> getContactByName(String name) {
        ObservableList<Contact> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM contacts WHERE Contact_Name = ?";
        try {
            data = returnContactsList(SqlDbHelper.getObjectByName(query,name));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * return all the contacts in the database
     * @return list of all the contacts
     */
    @Override
    public ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM contacts";
        try (Statement statement = SqlDbHelper.getConnection().createStatement()) {
            data = returnContactsList(statement.executeQuery(query));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * adds the data to the contacts table
     * @param newContact the contact object to be added to the database
     * @return boolean whether the add was successful
     */
    @Override
    public boolean addContact(Contact newContact) {
        boolean contactWasAdded = false;
        Connection connection = SqlDbHelper.getConnection();
        String query = "INSERT INTO contacts (Contact_Name,Email) " +
                "VALUES (?,?)";
        try {
            SqlDbHelper.setPreparedStatement(connection,query);
            PreparedStatement preparedStatement = SqlDbHelper.getPreparedStatement();
            preparedStatement.setString(1,newContact.getName());
            preparedStatement.setString(2,newContact.getEmail());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Contact will be added: Continue?");
            alert.showAndWait(); // too much?
            if (alert.getResult() == ButtonType.OK) {
                preparedStatement.execute();
                contactWasAdded = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactWasAdded;
    }

    /**
     * updates the contact data in the database
     * @param contactToUpdate the contact to be updated
     */
    @Override
    public void updateContact(Contact contactToUpdate) {
        Connection connection = SqlDbHelper.getConnection();
        String query = "UPDATE contacts " +
                "SET Contact_Name = ?," +
                "Email = ?, " +
                "WHERE Contact_ID = ?";
        try {
            SqlDbHelper.setPreparedStatement(connection, query);
            PreparedStatement preparedStatement = SqlDbHelper.getPreparedStatement();
            preparedStatement.setString(1,contactToUpdate.getName());
            preparedStatement.setString(2,contactToUpdate.getEmail());
            preparedStatement.setInt(3,contactToUpdate.getContactId());
            preparedStatement.execute(); // confirmation?
        } catch (SQLException e) {
            e.printStackTrace();
        } new Alert(Alert.AlertType.INFORMATION,"User " + " was updated").showAndWait();
    }

    /**
     * deletes a contact from the database
     * @param contactToBeDeleted the contact to be deleted
     * @return boolean whether the contact was deleted
     */
    @Override
    public boolean deleteContact(Contact contactToBeDeleted) {
        boolean contactWasDeleted = false;
        String query = "DELETE FROM contacts WHERE Contact_ID = ?";
        if (SqlDbHelper.deleteObject(query, contactToBeDeleted.getContactId()))
            contactWasDeleted = true;
        return contactWasDeleted;
    }

    /**
     * returns a list of the contacts as objects from the query to the database
     * @param rs the results of the query
     * @return list of the contacts
     * @throws SQLException
     */
    public static ObservableList<Contact> returnContactsList(ResultSet rs) throws SQLException {
        ObservableList<Contact> list = FXCollections.observableArrayList();
        while (rs.next()) {
            Contact contact = new Contact();
            contact.setContactId(rs.getInt("Contact_ID"));
            contact.setName(rs.getString("Contact_Name"));
            contact.setEmail(rs.getString("Email"));
            list.add(contact);
        }
        return list;
    }

}
