package com.example.aluno.trabalho4bimestretentativa2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoSQLite extends SQLiteOpenHelper {

    public BancoSQLite(Context context){
        // contexto,nomedobanco,cursor, versão
        super(context, "banco.db",null,1);
    }

    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE imagem(codigo integer primary key autoincrement, foto blob);";
        db.execSQL(sql);
        String sql2 = "CREATE TABLE problema(codigo integer primary key autoincrement, usuario text, descr text, latitude real, longitude real, dia timestamp default current_timestamp, issync int, codimg int)";
        db.execSQL(sql2);
        String sql3 = "CREATE TABLE pessoa(codigo integer primary key autoincrement, usuario text unique, senha text);";
        db.execSQL(sql3);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS imagem");
        db.execSQL("DROP TABLE IF EXISTS problema");
        db.execSQL("DROP TABLE IF EXISTS pessoa");
        onCreate(db);
    }
}
