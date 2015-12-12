package com.example.teste.teste;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.teste.teste.service.IServiceOrder;

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
                final View menuView=factory.inflate(R.layout.list_view_item_long_click, null);
                AlertDialog.Builder longClickMenu=new AlertDialog.Builder(MainActivity.this).setView(menuView);
                final AlertDialog dialog=longClickMenu.create();
                Button editar=(Button) menuView.findViewById(R.id.editar);
                Button deletar=(Button) menuView.findViewById(R.id.deletar);

                editar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
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
                mensagens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                        final String mensagem = (String) parent.getAdapter().getItem(i);
                        final View menuView=factory.inflate(R.layout.list_view_item_click, null);
                        AlertDialog.Builder clickMenu=new AlertDialog.Builder(MainActivity.this).setView(menuView);
                        clickMenu.setTitle("Enviar mensagem");
                        clickMenu.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                
                            }
                        });
                        clickMenu.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        clickMenu.show();
                    }
                });

                deletar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        SQLiteDatabase db = new DatabaseHandler(MainActivity.this).getReadableDatabase();
                        db.delete("mensagens","mensagem=?",new String[]{mensagemAtual});

                        String[] listViewMensagens = todasMensagens();
                        ArrayAdapter<String> selectedUsersAdapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_list_item_1,
                                android.R.id.text1,
                                listViewMensagens);
                        mensagens.setAdapter(selectedUsersAdapter);
                    }
                });

                dialog.show();
                return false;
            }

        });
        //botao nova mensagem
        Button novaMensagem=(Button)findViewById(R.id.acrescentarMensagem);
        novaMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View viewNovaMensagem = factory.inflate(R.layout.nova_mensagem, null);
                final AlertDialog.Builder alertNovaMensagem = new AlertDialog.Builder(MainActivity.this).setView(viewNovaMensagem);
                alertNovaMensagem.setTitle("Nova mensagem");
                alertNovaMensagem.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText textoNovaMensagem = (EditText) viewNovaMensagem.findViewById(R.id.novaMensagem);
                        SQLiteDatabase db = new DatabaseHandler(MainActivity.this).getReadableDatabase();
                        ContentValues cv = new ContentValues();
                        cv.put("mensagem", textoNovaMensagem.getText().toString());
                        db.insert("mensagens", null, cv);
                        String[] listViewMensagens = todasMensagens();
                        ArrayAdapter<String> selectedUsersAdapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_list_item_1,
                                android.R.id.text1,
                                listViewMensagens);
                        mensagens.setAdapter(selectedUsersAdapter);
                    }
                });
                alertNovaMensagem.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertNovaMensagem.show();
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

