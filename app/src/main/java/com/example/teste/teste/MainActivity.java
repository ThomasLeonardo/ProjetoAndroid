package com.example.teste.teste;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity  {

    LayoutInflater factory = null;
    private ListView mensagens;
    private int usuario_id=-1;

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
                final View menuView = factory.inflate(R.layout.list_view_item_long_click, null);
                AlertDialog.Builder longClickMenu = new AlertDialog.Builder(MainActivity.this).setView(menuView);
                final AlertDialog dialog = longClickMenu.create();
                Button editar = (Button) menuView.findViewById(R.id.editar);
                Button deletar = (Button) menuView.findViewById(R.id.deletar);

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
                deletar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        SQLiteDatabase db = new DatabaseHandler(MainActivity.this).getReadableDatabase();
                        db.delete("mensagens", "mensagem=?", new String[]{mensagemAtual});

                        String[] listViewMensagens = todasMensagens();
                        ArrayAdapter<String> selectedUsersAdapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_list_item_1,
                                android.R.id.text1,
                                listViewMensagens);
                        mensagens.setAdapter(selectedUsersAdapter);
                    }
                });

                dialog.show();
                return true;
            }

        });
        mensagens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                final String mensagem = (String) parent.getAdapter().getItem(i);
                final View menuView = factory.inflate(R.layout.list_view_item_click, null);
                AlertDialog.Builder clickMenu = new AlertDialog.Builder(MainActivity.this).setView(menuView);
                clickMenu.setTitle("Enviar mensagem");
                clickMenu.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText destinatario = (EditText) menuView.findViewById(R.id.escolherNumero);
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                        sendIntent.putExtra("address", destinatario.getText().toString());
                        sendIntent.putExtra("sms_body", mensagem);
                        sendIntent.setType("vnd.android-dir/mms-sms");
                        startActivity(sendIntent);
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
                        if(usuario_id!=-1) {
                            ContentValues cv = new ContentValues();
                            cv.put("mensagem", textoNovaMensagem.getText().toString());
                            cv.put("usuario_id", usuario_id);
                            db.insert("mensagens", null, cv);
                        }
                        else{
                            Toast toast=Toast.makeText(MainActivity.this,"Nenhum usuario logado",Toast.LENGTH_SHORT);
                            toast.show();
                        }
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

        final Button login=(Button)findViewById(R.id.login);
        final Button logout=(Button)findViewById(R.id.logout);
        final Button cadastrar=(Button)findViewById(R.id.cadastrar);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View viewLogin = factory.inflate(R.layout.login, null);
                final AlertDialog.Builder alertLogin = new AlertDialog.Builder(MainActivity.this).setView(viewLogin);
                alertLogin.setTitle("Login");
                alertLogin.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteDatabase db = new DatabaseHandler(MainActivity.this).getReadableDatabase();
                        EditText usuario = (EditText) viewLogin.findViewById(R.id.usuario);
                        EditText senha = (EditText) viewLogin.findViewById(R.id.senha);
                        Cursor cursor =db.rawQuery("SELECT _id FROM usuarios WHERE usuario=? AND senha=?",new String[]{usuario.getText().toString(),senha.getText().toString()});
                        if(cursor!=null)
                        {
                            if(cursor.moveToFirst()) {
                                usuario_id = cursor.getInt(0);
                                cursor.close();
                                logout.setVisibility(View.VISIBLE);
                                login.setVisibility(View.GONE);
                                cadastrar.setVisibility(View.GONE);
                                String[] listViewMensagens = todasMensagens();
                                ArrayAdapter<String> selectedUsersAdapter = new ArrayAdapter<String>(MainActivity.this,
                                        android.R.layout.simple_list_item_1,
                                        android.R.id.text1,
                                        listViewMensagens);
                                mensagens.setAdapter(selectedUsersAdapter);
                            }
                            else {
                                Toast toast=Toast.makeText(MainActivity.this,"Usuario ou senha incorretos",Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                        else
                        {
                            Toast toast=Toast.makeText(MainActivity.this,"Usuario ou senha incorretos",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
                alertLogin.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alertLogin.show();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario_id=-1;
                logout.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
                cadastrar.setVisibility(View.VISIBLE);
                String[] listViewMensagens = todasMensagens();
                ArrayAdapter<String> selectedUsersAdapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        listViewMensagens);
                mensagens.setAdapter(selectedUsersAdapter);
            }
        });
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View viewCadastrar = factory.inflate(R.layout.login, null);
                final AlertDialog.Builder alertCadastrar = new AlertDialog.Builder(MainActivity.this).setView(viewCadastrar);
                alertCadastrar.setTitle("Cadastrar novo usuario");
                alertCadastrar.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteDatabase db = new DatabaseHandler(MainActivity.this).getReadableDatabase();
                        EditText usuario = (EditText) viewCadastrar.findViewById(R.id.usuario);
                        EditText senha = (EditText) viewCadastrar.findViewById(R.id.senha);
                        ContentValues cv = new ContentValues();
                        cv.put("usuario", usuario.getText().toString());
                        cv.put("senha", senha.getText().toString());
                        db.insert("usuarios", null, cv);
                        usuario_id=Integer.parseInt(senha.getText().toString());
                        logout.setVisibility(View.VISIBLE);
                        login.setVisibility(View.GONE);
                        cadastrar.setVisibility(View.GONE);
                    }
                });
                alertCadastrar.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alertCadastrar.show();
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
        Cursor cursor = db.rawQuery("select usuario_id,mensagem from mensagens", null);
        if (cursor != null) {
            if(cursor.moveToFirst())
            {
                do{
                    if(cursor.getInt(0)==usuario_id)
                        mensagens.add(cursor.getString(1));
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        return mensagens.toArray(new String[mensagens.size()]);
    }
}

