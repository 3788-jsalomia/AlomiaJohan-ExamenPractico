package org.edu.espe.alomiajohan_examenp3.repository;

import org.edu.espe.alomiajohan_examenp3.model.RoomReservation;

public interface ReservationRepository {
    RoomReservation save(RoomReservation roomReservation);
    RoomReservation getRoomReservation(String roomId);


}
