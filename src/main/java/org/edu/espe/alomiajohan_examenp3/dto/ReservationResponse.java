package org.edu.espe.alomiajohan_examenp3.dto;

public class ReservationResponse {
    private String roomId;
    private String reservedByEmail;
    private Integer hours;

    public ReservationResponse(String roomId,String reservedByEmail, Integer hours) {
        this.roomId = roomId;
        this.reservedByEmail = reservedByEmail;
        this.hours = hours;
    }

    public String getRoomId() {
        return roomId;
    }
    public String getReservedByEmail() {
        return reservedByEmail;
    }
    public Integer getHours() {
        return hours;
    }
}
