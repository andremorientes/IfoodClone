package com.example.ifoodclone.empresa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ifoodclone.R;
import com.example.ifoodclone.helper.GetMask;
import com.example.ifoodclone.model.Empresa;
import com.example.ifoodclone.model.Produto;
import com.squareup.picasso.Picasso;

public class EmpresaProdutoDetalhesActivity extends AppCompatActivity {

    private ImageView img_produto;
    private TextView text_produto;
    private TextView text_descricao;
    private TextView text_valor;
    private TextView text_valor_antigo;
    private TextView text_empresa;
    private TextView text_tempo_entrega;

    private ConstraintLayout btn_adicionar;
    private TextView text_qtd_produto;
    private TextView text_total_produto;
    private ImageButton btn_remover;
    private ImageButton btn_add;

    private Produto produto;
    private Empresa empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_produto_detalhes);

        iniciaComponentes();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            produto = (Produto) bundle.getSerializable("produtoSelecionado");

            configDados();
        }

        configCliques();

    }

    private void configDados(){
        Picasso.get().load(produto.getUrlImagem()).into(img_produto);
        text_produto.setText(produto.getNome());
        text_descricao.setText(produto.getDescricao());
        text_valor.setText(getString(R.string.text_valor, GetMask.getValor(produto.getValor())));

        if(produto.getValorAntigo() > 0){
            text_valor_antigo.setText(getString(R.string.text_valor, GetMask.getValor(produto.getValorAntigo())));
        }else {
            text_valor_antigo.setText("");
        }
    }


    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void iniciaComponentes(){
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Detalhes");

        img_produto = findViewById(R.id.img_produto);
        text_produto = findViewById(R.id.text_produto);
        text_descricao = findViewById(R.id.text_descricao);
        text_valor = findViewById(R.id.text_valor);
        text_valor_antigo = findViewById(R.id.text_valor_antigo);
        text_empresa = findViewById(R.id.text_empresa);
        text_tempo_entrega = findViewById(R.id.text_tempo_entrega);

        btn_adicionar = findViewById(R.id.btn_adicionar);
        text_qtd_produto = findViewById(R.id.text_qtd_produto);
        text_total_produto = findViewById(R.id.text_total_produto);
        btn_remover = findViewById(R.id.btn_remover);
        btn_add = findViewById(R.id.btn_add);
    }

}