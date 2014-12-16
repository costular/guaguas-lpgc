package com.costular.guaguaslaspalmas.model;

/**
 * Created by Diego on 30/11/2014.
 */
public class FavoriteStop {

    private String letter;
    private String custom;
    private String defaul;

    public FavoriteStop(String letter, String custom, String defaul) {
        this.letter = letter;
        this.custom = custom;
        this.defaul = defaul;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public String getDefaul() {
        return defaul;
    }

    public void setDefaul(String defaul) {
        this.defaul = defaul;
    }
}
