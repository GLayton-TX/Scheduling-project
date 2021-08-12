package SchedulingProgram.DatabaseAccessObjects;

import SchedulingProgram.Model.Customer;
import SchedulingProgram.Utilities.SqlDbHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;
import java.time.LocalDateTime;

public class CustomerDAOImpl implements CustomerDAO {
    private static Integer nextCustomerId = 0;

    /**
     * returns a list of customers who's ID matches the search
     * @param id the ID to search for
     * @return list of customers who match the search
     */
    @Override
    public ObservableList<Customer> getCustomerByID(Integer id) {
        return null;
    }

    /**
     * returns a list of countries who's name matches the search
     * @param name the name to search for
     * @return list of countries who match the search
     */
    @Override
    public ObservableList<Customer> getCustomerByName(String name) {
        return null;
    }

    /**
     * returns all the customers in the database
     * @return list of all customers objects
     */
    @Override
    public ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> data = FXCollections.observableArrayList();
        try {
            data = returnCustomerList(SqlDbHelper.getAllObjects("customers"));
        } catch (SQLException e) {
            e.printStackTrace();
        } //todo change other classes to this version
        return data;
    }

    /**
     * adds a customer object to the database
     * @param newCustomer the customer object to be added
     * @return boolean whether the add was successful
     */
    @Override
    public boolean addCustomer(Customer newCustomer) {
        boolean customerWasAdded = false;
        Connection connection = SqlDbHelper.getConnection();
        String query = "INSERT INTO customers (Customer_ID,Customer_Name, Address, Postal_Code,Phone,Create_Date,Created_By,Last_Update,Last_Updated_By,Division_ID) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try {
            SqlDbHelper.setPreparedStatement(connection,query);
            PreparedStatement preparedStatement = SqlDbHelper.getPreparedStatement();
            preparedStatement.setInt(1,newCustomer.getID());
            preparedStatement.setString(2,newCustomer.getName());
            preparedStatement.setString(3,newCustomer.getAddress()); //todo use logged in user?
            preparedStatement.setString(4,newCustomer.getPostalCode());
            preparedStatement.setString(5,newCustomer.getPhone());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(7,"User");//todo get logged in user?
            preparedStatement.setTimestamp(8,Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(9,"User");//todo get logged in user?
            preparedStatement.setInt(10,newCustomer.getDivisionID());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Customer will be added: Continue?");
            alert.showAndWait(); // too much?
            if (alert.getResult() == ButtonType.OK) {
                preparedStatement.execute();
                customerWasAdded = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerWasAdded;
    }

    /**
     * updates a customer object to the database
     * @param customerToUpdate the customer object to be updated
     * @return boolean whether the update was successful
     */
    @Override
    public boolean modifyCustomer(Customer customerToUpdate) {
        boolean customerWasModified = false;
        Connection connection = SqlDbHelper.getConnection();
        String query = "UPDATE customers " +
                "SET Customer_Name = ?," +
                "Address = ?, " +
                "Postal_Code = ?, " +
                "Phone = ?, " +
                "Division_ID = ?, " +
                "Last_Update =?, " +
                "Last_Updated_By =? " +
                "WHERE Customer_ID = ?";
        try {
            SqlDbHelper.setPreparedStatement(connection, query);
            PreparedStatement preparedStatement = SqlDbHelper.getPreparedStatement();
            preparedStatement.setString(1,customerToUpdate.getName());
            preparedStatement.setString(2,customerToUpdate.getAddress());
            preparedStatement.setString(3,customerToUpdate.getPostalCode());
            preparedStatement.setString(4,customerToUpdate.getPhone());
            preparedStatement.setInt(5,customerToUpdate.getDivisionID());
            preparedStatement.setTimestamp(6,Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(7,customerToUpdate.getLastUpdatedBy());
            preparedStatement.setInt(8,customerToUpdate.getID());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Customer will be updated: Continue?");
            alert.showAndWait(); // too much?
            if (alert.getResult() == ButtonType.OK) {
                preparedStatement.execute();
                customerWasModified = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerWasModified;
    }

    /**
     * deletes a customer from the database
     * @param customerToBeDeleted the customer to be deleted
     * @return boolean whether the delete was successful
     */
    @Override
    public boolean deleteCustomer(Customer customerToBeDeleted) {
        boolean customerWasDeleted = false;
        String query = "DELETE FROM customers WHERE Customer_ID = ?";
        if (SqlDbHelper.deleteObject(query, customerToBeDeleted.getID()))
            customerWasDeleted = true;
        return customerWasDeleted;
    }

    /**
     * formats the data from the database into a customer object to be used by the program
     * @param rs the result of the SQL query
     * @return a list of the customer objects created
     * @throws SQLException Exception in case the query fails
     */
    public static ObservableList<Customer> returnCustomerList(ResultSet rs) throws SQLException {
        ObservableList<Customer> list = FXCollections.observableArrayList();
        while (rs.next()) {
            Customer customer = new Customer();
            customer.setID(rs.getInt("Customer_ID"));
            if (customer.getID() > nextCustomerId) nextCustomerId = customer.getID();
            customer.setName(rs.getString("Customer_Name"));
            customer.setAddress(rs.getString("Address"));
            customer.setPostalCode(rs.getString("Postal_Code"));
            customer.setPhone(rs.getString("Phone"));
            customer.setCreatedOn(rs.getTimestamp("Create_Date").toLocalDateTime());
            customer.setCreatedBy(rs.getString("Created_By"));
            customer.setLastUpdate(rs.getTimestamp("Last_Update").toLocalDateTime());
            customer.setLastUpdatedBy(rs.getString("Last_Updated_By"));
            customer.setDivisionID(rs.getInt("Division_ID"));
            list.add(customer);
        }

        return list;
    }

    /**
     * gets the next available customer ID
     * @return the next ID available
     */
    public static Integer getNextCustomerId() {
        return nextCustomerId + 1;
    }
}
