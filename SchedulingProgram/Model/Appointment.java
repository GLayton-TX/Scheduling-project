package SchedulingProgram.Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Appointment {

    Integer appointmentId;
    String appointmentTitle;
    String appointmentDescription;
    String appointmentLocation;
    String appointmentType;
    LocalDateTime appointmentStartTime;
    LocalDateTime appointmentEndTime;
    LocalDateTime appointmentCreatedOn;
    String appointmentCreatedBy;
    LocalDateTime appointmentLastUpdatedOn;
    String appointmentLastUpdatedBy;
    Integer customerId;
    Integer userId;
    Integer contactId;

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    public void setAppointmentDescription(String appointmentDescription) {
        this.appointmentDescription = appointmentDescription;
    }

    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public LocalDateTime getAppointmentStartTime() {
        return appointmentStartTime;
    }

    public void setAppointmentStartTime(LocalDateTime appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
    }

    public LocalDateTime getAppointmentEndTime() {
        return appointmentEndTime;
    }

    public void setAppointmentEndTime(LocalDateTime appointmentEndTime) {
        this.appointmentEndTime = appointmentEndTime;
    }

    public LocalDateTime getAppointmentCreatedOn() {
        return appointmentCreatedOn;
    }

    public void setAppointmentCreatedOn(LocalDateTime appointmentCreatedOn) {
        this.appointmentCreatedOn = appointmentCreatedOn;
    }

    public String getAppointmentCreatedBy() {
        return appointmentCreatedBy;
    }

    public void setAppointmentCreatedBy(String appointmentCreatedBy) {
        this.appointmentCreatedBy = appointmentCreatedBy;
    }

    public LocalDateTime getAppointmentLastUpdatedOn() {
        return appointmentLastUpdatedOn;
    }

    public void setAppointmentLastUpdatedOn(LocalDateTime appointmentLastUpdatedOn) {
        this.appointmentLastUpdatedOn = appointmentLastUpdatedOn;
    }

    public String getAppointmentLastUpdatedBy() {
        return appointmentLastUpdatedBy;
    }

    public void setAppointmentLastUpdatedBy(String appointmentLastUpdatedBy) {
        this.appointmentLastUpdatedBy = appointmentLastUpdatedBy;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }


}
