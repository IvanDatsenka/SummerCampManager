package org.example.service.impl;
import org.example.dao.RoomDao;
import org.example.entity.Room;
import org.example.service.RoomService;
import java.util.List;
import java.util.Optional;

public class RoomServiceImpl implements RoomService {
    private final RoomDao roomDao = RoomDao.getInstance();
    public List<Room> getRooms(){
        return roomDao.findAll();
    }

    public Optional<Long> findIdByRoomNumber(String number) {
        return roomDao.findIdByName(number);
    }
}
