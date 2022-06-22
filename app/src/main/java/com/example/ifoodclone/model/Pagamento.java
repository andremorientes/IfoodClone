package com.example.ifoodclone.model;

public class Pagamento {

    private String descricao;
    private boolean status= false;

    public Pagamento() {
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
