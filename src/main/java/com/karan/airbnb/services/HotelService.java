package com.karan.airbnb.services;

import com.karan.airbnb.dtos.HotelDto;
import com.karan.airbnb.dtos.HotelInfoDto;
import org.springframework.http.ResponseEntity;

public interface HotelService {
    HotelDto createNewHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long hotelId);
    HotelDto updateHotelById(Long hotelId, HotelDto hotelDto);
    void deleteHotelById(Long hotelId);
    void activateHotel(Long hotelId);
    HotelInfoDto getHotelInfoById(Long hotelId);
}
