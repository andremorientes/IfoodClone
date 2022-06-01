package com.example.ifoodclone.empresa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.ifoodclone.R;
import com.santalu.maskara.widget.MaskEditText;

public class EmpresaFinalizaCadastroActivity extends AppCompatActivity {
    private ImageView img_logo;
    private EditText edt_nome;
    private MaskEditText edt_telefone;
    private CurrencyEditText edt_taxa_entrega;
    private CurrencyEditText edt_pedido_minimo;
    private EditText edt_tempo_minimo;
    private EditText edt_tempo_maximo;
    private  EditText edt_categoria;
    private ProgressBar progressBar;

    private String caminhoLogo= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_finaliza_cadastro);

        iniciaComponentes();
    }

    public void validaDados(View view){

        String nome = edt_nome.getText().toString().trim();
        String telefone= edt_telefone.getUnMasked();
        double taxaEntrega= (double) edt_taxa_entrega.getRawValue()/100;
        double pedidoMinimo= (double) edt_taxa_entrega.getRawValue()/100;
        int tempoMinimo = Integer.parseInt(edt_tempo_minimo.getText().toString());
        int tempoMaximo = Integer.parseInt(edt_tempo_maximo.getText().toString());
        String categoria= edt_categoria.getText().toString();

        if(!caminhoLogo.isEmpty()){
            if (!nome.isEmpty()){
                if (edt_telefone.isDone()){
                    if(tempoMinimo>0){
                        if (tempoMaximo>0){
                            if(!categoria.isEmpty()){

                            }else{
                                edt_categoria.requestFocus();
                                edt_categoria.setError("Informe uma categoria para seu cadastro");
                            }

                        }else{
                            edt_tempo_maximo.requestFocus();
                            edt_tempo_maximo.setError("Informe o tempo máximo de entrega");
                        }

                    }else{
                        edt_tempo_minimo.requestFocus();
                        edt_tempo_minimo.setError("Informe tempo minimo de entrega");
                    }


                }else{
                    edt_telefone.requestFocus();
                    edt_telefone.setText("Informe um telefone para cadastro");
                }

            }else{
                edt_nome.requestFocus();
                edt_nome.setError("Informe um nome para o seu cadastro");
            }

        }else{
            progressBar.setVisibility(View.GONE);
            ocultarTeclado();
            erroAutenticacao("Selecione uma logo para o seu cadastro");

        }
    }

    private void iniciaComponentes(){
        img_logo= findViewById(R.id.img_logo);
        edt_nome=findViewById(R.id.edt_nome);
        edt_telefone= findViewById(R.id.edt_telefone);
        edt_taxa_entrega= findViewById(R.id.edt_taxa_entrega);
        edt_pedido_minimo= findViewById(R.id.edt_pedido_minimo);
        edt_tempo_minimo= findViewById(R.id.edt_tempo_minimo);
        edt_tempo_maximo= findViewById(R.id.edt_tempo_maximo);
        edt_categoria= findViewById(R.id.edt_categoria);
        progressBar= findViewById(R.id.progressBar);

    }

    private void erroAutenticacao(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atençao");
        builder.setMessage(msg);
        builder.setPositiveButton("OK", ((dialog, which) -> {
            dialog.dismiss();
        }));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private  void ocultarTeclado(){
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                edt_nome.getWindowToken(),0
        );
    }
}