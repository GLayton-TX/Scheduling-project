package SchedulingProgram.Utilities;

import SchedulingProgram.DatabaseAccessObjects.AppointmentDAO;
import SchedulingProgram.DatabaseAccessObjects.AppointmentDAOImpl;
import SchedulingProgram.Model.Appointment;
import SchedulingProgram.Model.Customer;
import SchedulingProgram.View_Controller.LogInScreen;
import javafx.scene.control.Alert;

import java.time.*;
import java.time.format.TextStyle;
import java.util.Locale;

public class Validate {

    /**
     * method to verify if there is a time conflict with the requested appointment time and an already existing appointment
     * @param customerId the id number of the customer being checked
     * @param startTime the start time of the requested appointment
     * @param endTime the end time of the requested appointment
     * @return boolean value of whether there is a time conflict
     */
    static boolean hasConflictingAppointment(Integer customerId, LocalDateTime startTime, LocalDateTime endTime) {
        boolean hasConflict = false;
        AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
        for (Appointment appointment : appointmentDAO.getAllAppointments()) {
            if (appointment.getCustomerId().equals(customerId)) {
                if ((TimeHelper.utcToZoneDateTime(appointment.getAppointmentStartTime())).toLocalTime().isAfter(startTime.toLocalTime())
                        && (TimeHelper.utcToZoneDateTime(appointment.getAppointmentStartTime()).toLocalTime().isBefore(endTime.toLocalTime()))) {
                    hasConflict = true;
                    new Alert(Alert.AlertType.INFORMATION, "This Customer (" + customerId + ") has a conflicting appointment\n"
                            + "Appointment ID: " + appointment.getAppointmentId()
                            + "\nStart: " + TimeHelper.utcToZoneDateTime(appointment.getAppointmentStartTime()).format(TimeHelper.dateTimeFormatter)
                            + " " + ZoneId.systemDefault().getDisplayName(TextStyle.SHORT_STANDALONE, Locale.ROOT)).showAndWait();
                }
            }
        } return hasConflict;
    }

    /**
     * method to check if the requested appointment time is within the set business hours
     * @param startTime the start time of the requested appointment
     * @param endTime the end time of the requested appointment
     * @return boolean value of whether the time is outside of business hours
     */
    static boolean isOutsideBusinessHours (LocalDateTime startTime, LocalDateTime endTime) {
        boolean isOutsideHours = false;
        OffsetTime businessOpensAt = OffsetTime.of(8,0,0,0, ZoneOffset.of("-04:00"));
        OffsetTime businessClosesAt = OffsetTime.of(22,0,0,0,ZoneOffset.of("-04:00"));
        LocalTime localTimeOpensAt = businessOpensAt.withOffsetSameInstant(OffsetTime.now().getOffset()).toLocalTime();
        LocalTime localTimeClosesAt = businessClosesAt.withOffsetSameInstant(OffsetTime.now().getOffset()).toLocalTime();
        if (startTime.toLocalTime().isBefore(localTimeOpensAt) || endTime.toLocalTime().isAfter(localTimeClosesAt))
        {
            isOutsideHours = true;
            new Alert(Alert.AlertType.ERROR, "Appointment time must be between 8:00am and 10:00pm EST\n" +
                    "** " + localTimeOpensAt + " to " + localTimeClosesAt + " " + ZoneId.systemDefault().getDisplayName(TextStyle.SHORT_STANDALONE, Locale.ROOT) + " **").showAndWait();
        } return isOutsideHours;
    }

