package upiicsa.com.alertacdmx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import upiicsa.com.alertacdmx.model.utilities.ConectToServer;
import upiicsa.com.alertacdmx.model.utilities.CustomAdapter;

public class MainPublicacion extends AppCompatActivity {

    private Intent mapas;
    private String[] mySession;
    private double latitud;
    private double longitiud;

    /*Para llenar la lista*/
    private ArrayList<String> idsUsuario; //listo
    //private ArrayList<Bitmap> imgUsuario;
    private ArrayList<String> fechasPbl;//listo
    private ArrayList<String> resenaPbl;//listo
    private ArrayList<String> namesUsuario; //listo
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_publicacion);
        this.mapas = getIntent();
        this.mySession = this.mapas.getStringArrayExtra("session");
        this.latitud = this.mapas.getDoubleExtra("latitud", 0);
        this.longitiud = this.mapas.getDoubleExtra("longitud", 0);
        idsUsuario=new ArrayList<String>();
        //imgUsuario=new ArrayList<Bitmap>();
        fechasPbl=new ArrayList<String>();
        resenaPbl=new ArrayList<String>();
        namesUsuario=new ArrayList<String>();
        mContext=this;
        loadPosts();
        loadDataUserForList();
        ListView listView = (ListView) findViewById(R.id.list_publicaciones);
        CustomAdapter adapter = new CustomAdapter(this, fechasPbl, resenaPbl,namesUsuario);
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_second,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_nuevaPbl:
                Intent nueva = new Intent(mContext,NewPublicacion.class);
                nueva.putExtra("session",mySession);
                nueva.putExtra("latitud",this.latitud);
                nueva.putExtra("longitud",this.longitiud);
                startActivity(nueva);
                return false;
            default:
                return false;
        }
    }

    public void loadPosts() {
        final String baseUrl = "http://www.alertacdmx.com/Requests.php";
        ConectToServer conn = new ConectToServer();
        Uri objectURL = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter("id_request", "03")
                .appendQueryParameter("latitud", this.latitud + "")
                .appendQueryParameter("longitud", this.longitiud + "").build();
        try {

            URL linkPost = new URL(objectURL.toString());
            conn.execute(linkPost);
            JSONObject jsonPost = new JSONObject(conn.get().trim());
            JSONArray jsonArrayPost = jsonPost.getJSONArray("publicaciones");
            for (int objs = 0; objs < jsonArrayPost.length(); objs++) {
                String tempRersena = jsonArrayPost.getJSONObject(objs).getString("pbl_texto");
                this.resenaPbl.add(tempRersena);
                String tempIdUsuario = jsonArrayPost.getJSONObject(objs).getString("user_id");
                this.idsUsuario.add(tempIdUsuario);
                String tempFechapbl = jsonArrayPost.getJSONObject(objs).getString("pbl_fecha");
                this.fechasPbl.add(tempFechapbl);
            }

        } catch (MalformedURLException urlEx) {
            Log.e(urlEx.getClass().toString(), urlEx.getMessage());
        } catch (InterruptedException iex) {
            Log.e(iex.getClass().toString(), iex.getMessage());
        } catch (ExecutionException exx) {
            Log.e(exx.getClass().toString(), exx.getMessage());
        } catch (JSONException jex) {
            Log.e(jex.getClass().toString()+"PINA", jex.getLocalizedMessage());
        }



    }

    public void loadDataUserForList() {
        for (int users = 0; users < idsUsuario.size(); users++) {
            final String baseUrl = "http://www.alertacdmx.com/Requests.php";
            ConectToServer conn = new ConectToServer();
            Uri objectURLa = Uri.parse(baseUrl).buildUpon()
                    .appendQueryParameter("id_request", "04")
                    .appendQueryParameter("user_id", idsUsuario.get(users) + "").build();
            try {
                URL linkUserData = new URL(objectURLa.toString());
                conn.execute(linkUserData);
                JSONObject json = new JSONObject(conn.get().trim());
                this.namesUsuario.add(json.getString("user_displayName"));
                /*URL url = new URL(json.getString("user_image"));
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                this.imgUsuario.add(bmp);*/
            } catch (MalformedURLException urlEx) {
                Log.e(urlEx.getClass().toString(), urlEx.getMessage());
            } catch (InterruptedException iex) {
                Log.e(iex.getClass().toString(), iex.getMessage());
            } catch (ExecutionException exx) {
                Log.e(exx.getClass().toString(), exx.getMessage());
            } catch (JSONException jex) {
                Log.e(jex.getClass().toString(), jex.getMessage());
            } catch (IOException iox) {
                Log.i("IO", iox.getMessage());
            }
        }

    }


}
