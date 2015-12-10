package com.example.teste.teste.service;

import com.example.teste.teste.model.Car;
import com.example.teste.teste.model.User;

/**
 * Created by 152080631 on 28/11/2015.
 */
public interface IServiceOrder {


    void getCars(User u, IResult result);
    void getOrders(Car c, IResult result);


}
