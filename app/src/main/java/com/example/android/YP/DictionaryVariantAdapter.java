package com.example.android.YP;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter для реализации listView по классу DictionaryVariant
 */

public class DictionaryVariantAdapter extends ArrayAdapter<DictionaryVariant> {


    public DictionaryVariantAdapter(Context context, int resource, List<DictionaryVariant> object){
        super(context,resource,object);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = ((Activity) getContext())
                    .getLayoutInflater()
                    .inflate(R.layout.translate_text, parent, false);
        }

        final DictionaryVariant currentTranslation = getItem(position);

        TextView typeOfSpeach = (TextView) convertView.findViewById(R.id.typeOfSpeach);
        TextView num = (TextView) convertView.findViewById(R.id.num);
        TextView variant =(TextView)convertView.findViewById(R.id.variant);
        TextView meaning = (TextView)convertView.findViewById(R.id.meaning);

        typeOfSpeach.setText(currentTranslation.getTypeOfSpeach());
        variant.setText(currentTranslation.getVariant());
        num.setText("1");
        meaning.setText(currentTranslation.getMeaning());



        return convertView;

    }
}
