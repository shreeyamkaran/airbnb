package com.karan.airbnb.services.impl;

import com.karan.airbnb.dtos.HotelDto;
import com.karan.airbnb.dtos.HotelInfoDto;
import com.karan.airbnb.dtos.RoomDto;
import com.karan.airbnb.entities.Hotel;
import com.karan.airbnb.entities.Room;
import com.karan.airbnb.exceptions.BadRequestException;
import com.karan.airbnb.exceptions.ResourceNotFoundException;
import com.karan.airbnb.repositories.HotelRepository;
import com.karan.airbnb.repositories.RoomRepository;
import com.karan.airbnb.services.HotelService;
import com.karan.airbnb.services.InventoryService;
import com.karan.airbnb.services.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;
    private final RoomService roomService;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating a new hotel with name: {}", hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setActive(false);
        hotel = hotelRepository.save(hotel);
        log.info("Created a new hotel with id: {}", hotel.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long hotelId) {
        log.info("Getting a hotel with id: {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long hotelId, HotelDto hotelDto) {
        log.info("Updating a hotel with id: {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));
        modelMapper.map(hotelDto, hotel);
        hotel.setId(hotelId);
        hotel = hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long hotelId) {
        log.info("Deleting a hotel with id: {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));
        List<Room> rooms = roomRepository.findByHotelId(hotelId);
        for (Room room : rooms) {
            inventoryService.deleteAllInventories(room);
            roomService.deleteRoomById(hotelId, room.getId());
        }
        hotelRepository.deleteById(hotelId);
    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId) {
        log.info("Activating a hotel with id: {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));
        if (hotel.getActive()) {
            throw new BadRequestException("Hotel is already active.");
        }
        hotel.setActive(true);
        // assuming only to be done once
        List<Room> rooms = roomRepository.findByHotelId(hotelId);
        for (Room room : rooms) {
            inventoryService.initializeRoomForAYear(room);
        }
    }

    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
        Hotel hotelEntity = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));
        HotelDto hotelDto = modelMapper.map(hotelEntity, HotelDto.class);
        List<Room> roomEntities = roomRepository.findByHotelId(hotelId);
        List<RoomDto> roomDtos = roomEntities.stream().map(room -> modelMapper.map(room, RoomDto.class)).toList();
        return HotelInfoDto.builder()
                .hotel(hotelDto)
                .rooms(roomDtos)
                .build();
    }

}
