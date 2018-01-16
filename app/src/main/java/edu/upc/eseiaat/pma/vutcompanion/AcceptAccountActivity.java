package edu.upc.eseiaat.pma.vutcompanion;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AcceptAccountActivity extends AppCompatActivity {

    private ArrayList<account> accountList;
    private ArrayList<String> idList;
    private accountAdapter adapter;
    ListView list;
    RequestQueue requestQueue;
    String acceptUrl = "http://192.168.1.40/manageAccounts/acceptAccount.php";
    String showUrl = "http://192.168.1.40/manageAccounts/showAccount.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_account);

        accountList = new ArrayList<>();
        idList = new ArrayList<>();
        list = (ListView) findViewById(R.id.list);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //Fem un request per a un objecte de tipus JSON, el qual ens retornarà un array JSON de totes les entrades de la base de dades
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,showUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray users = response.getJSONArray("users");   //guardem l'ARRAY

                    for (int i = 0; i < users.length(); i++){           //agafem per individual cada objecte, i n'extreiem la informació que volem
                        JSONObject user = users.getJSONObject(i);

                        if (user.getString("accepted").equals("0")){
                            String email = user.getString("email");
                            accountList.add(new account(email, false));
                            String id = user.getString("id");
                            idList.add(new String(id));                     //anem actualitzant la llista
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
                maybeAcceptItem(pos);
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
    private void maybeAcceptItem(final int pos) {       //Procedim a mostrar un alert dialog per a confirmar que l'usuari vol acceptar un usuari
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        builder.setMessage(String.format("Are you sure you want to accept '%s'?", accountList.get(pos).getText()));
        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                acceptFromDb(pos);
                accountList.remove(pos);                    //L'esborrem de la llista que es mostra per pantalla
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    private void acceptFromDb(final int pos) {              //Modifiquem el camp "accepted" de l'usuari, que passara a ser 1
                                                            //El metode es molt semblant al que fem servir per a rebre dades
        StringRequest request = new StringRequest(Request.Method.POST,acceptUrl, new Response.Listener<String>() {
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
                parameters.put("id",idList.get(pos));                   //Enviem la id de la persona que volem acceptar,
                                                                        //així la localitzem a la base de dades
                return parameters;
            }
        };
        requestQueue.add(request);
    }

    public void multipleAccept(View view) {
        for (int i = 0; i < accountList.size(); i++){
            if (accountList.get(i).isChecked()){
                maybeAcceptItem(i);
            }
        }
    }
}
