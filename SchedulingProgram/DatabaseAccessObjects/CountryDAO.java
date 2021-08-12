package SchedulingProgram.DatabaseAccessObjects;

import SchedulingProgram.Model.Country;
import javafx.collections.ObservableList;

public interface CountryDAO {

    ObservableList<Country> getCountryByID(Integer id);

    ObservableList<Country> getCountryByName(String name);

    ObservableList<Country> getAllCountries();

    boolean addCountry(Country newCountry);

    void updateCountry(Country CountryToUpdate);

    boolean deleteCountry(Country CountryToBeDeleted);

}
