package com.example.ifoodclone.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class ItemPedido implements Serializable {

    private Long id;
    private String idTem;
    private String item;
    private String urlImagem;
    private Double valor;
    private int quantidade;

    public ItemPedido() {
    }

    @Exclude
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdTem() {
        return idTem;
    }

    public void setIdTem(String idTem) {
        this.idTem = idTem;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
