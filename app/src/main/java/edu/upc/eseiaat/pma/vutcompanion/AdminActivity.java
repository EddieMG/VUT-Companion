package edu.upc.eseiaat.pma.vutcompanion;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class AdminActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener {

    public static String EmailKey = "EmailKey";
    private static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer_admin);

        email = getIntent().getExtras().getString(EmailKey);

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer_admin, menu);
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
            Intent account = new Intent(AdminActivity.this, HomeActivity.class);
            account.putExtra(EmailKey,email);
            startActivity(account);

        } else if (id == R.id.test) {
            Intent account = new Intent(AdminActivity.this, TestActivity.class);
            account.putExtra(EmailKey,email);
            startActivity(account);

        } else if (id == R.id.results) {
            Intent account = new Intent(AdminActivity.this, ResultsListActivity.class);
            account.putExtra(EmailKey,email);
            startActivity(account);

        } else if (id == R.id.account) {
            Intent account = new Intent(AdminActivity.this, MyAccountActivity.class);
            account.putExtra(EmailKey,email);
            startActivity(account);
        } else if (id == R.id.admin) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Segons el botó que cliquem entrem en una activitat o en una altra

    public void acceptAccounts(View view) {
        Intent accept = new Intent(AdminActivity.this, AcceptAccountActivity.class);
        startActivity(accept);
    }

    public void eliminateAccounts(View view) {
        Intent eliminate = new Intent(AdminActivity.this, EliminateAccountActivity.class);
        startActivity(eliminate);
    }
}