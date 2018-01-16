package edu.upc.eseiaat.pma.vutcompanion;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener {

    private String email;
    private boolean isAdmin;
    String showUrl = "http://192.168.1.40/manageAccounts/showAccount.php";
    RequestQueue requestQueue;
    public static String EmailKey = "EmailKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // TODO: 15/1/2018 FER QUE QUALSEVOL INTENT CANVII EL VALOR DE email. 
        email = getIntent().getExtras().getString(LoginActivity.TextKey);

        checkIfAdmin(email);

    }

    private void checkIfAdmin(final String email) {
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,showUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray users = response.getJSONArray("users");

                    for (int i = 0; i < users.length(); i++){
                        JSONObject user = users.getJSONObject(i);

                        if (user.getString("email").equals(email) && user.getString("admin").equals("1")){
                           isAdmin = true;
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
        getMenuInflater().inflate(R.menu.nav_drawer_home, menu);
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
        } else if (id == R.id.test) {
            Intent account = new Intent(HomeActivity.this, TestActivity.class);
            startActivity(account);

        } else if (id == R.id.results) {
            Intent account = new Intent(HomeActivity.this, ResultsListActivity.class);
            startActivity(account);

        } else if (id == R.id.account) {
            Intent account = new Intent(HomeActivity.this, MyAccountActivity.class);
            startActivity(account);
        } else if (id == R.id.admin) {
            if (isAdmin){
                Intent account = new Intent(HomeActivity.this, AdminActivity.class);
                startActivity(account);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void gotoMyAccount(View view) {
        Intent account = new Intent(HomeActivity.this, MyAccountActivity.class);
        account.putExtra(EmailKey,email);
        startActivity(account);
    }

    public void gotoTest(View view) {
        Intent test = new Intent(HomeActivity.this,TestActivity.class);
        startActivity(test);
    }

    public void gotoResults(View view) {
        Intent results = new Intent(HomeActivity.this, ResultsListActivity.class);
        startActivity(results);
    }

    public void gotoAdmin(View view) {
        if (isAdmin){
            Intent admin = new Intent(HomeActivity.this, AdminActivity.class);
            startActivity(admin);
        }else {
            Toast.makeText(getApplicationContext(), "You are not an administrator", Toast.LENGTH_SHORT).show();
        }
    }
}