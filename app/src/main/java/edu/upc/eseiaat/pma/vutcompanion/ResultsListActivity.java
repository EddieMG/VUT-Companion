package edu.upc.eseiaat.pma.vutcompanion;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class ResultsListActivity extends AppCompatActivity {
    ArrayList<HashMap<String,String>>data;
    private ListView list;
    private SimpleAdapter adapter;
    public int  contador = 0;
    private static final int MAX_BYTES = 8000;
    private static final  String FILENAME = "shoppinglist.txt";
    public static String  TextKey = "TextKey";
    public static String  TextKey2 = "TextKey2";
    public static String  Num_Graph = "NumberOfGraphics";
    private String Data;
    private String Nom;
    private int num_graph;

    private void writeItemList() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            for (int i = 0; i < contador; i++) {

                HashMap<String, String> hash = data.get(i);
                String title = hash.get("title");
                String date = hash.get("date");

                String line = String.format("%s;%s\n", title, date);
                fos.write(line.getBytes());

            }

            fos.close(); // ... and close

        } catch (FileNotFoundException e) {
            Log.e("Eddie", "writeItemList: FileNotFoundException");
        } catch (IOException e) {
            Log.e("Eddie", "writeItemList: IOException ");

        } catch (IndexOutOfBoundsException e) {
            Log.e("Eddie", "IndexOutOfBoundsException ");
        }
    }



    private  void readItemList() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            byte[] buffer = new byte[MAX_BYTES];
            int nread = fis.read(buffer);
            if (nread > 0) {
                String content = new String(buffer, 0, nread);
                String[] lines = content.split("\n");
                for (String line : lines) {
                    String[] parts = line.split(";");
                    String title = parts[0];
                    String date = parts[1];
                    if (parts[1].isEmpty()){
                        return;
                    }
                    String[] preu_partit = parts[1].split(" ");
                    String valor_preu = preu_partit[0];

                    HashMap<String, String> datum2 = new HashMap<String, String>();
                    datum2.put("title", title);
                    datum2.put("date", date);

                    if (!title.isEmpty()) {
                        Log.e("Eddie", "datum");
                        data.add(datum2);
                        adapter.notifyDataSetChanged();
                        contador++;
                    }
                }
            }
            fis.close();
        } catch (FileNotFoundException e) {
            Log.i("edd", "readItemList: FileNotFoundException");
        } catch (IOException e) {
            Log.e("edd", "readItemList: IOException");
        }
    }

    @Override
    public void onStop() {

        super.onStop();

        writeItemList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_list);

        Data = getIntent().getExtras().getString(ResultsListActivity.TextKey);
        Nom = getIntent().getExtras().getString(ResultsListActivity.TextKey2);
        num_graph = getIntent().getExtras().getInt(ResultsListActivity.Num_Graph);
        list = (ListView)findViewById(R.id.list1);

        data = new ArrayList<HashMap<String,String>>();

        adapter= new SimpleAdapter(this,data,android.R.layout.simple_list_item_2,new String[]{"title","date"},new int[]{android.R.id.text1,android.R.id.text2});
        list.setAdapter(adapter);
        readItemList();
        HashMap<String,String> datum2 = new HashMap<String, String>();
        datum2.put("title",Nom);
        datum2.put("date",Data);
        data.add(datum2);
        adapter.notifyDataSetChanged();
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> list, View view, int pos, long id) {
                maybeRemoveItem(pos);
                return true;
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                HashMap<String, String> hash = data.get(pos);
                String date = hash.get("date");
                String title = hash.get("title");
                Intent intent = new Intent(ResultsListActivity.this, ResultsActivity.class);
                intent.putExtra(TextKey,title + " " + date);
                intent.putExtra(Num_Graph,num_graph);
                startActivity(intent);
            }
        });}




    private void maybeRemoveItem(final int pos) {

        data.remove(pos);
        adapter.notifyDataSetChanged();

    }


}

