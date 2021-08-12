package SchedulingProgram.Utilities;

import java.sql.*;

public class SqlDbHelper {

    private static final String dbProtocol = "jdbc";
    private static final String dbVendor = "mysql";
    private static final String dbIpAddress = "3.227.166.251";
    private static final String dbName ="WJ06oKD";
    private static final String dbUrl = dbProtocol + ":" + dbVendor + ":" + "//" + dbIpAddress + "/" + dbName;
    private static final String userName ="U06oKD";
    private static final String password ="53688825744";
    private static final String dbDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection connection = null;
    private static PreparedStatement preparedStatement;


    /**
     * opens the connection to the SQL database
     */
    public static void openConnection() {
        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            connection = DriverManager.getConnection(dbUrl, userName, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * closes the connection to the SQL database
     * @throws Exception Numerous possible SQL Exceptions could occur
     */
    public static void closeConnection() throws Exception {
        connection.close();
    }

    /**
     * calls the connection to the SQL database
     * @return the connection to the database
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * creates a prepared statement to be used when accessing the database
     * @param connection the connection to the database
     * @param sqlStatement the statement passed to the database
     * @throws SQLException Exception based on improper SQl query being used
     */
    public static void setPreparedStatement(Connection connection, String sqlStatement) throws SQLException
    {
        preparedStatement = connection.prepareStatement(sqlStatement);
    }

    /**
     * retrieves a prepared statement with the connection to the database
     * @return the completed prepared statement
     */
    public static PreparedStatement getPreparedStatement()
    {
        return preparedStatement;
    }

    /**
     * method to retrieve an object from the database based on a primary key
     * @param query the passed query appropriate to the table searched
     * @param id the value of the primary key searched
     * @return the result set of the database query
     */
    public static ResultSet getObjectByID(String query, Integer id) {
        ResultSet data = null;
        try {
            setPreparedStatement(getConnection(),query);
            PreparedStatement preparedStatement = getPreparedStatement();
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
            data = preparedStatement.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * method to retrieve an object from the database based on a name value
     * @param query  the passed query appropriate to the table searched
     * @param name the value of the name searched
     * @return the result set of the database query
     */
    public static ResultSet getObjectByName(String query, String name) {
        ResultSet data = null;
        try {
            setPreparedStatement(getConnection(),query);
            PreparedStatement preparedStatement = getPreparedStatement();
            preparedStatement.setString(1,name);
            preparedStatement.execute();
            data = preparedStatement.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * method to retrieve an object from the database based on a custom search
     * @param className the name of the class (table) to searched
     * @param classColumn the name of the table column to be searched
     * @param searchedObject the object to be searched, filters allow for multiple types
     * @return the result set of the database query
     */
    public static ResultSet getObjectBySearch(String className, String classColumn, Object searchedObject) {
        ResultSet data = null;
        String query = "SELECT * FROM " + className + " WHERE " + classColumn + " = ?";
        try {
            setPreparedStatement(getConnection(),query);
            PreparedStatement preparedStatement = getPreparedStatement();
            if (searchedObject instanceof String) preparedStatement.setString(1,searchedObject.toString());
            else if (searchedObject instanceof Integer) preparedStatement.setInt(1,Integer.parseInt(searchedObject.toString()));
            else preparedStatement.setTimestamp(1,Timestamp.valueOf(searchedObject.toString()));
            preparedStatement.execute();
            data = preparedStatement.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * method to retrieve an object from the database based on a custom search
     * @param start the starting time for the search
     * @param end the end time for the search
     * @return the result set of the database query
     */
    public static ResultSet getObjectByRange (Timestamp start, Timestamp end) {
        ResultSet data = null;
        String query = "SELECT * FROM appointments WHERE Start >= ? AND End <= ?";
        try {
            setPreparedStatement(getConnection(),query);
            PreparedStatement preparedStatement = getPreparedStatement();
            preparedStatement.setTimestamp(1,start);
            preparedStatement.setTimestamp(2,end);
            preparedStatement.execute();
            data = preparedStatement.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * method to return all the objects of a type from the database
     * @param className the class(table) name to be returned
     * @return the result set of the database query
     */
    public static ResultSet getAllObjects(String className) {
        ResultSet data = null;
        String query = "SELECT * FROM " + className;
        try {
            Statement statement = getConnection().createStatement();
            data = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * method to delete an object from the database
     * @param query the passed query appropriate to the table searched
     * @param objectToBeDeletedID the value of the primary key of the object to be deleted
     * @return boolean of whether the delete was successful or not
     */
    public static boolean deleteObject(String query, Integer objectToBeDeletedID) {
        boolean objectWasDeleted = false;
        try {
            setPreparedStatement(getConnection(),query);
            PreparedStatement preparedStatement = getPreparedStatement();
            preparedStatement.setInt(1,objectToBeDeletedID);
            preparedStatement.execute();
            objectWasDeleted = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objectWasDeleted;
    }





}
