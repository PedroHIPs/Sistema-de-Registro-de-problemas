package com.example.aluno.trabalho4bimestretentativa2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;

public class ImagemDAO {


    public void gravar(Context context, Imagem obj) throws Exception {
        BancoSQLite conexao;
        SQLiteDatabase bb;
        ContentValues campos = new ContentValues();
        long codigo;
        try {
            conexao = new BancoSQLite(context);
            bb = conexao.getWritableDatabase();
            campos.put("foto", obj.getFoto());
            codigo = bb.insert("imagem", null, campos); //devolve o código gerado pelo sql lite
            obj.setCodigo((int) codigo);
            bb.close();
        } catch (Exception ex) {
            throw new Exception("Erro ao gravar: " + ex.getMessage());
        }
    }

    public Cursor listar(Context context, String codI) throws Exception{
        BancoSQLite conexao;
        SQLiteDatabase bb;
        Cursor tabela= null;
        try{
            conexao = new BancoSQLite(context);
            bb = conexao.getReadableDatabase();
            tabela = bb.rawQuery ("Select codigo,foto from imagem where codigo=?",new String[]{codI});
            return(tabela);
        }
        catch (Exception ex){
            throw new Exception("Erro ao consultar: "+ex.getMessage());
        }
    }

    public int sincronizar(Imagem obj) throws Exception {
        BancoPost bb=null;
        BancoExecuteUpdate bbExec=null;
        int resp=-1;
        String erro="";
        PreparedStatement teste;
        try {
            bb = new BancoPost();
            bb.comando = BancoPost.conexao.prepareStatement("insert into problema(codproblema,foto) values (?,?)");
            bb.comando.setInt(1, obj.getCodproblema());
            bb.comando.setBytes(2, obj.getFoto());

            bbExec =new BancoExecuteUpdate(bb.comando);
            resp=bbExec.execute().get(); // execute passa os parãmetros para o doInBackground que pode ser nenhum ou muitos  e executa ele
            // já o get captura o valor de retorno do doInBackground.
            bb.desconectar();
            return(resp);
        } catch (Exception ex) {
            if((bb!=null)&&(bb.erro.length()>0))
                erro=bb.erro;
            if((bbExec!=null)&&(bbExec.erro.length()>0))
                erro+="  "+ bbExec.erro;
            throw new Exception("Erro ao sincronizar imagem: "+ ex.getMessage()+ erro);
        }
    }
}
