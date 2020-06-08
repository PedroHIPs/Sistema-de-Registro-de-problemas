package com.example.aluno.trabalho4bimestretentativa2;

public class Pessoa {
    private int codigo;
    private String usuario;
    private String senha;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = Integer.parseInt(codigo);
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
