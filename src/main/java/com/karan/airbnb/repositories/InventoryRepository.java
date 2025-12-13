package com.karan.airbnb.repositories;

import com.karan.airbnb.entities.Inventory;
import com.karan.airbnb.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    void deleteByRoom(Room room);
}
