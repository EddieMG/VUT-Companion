package edu.upc.eseiaat.pma.vutcompanion;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EliminateAccountActivity extends AppCompatActivity {

    private ArrayList<account> accountList;
    private accountAdapter adapter;
    ListView list;
    RequestQueue requestQueue;
    ArrayList<String> idList;
    String eliminateUrl = "http://192.168.43.65/manageAccounts/eliminateAccount.php";
    String showUrl = "http://192.168.43.65/manageAccounts/showAccount.php";

    //Com hem fet en el cas de l'activitat per a acceptar usuaris, per a eliminar-los fem exactament
    //el mateix, primer cridem una funció que ens permet llegir de la base de dades tots els usuaris
    // que han estat acceptats, i en fem un ListView.
    //Seguidament en comptes de modificar un paràmetre de l'usuari amb la id especificada, per a aquest
    //cas n'eliminem tota la informació. El codi dins de l'activitat per a dur a terme aquesta funció
    //és el mateix pràcticament, i el que realment marca la diferència a l'hora d'escriure/esborrar a
    //la base de dades són els arxius PHP que cridem amb la direcció de dalt (http://192.168.1.40/manage/eliminateAccount.php)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminate_account);

        accountList = new ArrayList<>();
        idList = new ArrayList<>();

        list = (ListView) findViewById(R.id.list);
        requestQueue = Volley.newRequestQueue(getApplicationContext());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,showUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray users = response.getJSONArray("users");

                    for (int i = 0; i < users.length(); i++){
                        JSONObject user = users.getJSONObject(i);

                        if (user.getString("accepted").equals("1")){
                            String email = user.getString("email");
                            accountList.add(new account(email, false));
                            String id = user.getString("id");
                            idList.add(new String(id));
                            adapter.notifyDataSetChanged();
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

        if (!accountList.isEmpty()){

        }

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
                removeFromDb(pos);
                accountList.remove(pos);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    private void removeFromDb(final int pos) {

        StringRequest request = new StringRequest(Request.Method.POST,eliminateUrl, new Response.Listener<String>() {
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
                parameters.put("id",idList.get(pos));

                return parameters;
            }
        };
        requestQueue.add(request);
    }

    public void multipleEliminate(View view) {
        for (int i = 0; i < accountList.size(); i++){
            if (accountList.get(i).isChecked()){
                maybeRemoveItem(i);
            }
        }
    }
}
