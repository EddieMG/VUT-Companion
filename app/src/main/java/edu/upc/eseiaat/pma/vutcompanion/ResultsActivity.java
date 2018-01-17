
package edu.upc.eseiaat.pma.vutcompanion;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ResultsActivity extends AppCompatActivity {

    LineGraphSeries<DataPoint> series;
    String showUrl ="http://192.168.43.65/test_data/showData.php";
    public RequestQueue requestQueue;
    public int a;
    public int b;
    public static String EmailKey = "EmailKey";
    private static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {                //Creem una activitat per llegir els resultats d'un experiment al servidor.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        email = getIntent().getExtras().getString(HomeActivity.EmailKey);

        TextView title = (TextView) findViewById(R.id.resultTitle);
        TextView date = (TextView) findViewById(R.id.dateTextView);

        String text = getIntent().getExtras().getString(ResultsListActivity.TextKey);
        String dateRecived = getIntent().getExtras().getString(ResultsListActivity.TextKey2);

        title.setText(text);
        date.setText(dateRecived);

        READ(dateRecived);
    }

public void READ(final String Magnitud){                //Llegim del servidor les dades de l'experminent en funció de la magnitud que carreguem de la ResultsListActivity.

    String[] lines = Magnitud.split(" ");               //Separem de lstring el valor que ens interessa.
    final String Magnitud1=lines[1];
    requestQueue = Volley.newRequestQueue(getApplicationContext());

//Fem un request per a un objecte de tipus JSON, el qual ens retornarà un array JSON de totes les entrades de la base de dades
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,showUrl, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {   //Fem la request amb Json al server

            try {
                JSONArray students = response.getJSONArray("students");


                DataPoint[] values = new DataPoint[students.length()];   //Creem un datapoint amb la length de la taula del servidor
                for (int i=0 ; i < students.length(); i++){
                    JSONObject data = students.getJSONObject(i);

                    a=data.getInt("time_series");                       //Dins del for nem afegint als datapoints els valors de la taula del servidor
                    b=data.getInt(Magnitud1);
                    DataPoint v = new DataPoint(a, b);
                    values[i] = v;
                }
                series = new LineGraphSeries<DataPoint>(values);                //Creem la Linear Graph Series
                GraphView graph = (GraphView) findViewById(R.id.graph);
                graph.addSeries(series);                                        //Grafiquem la serie



            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("EDDI","exeption");
            }  catch (IndexOutOfBoundsException e) {
                Log.e("Eddie", "IndexOutOfBoundsException ");
            }


        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            System.out.println(error);
        }
    });
    requestQueue.add(jsonObjectRequest);

}
}

