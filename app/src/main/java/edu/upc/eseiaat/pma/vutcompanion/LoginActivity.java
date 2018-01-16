package edu.upc.eseiaat.pma.vutcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText email,password;
    private Button sign_in_register;
    private RequestQueue requestQueue;
    private static final String URL = "http://192.168.1.40/login/user_control.php";
    private StringRequest request;
    public static String EmailKey = "EmailKey";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        sign_in_register = (Button) findViewById(R.id.email_sign_in_button);

        requestQueue = Volley.newRequestQueue(this);
        //Com hem fet en altres casos anterior, per a comprovar si les dades
        //si l'usuari pertany a la base de dades, realitzem una petició al
        //servidor, el qual, a través del codi PHP descrit a l'arxiu ubicat en
        //"http://192.168.1.40/login/user_control.php", a partir del email i
        //la contrassenya donarà 3 resultats possibles.
        //El 1r és que l'usuari es trobi a la base de dades amb la contrassenya escrita, per tant podrà accedir a l'app
        //La 2a és que l'usuari no es trobi a la base de dades amb l'email, situació en la que es crearà un usuari nou,
        //que encara pendent d'acceptació no podrà entrar.
        //Finalment hi ha l'opció de que l'usuari es trobi dins la base de dades però la contrassenya no coincideixi, o que
        //totes dues coses coincideixin, però que la conta no hagi sigut aprovada encara, situació en la qual l'usuari no podrà
        //entrar.
        //En tots els casos sempre es notifica a l'usuari del que està succeint a través d'un Toast

        sign_in_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.names().get(0).equals("success")){
                                Toast.makeText(getApplicationContext(),jsonObject.getString("success"),Toast.LENGTH_SHORT).show();
                                Intent account = new Intent(LoginActivity.this, HomeActivity.class);
                                account.putExtra(EmailKey,email.getText().toString());
                                startActivity(account);
                            }else {
                                if(jsonObject.names().get(0).equals("requested")){
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("requested"), Toast.LENGTH_SHORT).show();
                                }
                                Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> hashMap = new HashMap<String, String>();
                        hashMap.put("email",email.getText().toString());
                        hashMap.put("password",password.getText().toString());

                        return hashMap;
                    }
                };

                requestQueue.add(request);
            }
        });
    }
}