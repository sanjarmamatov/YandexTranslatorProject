package com.example.android.YP;

/**
 * Класс предназначенный для хранения данных для текста, который нужно перевести; переведенный текст,
 * языки перевода, иконка закладки
 */

class TranslateWord {

    private String textToTranslate;
    private String translatedText;
    private int favoriteWordImageButton;
    private String lang;

    public TranslateWord(){}

    public TranslateWord(String textToTranslate, String translatedText, int favoriteWord, String lang){
        this.textToTranslate = textToTranslate;
        this.translatedText = translatedText;
        this.favoriteWordImageButton = favoriteWord;
        this.lang = lang;
    }

    public int getFavoriteWordImageButton() {
        return favoriteWordImageButton;
    }

    public void setFavoriteWordImageButton(int favoriteWordImageButton) {
        this.favoriteWordImageButton = favoriteWordImageButton;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTextToTranslate() {
        return textToTranslate;
    }

    public void setTextToTranslate(String textToTranslate) {
        this.textToTranslate = textToTranslate;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }


}
