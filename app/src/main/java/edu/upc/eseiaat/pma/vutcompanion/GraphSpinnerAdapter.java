package edu.upc.eseiaat.pma.vutcompanion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import java.util.ArrayList;


/**
 * Created by ferra_000 on 9/1/2018.
 */

// TODO: 9/1/2018 ARA MATEIX PER PANTALLA SURT EL QUE VOLEM, PERÒ A LA QUE FEM SCROLL ES PERD TOTA LA INFORMACIÓ QUE HAVIEM CANVIAT DURANT L'US DE L'APP.

//ENTENC QUE HE COPIAT L'ESTRUCTURA D'UN EXEMPLE DE STACKOVERFLOW QUE HI HAVIA SPINNER DINS LA LLISTA, PERÒ CREC Q AQUESTA CLASSES ES PODRIA DIVIDIR EN LES
//QUE FEIEM AMB EL PAUEK, QUE EREN UNA PER A L'ITEM, I L'ALTRE PER A L'ADAPTADOR...PERÒ NOSE, AIXÒ DE QUE ES VAGI ESBORRANT LA INFORMACIÓ CANVIADA PORTO ESTONA
//INTENTANT VEURE COM FER-HO AMB EL VIDEO DE SHOPPING LIST 2, QUE LI PASSA EL MATEIX AMB ELS CHECKBOX.

public class GraphSpinnerAdapter extends BaseAdapter {

    private ArrayList<String> mSpinnerItems;
    private ArrayList<String> mData;
    private Context mContext;

    public GraphSpinnerAdapter(ArrayList<String> data,ArrayList<String> spinnerItems, Context context) {
        mData = data;
        mContext = context;
        mSpinnerItems = spinnerItems;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.graph_spinner_item,null);

            /*LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.graph_spinner_item, null);*/
        }

        Spinner spinner = (Spinner) view.findViewById(R.id.row_item_spinner);

        //textView.setText(mData.get(position));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,R.array.options,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i);
                // TODO: 9/1/2018 FER QUE A PARTIR DE L'ITEM SELECCIONAT ES CANVII LES DADES QUE ESTEM AGAFANT DEL SERVIDOR.
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }
}