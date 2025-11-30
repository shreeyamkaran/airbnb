package com.karan.airbnb.repositories;

import com.karan.airbnb.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
