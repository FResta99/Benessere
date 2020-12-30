package com.interfacciabili.benessere.model;

public class Dieta {
    private String clienteDieta;
    private String colazione1;
    private String colazione2;
    private String pranzo1;
    private String pranzo2;
    private String cena1;
    private String cena2;

    public Dieta(String clienteDieta, String colazione1, String colazione2, String pranzo1, String pranzo2, String cena1, String cena2) {
        this.clienteDieta = clienteDieta;
        this.colazione1 = colazione1;
        this.colazione2 = colazione2;
        this.pranzo1 = pranzo1;
        this.pranzo2 = pranzo2;
        this.cena1 = cena1;
        this.cena2 = cena2;
    }

    public Dieta() {
    }

    @Override
    public String toString() {
        return "Dieta{" +
                "clienteDieta='" + clienteDieta + '\'' +
                ", colazione1='" + colazione1 + '\'' +
                ", colazione2='" + colazione2 + '\'' +
                ", pranzo1='" + pranzo1 + '\'' +
                ", pranzo2='" + pranzo2 + '\'' +
                ", cena1='" + cena1 + '\'' +
                ", cena2='" + cena2 + '\'' +
                '}';
    }

    public String getClienteDieta() {
        return clienteDieta;
    }

    public void setClienteDieta(String clienteDieta) {
        this.clienteDieta = clienteDieta;
    }

    public String getColazione1() {
        return colazione1;
    }

    public void setColazione1(String colazione1) {
        this.colazione1 = colazione1;
    }

    public String getColazione2() {
        return colazione2;
    }

    public void setColazione2(String colazione2) {
        this.colazione2 = colazione2;
    }

    public String getPranzo1() {
        return pranzo1;
    }

    public void setPranzo1(String pranzo1) {
        this.pranzo1 = pranzo1;
    }

    public String getPranzo2() {
        return pranzo2;
    }

    public void setPranzo2(String pranzo2) {
        this.pranzo2 = pranzo2;
    }

    public String getCena1() {
        return cena1;
    }

    public void setCena1(String cena1) {
        this.cena1 = cena1;
    }

    public String getCena2() {
        return cena2;
    }

    public void setCena2(String cena2) {
        this.cena2 = cena2;
    }

    /**
     * TODO Versione più flessibile, da gestire in seguito
    private String clienteDieta;
    private int idAlimento;

    public Dieta(String clienteDieta, int idAlimento) {
        this.clienteDieta = clienteDieta;
        this.idAlimento = idAlimento;
    }

    public Dieta() {
    }

    @Override
    public String toString() {
        return "Dieta{" +
                "clienteDieta='" + clienteDieta + '\'' +
                ", idAlimento=" + idAlimento +
                '}';
    }

    public String getClienteDieta() {
        return clienteDieta;
    }

    public void setClienteDieta(String clienteDieta) {
        this.clienteDieta = clienteDieta;
    }

    public int getIdAlimento() {
        return idAlimento;
    }

    public void setIdAlimento(int idAlimento) {
        this.idAlimento = idAlimento;
    }
     **/
}

