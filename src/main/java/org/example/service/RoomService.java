package org.example.service;

import org.example.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    List<Room> getRooms();
    Optional<Long> findIdByRoomNumber(String number);
}
