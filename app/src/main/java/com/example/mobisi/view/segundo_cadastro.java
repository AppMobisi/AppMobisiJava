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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mobisi.R;
import com.example.mobisi.tools.MaskEnum;
import com.example.mobisi.tools.MaskFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link segundo_cadastro#newInstance} factory method to
 * create an instance of this fragment.
 */
public class segundo_cadastro extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public segundo_cadastro() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment segundo_cadastro.
     */
    // TODO: Rename and change types and number of parameters
    public static segundo_cadastro newInstance(String param1, String param2) {
        segundo_cadastro fragment = new segundo_cadastro();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static segundo_cadastro newInstance(){
        segundo_cadastro fragment = new segundo_cadastro();
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
        return inflater.inflate(R.layout.fragment_segundo_cadastro, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinner = view.findViewById(R.id.cadastro_SpinnerCategoria);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.deficiencias, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    ((Cadastro) requireActivity()).setiTipoDeficiencia(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Lidar com a seleção de nada, se necessário
            }
        });

        EditText cpf = view.findViewById(R.id.CpfAtualizar);
        MaskFormatter maskFormatterCpf = new MaskFormatter(MaskEnum.CPF.getMask(), cpf);
        cpf.addTextChangedListener(maskFormatterCpf);

        EditText telefone = view.findViewById(R.id.TelefoneAtualizar);
        MaskFormatter maskFormatterTelefone = new MaskFormatter(MaskEnum.TELEFONE.getMask(), telefone);
        telefone.addTextChangedListener(maskFormatterTelefone);

        EditText cep = view.findViewById(R.id.CepAtualizar);
        MaskFormatter maskFormatter = new MaskFormatter(MaskEnum.CEP.getMask(), cep);
        cep.addTextChangedListener(maskFormatter);


    }
}