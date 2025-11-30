package com.karan.airbnb.services;

import com.karan.airbnb.dtos.HotelDto;

public interface HotelService {
    HotelDto createNewHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long hotelId);
    HotelDto updateHotelById(Long hotelId, HotelDto hotelDto);
    void deleteHotelById(Long hotelId);
}
