package com.example.ifoodclone.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ifoodclone.model.Empresa;
import com.example.ifoodclone.model.Endereco;
import com.example.ifoodclone.model.EntregaPedido;

public class EntregaDAO {

    private SQLiteDatabase write;
    private SQLiteDatabase  read;

    public EntregaDAO(Context context) {
        DbHelper dbHelper= new DbHelper(context);
        write= dbHelper.getWritableDatabase();
        read= dbHelper.getReadableDatabase();
    }

    public void salvarEndereco(Endereco endereco){
        ContentValues cv= new ContentValues();
        cv.put(DbHelper.COLUNA_FORMA_PAGAMENTO, "");
        cv.put(DbHelper.COLUNA_ENDERECO_LOGRADOURO, endereco.getLogradouro());
        cv.put(DbHelper.COLUNA_ENDERECO_BAIRRO, endereco.getBairro());
        cv.put(DbHelper.COLUNA_ENDERECO_MUNICIPIO, endereco.getMunicipio());
        cv.put(DbHelper.COLUNA_ENDERECO_REFERENCIA, endereco.getReferencia());

        try {
            write.insert(DbHelper.TABELA_ENTREGA, null, cv);
            Log.i("INFO_DB", "onCreate: Sucesso ao salvar a tabela");
        }catch (Exception e){
            Log.i("INFO_DB", "onCreate: Erro ao salvar a tabela");
        }
    }

    public void actualizarEndereco(Endereco endereco){
        ContentValues cv= new ContentValues();

        cv.put(DbHelper.COLUNA_ENDERECO_LOGRADOURO, endereco.getLogradouro());
        cv.put(DbHelper.COLUNA_ENDERECO_BAIRRO, endereco.getBairro());
        cv.put(DbHelper.COLUNA_ENDERECO_MUNICIPIO, endereco.getMunicipio());
        cv.put(DbHelper.COLUNA_ENDERECO_REFERENCIA, endereco.getReferencia());

        try {
            write.update(DbHelper.TABELA_ENTREGA, cv, null,null);
            Log.i("INFO_DB", "onCreate: Sucesso ao actualizar o endereço");
        }catch (Exception e){
            Log.i("INFO_DB", "onCreate: Erro ao actualizar o endereço");
        }
    }

    public void salvarPagamento(String formaPagamento){

        ContentValues cv= new ContentValues();
        cv.put(DbHelper.COLUNA_FORMA_PAGAMENTO, formaPagamento);

        try {
            write.update(DbHelper.TABELA_EMPRESA, cv, null, null);
            Log.i("INFO_DB", "onCreate: Sucesso ao actualizar a tabela");
        }catch (Exception e){
            Log.i("INFO_DB", "onCreate: Erro ao actualizar a tabela");
        }
    }

    public EntregaPedido getEntrega(){
        EntregaPedido entregaPedido= new EntregaPedido();
        Endereco endereco= new Endereco();

        String sql= " SELECT * FROM " +DbHelper.TABELA_ENTREGA + ";" ;
        Cursor cursor= read.rawQuery(sql, null);

        while (cursor.moveToNext()){
            @SuppressLint("Range") String forma_pagamento = cursor.getString(cursor.getColumnIndex(DbHelper.COLUNA_FORMA_PAGAMENTO));
            @SuppressLint("Range") String logradouro = cursor.getString(cursor.getColumnIndex(DbHelper.COLUNA_ENDERECO_LOGRADOURO));
            @SuppressLint("Range") String bairro = cursor.getString(cursor.getColumnIndex(DbHelper.COLUNA_ENDERECO_BAIRRO));
            @SuppressLint("Range") String municipio = cursor.getString(cursor.getColumnIndex(DbHelper.COLUNA_ENDERECO_MUNICIPIO));
            @SuppressLint("Range") String referencia = cursor.getString(cursor.getColumnIndex(DbHelper.COLUNA_ENDERECO_REFERENCIA));


            endereco.setLogradouro(logradouro);
            endereco.setBairro(bairro);
            endereco.setMunicipio(municipio);
            endereco.setReferencia(referencia);

            entregaPedido.setFormaPagamento(forma_pagamento);
            entregaPedido.setEndereco(endereco);

        }
        return  entregaPedido;

    }
}