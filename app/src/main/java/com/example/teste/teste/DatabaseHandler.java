package com.example.teste.teste;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Thomas on 11/12/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="myDB_db";
    private static final String TABLE_MENSAGENS="mensagens";
    private static final String TABLE_USUARIOS="usuarios";

    private static final String KEY_MENSAGENS_ID="_id";
    private static final String MENSAGEM="mensagem";
    private static final String USUARIO_MENSAGEM="usuario_id";

    private static final String KEY_USUARIOS_ID="_id";
    private static final String USUARIO="usuario";
    private static final String SENHA="senha";
    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DatabaseHandler(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MENSAGENS_TABLE="CREATE TABLE "+TABLE_MENSAGENS+"("+KEY_MENSAGENS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+USUARIO_MENSAGEM+" INTEGER,"+MENSAGEM+" TEXT "+");";
        db.execSQL(CREATE_MENSAGENS_TABLE);
        ContentValues values=new ContentValues();
        values.put("mensagem", "Hello World");
        db.insert(TABLE_MENSAGENS, null, values);

        String CREATE_USUARIOS_TABLE="CREATE TABLE "+TABLE_USUARIOS+"("+KEY_USUARIOS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+USUARIO+" TEXT,"+SENHA+" TEXT"+");";
        db.execSQL(CREATE_USUARIOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_MENSAGENS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USUARIOS);
        onCreate(db);
    }
}