    /**
     * method to validate the information passed to the program before adding it to the database
     * @param appointmentId the id number for this appointment
     * @param appointmentTitle the title of this appointment
     * @param appointmentDescription the description of this appointment
     * @param appointmentLocation the location for this appointment
     * @param appointmentType the type of this appointment
     * @param startTime the start time of this appointment
     * @param endTime the end time of this appointment
     * @param createdOn the timestamp this appointment was created
     * @param createdBy the string value of the user who created this appointment
     * @param lastUpdatedOn the timestamp of the last update to this appointment
     * @param lastUpdatedBy the string value of the user who last updated this appointment
     * @param customerId the customer id assigned to this appointment
     * @param userId the user id assigned to this appointment
     * @param contactId the contact id assigned to this appointment
     * @return the validated data saved as an appointment object
     */
    public static Appointment validateAppointment (Integer appointmentId, String appointmentTitle, String appointmentDescription,String appointmentLocation, String appointmentType, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime createdOn, String createdBy, LocalDateTime lastUpdatedOn, String lastUpdatedBy, Integer customerId, Integer userId, Integer contactId) {
        boolean valid = true;
        Appointment validAppointment = null;
        if
        ( appointmentTitle.isEmpty() ||
                appointmentDescription.isEmpty() ||
                appointmentLocation.isEmpty() ||
                appointmentType.isEmpty()
        ) {
            valid = false;
            new Alert(Alert.AlertType.ERROR, "Please fill out all fields").showAndWait();
        }
        if (isOutsideBusinessHours(startTime,endTime)) {
            valid = false;
        }
        if (hasConflictingAppointment(customerId,startTime,endTime)) {
            valid = false;
        }
        if (valid) {
            validAppointment = new Appointment();
            validAppointment.setAppointmentId(appointmentId);
            validAppointment.setAppointmentTitle(appointmentTitle);
            validAppointment.setAppointmentDescription(appointmentDescription);
            validAppointment.setAppointmentLocation(appointmentLocation);
            validAppointment.setAppointmentType(appointmentType);
            validAppointment.setAppointmentStartTime(TimeHelper.zoneDateTimeToUtc(startTime));
            validAppointment.setAppointmentEndTime(TimeHelper.zoneDateTimeToUtc(endTime));
            validAppointment.setAppointmentCreatedOn(createdOn);
            validAppointment.setAppointmentCreatedBy(createdBy);
            validAppointment.setAppointmentLastUpdatedOn(lastUpdatedOn);
            validAppointment.setAppointmentLastUpdatedBy(lastUpdatedBy);
            validAppointment.setCustomerId(customerId);
            validAppointment.setUserId(userId);
            validAppointment.setContactId(contactId);
        }
        return validAppointment;
    }

    /**
     * method to validate the information passed to the program before adding it to the database
     * @param customerId the customer id for this customer
     * @param customerName the name assigned to this customer
     * @param customerAddress the address assigned to this customer
     * @param customerPostalCode the postal code assigned to this customer
     * @param customerPhone the phone number assigned to this customer
     * @param created the timestamp this appointment was created
     * @param createdBy the string value of the user who created this customer
     * @param lastUpdate the timestamp of the last update to this customer
     * @param lastUpdateBy the timestamp of the last update to this customer
     * @param divisionId the division id number assigned to this customer
     * @return the validated data saved as a customer object
     */
    public static Customer validateCustomer(Integer customerId,String customerName, String customerAddress, String customerPostalCode, String customerPhone, LocalDateTime created, String createdBy, LocalDateTime lastUpdate, String lastUpdateBy, Integer divisionId) {
        boolean valid = true;
        Customer validCustomer = null;
        if (customerName.isEmpty() ||
                customerAddress.isEmpty() ||
                customerPostalCode.isEmpty() ||
                customerPhone.isEmpty()
        ) {
            valid = false;
            new Alert(Alert.AlertType.ERROR, "Please fill out all fields").showAndWait();
        }
        if (valid) {
            validCustomer = new Customer();
            validCustomer.setID(customerId);
            validCustomer.setName(customerName);
            validCustomer.setAddress(customerAddress);
            validCustomer.setPostalCode(customerPostalCode);
            validCustomer.setPhone(customerPhone);
            validCustomer.setCreatedOn(created);
            validCustomer.setCreatedBy(createdBy);
            validCustomer.setLastUpdate(lastUpdate);
            validCustomer.setLastUpdatedBy(lastUpdateBy);
            validCustomer.setDivisionID(divisionId);
        }
        return validCustomer;
    }



}
