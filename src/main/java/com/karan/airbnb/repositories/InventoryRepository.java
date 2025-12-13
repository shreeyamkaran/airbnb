package com.karan.airbnb.repositories;

import com.karan.airbnb.entities.Hotel;
import com.karan.airbnb.entities.Inventory;
import com.karan.airbnb.entities.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    void deleteByRoom(Room room);

    @Query("""
        SELECT DISTINCT i.hotel\s
        FROM Inventory i\s
        WHERE\s
            i.city = :city\s
            AND\s
            i.date BETWEEN :startDate AND :endDate\s
            AND\s
            i.closed = false
            AND\s
            i.totalCount - i.bookedCount >= :roomCount
        GROUP BY i.hotel, i.room
        HAVING COUNT(i.date) = :dateCount
   \s""")
    Page<Hotel> findHotelsWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomCount") Integer roomCount,
            @Param("dateCount") Long dateCount,
            Pageable pageable
    );
}
