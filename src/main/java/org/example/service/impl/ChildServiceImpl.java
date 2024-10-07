package org.example.service.impl;
import org.example.dao.*;
import org.example.entity.Child;
import org.example.service.ChildService;

import java.util.Optional;

public class ChildServiceImpl implements ChildService {
    private final SquadDao squadDao = SquadDao.getInstance();
    private final RoomDao roomDao = RoomDao.getInstance();
    private final SportSectionDao sportSectionDao = SportSectionDao.getInstance();
    private final ChildDao childDao = ChildDao.getInstance();

    @Override
    public Child addChildFromTableView(String name, String secondName, String squadName, String sportSectionName, String roomNumber) {
        Optional<Long> roomId = roomDao.findIdByName(roomNumber);
        if (roomId.isEmpty())
            return null;
        Optional<Long> squadId = squadDao.findIdByName(squadName);
        if(squadId.isEmpty())
            return null;
        Optional<Long> sportSectionId = sportSectionDao.findIdByName(sportSectionName);
        if(sportSectionId.isEmpty())
            return null;

        return childDao.save(Child.builder()
                        .secondName(secondName)
                        .firstName(name)
                        .sportSectionId(sportSectionId.get())
                        .roomId(roomId.get())
                        .squadId(squadId.get())
                .build());
    }
}
