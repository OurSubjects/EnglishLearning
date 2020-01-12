package com.englishlearning.android.models;

public class historyClass {

    private String _id;
    private String textInput;
    private String question;

    public historyClass(String _id, String textInput){
        super();
        this._id = _id;
        this.textInput = textInput;

    }

    public String get_id() {
        return _id;
    }
    public String getTextInput() {
        return textInput;
    }


    @Override
    public String toString(){
        return _id + ". " + textInput;
    }

}
