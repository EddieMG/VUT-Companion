package edu.upc.eseiaat.pma.vutcompanion;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static java.lang.System.out;


public class TestActivity extends AppCompatActivity


        implements NavigationView.OnNavigationItemSelectedListener {




    private AlertDialog.Builder alert;
    public static String  TextKey = "TextKey";
    public static String  TextKey2 = "TextKey2";
    public static String  TextKey3 = "TextKey3";
    public static String  Nom;
    public static String  Data;
    public static String  Magnitud;
    String showUrl ="http://192.168.1.40/test_data/showData.php";
    public RequestQueue requestQueue;
    public int a;
    public int b;
    public LineGraphSeries<DataPoint> series;



    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer_test);

        popup();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

        getMenuInflater().inflate(R.menu.nav_drawer_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.home) {

            Intent account = new Intent(TestActivity.this, HomeActivity.class);
            startActivity(account);
        } else if (id == R.id.test) {


        } else if (id == R.id.results) {
            Intent account = new Intent(TestActivity.this, ResultsListActivity.class);
            startActivity(account);

        } else if (id == R.id.account) {
            Intent account = new Intent(TestActivity.this, MyAccountActivity.class);
            startActivity(account);
        } else if (id == R.id.admin) {
            Intent account = new Intent(TestActivity.this, AdminActivity.class);
            startActivity(account);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void popup() {              //Creem un popup que solicitarà les dades necessaries per
                                        // realitzar les operacions de l'activitat, en aquest
                                        //cas obtenim les string per posar un titol una data i unna magnitud.
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
        final EditText editText2 = new EditText(context);
        final TextView text3=new TextView(context);
        final EditText editText3 = new EditText(context);
        editText2.setHint("Nom");
        editText2.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        layout.addView(text1,layoutParams);

        layout.addView(editText2,layoutParams);

        editText1.setHint("Data");
        editText1.setInputType(InputType.TYPE_CLASS_DATETIME);

        layout.addView(text2,layoutParams);
        layout.addView(editText1,layoutParams);

        editText3.setHint("Magnitud");
        editText3.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        layout.addView(text3,layoutParams);

        layout.addView(editText3,layoutParams);
        alert.setTitle("Nova Entrada");
        alert.setView(layout);
        alert.setPositiveButton("Introduir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                Nom = editText2.getText().toString();
                Data = editText1.getText().toString();
                Magnitud= editText3.getText().toString();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alert.show();
    }

    public void GoToResults(View view) {                                            //Et porta a la REsultsListActivity emmagatzmemant la informació necessaria.
        Intent intent = new Intent(TestActivity.this, ResultsListActivity.class);
        intent.putExtra(TextKey,Data);
        intent.putExtra(TextKey2,Nom);
        intent.putExtra(TextKey3,Magnitud);
        startActivity(intent);
    }

    public void addgraph(View view) {


        READ(Magnitud);

    }


    public void READ( final String Magnitud){                       //Llegim del servidor les dades de l'experminent en funció de la magnitud que carreguem de la ResultsListActivity.
            requestQueue = Volley.newRequestQueue(getApplicationContext());


    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,showUrl, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

            try {
                JSONArray students = response.getJSONArray("students");

                DataPoint[] values = new DataPoint[students.length()];//Creem un datapoint amb la length de la taula del servidor
                for (int i=0 ; i < students.length(); i++){
                    JSONObject data = students.getJSONObject(i);

                 
                    a=data.getInt("time_series");
                    b=data.getInt(Magnitud);                //Dins del for nem afegint als datapoints els valors de la taula del servidor
                    DataPoint v = new DataPoint(a, b);
                    values[i] = v;

                }

                series = new LineGraphSeries<DataPoint>(values);            //Creem la Linear Graph Series
                GraphView graph = (GraphView) findViewById(R.id.graph);
                graph.addSeries(series);                                     //Grafiquem la serie



            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("EDDI","exeption");
        } catch (IndexOutOfBoundsException e) {
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

}}

