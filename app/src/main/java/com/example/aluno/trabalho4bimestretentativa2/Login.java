package com.example.aluno.trabalho4bimestretentativa2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private EditText edtName;
    private EditText edtPass;
    private Boolean log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtName = (EditText) findViewById(R.id.edtName);
        edtPass = (EditText) findViewById(R.id.edtSenha);
    }

    public void cadastrar(View v){
        Pessoa obj;
        PessoaDAO objDao;
        try{
            obj = new Pessoa();
            obj.setUsuario(edtName.getText().toString());
            obj.setSenha(edtPass.getText().toString());
            objDao = new PessoaDAO();
            objDao.cadastrar(getBaseContext(), obj);
            Toast.makeText(this, "Usuário Cadastrado: " + edtName.getText(), Toast.LENGTH_LONG).show();
            edtName.setText("");
            edtPass.setText("");
            edtName.requestFocus();
        }catch (Exception ex){
            Toast.makeText(this, "Erro ao cadastrar: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void logar(View v){
        PessoaDAO objDao;
        String codU;
        try{
            objDao = new PessoaDAO();
            log = objDao.logar(getBaseContext(), edtName.getText().toString(), edtPass.getText().toString());
            if(log){
                codU = edtName.getText().toString();
                Bundle codUser= new Bundle();
                codUser.putString("key", codU);
                Toast.makeText(this, "Usuário logado: " + edtName.getText() + ". Redirecionando a tela de problemas", Toast.LENGTH_LONG).show();
                Intent inte = new Intent(Login.this, TelaProblemas.class);
                inte.putExtras(codUser);
                startActivity(inte);
            }
            if(!log){
                Toast.makeText(this, "Usuário não logado verifique usuário e senha", Toast.LENGTH_LONG).show();
            }

        }catch (Exception ex){
            Toast.makeText(this, "Erro ao logar: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
