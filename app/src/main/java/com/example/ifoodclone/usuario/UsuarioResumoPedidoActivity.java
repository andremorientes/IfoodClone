package com.example.ifoodclone.usuario;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ifoodclone.DAO.EmpresaDAO;
import com.example.ifoodclone.DAO.EntregaDAO;
import com.example.ifoodclone.DAO.ItemPedidoDAO;
import com.example.ifoodclone.R;
import com.example.ifoodclone.adapter.CarrinhoAdapter;
import com.example.ifoodclone.adapter.ProdutoCarrinhoAdapter;
import com.example.ifoodclone.helper.FirebaseHelper;
import com.example.ifoodclone.helper.GetMask;
import com.example.ifoodclone.model.Endereco;
import com.example.ifoodclone.model.ItemPedido;
import com.example.ifoodclone.model.Pagamento;
import com.example.ifoodclone.model.Pedido;
import com.example.ifoodclone.model.Produto;
import com.example.ifoodclone.model.StatusPedido;

import java.util.ArrayList;
import java.util.List;

public class UsuarioResumoPedidoActivity extends AppCompatActivity implements CarrinhoAdapter.OnClickListener {

    private final int REQUEST_ENDERECO = 200;
    private final int REQUEST_PAGAMENTO = 300;

    private RecyclerView rv_produtos;
    private final List<Produto> produtoList = new ArrayList<>();
    private List<ItemPedido> itemPedidoList = new ArrayList<>();
    private CarrinhoAdapter carrinhoAdapter;

    private TextView text_endereco;
    private TextView text_forma_pagamento;
    private TextView text_subtotal;
    private TextView text_taxa_entrega;
    private TextView text_total;
    private Button btn_finalizar;

    private Endereco endereco;
    private String pagamento;

    private EntregaDAO entregaDAO;
    private ItemPedidoDAO itemPedidoDAO;
    private EmpresaDAO empresaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_resumo_pedido);

        entregaDAO = new EntregaDAO(this);
        itemPedidoDAO = new ItemPedidoDAO(this);
        empresaDAO = new EmpresaDAO(this);

        iniciaComponentes();

        configDados();

        configRv();
        configCliques();

    }

    private void configCliques() {
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
        findViewById(R.id.btn_finalizar).setOnClickListener(v -> finalizarPedido());
    }

    private void finalizarPedido() {

        double subtotal = itemPedidoDAO.getTotal();
        double taxa_entrega = empresaDAO.getEmpresa().getTaxaEntrega();
        double total = subtotal + taxa_entrega;

        Pedido pedido = new Pedido();

        pedido.setIdCliente(FirebaseHelper.getIdFirebase());
        pedido.setIdEmpresa(empresaDAO.getEmpresa().getId());
        pedido.setFormaPagamento(pagamento);
        pedido.setStatusPedido(StatusPedido.PENDENTE);
        pedido.setItemPedidoList(itemPedidoDAO.getList());
        pedido.setTaxaEntrega(taxa_entrega);
        pedido.setSubtotal(subtotal);
        pedido.setTotalPedido(total);
        pedido.setEnderecoEntrega(entregaDAO.getEndereco());
        pedido.salvar();

        itemPedidoDAO.limparCarrinho();

        Intent intent = new Intent(this, UsuarioHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("id", 2);
        startActivity(intent);
    }

    private void configRv() {
        rv_produtos.setLayoutManager(new LinearLayoutManager(this));
        rv_produtos.setHasFixedSize(true);
        carrinhoAdapter = new CarrinhoAdapter(itemPedidoDAO.getList(), getBaseContext(), this);
        rv_produtos.setAdapter(carrinhoAdapter);


    }

    private void configDados() {

        endereco = entregaDAO.getEndereco();
        pagamento = entregaDAO.getEntrega().getFormaPagamento();

        StringBuilder enderecoCompleto = new StringBuilder()
                .append(endereco.getLogradouro())
                .append("\n")
                .append(endereco.getBairro())
                .append(", ")
                .append(endereco.getMunicipio())
                .append("\n")
                .append(endereco.getReferencia());


        text_endereco.setText(enderecoCompleto);
        text_forma_pagamento.setText(pagamento);
        configSaldo();
    }


    private void configSaldo() {
        double subtotal = 0;
        double taxa_entrega = 0;
        double total = 0;

        if (!itemPedidoDAO.getList().isEmpty()) {
            subtotal = itemPedidoDAO.getTotal();
            taxa_entrega = empresaDAO.getEmpresa().getTaxaEntrega();
            total = subtotal + taxa_entrega;
        }

        text_subtotal.setText(getString(R.string.text_valor, GetMask.getValor(subtotal)));
        text_taxa_entrega.setText(getString(R.string.text_valor, GetMask.getValor(taxa_entrega)));
        text_total.setText(getString(R.string.text_valor, GetMask.getValor(total)));
    }

    public void alteraEndereco(View view) {

        Intent intent = new Intent(this, UsuarioSelecionaEnderecoActivity.class);
        startActivityForResult(intent, REQUEST_ENDERECO);
    }

    public void alteraPagamento(View view) {

        Intent intent = new Intent(this, UsuarioSelecionaPagamentoActivity.class);
        startActivityForResult(intent, REQUEST_PAGAMENTO);
    }

    private void iniciaComponentes() {
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Resumo pedido");

        text_endereco = findViewById(R.id.text_endereco);
        text_forma_pagamento = findViewById(R.id.text_forma_pagamento);
        text_taxa_entrega = findViewById(R.id.text_taxa_entrega);
        text_subtotal = findViewById(R.id.text_subtotal);
        text_total = findViewById(R.id.text_total);
        btn_finalizar = findViewById(R.id.btn_finalizar);
        rv_produtos = findViewById(R.id.rv_produtos);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ENDERECO) {
                endereco = (Endereco) data.getSerializableExtra("enderecoSelecionado");
                entregaDAO.actualizarEndereco(endereco);
                configDados();
            }
        } else if (requestCode == REQUEST_PAGAMENTO) {

            Pagamento formaPagamento = (Pagamento) data.getSerializableExtra("pagamentoSelecionado");
            pagamento = formaPagamento.getDescricao();
            entregaDAO.salvarPagamento(pagamento);
            configDados();
        }
    }

    @Override
    public void OnClick(ItemPedido itemPedido) {

    }
}