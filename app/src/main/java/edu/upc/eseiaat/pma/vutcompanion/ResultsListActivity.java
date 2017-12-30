package edu.upc.eseiaat.pma.vutcompanion;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultsListActivity extends AppCompatActivity {

    private ArrayList<String> resultList;
    private ArrayAdapter<String> adapter;
    private ListView list;
    public static String  TextKey = "TextKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_list);

        list = (ListView) findViewById(R.id.list);

        resultList = new ArrayList<>();
        resultList.add("Test 0          6/09/1996");
        resultList.add("Test 1          17/09/1996");
        resultList.add("Test 2          22/09/2017");
        resultList.add("Test 3          30/10/2017");
        resultList.add("Test 4          11/11/2017");
        resultList.add("Test 5          12/11/2017");
        resultList.add("Test 6          8/12/2017");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resultList);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {

                Intent intent = new Intent(ResultsListActivity.this, ResultsActivity.class);
                intent.putExtra(TextKey, resultList.get(pos));
                startActivity(intent);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> list, View item, int pos, long id) {
                maybeRemoveItem(pos);
                return true;
            }
        });

        list.setAdapter(adapter);
    }

    private void maybeRemoveItem(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        builder.setMessage(String.format("Are you sure you want to remove '%s'?", resultList.get(pos)));
        builder.setPositiveButton(R.string.erease, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                resultList.remove(pos);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }
}
