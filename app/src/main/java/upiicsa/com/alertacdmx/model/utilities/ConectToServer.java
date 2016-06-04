package upiicsa.com.alertacdmx.model.utilities;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by AndrewAlan on 20/05/2016.
 */
public class ConectToServer extends AsyncTask<URL, Void, String> {

    public String respuesta;
    public ConectToServer(){
        respuesta = new String();
    }
    private String connect(URL link) {

        HttpURLConnection conn=null;
        BufferedReader reader = null;

        try {


            conn = (HttpURLConnection) link.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            InputStream inputStream = conn.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            String resultado ;
            resultado = buffer.toString();
            return resultado;

        } catch (ProtocolException pex) {
            Log.e("URL protocol", pex.getMessage());
        } catch (IOException ioex) {
            Log.e("URL io", ioex.toString());
            return null;
        }
        finally {
            if(conn != null){
                conn.disconnect();
            }
            if(reader != null){
                try {
                    reader.close();
                }catch (IOException ioex2){
                    Log.i("Flujos reader",ioex2.getMessage());
                }

            }
        }
        return  null;
    }

    @Override
    protected String doInBackground(URL... params) {
        return connect(params[0]);
    }

  @Override
  protected void onPostExecute(String result) {
        if(!result.equals(""))
            this.respuesta = result;

  }

    }
