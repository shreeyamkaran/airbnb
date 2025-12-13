package com.karan.airbnb.services;

import com.karan.airbnb.entities.Room;

public interface InventoryService {
    void initializeRoomForAYear(Room room);
    void deleteFutureInventories(Room room);
}
