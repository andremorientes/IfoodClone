package com.example.ifoodclone.fragment.empresa;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ifoodclone.R;
import com.example.ifoodclone.adapter.FinanceiroAdapter;
import com.example.ifoodclone.helper.FirebaseHelper;
import com.example.ifoodclone.helper.GetMask;
import com.example.ifoodclone.model.Pedido;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EmpresaFinanceiroFragment extends Fragment {

    private FinanceiroAdapter financeiroAdapter;
    private List<Pedido> pedidoList= new ArrayList<>();

    private EditText edt_inicio, edt_final;
    private Button btn_filtrar, btn_todos;
    private TextView text_saldo, text_receita;

    private ProgressBar progressBar;

    private RecyclerView rv_despesas;
    private String data_inicio;
    private String data_final;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_empresa_financeiro, container, false);


        iniciaComponentes(view);
        configCliques();
        return view;
    }

    private void configCliques(){
        btn_filtrar.setOnClickListener(v -> {
             data_inicio= edt_inicio.getText().toString();
             data_final= edt_final.getText().toString();

            if (!data_inicio.isEmpty()){
                if (!data_final.isEmpty()){
                    filtrarPedidos();
                }else{
                    edt_final.requestFocus();
                    edt_final.setError("Informe a data.");
                }

            }else{
                edt_inicio.requestFocus();
                edt_inicio.setError("informe a data");
            }
        });
    }

    private void filtrarPedidos() {

        DatabaseReference pedidoRef= FirebaseHelper.getDatabaseReference()
                .child("empresaPedidos")
                .child(FirebaseHelper.getIdFirebase());
        pedidoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    pedidoList.clear();
                    for (DataSnapshot ds: snapshot.getChildren()){
                        Pedido pedido= ds.getValue(Pedido.class);

                        verificaDataPedidos(pedido);
                    }

                    Collections.reverse(pedidoList);
                    financeiroAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void verificaDataPedidos(Pedido pedido){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("PT","mz"));

        String strDataPedido = GetMask.getDate(pedido.getDataPedido(), 1);

        try {
            Date dataPedido= sdf.parse(strDataPedido);
            Date dataInicio= sdf.parse(data_inicio);
            Date dataFinal= sdf.parse(data_final);
        }catch (Exception e ){

        }

    }

    private void iniciaComponentes(View view){
        edt_inicio= view.findViewById(R.id.edt_inicio);
        edt_final= view.findViewById(R.id.edt_final);
        btn_filtrar= view.findViewById(R.id.btn_filtrar);
        btn_todos= view.findViewById(R.id.btn_todos);
        text_receita= view.findViewById(R.id.text_receita);
        text_saldo= view.findViewById(R.id.text_saldo);
        progressBar= view.findViewById(R.id.progressBar);
        rv_despesas= view.findViewById(R.id.rv_despesas);

    }
}