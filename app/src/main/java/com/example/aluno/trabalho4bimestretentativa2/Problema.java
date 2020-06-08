package com.example.aluno.trabalho4bimestretentativa2;


import java.sql.Timestamp;

public class Problema {
    private int codigo;
    private String usuario;
    private String descr;
    private Double latitude;
    private Double longitude;
    private Timestamp dia;
    private int codimg;
    private int issync;

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

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLatitude(String latitude) {

        this.latitude = Double.parseDouble(latitude);
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = Double.parseDouble(longitude);
    }

    public Timestamp getDia() {
        return dia;
    }

    public void setDia(Timestamp dia) {
        this.dia = dia;
    }

    public int getIssync() {
        return issync;
    }

    public void setIssync(int issync) {
        this.issync = issync;
    }

    public void setIssync(String issync) {
        this.issync = Integer.parseInt(issync);
    }

    public int getCodimg() {
        return codimg;
    }

    public void setCodimg(int codimg) {
        this.codimg = codimg;
    }

    public void setCodimg(String codimg) {
        this.codimg = Integer.parseInt(codimg);
    }
}
