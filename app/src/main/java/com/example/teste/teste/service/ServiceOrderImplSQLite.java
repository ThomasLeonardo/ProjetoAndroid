package com.example.teste.teste.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.teste.teste.model.Car;
import com.example.teste.teste.model.User;

import java.util.ArrayList;

/**
 * Created by 152080631 on 28/11/2015.
 */
public class ServiceOrderImplSQLite implements IServiceOrder {

    @Override
    public void getCars(User u, IResult result) {

        SQLiteDatabase sql = null;
        Cursor c = sql.query("tabela", null, null, null, null, null, null);

        ArrayList<Car> lista = new ArrayList<Car>();

        if (true) {
            result.onSuccess(lista);
        }else{
            result.onError("error");
        }

    }

    @Override
    public void getOrders(Car c, IResult result) {

    }
}
