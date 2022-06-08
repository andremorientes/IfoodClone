package com.example.ifoodclone.empresa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.ifoodclone.R;

import java.util.Locale;

public class EmpresaFormProdutoActivity extends AppCompatActivity {

    private ImageView img_produto;
    private EditText edt_produto;
    private CurrencyEditText edt_valor;
    private CurrencyEditText edt_valor_antigo;
    private Button btn_categoria;
    private EditText edt_descricao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_form_produto);

        iniciaComponentes();
        configCliques();
    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());


    }

    private void iniciaComponentes(){
        TextView  text_toolbar= findViewById(R.id.text_toolbar);
        text_toolbar.setText("Novo Produto");

        img_produto= findViewById(R.id.img_produto);
        edt_produto= findViewById(R.id.edt_produto);

        edt_valor= findViewById(R.id.edt_valor);
        edt_valor.setLocale(new Locale("PT", "mz"));

        edt_valor_antigo= findViewById(R.id.edt_valor_antigo);
        edt_valor_antigo.setLocale(new Locale("PT", "mz"));

        btn_categoria= findViewById(R.id.btn_categoria);
        edt_descricao= findViewById(R.id.edt_descricao);

    }
}