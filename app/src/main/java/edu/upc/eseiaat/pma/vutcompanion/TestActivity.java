package edu.upc.eseiaat.pma.vutcompanion;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TestActivity extends AppCompatActivity {
    ArrayList<HashMap<String,String>>data;
    private String[] titleArray,subItemArray;
    private ListView list;
    private Button btn_add;
    private EditText edit_item;
    private SimpleAdapter adapter;
    //  private HashMap<String,String> datum;
    private AlertDialog.Builder alert;
    private Integer PreuTotal;
    private TextView Total;
    public int  contador = 0;
    private static final int MAX_BYTES = 8000;
    private static final  String FILENAME = "shoppinglist.txt";
    public static String  TextKey = "TextKey";
    private void writeItemList(){
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            for (int i = 0; i < contador; i++) {

                HashMap<String, String> hash = data.get(i);
                String title = hash.get("title");
                String preu = hash.get("preu");

                String line = String.format("%s;%s\n", title, preu);
                fos.write(line.getBytes());
                //s.writeChar('l');
            }

            fos.close(); // ... and close.
            fos.close();

        } catch (FileNotFoundException e) {
            Log.e("Eddie", "writeItemList: FileNotFoundException");
        } catch (IOException e) {
            Log.e("Eddie", "writeItemList: IOException ");
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
                    String preu = parts[1];
                    if (parts[1].isEmpty()){
                        return;
                    }
                    String[] preu_partit = parts[1].split(" ");
                    String valor_preu = preu_partit[0];

                    HashMap<String, String> datum2 = new HashMap<String, String>();
                    datum2.put("title", title);
                    datum2.put("preu", preu);

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

        // Assert.assertEquals(data.hashCode(), data.hashCode());
        //  Assert.assertEquals(data.toString(), data.toString());
        //  Assert.assertTrue(data.equals(data));
    }

    @Override
    public void onStop() {

        super.onStop();

        writeItemList();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        list = (ListView)findViewById(R.id.list2);
        btn_add = (Button)findViewById(R.id.addButton);
        data = new ArrayList<HashMap<String,String>>();




        adapter= new SimpleAdapter(this,data,android.R.layout.simple_list_item_2,new String[]{"title","preu"},new int[]{android.R.id.text1,android.R.id.text2});
        list.setAdapter(adapter);
        readItemList();

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> list, View view, int pos, long id) {
                maybeRemoveItem(pos);
                return true;
            }
        });
    /*    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                HashMap<String, String> hash = data.get(pos);
                String preu = hash.get("preu");
                String titol = hash.get("title");
                Intent intent = new Intent(ResultsListActivity.this, ResultsActivity.class);
                intent.putExtra(TextKey,preu+" "+titol);
                startActivity(intent);
            }
        });
*/
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
//                text1.setText("Nom");
                editText.setHint("Nom");
                editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

                layout.addView(text1,layoutParams);

                layout.addView(editText,layoutParams);

//                text2.setText("Preu");
                editText1.setHint("Data");
                editText1.setInputType(InputType.TYPE_CLASS_DATETIME);
                layout.addView(text2,layoutParams);
                layout.addView(editText1,layoutParams);
                alert.setTitle("Nova Entrada");
                alert.setView(layout);
                alert.setPositiveButton("Introduir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {



                        String Nom = editText.getText().toString();
                        String Preu = editText1.getText().toString();
                        HashMap<String,String> datum2 = new HashMap<String, String>();
                        datum2.put("title",Nom);
                        datum2.put("preu",Preu);

                        if (!Nom.isEmpty()){
                            Log.e("Eddie","datum");
                            data.add(datum2);
                            adapter.notifyDataSetChanged();
                            contador++;
                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

                alert.show();
            }
        });}


    private void maybeRemoveItem(final int pos) {

        data.remove(pos);
        adapter.notifyDataSetChanged();

    }


}
