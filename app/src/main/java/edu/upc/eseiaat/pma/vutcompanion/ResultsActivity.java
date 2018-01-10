
package edu.upc.eseiaat.pma.vutcompanion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ResultsActivity extends AppCompatActivity {

    private int num_graph;
    private ListView list;
    private int n = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView title = (TextView) findViewById(R.id.resultTitle);

        String text = getIntent().getExtras().getString(ResultsListActivity.TextKey);
        num_graph = getIntent().getExtras().getInt(ResultsListActivity.Num_Graph);
        title.setText(text);

        list = (ListView) findViewById(R.id.resultsList);
        final ArrayList<String> mData = new ArrayList<>();
        ArrayList<String> mSpinnerData = new ArrayList<>();
        final GraphSpinnerAdapter adapter = new GraphSpinnerAdapter(mData, mSpinnerData, this);

        while (n < num_graph){
            mData.add("new");
            adapter.notifyDataSetChanged();
            n++;
        }
        list.smoothScrollToPosition(mData.size()-1);
        list.setAdapter(adapter);
    }
}

