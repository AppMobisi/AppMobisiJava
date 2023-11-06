package com.example.mobisi.view;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mobisi.R;
import com.example.mobisi.model.AnunciosFavoritos;
import com.example.mobisi.model.EstabelecimentoFavoritos;

import java.util.ArrayList;

public class AdapterEstabelecimentos extends BaseAdapter {

    private Context applicationContext;
    private ArrayList<EstabelecimentoFavoritos> estabelecimentos;
    private LayoutInflater inflater;

    public AdapterEstabelecimentos(Context applicationContext, ArrayList<EstabelecimentoFavoritos> estabelecimentos){
        this.applicationContext = applicationContext;
        this.estabelecimentos = estabelecimentos;
        this.inflater =(LayoutInflater.from(applicationContext));
    }


    @Override
    public int getCount() {
        return estabelecimentos.size();
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
        view = inflater.inflate(R.layout.adpter_estabelecimento, null);
        TextView Nome = view.findViewById(R.id.NomeEstabelecimento);
        TextView Endereco = view.findViewById(R.id.EnderecoEstabelecimento);
        ImageView foto_substituir = view.findViewById(R.id.substituir_Establecimento);
        ImageView fotoEstabelecimento = view.findViewById(R.id.Foto_estabelecimento);
        RatingBar ratingBar = view.findViewById(R.id.Nota);


        EstabelecimentoFavoritos estabelecimentoFavoritos = estabelecimentos.get(i);
        Nome.setText(estabelecimentoFavoritos.getNome());
        Endereco.setText(estabelecimentoFavoritos.getEndereco());

        Glide.with(view)
                .load(Uri.parse(estabelecimentoFavoritos.getcFoto()))
                .centerCrop()
                .into(fotoEstabelecimento);

        fotoEstabelecimento.setVisibility(View.VISIBLE);
        foto_substituir.setVisibility(View.GONE);


        ratingBar.setRating(estabelecimentoFavoritos.getnNota());


        return view;
    }
}
