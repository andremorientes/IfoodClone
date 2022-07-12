package com.example.ifoodclone.model;

import com.example.ifoodclone.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

public class Pagamento implements Serializable {

    private String descricao;
    private boolean status= false;

    public Pagamento() {
    }

    public static void salvar(List<Pagamento> pagamentoList){
        DatabaseReference pagamentoRef= FirebaseHelper.getDatabaseReference()
                .child("recebimentos")
                .child(FirebaseHelper.getIdFirebase());
        pagamentoRef.setValue(pagamentoList);
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
