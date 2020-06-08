package com.example.aluno.trabalho4bimestretentativa2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.PreparedStatement;
import java.util.concurrent.ExecutorCompletionService;

public class ProblemaDAO {

    public void gravar(Context context, Problema obj) throws Exception {
        BancoSQLite conexao;
        SQLiteDatabase bb;

        try {
            conexao = new BancoSQLite(context);
            bb = conexao.getWritableDatabase();
            bb.execSQL("insert into problema(usuario,descr,latitude,longitude,issync,codimg) values (?,?,?,?,?,?)",
                    new String[]{obj.getUsuario(), obj.getDescr(), String.valueOf(obj.getLatitude()), String.valueOf(obj.getLongitude()), String.valueOf(obj.getIssync()), String.valueOf(obj.getCodimg())});
            bb.close();
        } catch (Exception ex) {
            throw new Exception("Erro ao gravar problema: " + ex.getMessage());
        }
    }

    public void remover(Context context, Problema obj) throws Exception {
        BancoSQLite conexao;
        SQLiteDatabase bb;
        try {
            conexao = new BancoSQLite(context);
            bb = conexao.getWritableDatabase();
            bb.execSQL("Delete from problema where codigo= ?",
                    new String[]{String.valueOf(obj.getCodigo())});
            bb.close();
        } catch (Exception ex) {
            throw new Exception("Erro ao remover problema: " + ex.getMessage());
        }
    }

    public void alterar(Context context, Problema obj) throws Exception {
        BancoSQLite conexao;
        SQLiteDatabase bb;
        try {
            conexao = new BancoSQLite(context);
            bb = conexao.getWritableDatabase();
            bb.execSQL("update problema set descr= ? where codigo=?",
                    new String[]{obj.getDescr(), String.valueOf(obj.getCodigo())});
            bb.close();
        } catch (Exception ex) {
            throw new Exception("Erro ao alterar problema: " + ex.getMessage());
        }
    }

    public void verificar(Context context, Problema obj) throws Exception {
        BancoSQLite conexao;
        SQLiteDatabase bb;
        try {
            conexao = new BancoSQLite(context);
            bb = conexao.getWritableDatabase();
            bb.execSQL("update problema set issync= 1 where codigo=?",
                    new String[]{String.valueOf(obj.getCodigo())});
            bb.close();
        } catch (Exception ex) {
            throw new Exception("Erro ao verificar problema: " + ex.getMessage());
        }
    }


    public Cursor listar(Context context) throws Exception {
        BancoSQLite conexao;
        SQLiteDatabase bb;
        Cursor tabela = null;
        Problema obj;

        try {
            conexao = new BancoSQLite(context);
            bb = conexao.getReadableDatabase();
            tabela = bb.rawQuery("Select codigo,usuario,descr,latitude,longitude,dia,issync,codimg from problema", null);

            if ((tabela != null) && (tabela.moveToNext())) {
                obj = new Problema();
                obj.setCodigo(tabela.getInt(0));
                obj.setUsuario(tabela.getString(1));
                obj.setDescr(tabela.getString(2));
                obj.setLatitude(tabela.getDouble(3));
                obj.setLongitude(tabela.getDouble(4));
            }
            bb.close();
            return (tabela);
        } catch (Exception ex) {
            throw new Exception("Erro ao preencher: " + ex.getMessage());
        }
    }

    public Problema listarporCod(Context context, String cod) throws Exception {
        BancoSQLite conexao;
        SQLiteDatabase bb;
        Cursor tabela = null;
        Problema obj;

        try {
            conexao = new BancoSQLite(context);
            bb = conexao.getReadableDatabase();
            tabela = bb.rawQuery("Select codigo,usuario,descr,latitude,longitude,dia,issync,codimg from problema where codigo=?", new String[]{cod});
            obj = new Problema();

            if ((tabela != null) && (tabela.moveToNext())) {
                obj.setCodigo(tabela.getInt(0));
                obj.setUsuario(tabela.getString(1));
                obj.setDescr(tabela.getString(2));
                obj.setLatitude(tabela.getDouble(3));
                obj.setLongitude(tabela.getDouble(4));
                obj.setIssync(tabela.getInt(6));
                obj.setCodimg(tabela.getInt(7));
            }
            bb.close();
            return (obj);
        } catch (Exception ex) {
            throw new Exception("Erro ao preencher: " + ex.getMessage());
        }
    }

    public int sincronizar(Problema obj) throws Exception {
        BancoPost bb=null;
        BancoExecuteUpdate bbExec=null;
        int resp=-1;
        String erro="";
        PreparedStatement teste;
        try {
            bb = new BancoPost();
            bb.comando = BancoPost.conexao.prepareStatement("insert into problema(usuario,descr,latitude,longitude,dia) values (?,?,?,?,?)");
            bb.comando.setString(1, obj.getUsuario());
            bb.comando.setString(2, obj.getDescr());
            bb.comando.setDouble(3, obj.getLatitude());
            bb.comando.setDouble(4, obj.getLongitude());
            bb.comando.setTimestamp(5, obj.getDia());

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
            throw new Exception("Erro ao sicronizar problema: "+ ex.getMessage()+ erro);
        }
    }
}