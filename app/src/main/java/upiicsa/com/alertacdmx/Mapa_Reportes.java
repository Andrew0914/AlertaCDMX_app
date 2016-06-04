package upiicsa.com.alertacdmx;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import upiicsa.com.alertacdmx.model.utilities.ConectToServer;

public class Mapa_Reportes extends AppCompatActivity {

    private  String[] mySession;
    private  Intent inicio;
    private Context mContext;
    private static final int CODE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa__reportes);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new MapaFragment());

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.tituloMenuBar);
        setSupportActionBar(myToolbar);
        inicio = getIntent();
        mySession = inicio.getStringArrayExtra("sesion");
        mContext = this;

    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                Intent regreso = new Intent(mContext,MainActivity.class);
                startActivityForResult(regreso,CODE);
                return false;
            default:
                return false;
        }
    }*/

    public  class MapaFragment extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener,GoogleMap.OnMarkerClickListener{
        private GoogleMap mMap;
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            // Unbicacion y zoom inicia
            LatLngBounds posicion = new LatLngBounds(new LatLng(19.400340, -99.136825),new LatLng(19.400826, -99.058205));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion.getCenter(),15)); //rango 2.0 a 21.0
            mMap.setOnMapLongClickListener(this);
            mMap.setOnMarkerClickListener(this);
            putMarkers();

        }

        @Override
        public void onMapLongClick(LatLng latLng) {
            //colocar un marcador co envento de click largo en el mapa recibe coordenadas el punto p
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("0")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            toPost(mySession,latLng);
        }

        @Override
        public boolean onMarkerClick(Marker marker) {
            toPost(mySession,marker.getPosition());
            return false;
        }







        public void putMarkers(){ //llenar el mapa de marcadores en la base de datos
            BitmapDescriptor rojo =  BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            BitmapDescriptor amarillo =  BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            BitmapDescriptor verde =  BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            //Llenado de los marcadores
            final String baseUrl = "http://www.alertacdmx.com/Requests.php";
            ConectToServer conn = new ConectToServer();
            Uri objectURL = Uri.parse(baseUrl).buildUpon()
                    .appendQueryParameter("id_request", "02").build();
            try{
                URL linkMarkers = new URL(objectURL.toString().toString());
                conn.execute(linkMarkers);
                JSONObject json = new JSONObject(conn.get().trim());
                JSONArray jsonArray = json.getJSONArray("marcadores");

                for(int objs=0;objs<jsonArray.length();objs++){
                    double latitud = jsonArray.getJSONObject(objs).getDouble("mrk_coordenada_latitud");
                    double longitud = jsonArray.getJSONObject(objs).getDouble("mrk_coordenada_longitud");
                    int incidencias = jsonArray.getJSONObject(objs).getInt("mrk_incidencias");

                    if(incidencias >=1 && incidencias<=2)// desicion de color incidencias niveles
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitud,longitud))
                            .title("Incidencias: "+incidencias)
                            .icon(verde));
                    if(incidencias >=2 && incidencias<=3)
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitud,longitud))
                                .title("Incidencias: "+incidencias)
                                .icon(amarillo));
                    if(incidencias >=3)
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitud,longitud))
                                .title("Incidencias: "+incidencias)
                                .icon(rojo));

                }

            }catch(MalformedURLException urlEx){
                Log.e(urlEx.getClass().toString(),urlEx.getMessage());
            }
            catch (InterruptedException iex){
                Log.e(iex.getClass().toString(),iex.getMessage());
            }
            catch (ExecutionException exx){
                Log.e(exx.getClass().toString(),exx.getMessage());
            }
            catch(JSONException jex){
                Log.e(jex.getClass().toString(),jex.getMessage());
            }
        }

        public void  toPost(String[] dataSession , LatLng position){

            Intent publicaciones = new Intent(mContext,MainPublicacion.class);
            publicaciones.putExtra("session",dataSession);
            publicaciones.putExtra("latitud",position.latitude);
            publicaciones.putExtra("longitud",position.longitude);

            mContext.startActivity(publicaciones);
        }
    }
}
