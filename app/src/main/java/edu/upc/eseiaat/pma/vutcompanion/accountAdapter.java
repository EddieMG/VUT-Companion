package edu.upc.eseiaat.pma.vutcompanion;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.List;



public class accountAdapter extends ArrayAdapter<account> {

    public accountAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View result = convertView;
        if(result == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            result = inflater.inflate(R.layout.item_layout, null);
        }
        CheckBox Item = (CheckBox)result.findViewById(R.id.item);
        account item = getItem(position);

        Item.setText(item.getText());
        Item.setChecked(item.isChecked());

        return result;
    }
}