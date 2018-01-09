
package edu.upc.eseiaat.pma.vutcompanion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView title = (TextView) findViewById(R.id.resultTitle);

        String text = getIntent().getExtras().getString(ResultsListActivity.TextKey);
        title.setText(text);
    }
}

