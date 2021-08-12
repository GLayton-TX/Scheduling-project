package SchedulingProgram.DatabaseAccessObjects;

import SchedulingProgram.Model.Appointment;
import javafx.collections.ObservableList;

import java.sql.Timestamp;

public interface AppointmentDAO {

    ObservableList<Appointment> getAppointmentsByMonth(Integer month);

    ObservableList<Appointment> getAppointmentsByWeek(Timestamp begin,Timestamp end);

    ObservableList<Appointment> getAllAppointments();

    boolean addAppointment(Appointment newAppointment);

    boolean modifyAppointment(Appointment appointmentToBeModified);

    boolean deleteAppointment(Appointment appointmentToBeDeleted);

}
