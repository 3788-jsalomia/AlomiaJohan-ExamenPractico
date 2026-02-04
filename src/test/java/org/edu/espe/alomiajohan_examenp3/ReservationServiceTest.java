package org.edu.espe.alomiajohan_examenp3;

import org.edu.espe.alomiajohan_examenp3.dto.ReservationResponse;
import org.edu.espe.alomiajohan_examenp3.model.RoomReservation;
import org.edu.espe.alomiajohan_examenp3.repository.ReservationRepository;
import org.edu.espe.alomiajohan_examenp3.service.ReservationService;
import org.edu.espe.alomiajohan_examenp3.service.UserPolicyClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {
    private ReservationRepository reservationRepository;
    private ReservationService reservationService;
    private UserPolicyClient userPolicyClient;

    @BeforeEach
    void setUp(){
        reservationRepository = Mockito.mock(ReservationRepository.class);
        userPolicyClient = Mockito.mock(UserPolicyClient.class);
        reservationService = new ReservationService(reservationRepository, userPolicyClient);
    }

    @Test
    void shouldCreate_ReservationSuccessfully() {

        String roomCode = "101";
        String invalidEmail = "johan@espe.edu.ec";
        Integer hours = 2;

        // Arrange
        RoomReservation savedReservation =
                new RoomReservation(roomCode, invalidEmail, hours);

        when(reservationRepository.getRoomReservation(roomCode)).thenReturn(null);
        when(userPolicyClient.hasActivePolicy(invalidEmail)).thenReturn(true);
        when(reservationRepository.save(any(RoomReservation.class)))
                .thenReturn(savedReservation);

        // Act
        ReservationResponse response =
                reservationService.createReservation(roomCode, invalidEmail, hours);

        // Assert
        assertNotNull(response);
        assertEquals(roomCode, response.getRoomId());
        assertEquals(invalidEmail, response.getReservedByEmail());
        assertEquals(hours, response.getHours());

        verify(reservationRepository).getRoomReservation(roomCode);
        verify(userPolicyClient).hasActivePolicy(invalidEmail);
        verify(reservationRepository).save(any(RoomReservation.class));
    }

    @Test
    void shouldFail_When_EmailIsInvalid() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                reservationService.createReservation("101", "correo-invalido", 2)
        );

        verifyNoInteractions(reservationRepository);
        verifyNoInteractions(userPolicyClient);
    }

    @Test
    void shouldFail_WhenHours_AreOutOfRange() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                reservationService.createReservation("101", "johan@espe.edu.ec", 10)
        );

        verifyNoInteractions(reservationRepository);
        verifyNoInteractions(userPolicyClient);
    }

    @Test
    void shouldFail_WhenRoom_IsAlreadyReserved() {
        // Arrange
        when(reservationRepository.getRoomReservation("101"))
                .thenReturn(new RoomReservation("101", "other@espe.edu.ec", 2));

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
                reservationService.createReservation("101", "user@espe.edu.ec", 2)
        );

        verify(reservationRepository).getRoomReservation("101");
        verifyNoInteractions(userPolicyClient);
        verify(reservationRepository, never()).save(any());
    }


}
