package com.example.aluno.trabalho4bimestretentativa2;

import android.os.AsyncTask;

import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class BancoExecuteUpdate  extends AsyncTask<String, Void, Integer> {
    private PreparedStatement comando;
    public String erro;

    public BancoExecuteUpdate(PreparedStatement com) {
        this.comando = com;
        erro = "";
    }

    @Override
    protected Integer doInBackground(String... strings) { //recebe um vetor com 0 ou mais strings
        int resp=-1;
        try {
            resp = comando.executeUpdate();
        } catch (Exception ex) {
            this.erro = ex.getMessage();
        }
        return resp;
    }


}

