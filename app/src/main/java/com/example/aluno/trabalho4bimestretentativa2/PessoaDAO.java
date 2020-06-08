package com.example.aluno.trabalho4bimestretentativa2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.PreparedStatement;

public class PessoaDAO {

    public void cadastrar(Context context, Pessoa obj) throws Exception {
        BancoSQLite conexao;
        SQLiteDatabase bb;
        try
        {
            conexao = new BancoSQLite(context);
            bb=conexao.getWritableDatabase();
            bb.execSQL("insert into pessoa(usuario,senha) values(?,?)",new String[]{obj.getUsuario(),obj.getSenha()});
            bb.close();
        }
        catch(Exception ex){
            throw new Exception("Erro ao cadastrar: "+ ex.getMessage());
        }
    }

    public boolean logar(Context context, String usuario, String senha) throws Exception {
        BancoSQLite conexao;
        SQLiteDatabase bb;
        Cursor tabela = null;
        Pessoa obj = null;
        try {
            conexao = new BancoSQLite(context);
            bb = conexao.getReadableDatabase();

            tabela = bb.rawQuery("Select codigo from pessoa where usuario=? and senha=?", new String[]{usuario, senha});
            if ((tabela != null) && (tabela.moveToNext())) {
                //bb.close();
                return (true);
            } else {
                //bb.close();
                return (false);
            }

        }
        catch (Exception ex){
            throw new Exception("Erro ao logar: "+ex.getMessage());
        }
    }

    public int getCode(Context context, String usuario, String senha) throws Exception {
        BancoSQLite conexao;
        SQLiteDatabase bb;
        Cursor tabela = null;
        Pessoa obj;

        try {
            conexao = new BancoSQLite(context);
            bb = conexao.getReadableDatabase();
            tabela = bb.rawQuery("Select codigo from pessoa where usuario=? and senha=?", new String[]{usuario, senha});

            if ((tabela != null) && (tabela.moveToNext())) {
                obj = new Pessoa();
                obj.setCodigo(tabela.getInt(0));
            }
            bb.close();
            return (tabela.getInt(0));
        } catch (Exception ex) {
            throw new Exception("Erro ao resgatar código: " + ex.getMessage());
        }
    }
}