package upiicsa.com.alertacdmx;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import upiicsa.com.alertacdmx.model.utilities.ConectToServer;

public class NewPublicacion extends AppCompatActivity {
    private Intent intent;
    private String[] mySession;
    private double latitud;
    private double longitiud;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_publicacion);
        this.intent = getIntent();
        this.mySession = this.intent.getStringArrayExtra("session");
        this.latitud = this.intent.getDoubleExtra("latitud", 0);
        this.longitiud = this.intent.getDoubleExtra("longitud", 0);
        this.mContext = this;

    }

    public void insertarPost(View view){
        try {
            EditText textoPost = (EditText) findViewById(R.id.texto_post);
            String textoInsertar = textoPost.getText().toString();
            if (textoInsertar.length() < 150) {
                final String baseUrl = "http://www.alertacdmx.com/Requests.php";
                ConectToServer conn = new ConectToServer();

                Uri objectURL = Uri.parse(baseUrl).buildUpon()
                        .appendQueryParameter("id_request", "01")
                        .appendQueryParameter("user_id", mySession[0])
                        .appendQueryParameter("pbl_texto", textoInsertar)
                        .appendQueryParameter("latitud", this.latitud + "")
                        .appendQueryParameter("longitud", this.longitiud + "").build();
                try {

                    URL linkPost = new URL(objectURL.toString());
                    conn.execute(linkPost);
                    JSONObject jsonPost = new JSONObject(conn.get().trim());
                    if (jsonPost.get("publicado").equals("si")) {
                        Toast.makeText(NewPublicacion.this, "Publicado", Toast.LENGTH_SHORT).show();
                        Intent mapaLoco = new Intent(mContext, Mapa_Reportes.class);
                        mContext.startActivity(mapaLoco);
                    } else {
                        Toast.makeText(NewPublicacion.this, "No se pudo publicar", Toast.LENGTH_SHORT).show();
                    }

                } catch (MalformedURLException uex) {
                } catch (JSONException jex) {
                } catch (InterruptedException ex) {
                } catch (ExecutionException exx) {
                }
            } else {
                Toast.makeText(NewPublicacion.this, "Solo 149 caracteres para una publicacion", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception eee){
            Log.i("AAAY",eee.getMessage());
        }
    }

    public void cancelPost(View view){
        Intent cancelar = new Intent(mContext,MainActivity.class);
        Toast.makeText(NewPublicacion.this, "Cancelado", Toast.LENGTH_SHORT).show();
        mContext.startActivity(cancelar);
    }
}
