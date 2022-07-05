package com.example.ifoodclone.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifoodclone.DAO.EmpresaDAO;
import com.example.ifoodclone.DAO.ItemPedidoDAO;
import com.example.ifoodclone.R;
import com.example.ifoodclone.adapter.CarrinhoAdapter;
import com.example.ifoodclone.adapter.ProdutoCarrinhoAdapter;
import com.example.ifoodclone.helper.FirebaseHelper;
import com.example.ifoodclone.model.ItemPedido;
import com.example.ifoodclone.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarrinhoActivity extends AppCompatActivity implements CarrinhoAdapter.OnClickListener, ProdutoCarrinhoAdapter.OnClickListener {

    private CarrinhoAdapter carrinhoAdapter;
    private ProdutoCarrinhoAdapter produtoCarrinhoAdapter;


    private RecyclerView rv_produtos;
    private RecyclerView rv_add_mais;
    private LinearLayout ll_add_mais;

    private List<Produto> produtoList= new ArrayList<>();

    private ItemPedidoDAO itemPedidoDAO;
    private EmpresaDAO empresaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        itemPedidoDAO= new ItemPedidoDAO(getBaseContext());
        empresaDAO= new EmpresaDAO(getBaseContext());

        iniciaComponentes();
        configCliques();
        configRv();
        recuperaIdsItensAddMais();
    }

    private void recuperaIdsItensAddMais(){
        DatabaseReference addMaisRef= FirebaseHelper.getDatabaseReference()
                .child("addMais")
                .child(empresaDAO.getEmpresa().getId());
        addMaisRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    List<String> idsItensList= new ArrayList<>();
                    for (DataSnapshot ds: snapshot.getChildren()){
                        String idProduto= ds.getValue(String.class);

                        idsItensList.add(idProduto);
                    }

                    recuperaProdutos(idsItensList);
                }else{
                    ll_add_mais.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperaProdutos(List<String> idsItensList) {
        DatabaseReference produtosRef= FirebaseHelper.getDatabaseReference()
                .child("produtos")
                .child(empresaDAO.getEmpresa().getId());
        produtosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Produto produto= ds.getValue(Produto.class);
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

    private void configRv(){
        rv_produtos.setLayoutManager(new LinearLayoutManager(this));
        rv_produtos.setHasFixedSize(true);
        carrinhoAdapter= new CarrinhoAdapter(itemPedidoDAO.getList(), getBaseContext(), this);
        rv_produtos.setAdapter(carrinhoAdapter);

        rv_add_mais.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        rv_add_mais.setHasFixedSize(true);
        produtoCarrinhoAdapter= new ProdutoCarrinhoAdapter(produtoList,getBaseContext(),this);
        rv_add_mais.setAdapter(produtoCarrinhoAdapter);

    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void iniciaComponentes(){
        TextView text_toolbar= findViewById(R.id.text_toolbar);
        text_toolbar.setText("Carrinho");

        rv_produtos=findViewById(R.id.rv_produtos);
        rv_add_mais=findViewById(R.id.rv_add_mais);

        ll_add_mais= findViewById(R.id.ll_add_mais);

    }

    @Override
    public void OnClick(ItemPedido itemPedido) {// RV PRINCIPAL
        Toast.makeText(this, itemPedido.getItem(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnClick(Produto produto) { //PEÇA MAIS

    }
}