package com.example.mobisi.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Anuncios implements Parcelable {
    private String cFoto;
    private String cTitulo;
    private String cDescricao;
    private Double nPreco;
    private Integer iAnuncianteId;
    private Integer iTipoDeficienciaId;
    private String id;

    public Anuncios( String cTitulo, String cDescricao, Double nPreco, Integer iTipoDeficienciaId) {
        this.cTitulo = cTitulo;
        this.cDescricao = cDescricao;
        this.nPreco = nPreco;
        this.iTipoDeficienciaId = iTipoDeficienciaId;
    }

    public Anuncios(){}

    protected Anuncios(Parcel in) {
        cFoto = in.readString();
        cTitulo = in.readString();
        cDescricao = in.readString();
        if (in.readByte() == 0) {
            nPreco = null;
        } else {
            nPreco = in.readDouble();
        }
        if (in.readByte() == 0) {
            iAnuncianteId = null;
        } else {
            iAnuncianteId = in.readInt();
        }
        if (in.readByte() == 0) {
            iTipoDeficienciaId = null;
        } else {
            iTipoDeficienciaId = in.readInt();
        }
        id = in.readString();
    }

    public static final Creator<Anuncios> CREATOR = new Creator<Anuncios>() {
        @Override
        public Anuncios createFromParcel(Parcel in) {
            return new Anuncios(in);
        }

        @Override
        public Anuncios[] newArray(int size) {
            return new Anuncios[size];
        }
    };

    public String getcFoto() {
        return cFoto;
    }

    public void setcFoto(String cFoto) {
        this.cFoto = cFoto;
    }

    public String getcTitulo() {
        return cTitulo;
    }

    public void setcTitulo(String cTitulo) {
        this.cTitulo = cTitulo;
    }

    public String getcDescricao() {
        return cDescricao;
    }

    public void setcDescricao(String cDescricao) {
        this.cDescricao = cDescricao;
    }

    public Double getnPreco() {
        return nPreco;
    }

    public void setnPreco(Double nPreco) {
        this.nPreco = nPreco;
    }

    public Integer getiAnuncianteId() {
        return iAnuncianteId;
    }

    public void setiAnuncianteId(Integer iAnuncianteId) {
        this.iAnuncianteId = iAnuncianteId;
    }

    public Integer getiTipoDeficienciaId() {
        return iTipoDeficienciaId;
    }

    public void setiTipoDeficienciaId(Integer iTipoDeficienciaId) {
        this.iTipoDeficienciaId = iTipoDeficienciaId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(cFoto);
        parcel.writeString(cTitulo);
        parcel.writeString(cDescricao);
        if (nPreco == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(nPreco);
        }
        if (iAnuncianteId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(iAnuncianteId);
        }
        if (iTipoDeficienciaId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(iTipoDeficienciaId);
        }
        parcel.writeString(id);
    }
}
