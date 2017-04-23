/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.YP;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment в котором происходит перевод текста
 */
public class TranslationFragment extends Fragment {

    String languageForFormer, languageForLatter, languageForHttpRequest, textToTranslate;
    Spinner spinnerForFormerLanguage, spinnerForLatterLanguage;
    ImageButton imageButtonChangeLanguage;
    int numberOfSelectedItemOfFormer, numberOfSelectedItemOfLatter;

    DictionaryVariantAdapter dictionaryVariantAdapter;
    List<DictionaryVariant> dictionaryVariantList;
    ListView listViewForDictionaryWord;

    DictionaryVariant dictionaryVariant;

    TextView translatedTextTextView, shownText;
    EditText textToTranslateEditText;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("text");


    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
           View view = inflater.inflate(R.layout.fragment_translationlayout, container, false);




        String[] arrayLanguages = {"Азербайджанский", "Албанский", "Амхарский","Английский",
                "Арабский", "Армянский", "Африкаанс", "Баскский", "Башкирский", "Белорусский",
                "Бенгальский", "Болгарский", "Боснийский", "Валлийский",
                "Венгерский", "Вьетнамский", "Гаитянский", "Галисийский", "Голландский",
                "Горномарийский", "Греческий", "Грузинский", "Гуджарати", "Датский",
                "Иврит", "Идиш", "Индонезийский", "Ирландский", "Итальянский", "Исландский",
                "Испанский", "Казахский", "Каннада", "Каталанский", "Киргизский", "Китайский",
                "Корейский", "Коса", "Латынь", "Латышский", "Литовский", "Люксембургский",
                "Малагасийский", "Малайский", "Малаялам", "Мальтийский", "Македонский",
                "Маори", "Маратхи","Марийский", "Монгольский", "Немецкий", "Непальский",
                "Норвежский", "Панджаби", "Папьяменто", "Персидский", "Польский", "Португальский",
                "Румынский", "Русский", "Себуанский", "Сербский", "Сингальский", "Словацкий",
                "Словенский", "Суахили", "Сунданский", "Таджикский", "Тайский", "Тагальский",
                "Тамильский", "Татарский", "Телугу", "Турецкий", "Удмуртский", "Узбекский",
                "Украинский", "Урду", "Финский", "Французский", "Хинди", "Хорватский",
                "Чешский", "Шведский", "Шотландский", "Эстонский", "Эсперанто", "Яванский","Японский"};



        final String[] arrayLanguageId = {"az","sq","am","en", "ar" ,"hy","af", "eu", "ba",
                "be", "bn", "bg", "bs", "cy", "hu", "vi", "ht", "gl", "nl", "mrj", "el",
                "ka", "gu", "da", "he", "yi", "id", "ga", "it", "is", "es", "kk", "kn", "ca",
                "ky", "zh", "ko" ,"xh", "la", "lv", "lt", "lb", "mg", "ms", "ml", "mt", "mk",
                "mi" ,"mr", "mhr", "mn", "de", "ne", "no", "pa", "pap", "fa", "pl", "pt",
                "ro", "ru", "ceb", "sr", "si", "sk", "sl", "sw", "su","tg", "th", "tl" ,"ta",
                "tt" ,"te", "tr", "udm" ,"uz" ,"uk", "ur", "fi", "fr", "hi" ,"hr", "cs" ,"sv",
                "gd", "et", "eo", "jv", "ja"};




        spinnerForFormerLanguage =(Spinner)view.findViewById(R.id.spinner_nav);
        spinnerForLatterLanguage =(Spinner)view.findViewById(R.id.spinner_nav2);
        translatedTextTextView = (TextView)view.findViewById(R.id.textView);
        textToTranslateEditText =(EditText)view.findViewById(R.id.editText2);
        imageButtonChangeLanguage =(ImageButton)view.findViewById(R.id.imageButton);
        shownText=(TextView)view.findViewById(R.id.textView2);

