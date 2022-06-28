package com.example.ifoodclone.empresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ifoodclone.R;
import com.example.ifoodclone.activity.autenticacao.LoginActivity;
import com.example.ifoodclone.helper.FirebaseHelper;
import com.example.ifoodclone.helper.GetMask;
import com.example.ifoodclone.model.Empresa;
import com.example.ifoodclone.model.Favorito;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EmpresaCardapioActivity extends AppCompatActivity {

    private List<String> favoritoList= new ArrayList<>();
    private Favorito favorito= new Favorito();

    private ImageView img_logo_empresa;
    private TextView text_empresa;
    private TextView text_categoria_empresa;
    private TextView text_tempo_minimo;
    private TextView text_tempo_maximo;
    private TextView text_taxa_entrega;
    private LikeButton btn_like;

    private RecyclerView rv_categorias;
    private ProgressBar progressBar;
    private TextView text_info;

    private Empresa empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_cardapio);

        iniciaComponentes();

        Bundle bundle= getIntent().getExtras();
        if (bundle!= null){
            empresa=(Empresa) bundle.getSerializable("empresaSelecionada");
            configDados();
        }


        configCliques();

        recuperaFavorito();
    }

    private void configDados(){
        Picasso.get().load(empresa.getUrlLogo()).into(img_logo_empresa);
        text_empresa.setText(empresa.getNome());
        text_categoria_empresa.setText(empresa.getCategoria());
        text_tempo_minimo.setText(empresa.getTempoMinEntrega() + "-");
        text_tempo_maximo.setText(empresa.getTempoMaxEntrega() + " min");

        if(empresa.getTaxaEntrega() > 0){
            text_taxa_entrega.setText(getString(R.string.text_valor, GetMask.getValor(empresa.getTaxaEntrega())));
        }else {
            text_taxa_entrega.setTextColor(Color.parseColor("#2ED67E"));
            text_taxa_entrega.setText("ENTREGA GRÁTIS");
        }
    }

    private void recuperaFavorito(){
        if(FirebaseHelper.getAutenticado()){
            DatabaseReference favoritosRef = FirebaseHelper.getDatabaseReference()
                    .child("favoritos")
                    .child(FirebaseHelper.getIdFirebase());
            favoritosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot ds : snapshot.getChildren()){
                            String idFavorito = ds.getValue(String.class);
                            favoritoList.add(idFavorito);
                        }

                        if(favoritoList.contains(empresa.getId())){
                            btn_like.setLiked(true);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());

        btn_like.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                configFavorito();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                configFavorito();
            }
        });
    }

    private void configFavorito(){
        if(FirebaseHelper.getAutenticado()){
            if(!favoritoList.contains(empresa.getId())){
                favoritoList.add(empresa.getId());
            }else {
                favoritoList.remove(empresa.getId());
            }

            favorito.setFavoritoList(favoritoList);
            favorito.salvar();
        }else {
            btn_like.setLiked(false);
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void iniciaComponentes(){
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Cardápio");

        img_logo_empresa = findViewById(R.id.img_logo_empresa);
        text_empresa = findViewById(R.id.text_empresa);
        text_categoria_empresa = findViewById(R.id.text_categoria_empresa);
        text_tempo_minimo = findViewById(R.id.text_tempo_minimo);
        text_tempo_maximo = findViewById(R.id.text_tempo_maximo);
        text_taxa_entrega = findViewById(R.id.text_taxa_entrega);
        btn_like = findViewById(R.id.btn_like);
        rv_categorias = findViewById(R.id.rv_categorias);
        progressBar = findViewById(R.id.progressBar);
        text_info = findViewById(R.id.text_info);
    }

}