package com.example.android.YP;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс фрагмент, в котором отображаются история переводов
 *
 */

public class FavoriteWordFragment extends Fragment {

    ListView listView;

    TranslateWordAdapter translateWordAdapter;
    List<TranslateWord> translateWordList;

    // создадем ветвь "favoriteWord" в базе данных Firebase
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef2 = database.getReference("favoriteWord");


    private ChildEventListener childEventListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_blank, container, false);


        listView =(ListView)view.findViewById(R.id.listView2);
        translateWordList = new ArrayList<>();

        translateWordAdapter = new TranslateWordAdapter(getActivity(), R.layout.translate_text, translateWordList);
        listView.setAdapter(translateWordAdapter);

        //childEventListener - listener, срабатывает тогда, когда новый объект был сохранен в базе дынных
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
             //   получит все данные с базы данных firebase в ветви "favoriteWord"
                TranslateWord translateWord = dataSnapshot.getValue(TranslateWord.class);
                translateWordAdapter.add(translateWord);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        // добавляем слушатель myRef2
        myRef2.addChildEventListener(childEventListener);




        return view;

    }
}
