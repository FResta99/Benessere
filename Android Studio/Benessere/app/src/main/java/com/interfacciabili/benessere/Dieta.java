package com.interfacciabili.benessere;

public class Dieta {
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
}

