package org.edu.espe.alomiajohan_examenp3.service;

import org.edu.espe.alomiajohan_examenp3.dto.ReservationResponse;
import org.edu.espe.alomiajohan_examenp3.model.RoomReservation;
import org.edu.espe.alomiajohan_examenp3.repository.ReservationRepository;

public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserPolicyClient userPolicyClient;

    public ReservationService(ReservationRepository reservationRepository, UserPolicyClient userPolicyClient) {
        this.reservationRepository = reservationRepository;
        this.userPolicyClient = userPolicyClient;
    }

    public ReservationResponse createReservation(String roomCode, String email, int hours){
        //Validaciones

        //roomCode no puede ser nulo ni vacío.
        if(roomCode == null || roomCode.isEmpty()){
            throw new IllegalArgumentException("roomCode no puede ser nulo ni vacío.");
        }
        //email debe tener un formato válido.
        if(email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")){
            throw new IllegalArgumentException("email debe tener un formato válido.");
        }
        //hours debe ser mayor a 0 y menor o igual a 8.
        if(hours <= 0 || hours > 8){
            throw new IllegalArgumentException("hours debe ser mayor a 0 y menor o igual a 8.");
        }
        //No se puede crear una reserva si la sala ya se encuentra reservada.
        if(reservationRepository.getRoomReservation(String.valueOf(Integer.parseInt(roomCode))) != null){
            throw new IllegalStateException("No se puede crear una reserva si la sala ya se encuentra reservada.");
        }
        //No se permite crear reservas para usuarios bloqueados por políticas institucionales.
        if(!userPolicyClient.hasActivePolicy(email)){
            throw new IllegalStateException("No se permite crear reservas para usuarios bloqueados por políticas institucionales.");
        }

        RoomReservation reservation = new RoomReservation(roomCode, email, hours);
        RoomReservation savedReservation = reservationRepository.save(reservation);

        return new ReservationResponse(savedReservation.getRoomCode(), savedReservation.getReservedByEmail(), savedReservation.getHours());
    }
}
