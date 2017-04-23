package com.example.android.YP;

/**
 * Класс предназначен для хранения данных, предназначенные для вариантов переводов
 */

public class DictionaryVariant {


    /**
     *   typeOfSpeach - переменная, предназначенная для хранения части речи переводимого слова
     *   variant - переменная, предназначенная для хранения вариантов переводов
     *   meaning - переменная, предназначенная для хранения переводов вариантов переводов
     */

    private String typeOfSpeach;
    private String variant;
    private String meaning;


    public DictionaryVariant(){}

    public String getTypeOfSpeach() {
        return typeOfSpeach;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public void setTypeOfSpeach(String typeOfSpeach) {
        this.typeOfSpeach = typeOfSpeach;
    }
}
