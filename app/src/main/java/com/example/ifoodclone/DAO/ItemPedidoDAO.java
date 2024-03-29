package com.example.ifoodclone.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ifoodclone.model.Endereco;
import com.example.ifoodclone.model.ItemPedido;
import com.google.android.gms.common.util.Strings;

import java.util.ArrayList;
import java.util.List;

public class ItemPedidoDAO {

    private SQLiteDatabase write;
    private SQLiteDatabase read;

    public ItemPedidoDAO(Context context) {

        DbHelper dbHelper = new DbHelper(context);
        write = dbHelper.getWritableDatabase();
        read = dbHelper.getReadableDatabase();
    }

    public long salvar(ItemPedido itemPedido) {

        long id = 0;

        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUNA_ID_FIREBASE, itemPedido.getItem());
        cv.put(DbHelper.COLUNA_NOME, itemPedido.getItem());
        cv.put(DbHelper.COLUNA_URL_IMAGEM, itemPedido.getUrlImagem());
        cv.put(DbHelper.COLUNA_VALOR, itemPedido.getValor());
        cv.put(DbHelper.COLUNA_QUANTIDADE, itemPedido.getQuantidade());

        try {
            id = write.insert(DbHelper.TABELA_ITEM_PEDIDO, null, cv);
            Log.i("INFO_DB", "onCreate: Sucesso ao salvar a tabela");
        } catch (Exception e) {
            Log.i("INFO_DB", "onCreate: Erro ao salvar a tabela");
        }
        return id;
    }

    public void actualizar(ItemPedido itemPedido) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUNA_QUANTIDADE, itemPedido.getQuantidade());

        try {
            String where = "id=?";
            String[] args = {String.valueOf(itemPedido.getId())};
            write.update(DbHelper.TABELA_ITEM_PEDIDO, cv, where, args);
            Log.i("INFO_DB", "onCreate: Sucesso ao actualizar a tabela");
        } catch (Exception e) {
            Log.i("INFO_DB", "onCreate: Erro ao actualizar a tabela");
        }

    }

    public List<ItemPedido> getList() {
        List<ItemPedido> itemPedidoList = new ArrayList<>();

        String sql = "SELECT * FROM " + DbHelper.TABELA_ITEM_PEDIDO + ";";
        Cursor cursor = read.rawQuery(sql, null);


        while (cursor.moveToNext()) {
            @SuppressLint("Range") Long id_local = cursor.getLong(cursor.getColumnIndex(DbHelper.COLUNA_ID));
            @SuppressLint("Range") String id_firebase = cursor.getString(cursor.getColumnIndex(DbHelper.COLUNA_ID_FIREBASE));
            @SuppressLint("Range") String item_nome = cursor.getString(cursor.getColumnIndex(DbHelper.COLUNA_NOME));
            @SuppressLint("Range") String url_imagem = cursor.getString(cursor.getColumnIndex(DbHelper.COLUNA_URL_IMAGEM));
            @SuppressLint("Range") double valor = cursor.getDouble(cursor.getColumnIndex(DbHelper.COLUNA_VALOR));
            @SuppressLint("Range") int quantidade = cursor.getInt(cursor.getColumnIndex(DbHelper.COLUNA_QUANTIDADE));

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setId(id_local);
            itemPedido.setItem(id_firebase);
            itemPedido.setItem(item_nome);
            itemPedido.setUrlImagem(url_imagem);
            itemPedido.setValor(valor);
            itemPedido.setQuantidade(quantidade);

            itemPedidoList.add(itemPedido);
        }
        return itemPedidoList;
    }

    public Double getTotal() {
        double total = 0;
        for (ItemPedido itemPedido : getList()) {
            total += itemPedido.getValor() * itemPedido.getQuantidade();
        }
        return total;
    }

    public void remover(Long id) {

        try {
            String where = "id=?";
            String[] args = {String.valueOf(id)};
            write.delete(DbHelper.TABELA_ITEM_PEDIDO, where, args);
            Log.i("INFO_DB", "onCreate: Sucesso ao deletar item");
        } catch (Exception e) {
            Log.i("INFO_DB", "onCreate: Erro ao deletarc item");
        }

    }

    public void removerTodos() {
        try {
            write.delete(DbHelper.TABELA_ITEM_PEDIDO, null, null);
            Log.i("INFO_DB", "onCreate: Sucesso ao deletar todos os itens");
        } catch (Exception e) {
            Log.i("INFO_DB", "onCreate: Erro ao deletar todos os itens");
        }

    }

    public void limparCarrinho() {
        try {
            write.delete(DbHelper.TABELA_EMPRESA, null, null);
            write.delete(DbHelper.TABELA_ENTREGA, null, null);
            write.delete(DbHelper.TABELA_ITEM_PEDIDO, null, null);
            Log.i("INFO_DB", "onCreate: Sucesso ao limpar o carrinho");
        } catch (Exception e) {
            Log.i("INFO_DB", "onCreate: Erro ao limpar o carrinho");
        }

    }
}
