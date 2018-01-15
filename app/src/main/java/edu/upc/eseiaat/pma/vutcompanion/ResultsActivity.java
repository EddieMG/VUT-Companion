
package edu.upc.eseiaat.pma.vutcompanion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class ResultsActivity extends AppCompatActivity {

    LineGraphSeries<DataPoint> series;

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
        readItemList(String.format(text+dateRecived));

    }
    private  void readItemList(final  String FILENAME) {
                try {
                    Log.i("edd", "readItemList");
                       FileInputStream fis = openFileInput(FILENAME);
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME));
                    series=(LineGraphSeries<DataPoint>)ois.readObject();
                    ois.close();
                    GraphView graph = (GraphView) findViewById(R.id.graph);
                    graph.addSeries(series);

                    } catch (FileNotFoundException e) {
                        Log.i("edd", "readItemList: FileNotFoundException");
                    } catch (IOException e) {
                        Log.e("edd", "readItemList: IOException");
                   } catch(ClassNotFoundException e) {
                        Log.e("edd", "classnotfound");
    }
}}

