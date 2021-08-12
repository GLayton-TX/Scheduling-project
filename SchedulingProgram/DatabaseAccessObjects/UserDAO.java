package SchedulingProgram.DatabaseAccessObjects;

import SchedulingProgram.Model.User;
import javafx.collections.ObservableList;

public interface UserDAO {

    ObservableList<User> getUserByID(Integer id);

    ObservableList<User> getUserByName(String name);

    ObservableList<User> getAllUsers();

    boolean addUser(User newUser);

    void updateUser(User userToUpdate);

    boolean deleteUser(User userToBeDeleted);

}
