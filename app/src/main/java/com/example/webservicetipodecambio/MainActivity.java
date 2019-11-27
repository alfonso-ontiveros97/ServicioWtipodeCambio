package com.example.webservicetipodecambio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button btnCargar;
    TextView tvTipo, tvTipoFinal;
    EditText etInicio, etTermino;
    //Token que solicitaremos al banxico...
String token= "97e642a936bebade2315815917699d6fcfcc4bb39a83117b3260a5030288a1cd";
ListView list1;

    RequestQueue respuestaService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCargar= (Button) findViewById(R.id.btnCargar);
        tvTipo = (TextView) findViewById(R.id.TvtipoInicial);
        tvTipoFinal = (TextView) findViewById(R.id.TvtipoFinal);
        etInicio = (EditText) findViewById(R.id.etInicio);
        etTermino = (EditText) findViewById(R.id.etTermino);

        list1 = (ListView) findViewById(R.id.list1);

     btnCargar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             fecha();

         }
     });
    }
    public void fecha(){

//Pasar cajas de texto a una variable
        String fechaincio = etInicio.getText().toString();
        String fechaTermino = etTermino.getText().toString();
        //lo de las variables lo pasamos a fechaIncio, fechaTermino.
        BuscarTipodeCambio(fechaincio,fechaTermino);

    }

    public void BuscarTipodeCambio(String inicio, String fin){
//Crear al adaptador
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        list1.setAdapter(adapter); //pasarlo a ala lista

  //   String url="https://www.banxico.org.mx/SieAPIRest/service/v1/series/SF43718/datos/2019-11-20/2019-11-24?token=97e642a936bebade2315815917699d6fcfcc4bb39a83117b3260a5030288a1cd&mediaType=json";
        //Pasamos la fecha de inicio, fin y al igual que el token.
        String url="https://www.banxico.org.mx/SieAPIRest/service/v1/series/SF43718/datos/"+inicio+"/"+fin+"" +
                "?token="+token+"&mediaType=json";
     JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
         @Override
         public void onResponse(JSONObject response) {

            // tvTipo.setText("Response: " + response.toString());
        try{

            /*
            Es un JSON objeto dentro tiene otro objeto y por ultimo un jsonArray
            Donde posteriormente buscaremos otro objeto que queramos.
             */
            JSONObject json = response.getJSONObject("bmx"); //Acceder al json
            JSONArray json2 = json.getJSONArray("series"); //Entrar array de series
            JSONObject jsonObject = json2.getJSONObject(0); //Entrar id serie
            JSONArray jsonArray3 = jsonObject.getJSONArray("datos"); //Entrar al arreglo datos
            JSONObject jsonObject2= null;

        //El tamaño de resultados almacenarlos en un list view
            for (int i = 0; i<=jsonArray3.length()-1; i++){ // Indice 0
                // le pasamos el indice
                jsonObject2= jsonArray3.getJSONObject(i);
              //  tvTipo.setText(jsonObject2.getString("dato")); //Inicial
//Lo adaptamos al listview
                String resultado = jsonObject2.getString("dato");
                String fecha = jsonObject2.getString("fecha");
                adapter.add("| Fecha: "+ fecha +"| Tipo de Cambio: "+ resultado);

            }
            //tvTipo.setText("Response: " + json.toString());
        }catch (JSONException e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
         }
     }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {

         }
     });

        respuestaService= Volley.newRequestQueue(this);
        respuestaService.add(jsonObjectRequest);
    }

}
