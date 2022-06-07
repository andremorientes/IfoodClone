package com.example.ifoodclone.empresa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ifoodclone.R;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

public class EmpresaCategoriasActivity extends AppCompatActivity {

    private SwipeableRecyclerView rv_categorias;
    private ProgressBar progressBar;
    private TextView text_info;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_categorias);

        iniciaComponentes();

        configCliques();
    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
        findViewById(R.id.ib_add).setOnClickListener(v -> showDialog());
    }

    private void showDialog(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        View view =getLayoutInflater().inflate(R.layout.dialog_add_categoria, null);
        builder.setView(view);

        EditText edt_categoria= view.findViewById(R.id.edt_categoria);
        Button  btn_fechar= view.findViewById(R.id.btn_fechar);
        Button  btn_salvar= view.findViewById(R.id.btn_salvar);

        String nomeCategoria= edt_categoria.getText().toString().trim();

        btn_salvar.setOnClickListener(v -> {
            if (!nomeCategoria.isEmpty()){

            }else{
                edt_categoria.requestFocus();
                edt_categoria.setError("Informe um nome");
            }
        });

        btn_fechar.setOnClickListener(v -> dialog.dismiss());
        dialog= builder.create();
        dialog.show();

    }


    private void iniciaComponentes(){

        TextView text_toolbar= findViewById(R.id.text_toolbar);
        text_toolbar.setText("Categoria");
        rv_categorias= findViewById(R.id.rv_categorias);
        progressBar= findViewById(R.id.progressBar);
        text_info=findViewById(R.id.text_info);

    }
}