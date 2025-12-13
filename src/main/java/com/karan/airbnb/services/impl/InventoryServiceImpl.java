package com.karan.airbnb.services.impl;

import com.karan.airbnb.dtos.HotelDto;
import com.karan.airbnb.entities.Hotel;
import com.karan.airbnb.entities.Inventory;
import com.karan.airbnb.entities.Room;
import com.karan.airbnb.exceptions.BadRequestException;
import com.karan.airbnb.repositories.InventoryRepository;
import com.karan.airbnb.services.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public void initializeRoomForAYear(Room room) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);
        for (; !today.isAfter(endDate); today = today.plusDays(1)) {
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .bookedCount(0)
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build();
            inventoryRepository.save(inventory);
        }
    }

    @Override
    public void deleteAllInventories(Room room) {
        LocalDate today = LocalDate.now();
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<HotelDto> searchHotels(String city, LocalDate startDate, LocalDate endDate, Integer roomCount, Integer pageNumber, Integer pageSize) {
        /*
            * Criteria for inventory:
            * 1. date range: [startDate, endDate]
            * 2. city
            * 3. totalCount - bookedCount >= roomCount
            * 4. closed = false
            * 5. each room type should be available between the requested date range
            * SQL Query ideas:
            * 1. Group by room type
            * 2. unique hotels
        */
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        long dateCount = ChronoUnit.DAYS.between(startDate, endDate) + 1L;
        Page<Hotel> pageOfHotels = inventoryRepository.findHotelsWithAvailableInventory(
                city, startDate, endDate, roomCount, dateCount,  pageable
        );
        return pageOfHotels.map(hotel -> modelMapper.map(hotel, HotelDto.class));
    }


}
