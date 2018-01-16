
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

    String showUrl ="http://192.168.1.40/test_data/showData.php";
    public RequestQueue requestQueue;
    public int a;
    public int b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView title = (TextView) findViewById(R.id.resultTitle);
        TextView date = (TextView) findViewById(R.id.dateTextView);

        String text = getIntent().getExtras().getString(ResultsListActivity.TextKey);
        String dateReceived = getIntent().getExtras().getString(ResultsListActivity.TextKey2);

        title.setText(text);
        date.setText(dateReceived);

        READ(dateReceived);
    }

public void READ(final String Magnitud){

    String[] lines = Magnitud.split(" ");
    final String Magnitud1=lines[1];
    requestQueue = Volley.newRequestQueue(getApplicationContext());


    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,showUrl, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

            try {
                JSONArray students = response.getJSONArray("students");

                DataPoint[] values = new DataPoint[students.length()];
                for (int i=0 ; i < students.length(); i++){
                    JSONObject data = students.getJSONObject(i);

                    a=data.getInt("time_series");
                    b=data.getInt(Magnitud1);
                    DataPoint v = new DataPoint(a, b);
                    values[i] = v;
                }
                series = new LineGraphSeries<DataPoint>(values);
                GraphView graph = (GraphView) findViewById(R.id.graph);
                graph.addSeries(series);

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

