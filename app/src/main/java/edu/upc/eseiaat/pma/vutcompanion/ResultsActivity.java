
package edu.upc.eseiaat.pma.vutcompanion;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
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
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class ResultsActivity extends AppCompatActivity {

    LineGraphSeries<DataPoint> series;
    private static final int MAX_BYTES = 8000;
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
        String dateRecived = getIntent().getExtras().getString(ResultsListActivity.TextKey2);

        title.setText(text);
        date.setText(dateRecived);
//        readItemList(String.format(text+dateRecived));
        READ(dateRecived);
    }
//    private  void readItemList(final  String FILENAME) {
//        try {
//            Log.i("edd", "readItemList");
//            FileInputStream fis = openFileInput(FILENAME);
//            byte[] buffer = new byte[MAX_BYTES];
//            int nread = fis.read(buffer);
//            int i=0;
//            DataPoint[] values = new DataPoint[25];
//            if (nread > 0) {
//                String content = new String(buffer, 0, nread);
//                String[] lines = content.split("\n");
//                for (String line : lines) {
//                    String[] parts = line.split(";");
//                    String c = parts[0];
//                    String d = parts[1];
//                    DataPoint v = new DataPoint(Integer.parseInt(c), Integer.parseInt(d));
//                    values[i] = v;
//                    i++;
//                    if (parts[1].isEmpty()) {
//                        return;
//
//                    }
//                }
//            }
//            series = new LineGraphSeries<DataPoint>(values);
//        } catch (IOException e) {
//                Log.e("edd", "readItemList:  results activity");
//        }
//
//
//    }
public void READ(final String Magnitud){

    String[] lines = Magnitud.split(" ");
    final String Magnitud1=lines[1];
    requestQueue = Volley.newRequestQueue(getApplicationContext());

    Log.i("EDDI","Pas 0");
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,showUrl, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.i("EDDI","Pas 1");
            try {
                JSONArray students = response.getJSONArray("students");
                Log.i("EDI","Pas 2");

                DataPoint[] values = new DataPoint[students.length()];
                for (int i=0 ; i < students.length(); i++){
                    JSONObject data = students.getJSONObject(i);

                    Log.i("EDDI","Pas 3");
                    a=data.getInt("time_series");
                    b=data.getInt(Magnitud1);
                    DataPoint v = new DataPoint(a, b);
                    values[i] = v;
                }
                series = new LineGraphSeries<DataPoint>(values);
                GraphView graph = (GraphView) findViewById(R.id.graph);
                graph.addSeries(series);
//               FET=true;

                Log.e("EDI","FET");
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

