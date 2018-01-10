package edu.upc.eseiaat.pma.vutcompanion;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class TestActivity extends AppCompatActivity {
    private AlertDialog.Builder alert;
    public static String  TextKey = "TextKey";
    public static String  TextKey2 = "TextKey2";
    public static String  Num_Graph = "NumberOfGraphics";
    public static String  Nom;
    public static String  Data;

    private ListView lvSpinner;
    private Button addPlot;
    private int num_graphs = 1;

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        showGraphsList();
        popup();

    }

    private void popup() {
        alert = new AlertDialog.Builder(TestActivity.this);
        Context context = alert.getContext();
        final LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(100, 0, 100, 0);

        final TextView text1=new TextView(context);
        final EditText editText1 = new EditText(context);
        final TextView text2=new TextView(context);
        final EditText editText = new EditText(context);

        editText.setHint("Nom");
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        layout.addView(text1,layoutParams);

        layout.addView(editText,layoutParams);

        editText1.setHint("Data");
        editText1.setInputType(InputType.TYPE_CLASS_DATETIME);
        layout.addView(text2,layoutParams);
        layout.addView(editText1,layoutParams);
        alert.setTitle("Nova Entrada");
        alert.setView(layout);
        alert.setPositiveButton("Introduir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                Nom = editText.getText().toString();
                Data = editText1.getText().toString();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alert.show();
    }

    private void showGraphsList() {
        lvSpinner = (ListView) findViewById(R.id.listview_spinner);
        addPlot = (Button) findViewById(R.id.addButton);

        final ArrayList<String> mData = new ArrayList<>();
        mData.add("Test1");
        ArrayList<String> mSpinnerData = new ArrayList<>();
        final GraphSpinnerAdapter adapter = new GraphSpinnerAdapter(mData, mSpinnerData, this);

        addPlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mData.add("new");
                adapter.notifyDataSetChanged();
                lvSpinner.smoothScrollToPosition(mData.size()-1);
                num_graphs++;
            }
        });

        lvSpinner.setAdapter(adapter);

    }

    public void GoToResults(View view) {
        Intent intent = new Intent(TestActivity.this, ResultsListActivity.class);
        intent.putExtra(TextKey,Data);
        intent.putExtra(TextKey2,Nom);
        intent.putExtra(Num_Graph,num_graphs);
        startActivity(intent);
    }
}

