package edu.upc.eseiaat.pma.vutcompanion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void gotoMyAccount(View view) {
        Intent account = new Intent(HomeActivity.this, MyAccountActivity.class);
        startActivity(account);
    }

    public void gotoTest(View view) {
        Intent test = new Intent(HomeActivity.this, TestActivity.class);
        startActivity(test);
    }

    public void gotoResults(View view) {
        Intent results = new Intent(HomeActivity.this, ResultsListActivity.class);
        startActivity(results);
    }

    public void gotoAdmin(View view) {
        Intent admin = new Intent(HomeActivity.this, AdminActivity.class);
        startActivity(admin);
    }
}
