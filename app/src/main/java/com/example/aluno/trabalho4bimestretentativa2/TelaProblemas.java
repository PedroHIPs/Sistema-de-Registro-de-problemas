package com.example.aluno.trabalho4bimestretentativa2;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TelaProblemas extends AppCompatActivity {

    private ListView lstProbs;
    private TextView teste;

    private ArrayList<String> ListRes;
    private ArrayList<Integer> ListC;
    private ArrayAdapter<String> adapter;

    private String chave;

    private LocationManager mgr;
    private String[] permissoes = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

    private Bitmap fotoTirada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_problemas);

        Bundle p = getIntent().getExtras();
        chave = p.getString("key");

        lstProbs = (ListView) findViewById(R.id.lstProblemas);
        teste = (TextView) findViewById(R.id.textView4);

        ListRes = new ArrayList<>();
        ListC = new ArrayList<>();

        mgr = (LocationManager) getSystemService(this.LOCATION_SERVICE);

        ArrayAdapter<String> dataAdapterLr = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, ListRes);
        lstProbs.setAdapter(dataAdapterLr);

        lstProbs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int pos=i;
                int issync;
                String r = "";
                Problema obj;
                ProblemaDAO dao;
                try
                {
                    if((pos>=0)&&(pos<ListRes.size())){
                        dao = new ProblemaDAO();
                        obj = dao.listarporCod(getBaseContext(),String.valueOf(ListC.get(pos)));
                        if(obj!=null){
                            issync = obj.getIssync();
                            if(issync == 0){
                                r+="Descrição: " + obj.getDescr() + ".    Registrado por: " + obj.getUsuario() + ".       Dia: " + obj.getDia() + "\n" +  "NÃO SICRONIZADO COM O BANCO";
                            }if(issync == 1){
                                r+="Descrição: " +  obj.getDescr() + ".    Registrado por: " + obj.getUsuario() + ".       Dia: " + obj.getDia() + "\n" + "SICRONIZADO";
                            }
                            MessageBox(r, obj.getDescr(), String.valueOf(obj.getCodigo()), String.valueOf(obj.getCodimg()), obj.getUsuario());
                        }
                    }
                }catch(Exception ex){
                    Toast.makeText(getBaseContext(), "Erro: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        /*
        lstProbs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int pos, cod, issync;
                String r = "";
                ProblemaDAO dao;
                Cursor tabela;
                pos = i;
                cod = ListC.get(pos);
                try{
                    dao = new ProblemaDAO();
                    tabela=dao.listarporCod(getBaseContext(), String.valueOf(cod));
                    if (tabela != null){
                        while (tabela.moveToNext()){
                            issync = tabela.getInt(6);
                            if(issync == 0){
                                r+="Descrição: " + tabela.getString(2) + ".    Registrado por: " + tabela.getString(1) + ".       Dia: " + tabela.getString(5) + "\n" +  "NÃO SICRONIZADO COM O BANCO";
                            }if(issync == 1){
                                r+="Descrição: " + tabela.getString(2) + ".    Registrado por: " + tabela.getString(1) + ".       Dia: " + tabela.getString(5) + "\n" + "SICRONIZADO";
                            }
                        }
                        MessageBox(r, tabela.getString(2), tabela.getString(0), tabela.getString(7));
                    }
                }catch (Exception ex){
                    Toast.makeText(getBaseContext(), "Erro: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });*/
    }

    public void capturarFoto(View v){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1); // devolve 1 em caso de sucesso.
            registrar(v);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == 1) && (resultCode == RESULT_OK)) {
            Bundle extras = data.getExtras();
            // byte[] vetor;
            // vetor = (byte[])extras.get("data");
            fotoTirada = (Bitmap) extras.get("data");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void registrar(final View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TelaProblemas.this);
        alertDialog.setTitle("Descrição");
        alertDialog.setMessage("Adicione uma descrição ao problema");

        final EditText input = new EditText(TelaProblemas.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Adicionar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ImagemDAO dao;
                        Imagem obj;
                        ByteArrayOutputStream vetorByte;
                        Problema objP;
                        ProblemaDAO objDAOP;
                        Location local;
                        double lat = 0;
                        double longi = 0;
                        try {
                            local =  getUltimaLocalizacaoConhecida();
                            if(local!=null){
                                obj = new Imagem();
                                vetorByte= new ByteArrayOutputStream();
                                fotoTirada.compress(Bitmap.CompressFormat.PNG, 0, vetorByte);//public boolean compress (Bitmap.CompressFormat format, int quality (0-100)maxima qualidade, OutputStream stream)
                                obj.setFoto(vetorByte.toByteArray());
                                dao= new ImagemDAO();
                                dao.gravar(getBaseContext(),obj);
                                Toast.makeText(getBaseContext(), "Imagem Salva com código "+obj.getCodigo(),Toast.LENGTH_LONG).show();
                                lat = local.getLatitude();
                                longi = local.getLongitude();
                                objP = new Problema();
                                objP.setDescr(input.getText().toString());
                                objP.setUsuario(chave);
                                objP.setLatitude(lat);
                                objP.setLongitude(longi);
                                objP.setCodimg(obj.getCodigo());
                                objP.setIssync(0);
                                objDAOP = new ProblemaDAO();
                                objDAOP.gravar(getBaseContext(), objP);
                                Toast.makeText(getBaseContext(), "Problema salvo com código "+objP.getCodigo(),Toast.LENGTH_LONG).show();
                                listar(v);
                            }else{
                                Toast.makeText(getBaseContext(), "Não há localização conhecida", Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception ex){
                            Toast.makeText(getBaseContext(), "Erro: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        alertDialog.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private Location getUltimaLocalizacaoConhecida() {
        mgr = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> provedores;
        Location bestLocation = null;
        Location local;

        if( (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION}, 1);//no lugar do um basta um maior que zero
        }

        provedores = mgr.getProviders(true);
        for (String provider : provedores) {
            local = mgr.getLastKnownLocation(provider);
            if (local != null) {
                if ((bestLocation == null) || (local.getAccuracy() < bestLocation.getAccuracy())) {
                    bestLocation = local;
                }
            }
        }
        return bestLocation;
    }

    public void MessageBox(String texto, String titulo, final String codP, String codI, final String verificacao){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle(titulo);
        dialogo.setMessage(texto);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        ImagemDAO dao;
        final Cursor tabela;
        byte[] vetorFoto;
        String verificaco = verificacao;

        final ImageView imgV = new ImageView(this);

        try
        {
            dao = new ImagemDAO();
            tabela = dao.listar(getBaseContext(), codI);
            if(tabela!=null)
            {
                if(tabela.moveToNext()){
                    Toast.makeText(this, "Codigo: "+tabela.getInt(0),Toast.LENGTH_LONG).show();
                    vetorFoto= (byte[]) tabela.getBlob(1);
                    Bitmap compressedBitmap = BitmapFactory.decodeByteArray(vetorFoto,0,vetorFoto.length);
                    imgV.setImageBitmap(compressedBitmap);
                }
                else {
                    Toast.makeText(this, "Não há foto armazenada ", Toast.LENGTH_LONG).show();
                }
            }

        }
        catch (Exception ex){
            Toast.makeText(this, "Erro: "+ex.getMessage(),Toast.LENGTH_LONG).show();
        }

        final TextView txtDescr = new TextView(this);
        txtDescr.setText("Alterar Descrição: ");
        final EditText input = new EditText(TelaProblemas.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        imgV.setLayoutParams(lp);

        layout.addView(imgV);
        layout.addView(input);
        dialogo.setView(layout);

        dialogo.setPositiveButton("Sincronizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Problema objSQ;
                Problema objPost;
                ProblemaDAO objDao;
                int issync;
                try {
                    objSQ = new Problema();
                    objPost = new Problema();
                    objDao = new ProblemaDAO();
                    objSQ = objDao.listarporCod(getBaseContext(),String.valueOf(codP));
                    if(objSQ!=null){
                        issync = objSQ.getIssync();
                        if(issync == 0){
                            objPost.setDescr(objSQ.getDescr());
                            objPost.setUsuario(objSQ.getUsuario());
                            objPost.setLatitude(objSQ.getLatitude());
                            objPost.setLongitude(objSQ.getLongitude());
                            objPost.setDia(objSQ.getDia());
                            objDao.sincronizar(objPost);
                            objDao.verificar(getBaseContext(), objSQ);
                            Toast.makeText(getBaseContext(), "Sincronizando" ,Toast.LENGTH_LONG).show();
                        }if(issync == 1){
                            Toast.makeText(getBaseContext(), "Já sicronizado com o banco", Toast.LENGTH_LONG).show();
                        }
                    }
                }catch (Exception ex){
                    Toast.makeText(getBaseContext(), "Erro ao remover: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        dialogo.setNegativeButton("Excluir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Problema obj;
                ProblemaDAO objDao;
                try {
                    obj = new Problema();
                    obj.setCodigo(codP);
                    objDao = new ProblemaDAO();
                    if(verificacao.equals(chave)) {
                        objDao.remover(getBaseContext(), obj);
                        Toast.makeText(getBaseContext(), "Excluindo" ,Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getBaseContext(), chave + ". Não é possivel excluir, apenas o(a) " + verificacao +" pode realizar tal ação", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception ex){
                    Toast.makeText(getBaseContext(), "Erro ao remover: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        dialogo.setNeutralButton("Alterar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Problema obj;
                ProblemaDAO objDao;
                try {
                    obj = new Problema();
                    obj.setCodigo(codP);
                    obj.setDescr(input.getText().toString());
                    objDao = new ProblemaDAO();
                    if(verificacao.equals(chave)) {
                        objDao.alterar(getBaseContext(), obj);
                        Toast.makeText(getBaseContext(), "Alterando descrição" ,Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getBaseContext(), chave + " Não é possivel alterar, apenas o(a) " + verificacao +" pode realizar tal ação", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception ex){
                    Toast.makeText(getBaseContext(), "Erro ao alterar: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        dialogo.show();
    }

    public void listar(View v){
        ProblemaDAO dao;
        Cursor tabela;
        int issync;
        try {
            dao = new ProblemaDAO();
            tabela=dao.listar(getBaseContext());
            if(tabela!=null) {
                ListC.clear();
                ListRes.clear();
                while(tabela.moveToNext()) {
                    issync = tabela.getInt(6);
                    if(issync == 0){
                        ListRes.add("Descrição: " + tabela.getString(2) + ".    Registrado por: " + tabela.getString(1) + ".       Dia: " + tabela.getString(5) + "\n" +  "NÃO SICRONIZADO COM O BANCO");
                        ListC.add(tabela.getInt(0));
                    }if(issync == 1){
                        ListRes.add("Descrição: " + tabela.getString(2) + ".    Registrado por: " + tabela.getString(1) + ".       Dia: " + tabela.getString(5) + "\n" + "SICRONIZADO");
                        ListC.add(tabela.getInt(0));
                    }
                    //codigo,usuario,descr,latitude,longitude,dia,issync,codimg
                }
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,ListRes);
                lstProbs.setAdapter(adapter);
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Erro ao listar: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void sincronizar(View v){

    }
}
