package com.bytatech.ayoos.service.dto;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ReservedSlot entity.
 */
public class ReservedSlotDTO implements Serializable {

    private Long id;

    private LocalDate date;

    private Double startTime;

    private Double endTime;


    private Long doctorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getStartTime() {
        return startTime;
    }

    public void setStartTime(Double startTime) {
        this.startTime = startTime;
    }

    public Double getEndTime() {
        return endTime;
    }

    public void setEndTime(Double endTime) {
        this.endTime = endTime;
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

        ReservedSlotDTO reservedSlotDTO = (ReservedSlotDTO) o;
        if (reservedSlotDTO.getId() == null || getId() == null) {
            return false;
        }
        
        if(reservedSlotDTO.getStartTime()==getStartTime()&&reservedSlotDTO.getDoctorId()==getDoctorId()){
        	return true;
        }
        return Objects.equals(getId(), reservedSlotDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReservedSlotDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", startTime=" + getStartTime() +
            ", endTime=" + getEndTime() +
            ", doctor=" + getDoctorId() +
            "}";
    }
}
