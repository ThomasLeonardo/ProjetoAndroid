package com.example.teste.teste.model;

import java.util.Calendar;

/**
 * Created by 152080631 on 28/11/2015.
 */
public class Order {


    private Calendar date;
    private User user;
    private Car car;
    private double total;

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
