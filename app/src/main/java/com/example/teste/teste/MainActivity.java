package com.example.teste.teste;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.teste.teste.model.Car;
import com.example.teste.teste.model.Order;
import com.example.teste.teste.model.User;
import com.example.teste.teste.service.IResult;
import com.example.teste.teste.service.IServiceOrder;
import com.example.teste.teste.service.ServiceOrderImplSQLite;

import java.util.ArrayList;

public class MainActivity extends Activity implements IResult {

    private IServiceOrder serviceOrder;
    ArrayAdapter<Car> adapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.action_settings);

        serviceOrder = new ServiceOrderImplSQLite();


        User u = new User();
        u.setLogin("kewn");
        serviceOrder.getCars(u, this);
    }


    @Override
    public void onSuccess(ArrayList<Car> l) {

        adapter = new ArrayAdapter<Car>(this, android.R.layout.activity_list_item, l);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onError(String msg) {

    }
}
