package SchedulingProgram.DatabaseAccessObjects;

import SchedulingProgram.Model.FirstLevelDivision;
import javafx.collections.ObservableList;

public interface FirstLevelDivisionDAO {

    ObservableList<FirstLevelDivision> getDivisionByID(Integer id);

    ObservableList<FirstLevelDivision> getDivisionByName(String name);

    ObservableList<FirstLevelDivision> getDivisionsByCountry(Integer countryId);

    ObservableList<FirstLevelDivision> getAllDivisions();

    boolean addDivision(FirstLevelDivision newDivision);

    void updateDivision(FirstLevelDivision DivisionToUpdate);

    boolean deleteDivision(FirstLevelDivision DivisionToBeDeleted);

}
