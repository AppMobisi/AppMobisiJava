package com.example.mobisi.model;

import android.webkit.JavascriptInterface;


public class InformacoesWeb {
    private Integer iUsuarioId;
    private Double CoordenadaX;
    private Double CoordendaY;

    public InformacoesWeb(Integer iUsuarioId, Double coordenadaX, Double coordendaY) {
        this.iUsuarioId = iUsuarioId;
        CoordenadaX = coordenadaX;
        CoordendaY = coordendaY;
    }

    public Integer getiUsuarioId() {
        return iUsuarioId;
    }

    public void setiUsuarioId(Integer iUsuarioId) {
        this.iUsuarioId = iUsuarioId;
    }

    public Double getCoordenadaX() {
        return CoordenadaX;
    }

    public void setCoordenadaX(Double coordenadaX) {
        CoordenadaX = coordenadaX;
    }

    public Double getCoordendaY() {
        return CoordendaY;
    }

    public void setCoordendaY(Double coordendaY) {
        CoordendaY = coordendaY;
    }
}
