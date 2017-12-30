package edu.upc.eseiaat.pma.vutcompanion;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class EliminateAccountActivity extends AppCompatActivity {

    private ArrayList<account> accountList;
    private accountAdapter adapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminate_account);

        accountList = new ArrayList<>();
        accountList.add(new account("Eduard",false));
        accountList.add(new account("Petao",false));
        accountList.add(new account("No",false));
        accountList.add(new account("Saps",false));
        accountList.add(new account("Llegir",false));
        accountList.add(new account("Entre",false));
        accountList.add(new account("Linies",false));


        list = (ListView) findViewById(R.id.list);
        adapter = new accountAdapter(this, android.R.layout.simple_list_item_1, accountList);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> list, View item, int pos, long id) {
                maybeRemoveItem(pos);
                return true;
            }
        });

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                accountList.get(pos).toggleChecked();
                adapter.notifyDataSetChanged();
            }
        });

    }
    private void maybeRemoveItem(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        builder.setMessage(String.format("Are you sure you want to remove '%s'?", accountList.get(pos).getText()));
        builder.setPositiveButton(R.string.erease, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                accountList.remove(pos);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }
}
