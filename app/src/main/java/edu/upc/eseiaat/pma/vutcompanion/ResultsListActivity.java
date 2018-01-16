package edu.upc.eseiaat.pma.vutcompanion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class ResultsListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<HashMap<String,String>> data;
    private ListView list;
    private SimpleAdapter adapter;
    public int  contador = 1;
    private static final int MAX_BYTES = 8000;
    private static final  String FILENAME = "shoppinglist.txt";
    public static String  TextKey = "TextKey";
    public static String  TextKey2 = "TextKey2";
    public static String  TextKey3 = "TextKey3";
    private String Data;
    private String Nom;
    public static String  Magnitud;
    private boolean gotExtra;

    private void writeItemList() {
        try {
            Log.e("eddie","escriptura11");
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            for (int i = 0; i < contador; i++) {

                HashMap<String, String> hash = data.get(i);
                String title = hash.get("title");
                String date = hash.get("date");

                String line = String.format("%s;%s\n", title, date);
                fos.write(line.getBytes());
                Log.e("eddie","escriptura");
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
        }catch(ArrayIndexOutOfBoundsException e){
            Log.i("edd", "ArrayIndexOutOfBoundsException: ");
        }
        catch (IOException e) {
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
        setContentView(R.layout.activity_nav_drawer_results_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try {
            Data = getIntent().getExtras().getString(ResultsListActivity.TextKey);
            Nom = getIntent().getExtras().getString(ResultsListActivity.TextKey2);
            Magnitud = getIntent().getExtras().getString(ResultsListActivity.TextKey3);
            gotExtra=true;
        }catch (NullPointerException e){gotExtra=false;}
        list = (ListView)findViewById(R.id.list1);

        data = new ArrayList<HashMap<String,String>>();

        adapter= new SimpleAdapter(this,data,android.R.layout.simple_list_item_2,new String[]{"title","date"},new int[]{android.R.id.text1,android.R.id.text2});
        list.setAdapter(adapter);
        readItemList();
        if (gotExtra){
            Log.e("eddie","bullshite");
            HashMap<String, String> datum2 = new HashMap<String, String>();
            datum2.put("title", Nom);
            datum2.put("date", Data+" "+Magnitud);
            data.add(datum2);
            adapter.notifyDataSetChanged();
            writeItemList();
        }

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
                intent.putExtra(TextKey,title);
                intent.putExtra(TextKey2,date);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer_results_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
            Intent account = new Intent(ResultsListActivity.this, HomeActivity.class);
            startActivity(account);

        } else if (id == R.id.test) {
            Intent account = new Intent(ResultsListActivity.this, TestActivity.class);
            startActivity(account);

        } else if (id == R.id.results) {


        } else if (id == R.id.account) {
            Intent account = new Intent(ResultsListActivity.this,MyAccountActivity.class);
            startActivity(account);
        } else if (id == R.id.admin) {
            Intent account = new Intent(ResultsListActivity.this, AdminActivity.class);
            startActivity(account);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void maybeRemoveItem(final int pos) {

        data.remove(pos);
        adapter.notifyDataSetChanged();

    }
}
