package com.example.aluno.trabalho4bimestretentativa2;

import android.graphics.Bitmap;
import android.media.Image;

public class Imagem {
    private int codigo;
    private int codproblema;
    private byte[] foto;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }


    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public int getCodproblema() {
        return codproblema;
    }

    public void setCodproblema(int codproblema) {
        this.codproblema = codproblema;
    }
}
