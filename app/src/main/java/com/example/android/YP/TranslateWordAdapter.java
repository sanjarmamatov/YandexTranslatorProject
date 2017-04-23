package com.example.android.YP;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

//Адаптер для реализации listView по классу TranslateWord

public class TranslateWordAdapter extends ArrayAdapter<TranslateWord> {
    /**
     * @param flag, определяет отмечено ли слово как избранное
     */
    boolean flag= true;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef2 = database.getReference("favoriteWord");



    public TranslateWordAdapter(Context context, int resource, List<TranslateWord> object){
        super(context,resource,object);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = ((Activity) getContext())
                    .getLayoutInflater()
                    .inflate(R.layout.translate_text, parent, false);
        }

        final TranslateWord currentTranslation = getItem(position);

        //View элементы ListView, отображающий историю перевода
        TextView upTextToTranslate = (TextView) convertView.findViewById(R.id.textToTranslate);
        TextView bottomTranslatedText = (TextView) convertView.findViewById(R.id.translatedText);

        final ImageButton favoriteButton = (ImageButton)convertView.findViewById(R.id.imageButton);
        TextView langText = (TextView)convertView.findViewById(R.id.lang);

        upTextToTranslate.setText(currentTranslation.getTextToTranslate());
        bottomTranslatedText.setText(currentTranslation.getTranslatedText());

        langText.setText(currentTranslation.getLang());
        favoriteButton.setImageResource(currentTranslation.getFavoriteWordImageButton());

        //Listener для ImageButton, слушающий изменение состояния кнопки, отвечающее за выделение слова как избранное
        favoriteButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // меняем изображение на кнопке
                int res;
                if (flag){
                    res = R.drawable.tapon;
                    favoriteButton.setImageResource(res);

                    TranslateWord translateWord = new TranslateWord(currentTranslation.getTextToTranslate(),currentTranslation.getTranslatedText(), res, "en-ru");
                    myRef2.push().setValue(translateWord);
                }
                else{
                // возвращаем первую картинку
                res = R.drawable.tapoff;
                favoriteButton.setImageResource(res);
                    myRef2.child(myRef2.getKey()).removeValue();

            }
                flag = !flag;

            }
        });



        return convertView;

    }

}
