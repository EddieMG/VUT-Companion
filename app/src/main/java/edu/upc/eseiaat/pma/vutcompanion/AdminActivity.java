package edu.upc.eseiaat.pma.vutcompanion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void acceptAccounts(View view) {
        Intent accept = new Intent(AdminActivity.this, AcceptAccountActivity.class);
        startActivity(accept);
    }

    public void eliminateAccounts(View view) {
        Intent eliminate = new Intent(AdminActivity.this, EliminateAccountActivity.class);
        startActivity(eliminate);
    }
}
