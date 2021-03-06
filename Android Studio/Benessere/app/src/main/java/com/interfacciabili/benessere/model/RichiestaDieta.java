package com.interfacciabili.benessere.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RichiestaDieta implements Parcelable {
    private int id;
    private String usernameCliente;
    private String nomeCliente;
    private String cognomeCliente;
    private String usernameDietologo;
    private int idAlimentoDaModificare;
    private String alimentoDaModificare;
    private String alimentoRichiesto;
    private String alimentoRichiestoPorzione;
    private String alimentoRichiestoTipoPorzione;
    private boolean isApprovata;

    public RichiestaDieta(int id, String usernameCliente, String nomeCliente, String cognomeCliente, String usernameDietologo, int idAlimentoDaModificare,
                          String alimentoDaModificare, String alimentoRichiesto, String alimentoRichiestoPorzione, String alimentoRichiestoTipoPorzione,
                          boolean isApprovata) {
        this.id = id;
        this.usernameCliente = usernameCliente;
        this.nomeCliente = nomeCliente;
        this.cognomeCliente = cognomeCliente;
        this.usernameDietologo = usernameDietologo;
        this.idAlimentoDaModificare = idAlimentoDaModificare;
        this.alimentoDaModificare = alimentoDaModificare;
        this.alimentoRichiesto = alimentoRichiesto;
        this.alimentoRichiestoPorzione = alimentoRichiestoPorzione;
        this.alimentoRichiestoTipoPorzione = alimentoRichiestoTipoPorzione;
        this.isApprovata = isApprovata;
    }

    public RichiestaDieta(String usernameCliente, String nomeCliente, String cognomeCliente, String usernameDietologo, int idAlimentoDaModificare,
                          String alimentoDaModificare, String alimentoRichiesto, String alimentoRichiestoPorzione, String alimentoRichiestoTipoPorzione,
                          boolean isApprovata) {
        this.id = -1;
        this.usernameCliente = usernameCliente;
        this.nomeCliente = nomeCliente;
        this.cognomeCliente = cognomeCliente;
        this.usernameDietologo = usernameDietologo;
        this.idAlimentoDaModificare = idAlimentoDaModificare;
        this.alimentoDaModificare = alimentoDaModificare;
        this.alimentoRichiesto = alimentoRichiesto;
        this.alimentoRichiestoPorzione = alimentoRichiestoPorzione;
        this.alimentoRichiestoTipoPorzione = alimentoRichiestoTipoPorzione;
        this.isApprovata = isApprovata;
    }

    protected RichiestaDieta(Parcel in) {
        id = in.readInt();
        usernameCliente = in.readString();
        nomeCliente = in.readString();
        cognomeCliente = in.readString();
        usernameDietologo = in.readString();
        idAlimentoDaModificare = in.readInt();
        alimentoDaModificare = in.readString();
        alimentoRichiesto = in.readString();
        alimentoRichiestoPorzione = in.readString();
        alimentoRichiestoTipoPorzione = in.readString();
        isApprovata = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(usernameCliente);
        dest.writeString(nomeCliente);
        dest.writeString(cognomeCliente);
        dest.writeString(usernameDietologo);
        dest.writeInt(idAlimentoDaModificare);
        dest.writeString(alimentoDaModificare);
        dest.writeString(alimentoRichiesto);
        dest.writeString(alimentoRichiestoPorzione);
        dest.writeString(alimentoRichiestoTipoPorzione);
        dest.writeByte((byte) (isApprovata ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RichiestaDieta> CREATOR = new Creator<RichiestaDieta>() {
        @Override
        public RichiestaDieta createFromParcel(Parcel in) {
            return new RichiestaDieta(in);
        }

        @Override
        public RichiestaDieta[] newArray(int size) {
            return new RichiestaDieta[size];
        }
    };

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

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getCognomeCliente() {
        return cognomeCliente;
    }

    public void setCognomeCliente(String cognomeCliente) {
        this.cognomeCliente = cognomeCliente;
    }

    public String getUsernameDietologo() {
        return usernameDietologo;
    }

    public void setUsernameDietologo(String usernameDietologo) {
        this.usernameDietologo = usernameDietologo;
    }

    public int getIdAlimentoDaModificare() {
        return idAlimentoDaModificare;
    }

    public void setIdAlimentoDaModificare(int idAlimentoDaModificare) {
        this.idAlimentoDaModificare = idAlimentoDaModificare;
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

    public String getAlimentoRichiestoPorzione() {
        return alimentoRichiestoPorzione;
    }

    public void setAlimentoRichiestoPorzione(String alimentoRichiestoPorzione) {
        this.alimentoRichiestoPorzione = alimentoRichiestoPorzione;
    }

    public String getAlimentoRichiestoTipoPorzione() {
        return alimentoRichiestoTipoPorzione;
    }

    public void setAlimentoRichiestoTipoPorzione(String alimentoRichiestoTipoPorzione) {
        this.alimentoRichiestoTipoPorzione = alimentoRichiestoTipoPorzione;
    }

    public boolean isApprovata() {
        return isApprovata;
    }

    public void setApprovata(boolean approvata) {
        isApprovata = approvata;
    }

    @Override
    public String toString() {
        return "Nuova richiesta da " + nomeCliente + " " + cognomeCliente;
    }
}