        listViewForDictionaryWord =(ListView)view.findViewById(R.id.listView);
        dictionaryVariantList = new ArrayList<>();





        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item , arrayLanguages); //selected item will look like a spinnerForFormerLanguage set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerForFormerLanguage.setAdapter(spinnerArrayAdapter);
        spinnerForLatterLanguage.setAdapter(spinnerArrayAdapter);



        //Spinner определяющий выбранный язык Former
        spinnerForFormerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                numberOfSelectedItemOfFormer = spinnerForFormerLanguage.getSelectedItemPosition();
                languageForFormer = arrayLanguageId[numberOfSelectedItemOfFormer];
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Spinner определяющий выбранный язык Latter
        spinnerForLatterLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                numberOfSelectedItemOfLatter = spinnerForLatterLanguage.getSelectedItemPosition();
                languageForLatter = arrayLanguageId[numberOfSelectedItemOfFormer];
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //Button, меняющий языки для перевода
        imageButtonChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int num = numberOfSelectedItemOfFormer;
                numberOfSelectedItemOfFormer = numberOfSelectedItemOfLatter;
                numberOfSelectedItemOfLatter = num;

                spinnerForFormerLanguage.setSelection(numberOfSelectedItemOfLatter);
                spinnerForLatterLanguage.setSelection(numberOfSelectedItemOfFormer);

            }
        });

        // TextChangedListener, слушающий изменение состояния  textToTranslateEditText
        textToTranslateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //Проверка на пустой textToTranslateEditText и translatedTextTextView

                if (textToTranslateEditText.getText().toString().equals("")){
                    Toast.makeText(getActivity().getApplicationContext(), "введите текст для перевода",
                            Toast.LENGTH_SHORT).show();
                }else if (translatedTextTextView.getText().toString().isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(), "введите текст",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                languageForHttpRequest = languageForFormer + "-" + languageForLatter;
                textToTranslate = textToTranslateEditText.getText().toString();

                // Таймер который отправляет
                new CountDownTimer(1200, 1000) {
                    String textBeforeTimer = textToTranslate;

                    @Override
                    public void onTick(long millisUntilFinished) {

                        if (textToTranslateEditText.getText().toString().equals("")) {
                            Toast.makeText(getActivity().getApplicationContext(), "введите текст",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            /*AssyncTask отпроавлет запрос на сервер, получает и парсит перевод в JSON формате
                            *@param textBeforeTimer - текст с textToTranslateEditText, до начала таймера
                            *@param languageForHttpRequest - языки переводов
                            */

                            new TaskTranslation().execute(textBeforeTimer, languageForHttpRequest);
                            /**
                             * Попытка получить варианты переаода преаодимого  текста.
                             * Вероятней всего, ошибка в парсинге полученного JSON
                             */
                            // new TaskDictionary().execute(languageForHttpRequest, textToTranslateEditText.getText().toString());
                        }

                    }

                    @Override
                    public void onFinish() {

                        /**
                         * Проверка на совпадение отправленного текатса для перевода перед началом
                         * таймера и текущим текстом в textToTranslateEditText                         *
                         */

                        if (textToTranslateEditText.getText().toString().equals(textBeforeTimer)) {
                            //Проверка на пустой textToTranslateEditText и translatedTextTextView
                            if (textToTranslateEditText.getText().toString().isEmpty() || translatedTextTextView
                                    .getText().toString().isEmpty()){
                                Toast.makeText(getActivity().getApplicationContext(), "введите текст",
                                        Toast.LENGTH_SHORT).show();
                            }else {
                                /**Сохраняем текст с textToTranslateEditText,
                                 * текст с translatedTextTextView, языки переводов languageForHttpRequest,
                                 * id иконки, характеризующее слово как не сохраненное в избранном
                                 *  в базе данных Firebase
                                 */
                                TranslateWord translateWord = new TranslateWord(textToTranslateEditText
                                        .getText().toString(),
                                        translatedTextTextView.getText().toString(), R.drawable.tapoff,
                                        languageForHttpRequest);
                                myRef.push().setValue(translateWord);}
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "перевод...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }.start();
            }
        });

        return view;
    }

    //AssyncTask отправляющий HttpRequest на сервер для получения перевода текста
    class TaskTranslation extends AsyncTask<String, Void, String> {

        //Устанавливаем связь с сервером
        @Override
        protected String doInBackground(String... params) {
            String urlRequest = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20170411T212807Z.453f1236ba984a10.1c90eef56783085c1db1dfde89ed1e15f31926ef";
            String inputText = params[0];
            String lang = params[1];
            String jsonResponse = "";
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;


            try {

                URL url = new URL(urlRequest);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                //формируем запрос по ключу, тексту, языку перевода
                DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                dataOutputStream.writeBytes("&text=" + URLEncoder.encode(inputText, "UTF-8") + "&lang=" + lang);


                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);


                }

            } catch (IOException e) {
                e.printStackTrace();
                // TODO: Handle the exception
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return jsonResponse;
        }

        //читаем ответ сервера
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        //парсим полученный JSON-ответ
        @Override
        protected void onPostExecute(String jsonResponse) {
            String textJson = "";

            JSONObject baseJsonResponse = null;
            try {
                baseJsonResponse = new JSONObject(jsonResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray textArray = null;
            try {
                textArray = baseJsonResponse.getJSONArray("text");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                textJson = textArray.getString(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(jsonResponse);
            //отображаем в translatedTextTextView полученный перевод текста
            translatedTextTextView.setText(textJson);
            //отображаем в shownText, текст для перевода
            shownText.setText(textToTranslateEditText.getText().toString());


        }
    }

    //AssyncTask для получения вариантов перевода
    class TaskDictionary extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {
            String urlRequest = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=dict.1.1.20170412T130004Z.80c807514d966221.bd1c4e01c013541d85691640fb8d390e43e35f9a";
            String lang = params[0];
            String inputText =  params[1];
            String jsonResponse = "";
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;


            try {

                URL url = new URL(urlRequest);
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                dataOutputStream.writeBytes("&lang=" + lang + "&text=" + URLEncoder.encode(inputText, "UTF-8") );


                if(urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);



                }

            }catch (IOException e) {
                e.printStackTrace();
                // TODO: Handle the exception
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        @Override
        protected void onPostExecute(String jsonResponseForDictionary) {
            String variant1 = "";
            String variant2 = "";
            String variant3 = "";
            String variant4 = "";
            String synonym1 = "";
            String synomym2 = "";
            String synonym3 = "";
            String type = "";

            JSONObject baseJsonResponse = null;
            try {
                baseJsonResponse = new JSONObject(jsonResponseForDictionary);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray defArray = null;
            try {
                defArray = baseJsonResponse.getJSONArray("def");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            JSONObject nullObj = null;
            try {
                nullObj = defArray.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }



            JSONArray tr = null;
            try {
                tr = nullObj.getJSONArray("tr");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject frstObj = null;
            try {
                frstObj = tr.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Парисим часть речи
            try {
                type =  frstObj.getString("pos");
            } catch (JSONException e) {
                e.printStackTrace();
            }



            JSONArray mean = null;
            try {
                mean = frstObj.getJSONArray("mean");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                variant1 =  mean.getString(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                variant2 =  mean.getString(1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                variant3 =  mean.getString(2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                variant4 =  mean.getString(3);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Парсим варианты перевода на перевеленном языке
            JSONArray syn = null;
            try {
                syn = frstObj.getJSONArray("syn");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            JSONObject syn1 = null;
            try {
                syn1 = syn.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                synonym1 =  syn1.getString("text");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject syn2 = null;
            try {
                syn2 = syn.getJSONObject(1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                synomym2 =  syn2.getString("text");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONObject syn3 = null;
            try {
                syn3 = syn.getJSONObject(2);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                synonym3 =  syn3.getString("text");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            super.onPostExecute(jsonResponseForDictionary);

            String variant = variant1 + ", " + variant2 + ", " + variant3 + ", " + variant4;
            dictionaryVariant.setVariant(variant);

            String synonym =  synonym1 + ", " + synomym2 + ", " + synonym3;
            dictionaryVariant.setMeaning(synonym);

            dictionaryVariant.setTypeOfSpeach(type);

            dictionaryVariantAdapter = new DictionaryVariantAdapter(getActivity(), R.layout.dictionary_layout, dictionaryVariantList);
            listViewForDictionaryWord.setAdapter(dictionaryVariantAdapter);


        }
    }


}


