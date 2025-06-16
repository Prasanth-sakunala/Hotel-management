package com.booking.hotelapplication.services.interfac;

import com.booking.hotelapplication.dto.LoginRequest;
import com.booking.hotelapplication.dto.Response;
import com.booking.hotelapplication.entity.User;


public interface IUserService {

    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);


}
