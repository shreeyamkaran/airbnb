package com.karan.airbnb.controllers;

import com.karan.airbnb.dtos.HotelDto;
import com.karan.airbnb.dtos.HotelInfoDto;
import com.karan.airbnb.services.HotelService;
import com.karan.airbnb.services.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelDto>> searchHotels(
            @RequestParam String city,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().plusWeeks(1)}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,
            @RequestParam(defaultValue = "1")
            Integer roomCount,
            @RequestParam(defaultValue = "0")
            Integer pageNumber,
            @RequestParam(defaultValue = "10")
            Integer pageSize
    ) {
        Page<HotelDto> pageOfHotels = inventoryService.searchHotels(city, startDate, endDate, roomCount, pageNumber, pageSize);
        return new ResponseEntity<>(pageOfHotels, HttpStatus.OK);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId) {
        HotelInfoDto hotelInfoDto = hotelService.getHotelInfoById(hotelId);
        return new ResponseEntity<>(hotelInfoDto, HttpStatus.OK);
    }

}
