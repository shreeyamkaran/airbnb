package com.karan.airbnb.services;

import com.karan.airbnb.dtos.HotelDto;
import com.karan.airbnb.entities.Room;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface InventoryService {
    void initializeRoomForAYear(Room room);
    void deleteAllInventories(Room room);
    Page<HotelDto> searchHotels(String city, LocalDate startDate, LocalDate endDate, Integer roomCount, Integer pageNumber, Integer pageSize);
}
