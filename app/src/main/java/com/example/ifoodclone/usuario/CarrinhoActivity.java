package com.example.ifoodclone.usuario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifoodclone.DAO.EmpresaDAO;
import com.example.ifoodclone.DAO.EntregaDAO;
import com.example.ifoodclone.DAO.ItemPedidoDAO;
import com.example.ifoodclone.R;
import com.example.ifoodclone.activity.autenticacao.LoginActivity;
import com.example.ifoodclone.adapter.CarrinhoAdapter;
import com.example.ifoodclone.adapter.ProdutoCarrinhoAdapter;
import com.example.ifoodclone.helper.FirebaseHelper;
import com.example.ifoodclone.model.Endereco;
import com.example.ifoodclone.model.ItemPedido;
import com.example.ifoodclone.model.Pagamento;
import com.example.ifoodclone.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarrinhoActivity extends AppCompatActivity implements CarrinhoAdapter.OnClickListener, ProdutoCarrinhoAdapter.OnClickListener {

    private final int REQUEST_LOGIN= 100;
    private final int REQUEST_ENDERECO= 200;
    private final int REQUEST_PAGAMENTO= 300;

    private CarrinhoAdapter carrinhoAdapter;
    private ProdutoCarrinhoAdapter produtoCarrinhoAdapter;


    private RecyclerView rv_produtos;
    private RecyclerView rv_add_mais;
    private LinearLayout ll_add_mais;
    private Button btn_continuar;

    private Pagamento pagamento;
    private TextView text_forma_pagamento;
    private TextView text_escolher_pagamento ;

    private LinearLayout ll_endereco;
    private Endereco endereco;
    private TextView text_logradouro;
    private TextView text_referencia;

    private List<Produto> produtoList = new ArrayList<>();

    private ItemPedidoDAO itemPedidoDAO;
    private EmpresaDAO empresaDAO;
    private EntregaDAO entregaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        itemPedidoDAO = new ItemPedidoDAO(getBaseContext());
        empresaDAO = new EmpresaDAO(getBaseContext());
        entregaDAO = new EntregaDAO(getBaseContext());

        iniciaComponentes();
        configCliques();
        configRv();
        recuperaIdsItensAddMais();

        recuperaEnderecos();
    }

    private void recuperaEnderecos() {

        if (FirebaseHelper.getAutenticado()) {
            DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                    .child("enderecos")
                    .child(FirebaseHelper.getIdFirebase());
            enderecoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            endereco = ds.getValue(Endereco.class);
                        }

                    }

                    entregaDAO.salvarEndereco(endereco);
                    configEndereco();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            configEndereco();
        }

    }

    private void configEndereco() {
        if (endereco != null) {
            text_logradouro.setText(endereco.getLogradouro());
            text_referencia.setText(endereco.getReferencia());
            ll_endereco.setVisibility(View.VISIBLE);
        }
        configStatus();

    }

    private void configPagamento() {
        text_forma_pagamento.setText(pagamento.getDescricao());
        configStatus();

    }


    private void configStatus(){
        if (endereco== null){
            btn_continuar.setText("Selecione o endereço");
        }else{
            if (pagamento== null){
                btn_continuar.setText("Selecione a forma de pagamento");
            }else {
                btn_continuar.setText("Continuar");
                text_escolher_pagamento.setText("Trocar");
            }
        }
    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
        findViewById(R.id.ib_limpar).setOnClickListener(v -> {
            itemPedidoDAO.limparCarrinho();
            finish();
        });

        btn_continuar.setOnClickListener(v -> continuar() );

        ll_endereco.setOnClickListener(v -> {
            Intent intent= new Intent(this, UsuarioSelecionaEnderecoActivity.class);
            startActivityForResult(intent, REQUEST_ENDERECO);
        });

        text_escolher_pagamento.setOnClickListener(v -> {
            Intent intent= new Intent(this, UsuarioSelecionaPagamentoActivity.class);
            startActivityForResult(intent, REQUEST_PAGAMENTO);
        });
    }

    private void continuar(){
        if (FirebaseHelper.getAutenticado()){

            if(endereco== null){

                Intent intent= new Intent(this, UsuarioSelecionaEnderecoActivity.class);
                startActivityForResult(intent, REQUEST_ENDERECO);
            }else{
                if (pagamento== null){
                    Intent intent= new Intent(this, UsuarioSelecionaPagamentoActivity.class);
                    startActivityForResult(intent, REQUEST_PAGAMENTO);
                }
            }


        }else{
            Intent intent= new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        }
    }

    private void recuperaIdsItensAddMais() {
        DatabaseReference addMaisRef = FirebaseHelper.getDatabaseReference()
                .child("addMais")
                .child(empresaDAO.getEmpresa().getId());
        addMaisRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<String> idsItensList = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String idProduto = ds.getValue(String.class);

                        idsItensList.add(idProduto);
                    }

                    recuperaProdutos(idsItensList);
                } else {
                    ll_add_mais.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperaProdutos(List<String> idsItensList) {
        DatabaseReference produtosRef = FirebaseHelper.getDatabaseReference()
                .child("produtos")
                .child(empresaDAO.getEmpresa().getId());
        produtosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Produto produto = ds.getValue(Produto.class);
                    if (idsItensList.contains(produto.getId())) produtoList.add(produto);
                }

                Collections.reverse(produtoList);
                produtoCarrinhoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configRv() {
        rv_produtos.setLayoutManager(new LinearLayoutManager(this));
        rv_produtos.setHasFixedSize(true);
        carrinhoAdapter = new CarrinhoAdapter(itemPedidoDAO.getList(), getBaseContext(), this);
        rv_produtos.setAdapter(carrinhoAdapter);

        rv_add_mais.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_add_mais.setHasFixedSize(true);
        produtoCarrinhoAdapter = new ProdutoCarrinhoAdapter(produtoList, getBaseContext(), this);
        rv_add_mais.setAdapter(produtoCarrinhoAdapter);

    }



    private void iniciaComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Carrinho");

        rv_produtos = findViewById(R.id.rv_produtos);
        rv_add_mais = findViewById(R.id.rv_add_mais);
        btn_continuar = findViewById(R.id.btn_continuar);

        ll_add_mais = findViewById(R.id.ll_add_mais);
        ll_endereco = findViewById(R.id.ll_endereco);

        text_logradouro = findViewById(R.id.text_logradouro);
        text_referencia = findViewById(R.id.text_referencia);

        text_forma_pagamento= findViewById(R.id.text_forma_pagamento);
        text_escolher_pagamento= findViewById(R.id.text_escolher_pagamento);

    }

    @Override
    public void OnClick(ItemPedido itemPedido) {// RV PRINCIPAL
        Toast.makeText(this, itemPedido.getItem(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnClick(Produto produto) { //PEÇA MAIS

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== RESULT_OK){
            if (requestCode== REQUEST_LOGIN){

                Intent intent= new Intent(this, UsuarioSelecionaEnderecoActivity.class);
                startActivityForResult(intent, REQUEST_ENDERECO);
            }else if(requestCode== REQUEST_ENDERECO){
                endereco = (Endereco)  data.getSerializableExtra("enderecoSelecionado");
                configEndereco();

                if (entregaDAO.getEndereco()== null){
                    entregaDAO.salvarEndereco(endereco);
                }else{
                    entregaDAO.actualizarEndereco(endereco);
                }
            }else if(requestCode== REQUEST_PAGAMENTO){
                pagamento = (Pagamento)  data.getSerializableExtra("pagamentoSelecionado");
                configPagamento();
                entregaDAO.salvarPagamento(pagamento.getDescricao());
            }
        }
    }
}