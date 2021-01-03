package com.interfacciabili.benessere.model;

public class RichiestaDieta {
    private int id;
    private String usernameCliente;
    private String usernameDietologo;
    private String alimentoDaModificare;
    private String alimentoRichiesto;
    private boolean isApprovata;

    public RichiestaDieta(String usernameCliente, String usernameDietologo, String alimentoDaModificare, String alimentoRichiesto, boolean isApprovata) {
        this.id = -1;
        this.usernameCliente = usernameCliente;
        this.usernameDietologo = usernameDietologo;
        this.alimentoDaModificare = alimentoDaModificare;
        this.alimentoRichiesto = alimentoRichiesto;
        this.isApprovata = isApprovata;
    }

    public RichiestaDieta(int idRichiesta, String usernameCliente, String usernameDietologo, String alimentoDaModificare, String alimentoRichiesto, boolean isApprovata) {
        this.id = idRichiesta;
        this.usernameCliente = usernameCliente;
        this.usernameDietologo = usernameDietologo;
        this.alimentoDaModificare = alimentoDaModificare;
        this.alimentoRichiesto = alimentoRichiesto;
        this.isApprovata = isApprovata;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsernameCliente() {
        return usernameCliente;
    }

    public void setUsernameCliente(String usernameCliente) {
        this.usernameCliente = usernameCliente;
    }

    public String getUsernameDietologo() {
        return usernameDietologo;
    }

    public void setUsernameDietologo(String usernameDietologo) {
        this.usernameDietologo = usernameDietologo;
    }

    public String getAlimentoDaModificare() {
        return alimentoDaModificare;
    }

    public void setAlimentoDaModificare(String alimentoDaModificare) {
        this.alimentoDaModificare = alimentoDaModificare;
    }

    public String getAlimentoRichiesto() {
        return alimentoRichiesto;
    }

    public void setAlimentoRichiesto(String alimentoRichiesto) {
        this.alimentoRichiesto = alimentoRichiesto;
    }

    public boolean isApprovata() {
        return isApprovata;
    }

    public void setApprovata(boolean approvata) {
        isApprovata = approvata;
    }

    @Override
    public String toString() {
        return "RichiestaDieta{" +
                "id=" + id +
                ", usernameCliente='" + usernameCliente + '\'' +
                ", usernameDietologo='" + usernameDietologo + '\'' +
                ", alimentoDaModificare='" + alimentoDaModificare + '\'' +
                ", alimentoRichiesto='" + alimentoRichiesto + '\'' +
                ", isApprovata=" + isApprovata +
                '}';
    }
}
