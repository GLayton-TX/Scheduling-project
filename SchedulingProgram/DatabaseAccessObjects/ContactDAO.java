package SchedulingProgram.DatabaseAccessObjects;

import SchedulingProgram.Model.Contact;
import javafx.collections.ObservableList;

public interface ContactDAO {

    ObservableList<Contact> getContactByID(Integer id);

    ObservableList<Contact> getContactByName(String name);

    ObservableList<Contact> getAllContacts();

    boolean addContact(Contact newContact);

    void updateContact(Contact contactToUpdate);

    boolean deleteContact(Contact contactToBeDeleted);

}
