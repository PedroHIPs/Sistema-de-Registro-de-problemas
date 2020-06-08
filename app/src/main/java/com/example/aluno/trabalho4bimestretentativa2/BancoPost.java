package com.example.aluno.trabalho4bimestretentativa2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BancoPost implements Runnable {

    public static Connection conexao = null;
    public PreparedStatement comando = null;
    public ResultSet tabela = null;
    public String erro = "";

    public BancoPost() throws Exception {
        conectar();
        if (erro.length() > 0)
            throw new Exception(this.erro);

    }

    public void conectar() {
        Thread tConexao;
        try {
            if ((conexao == null) || (conexao.isClosed())) {
                tConexao = new Thread(this);
                tConexao.start(); //inicia a execução do thread
                try {
                    tConexao.join(); // espera a conclusão do thread
                } catch (Exception ex) {
                    this.erro = "Erro ao conectar: " + ex.getMessage();
                }
            }
        } catch (Exception ex) {
            this.erro = "Erro ao conectar: " + ex.getMessage();
        }
    }

    public void desconectar() {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (Exception ex) {
                this.erro = "Erro ao desconectar: " + ex.getMessage();
            } finally {
                conexao = null;
            }
        }
    }

    @Override
    public void run() { // método executado quando o thread.start() é chamado
        try {
            Class.forName("org.postgresql.Driver");
            if ((conexao == null) || (conexao.isClosed())) {
                conexao = DriverManager.getConnection("jdbc:postgresql:/192.168.56.1:5432/LPB", "postgres", "1771");
                //conexao = DriverManager.getConnection("jdbc:postgresql://192.168.0.6:5432/Java", "postgres", "ifsp");
                //conexao=DriverManager.getConnection("jdbc:postgresql://10.114.78.78:5432/Java","postgres","ifsp");
            }
        } catch (Exception ex) {
            this.erro = "Erro de conexao run:" + ex.getMessage();
        }
    }
}


