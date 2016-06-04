package upiicsa.com.alertacdmx.model.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import upiicsa.com.alertacdmx.R;

/**
 * Created by AndrewAlan on 02/06/2016.
 */
public class CustomAdapter extends BaseAdapter {
    // Declare Variables
    private Context context;
    //private ArrayList<Bitmap> imgUsuario;
    private ArrayList<String> fechasPbl;
    private ArrayList<String> resenaPbl;//listo
    private ArrayList<String> namesUsuario; //listo
    private LayoutInflater inflater;

    public CustomAdapter(Context context, ArrayList<String> fechasPbl,
                         ArrayList<String> resenaPbl, ArrayList<String> namesUsuario) {
        this.context = context;
        //this.imgUsuario = imgUsuario;
        this.fechasPbl = fechasPbl;
        this.resenaPbl = resenaPbl;
        this.namesUsuario = namesUsuario;
    }

    @Override
    public int getCount() {
        return namesUsuario.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView txtName,txtResena,txtFecha;
        //ImageView imgImg;


        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.list_item_publicacion, parent, false);

        // Locate the TextViews in listview_item.xml
        txtName = (TextView) itemView.findViewById(R.id.list_row_nombre);
        txtResena = (TextView) itemView.findViewById(R.id.list_row_resena);
        txtFecha = (TextView) itemView.findViewById(R.id.list_row_fecha);
        //imgImg = (ImageView) itemView.findViewById(R.id.list_row_image);

        // Capture position and set to the TextViews
        txtName.setText(namesUsuario.get(position));
        txtResena.setText(this.resenaPbl.get(position));
        txtFecha.setText(this.fechasPbl.get(position));
        //imgImg.setImageBitmap(imgUsuario.get(position));

        return itemView;
    }
}
