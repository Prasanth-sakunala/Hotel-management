package com.booking.hotelapplication.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.booking.hotelapplication.dto.Response;
import com.booking.hotelapplication.dto.RoomDTO;
import com.booking.hotelapplication.entity.Room;
import com.booking.hotelapplication.exception.OurException;
import com.booking.hotelapplication.repo.RoomRepository;
import com.booking.hotelapplication.services.AwsS3Service;
import com.booking.hotelapplication.services.interfac.IRoomService;
import com.booking.hotelapplication.utils.Utils;


@Service
public class RoomService implements IRoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AwsS3Service awsS3Service;


    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String roomDescription) {
        Response response =new Response();
        try{
            String imageUrl=awsS3Service.saveImagesToS3(photo);
            Room room=new Room(roomType,roomPrice,imageUrl,roomDescription);
            Room savedRoom=roomRepository.save(room);
            RoomDTO roomDTO=Utils.mapRoomEntityToRoomDTO(savedRoom);
            response.setStatusCode(200);
            response.setRoom(roomDTO);

        }
        catch(OurException e){
            response.setStatusCode(0);
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error saving room"+e.getMessage());
        }
        return response;

    }

    @Override
    public List<String> getAllRoomTypes() {
        List<String> roomTypeList = roomRepository.findDistinctRoomTypes();
        return roomTypeList;
    }

    @Override
    public Response getAllRooms() {
        Response response=new Response();
        try{
            List<Room> roomList=roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomDTO> roomDTOList= Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoomList(roomDTOList);

        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Featching Rooms"+e.getMessage());
        }

        return response;
    }



    @Override
    public Response deleteRoom(Long roomId) {
        Response response =new Response();
        try{
            roomRepository.findById(roomId).orElseThrow(()-> new OurException("Room not found with id:"+roomId));
            roomRepository.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("Successful");

        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Featching Rooms"+e.getMessage());
        }

        return response;
    }

    @Override
    public Response updateRoom(Long roomId, String roomDescription, String roomType, BigDecimal roomPrice, MultipartFile photo) {
        Response response =new Response();
        try{
            String imageUrl=null;
            if(photo != null && !photo.isEmpty()) {
                imageUrl = awsS3Service.saveImagesToS3(photo);
            }
            Room room =roomRepository.findById(roomId).orElseThrow(()-> new OurException("Room not found with id:"+roomId));
            if(roomType !=null ) room.setRoomType(roomType);
            if(roomPrice !=null ) room.setRoomPrice(roomPrice);
            if(roomDescription !=null ) room.setRoomDescription(roomDescription);
            if(imageUrl!=null) room.setRoomPhotoUrl(imageUrl);

            Room updatedRoom=roomRepository.save(room);
            RoomDTO roomDTO=Utils.mapRoomEntityToRoomDTO(updatedRoom);
            response.setRoom(roomDTO);
            response.setStatusCode(200);
            response.setMessage("Successful");

        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Featching Rooms"+e.getMessage());
        }

        return response;
    }

    @Override
    public Response getRoomById(Long roomId) {
        Response response =new Response();
        try{
            Room room=roomRepository.findById(roomId).orElseThrow(()-> new OurException("Room not found with id:"+roomId));
            RoomDTO roomDTO=Utils.mapRoomEntityToRoomDTOPlusBookings(room);
            response.setRoom(roomDTO);
            response.setStatusCode(200);
            response.setMessage("Successful");

        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Featching Rooms"+e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response =new Response();
        try{
            List<Room> roomList=roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate,checkOutDate,roomType); 
            List<RoomDTO> roomDTOList=Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setRoomList(roomDTOList);
            response.setStatusCode(200);
            response.setMessage("Successful");

        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Featching Rooms "+e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAvailableRooms() {
        Response response =new Response();
        try{
            List<Room> roomList=roomRepository.getAllAvailableRooms(); 
            List<RoomDTO> roomDTOList=Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setRoomList(roomDTOList);
            response.setStatusCode(200);
            response.setMessage("Successful");

        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Featching Rooms"+e.getMessage());
        }
        return response;
    }



}
