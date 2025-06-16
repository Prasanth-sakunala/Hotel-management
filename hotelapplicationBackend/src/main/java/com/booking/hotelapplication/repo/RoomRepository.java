package com.booking.hotelapplication.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.booking.hotelapplication.entity.Room;

public interface RoomRepository extends JpaRepository<Room,Long> {
    @Query("select distinct r.roomType from Room r")
    List<String> findDistinctRoomTypes();

    @Query("select r from Room r where r.roomType LIKE %:roomType% AND r.id NOT IN (select bk.room.id from Booking bk where"+ "(bk.checkInDate <= :checkOutDate AND bk.checkOutDate >= :checkInDate))")
    List<Room> findAvailableRoomsByDatesAndTypes(LocalDate checkInDate,LocalDate checkOutDate, String roomType);

    @Query("select r from Room r where r.id not in (select b.room.id from Booking b)")
    List<Room> getAllAvailableRooms();

    
    
} 
