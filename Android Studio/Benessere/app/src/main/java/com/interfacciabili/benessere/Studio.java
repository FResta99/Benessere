package com.interfacciabili.benessere;

public class Studio {
    private String nTelelefono;
    private String indirizzo;

    public Studio(String nTelelefono, String indirizzo) {
        this.nTelelefono = nTelelefono;
        this.indirizzo = indirizzo;
    }

    public Studio(){

    }

    public String getnTelelefono() {
        return nTelelefono;
    }

    public void setnTelelefono(String nTelelefono) {
        this.nTelelefono = nTelelefono;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    @Override
    public String toString() {
        return "Studio{" +
                "nTelelefono='" + nTelelefono + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                '}';
    }
}
