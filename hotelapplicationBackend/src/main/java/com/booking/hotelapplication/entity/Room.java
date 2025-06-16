package com.booking.hotelapplication.entity;

import java.math.BigDecimal;
import java.util.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private String roomPhotoUrl;
    private String roomDescription;
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings=new ArrayList<>();
    @Override
    public String toString() {
        return "Room [id=" + id + ", roomType=" + roomType + ", roomPrice=" + roomPrice + ", roomPhotoUrl="
                + roomPhotoUrl + ", roomDescription=" + roomDescription +"]";
    }
    public Room(String roomType, BigDecimal roomPrice, String roomPhotoUrl, String roomDescription) {
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.roomPhotoUrl = roomPhotoUrl;
        this.roomDescription = roomDescription;
    }
    public Room(){}
    

}
