package com.bytatech.ayoos.service.dto;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SessionInfo entity.
 */
public class SessionInfoDTO implements Serializable {

    private Long id;

    private String sessionName;

    private ZonedDateTime date;

    private Integer weekDay;

    @DecimalMin(value = "0")
    private Double fromTime;

    @DecimalMax(value = "23")
    private Double toTime;


    private Long doctorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Integer getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(Integer weekDay) {
        this.weekDay = weekDay;
    }

    public Double getFromTime() {
        return fromTime;
    }

    public void setFromTime(Double fromTime) {
        this.fromTime = fromTime;
    }

    public Double getToTime() {
        return toTime;
    }

    public void setToTime(Double toTime) {
        this.toTime = toTime;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SessionInfoDTO sessionInfoDTO = (SessionInfoDTO) o;
        if (sessionInfoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sessionInfoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SessionInfoDTO{" +
            "id=" + getId() +
            ", sessionName='" + getSessionName() + "'" +
            ", date='" + getDate() + "'" +
            ", weekDay=" + getWeekDay() +
            ", fromTime=" + getFromTime() +
            ", toTime=" + getToTime() +
            ", doctor=" + getDoctorId() +
            "}";
    }
}
