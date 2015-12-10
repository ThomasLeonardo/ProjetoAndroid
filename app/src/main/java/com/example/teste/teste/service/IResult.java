package com.example.teste.teste.service;

import com.example.teste.teste.model.Car;

import java.util.ArrayList;

/**
 * Created by 152080631 on 28/11/2015.
 */
public interface IResult {

    public void onSuccess(ArrayList<Car> l);
    public void onError(String msg);

}
