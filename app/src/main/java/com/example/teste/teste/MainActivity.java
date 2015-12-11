package com.example.teste.teste;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.teste.teste.model.Car;
import com.example.teste.teste.model.Order;
import com.example.teste.teste.model.User;
import com.example.teste.teste.service.IResult;
import com.example.teste.teste.service.IServiceOrder;
import com.example.teste.teste.service.ServiceOrderImplSQLite;

import java.util.ArrayList;

public class MainActivity extends Activity  {

    private IServiceOrder serviceOrder;
    LayoutInflater factory = null;
    private ListView mensagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        factory=(LayoutInflater) getSystemService(MainActivity.this.LAYOUT_INFLATER_SERVICE);

        //listView
        mensagens = (ListView) findViewById(R.id.listView);
        String[] listViewMensagens = todasMensagens();
        ArrayAdapter<String> selectedUsersAdapter=new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                listViewMensagens);
        mensagens.setAdapter(selectedUsersAdapter);
        mensagens.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
                final String mensagemAtual = (String) parent.getAdapter().getItem(i);
                final View menuView=factory.inflate(R.layout.list_view_item_click,null);
                final AlertDialog.Builder longClickMenu=new AlertDialog.Builder(MainActivity.this).setView(menuView);

                Button editar=(Button) menuView.findViewById(R.id.editar);
                Button deletar=(Button) menuView.findViewById(R.id.deletar);

                //listener do botao editar
                editar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final View editarMensagem = factory.inflate(R.layout.item_editar_click, null);
                        final AlertDialog.Builder editar = new AlertDialog.Builder(MainActivity.this).setView(editarMensagem);
                        editar.setTitle("Editar mensagem");
                        editar.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SQLiteDatabase db = new DatabaseHandler(MainActivity.this).getReadableDatabase();

                                EditText mensagem = (EditText) editarMensagem.findViewById(R.id.mensagem);
                                ContentValues cv = new ContentValues();
                                cv.put("mensagem", mensagem.getText().toString());
                                db.update("mensagens", cv, "mensagem=?", new String[]{mensagemAtual});

                                String[] listViewMensagens = todasMensagens();

                                ArrayAdapter<String> selectedUsersAdapter = new ArrayAdapter<String>(MainActivity.this,
                                        android.R.layout.simple_list_item_1,
                                        android.R.id.text1,
                                        listViewMensagens);
                                mensagens.setAdapter(selectedUsersAdapter);
                            }
                        });
                        editar.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        editar.show();
                    }
                });

                longClickMenu.show();
                return false;
            }

        });
        //botao nova mensagem
        Button novaMensagem=(Button)findViewById(R.id.acrescentarMensagem);
        novaMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View novaMensagem = factory.inflate(R.layout.nova_mensagem, null);

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private String[] todasMensagens()
    {
        ArrayList<String> mensagens=new ArrayList<String>();
        SQLiteDatabase db = new DatabaseHandler(MainActivity.this).getReadableDatabase();
        Cursor cursor = db.rawQuery("select mensagem from mensagens", null);
        if (cursor != null) {
            if(cursor.moveToFirst())
            {
                int current=0;
                do{
                    mensagens.add(cursor.getString(0));
                }while(cursor.moveToNext());
            }

        }
        cursor.close();
        return mensagens.toArray(new String[mensagens.size()]);
    }
}

