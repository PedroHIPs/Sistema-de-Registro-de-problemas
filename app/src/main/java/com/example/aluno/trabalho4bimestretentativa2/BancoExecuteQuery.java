package com.example.aluno.trabalho4bimestretentativa2;

import android.os.AsyncTask;


import java.sql.PreparedStatement;
import java.sql.ResultSet;

//O SQL do banco deve rodar de forma assincrona
//AsyncTask(parâmetro, progresso, resultado
public class BancoExecuteQuery extends AsyncTask<String,Void, ResultSet> {
    private PreparedStatement comando;
    public String erro;
    public BancoExecuteQuery(PreparedStatement com) {
        this.comando=com;
        erro="";
    }

    @Override
    protected ResultSet doInBackground(String... strings) { //recebe um vetor com 0 ou mais strings
        ResultSet resultSet=null;
        try {
            resultSet = comando.executeQuery();
        }
        catch(Exception ex){
                this.erro=ex.getMessage();
        }
        return resultSet;
    }
}
