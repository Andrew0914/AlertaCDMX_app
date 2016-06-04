package upiicsa.com.alertacdmx.model.usuarios;

import android.net.Uri;
import android.util.Log;
import android.webkit.HttpAuthHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import upiicsa.com.alertacdmx.model.utilities.ConectToServer;

/**
 * Created by AndrewAlan on 17/05/2016.
 */
public class Usuarios {

    public String askFirstimeURL(String... parametros) {

        final String baseUrl = "http://www.alertacdmx.com/Requests.php"; //base del url no cambia

        Uri objectURL = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter("id_request", "00")
                .appendQueryParameter("user_id", parametros[0])
                .appendQueryParameter("user_email", parametros[1])
                .appendQueryParameter("user_displayName", parametros[2])
                .appendQueryParameter("user_image", parametros[3]).build();

        try {
            URL linkToFirst = new URL(objectURL.toString());
            ConectToServer conn = new ConectToServer();
            conn.execute(linkToFirst);
            JSONObject jsonObject = new JSONObject(conn.get().trim());
            switch (jsonObject.get("log").toString()) {
                case "in":
                    return "in";
                case "up":
                    return "up";
                case "no":
                    return "no";
                default:
                    return "no";
            }

        } catch (java.net.MalformedURLException ex) {
            Log.i("MalformedURL", ex.getMessage());
        } catch (InterruptedException iex) {
            Log.i("Interruption", iex.getMessage());
        } catch (ExecutionException eex) {
            Log.i("Execution", eex.getMessage());
        } catch (JSONException jex) {
            Log.i("JSON", jex.getMessage());
        }
        return "no";

    }


}