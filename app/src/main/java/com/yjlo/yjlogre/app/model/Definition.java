package com.yjlo.yjlogre.app.model;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;

/**
 * Created by siwei on 19/3/14.
 */
public class Definition {

    private String pos;
    private String def;

    public Definition(String pos, String def){
        this.pos = pos;
        this.def = def;
    }

    public void setPos(String p){
        this.pos = p;
    }

    public void setDef(String d){
        this.def = d;
    }

    public String getPos(){
        return this.pos;
    }

    public String getDef(){
        return this.def;
    }

    public String toString(){
        return pos +" "+ def;
    }

    public Spannable toSpanable(){
        Spannable spanna = new SpannableString(toString());
        spanna.setSpan(new BackgroundColorSpan(0xFFCCCCCC),0, getPos().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanna;
    }
}
