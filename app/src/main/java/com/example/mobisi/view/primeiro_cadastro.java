package com.example.mobisi.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobisi.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link primeiro_cadastro#newInstance} factory method to
 * create an instance of this fragment.
 */
public class primeiro_cadastro extends Fragment {

    private EditText etPassword;
    private TextView tvPasswordError;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public primeiro_cadastro() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment primeiro_cadastro.
     */
    // TODO: Rename and change types and number of parameters
    public static primeiro_cadastro newInstance(String param1, String param2) {
        primeiro_cadastro fragment = new primeiro_cadastro();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static  primeiro_cadastro newInstance(){
        primeiro_cadastro fragment = new primeiro_cadastro();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_primeiro_cadastro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etPassword = view.findViewById(R.id.cadastro_senha2);
        tvPasswordError = view.findViewById(R.id.senha_invalida);

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                tvPasswordError.setVisibility(View.GONE); // Esconde o erro antes de validar
                isValidPassword(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    public boolean isValidPassword(String senha){
        if (!senha.matches(".*[A-Z].*")) {
            showError("Deve conter pelo menos uma letra maiúscula.");
            return false;
        }

        // Verifica se tem no mínimo 6 caracteres
        if (senha.length() < 6) {
            showError("Mínimo de 6 caracteres.");
            return false;
        }

        return true;
    }

    public void showError(String erro){
        tvPasswordError.setText(erro);
        tvPasswordError.setVisibility(View.VISIBLE);
    }
}