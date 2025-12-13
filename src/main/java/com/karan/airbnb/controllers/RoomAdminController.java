package com.karan.airbnb.controllers;

import com.karan.airbnb.dtos.RoomDto;
import com.karan.airbnb.entities.Room;
import com.karan.airbnb.services.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
@Slf4j
public class RoomAdminController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDto> createNewRoom(@PathVariable Long hotelId, @RequestBody RoomDto roomDto) {
        RoomDto room = roomService.createNewRoom(hotelId, roomDto);
        return new ResponseEntity<>(room, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRooms(@PathVariable Long hotelId) {
        List<RoomDto> rooms = roomService.getAllRoomsInHotel(hotelId);
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long hotelId, @PathVariable Long roomId) {
        RoomDto room = roomService.getRoomById(hotelId, roomId);
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoomById(@PathVariable Long hotelId, @PathVariable Long roomId) {
        roomService.deleteRoomById(hotelId, roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
