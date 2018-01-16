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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyAccountActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private AlertDialog.Builder alert;
    public static String  password1;
    public static String  password2;
    private static String email;
    private TextView emailText;
    String showUrl = "http://192.168.1.40/manageAccounts/showAccount.php";
    String passwordUrl = "http://192.168.1.40/manageAccounts/passwordChange.php";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {        //En el metode onCreate implementem la toolbar,el menú i la navigation bar i recuperem les dades de la HomeActivity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer_my_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        email = getIntent().getExtras().getString(HomeActivity.EmailKey);
        emailText = (TextView) findViewById(R.id.emailText);

        emailText.setText(email);


    }

    private void popup() {                                              //Creem un popup que solicitarà les dades necessaries per realitzar les operacions de l'activitat, en aquest
                                                                        //cas obtenim les string per canviar la constrasenya.
        alert = new AlertDialog.Builder(MyAccountActivity.this);
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

        editText.setHint("Password");

        layout.addView(text1,layoutParams);

        layout.addView(editText,layoutParams);

        editText1.setHint("Confirm password");
        layout.addView(text2,layoutParams);
        layout.addView(editText1,layoutParams);
        alert.setTitle("Change password");
        alert.setView(layout);
        alert.setPositiveButton("Introduce", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                password1 = editText.getText().toString();
                password2 = editText1.getText().toString();

                if (password1.equals(password2)){
                    changePassword(email);
                }else {
                    Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                    editText.setText("");
                    editText1.setText("");
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

    private void changePassword(final String email) {                   //Comunicació amb el servidor per al canvi de contrasenya
        requestQueue = Volley.newRequestQueue(getApplicationContext());
//Fem un request per a un objecte de tipus JSON, el qual ens retornarà un array JSON de totes les entrades de la base de dades
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,showUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray users = response.getJSONArray("users");

                    for (int i = 0; i < users.length(); i++){
                        JSONObject user = users.getJSONObject(i);

                        if (user.getString("email").equals(email)){
                            String id = user.getString("id");
                            change(id);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void change(final String id) {                                          //Canvi del valor de password al servidor.
        StringRequest request = new StringRequest(Request.Method.POST,passwordUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String, String>();
                parameters.put("id",id);
                parameters.put("password",password1);

                return parameters;
            }
        };
        requestQueue.add(request);
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
        getMenuInflater().inflate(R.menu.nav_drawer_my_account, menu);
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
            Intent account = new Intent(MyAccountActivity.this, HomeActivity.class);
            startActivity(account);

        } else if (id == R.id.test) {
            Intent account = new Intent(MyAccountActivity.this, TestActivity.class);
            startActivity(account);

        } else if (id == R.id.results) {
            Intent account = new Intent(MyAccountActivity.this, ResultsListActivity.class);
            startActivity(account);

        } else if (id == R.id.account) {

        } else if (id == R.id.admin) {
            Intent account = new Intent(MyAccountActivity.this, AdminActivity.class);
            startActivity(account);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setpassword(View view) {
        popup();
    }
}