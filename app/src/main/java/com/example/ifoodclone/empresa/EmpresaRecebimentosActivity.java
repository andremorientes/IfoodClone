package com.example.ifoodclone.empresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ifoodclone.R;
import com.example.ifoodclone.helper.FirebaseHelper;
import com.example.ifoodclone.model.Pagamento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmpresaRecebimentosActivity extends AppCompatActivity {

    private List<Pagamento> pagamentoList = new ArrayList<>();

    private Pagamento dinheiro = new Pagamento();
    private Pagamento dinheiroEntrega = new Pagamento();
    private Pagamento cartaoCreditoEntrega = new Pagamento();
    private Pagamento cartaoCreditoRetirada = new Pagamento();
    private Pagamento cartaoCreditoApp = new Pagamento();


    private CheckBox cb_de;
    private CheckBox cb_dr;
    private CheckBox cb_cce;
    private CheckBox cb_ccr;
    private CheckBox cb_app;
    private EditText edt_public_key;
    private EditText edt_access_token;

    private ImageButton ib_salvar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_recebimentos);

        iniciaComponentes();
        configCliques();
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());

        //DINHEIRO NA ENTREGA
        cb_de.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dinheiro.setDescricao("Dinheiro na entrega");
            dinheiro.setStatus(isChecked);
        });
        //DINHEIRO NA RETIRA
        cb_dr.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dinheiroEntrega.setDescricao("Dinheiro na retirada");
            dinheiroEntrega.setStatus(isChecked);

        });
        //CARTAO DE CREDITO NA ENTREGA
        cb_cce.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartaoCreditoEntrega.setDescricao("Cartão de crédito na entrega");
            cartaoCreditoEntrega.setStatus(isChecked);
        });
        //CARTAO DE CREDITO NA RETIRADA
        cb_ccr.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartaoCreditoRetirada.setDescricao("Cartão de crédito na retirada");
            cartaoCreditoRetirada.setStatus(isChecked);
        });

        //PAGAMENTO PELO APP
        cb_app.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartaoCreditoApp.setDescricao("Cartão de crédito no App");
            cartaoCreditoApp.setStatus(isChecked);

        });
    }

    private void recuperaPagamentos() {
        DatabaseReference pagamentoRef = FirebaseHelper.getDatabaseReference()
                .child("recebimentos")
                .child(FirebaseHelper.getIdFirebase());
        pagamentoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Pagamento pagamento = ds.getValue(Pagamento.class);
                    configPagamentos(pagamento);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void configPagamentos(Pagamento pagamento) {
        switch (pagamento.getDescricao()) {
            case "Dinheiro na entrega":
                dinheiro = pagamento;
                cb_de.setChecked(dinheiro.getStatus());
                break;

            case "Dinheiro na retirada":
                dinheiroEntrega =pagamento;
                cb_dr.setChecked(dinheiroEntrega.getStatus());
                break;

            case "Cartão de crédito na entrega":
                cartaoCreditoEntrega = pagamento;
                cb_cce.setChecked( cartaoCreditoEntrega.getStatus());
                break;

            case "Cartão de crédito na retirada":
                cartaoCreditoRetirada = pagamento;
                cb_ccr.setChecked(cartaoCreditoRetirada.getStatus());
                break;

            case "Cartão de crédito no App":
                cartaoCreditoApp= pagamento;
                cb_app.setChecked(cartaoCreditoApp.getStatus());
                break;

        }
    }

    private void validaPagamentos() {

    }

    private void iniciaComponentes() {

        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Recebimentos");

        cb_de = findViewById(R.id.cb_de);
        cb_dr = findViewById(R.id.cb_dr);
        cb_cce = findViewById(R.id.cb_cce);
        cb_ccr = findViewById(R.id.cb_ccr);
        cb_app = findViewById(R.id.cb_app);


        edt_public_key = findViewById(R.id.edt_public_key);
        edt_access_token = findViewById(R.id.edt_acess_token);

        ib_salvar = findViewById(R.id.ib_salvar);
        progressBar = findViewById(R.id.progressBar);


    }
}