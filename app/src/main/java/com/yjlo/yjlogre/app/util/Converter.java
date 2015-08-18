package com.yjlo.yjlogre.app.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;

import com.yjlo.yjlogre.app.model.Definition;
import com.yjlo.yjlogre.app.model.SampleSentence;

import java.util.ArrayList;

/**
 * Created by siwei on 19/3/14.
 */
public class Converter {

    public static String definitionListToString(ArrayList<Definition> list){
        String result = "";
        if (list.size()>0){
            result += list.get(0).toString();
        }
        for (int i = 1; i < list.size(); i++){
            result += ("\n" + list.get(i).toString());
        }
        return result;
    }

    public static Spannable definitionListToSpannable(ArrayList<Definition> list){
        Spannable result = null;
        if (list.size()>0){
            result = list.get(0).toSpanable();
        }
        for (int i = 1; i < list.size(); i++){
            CharSequence addNewLing = TextUtils.concat(new SpannableString("\n"), list.get(i).toSpanable());
            result = new SpannableString(TextUtils.concat(result, addNewLing));
        }
        return result;
    }

    public static String sampleSentenceListToString(ArrayList<SampleSentence> list){
        String result = "";
        if (list.size()>0){
            result += list.get(0).toString();
        }
        for (int i = 1; i < list.size(); i++){
            result += ("\n\n" + list.get(i).toString());
        }
        return result;
    }

    public static String sampleSentenceListToHtml(ArrayList<SampleSentence> list, String keyword){
        String result = "";
        if (list.size()>0){
            result += list.get(0).toHtml(keyword);
        }
        for (int i = 1; i < list.size(); i++){
            result += (list.get(i).toHtml(keyword));
        }
        return result;
    }
}
