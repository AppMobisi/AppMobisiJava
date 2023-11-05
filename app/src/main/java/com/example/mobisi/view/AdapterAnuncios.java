package com.example.mobisi.view;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mobisi.R;
import com.example.mobisi.model.Anuncios;
import com.example.mobisi.model.AnunciosFavoritos;

import java.util.ArrayList;

public class AdapterAnuncios extends BaseAdapter {

    private Context applicationContext;
    private ArrayList<AnunciosFavoritos> anuncios;
    private LayoutInflater inflater;

    public AdapterAnuncios(Context applicationContext, ArrayList<AnunciosFavoritos> anuncios){
        this.applicationContext = applicationContext;
        this.anuncios = anuncios;
        this.inflater =(LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return anuncios.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.adapter_anuncio, null);
        TextView titulo = view.findViewById(R.id.titulo_Anuncio);
        TextView preco = view.findViewById(R.id.preco_Anuncio);
        TextView anunciante = view.findViewById(R.id.anunciante_Anuncio);
        ImageView foto_substituir = view.findViewById(R.id.substituir);
        ImageView fotoAnuncio = view.findViewById(R.id.foto_anuncio);


        AnunciosFavoritos anuncio = anuncios.get(i);
        titulo.setText(anuncio.getTitulo());
        preco.setText("R$" + anuncio.getPreco());
        anunciante.setText("Vendido por " + anuncio.getAnunciante());


        Glide.with(view)
                .load(Uri.parse(anuncio.getFoto()))
                .centerCrop()
                .into(fotoAnuncio);

        fotoAnuncio.setVisibility(View.VISIBLE);
        foto_substituir.setVisibility(View.GONE);



        return view;
    }
}
