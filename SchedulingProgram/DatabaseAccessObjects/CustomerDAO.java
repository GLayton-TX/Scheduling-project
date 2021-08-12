package SchedulingProgram.DatabaseAccessObjects;

import SchedulingProgram.Model.Customer;
import javafx.collections.ObservableList;

public interface CustomerDAO {

    ObservableList<Customer> getCustomerByID(Integer id);

    ObservableList<Customer> getCustomerByName(String name);

    ObservableList<Customer> getAllCustomers();

    boolean addCustomer(Customer newCustomer);

    boolean modifyCustomer(Customer customerToUpdate);

    boolean deleteCustomer(Customer customerToBeDeleted);


}
