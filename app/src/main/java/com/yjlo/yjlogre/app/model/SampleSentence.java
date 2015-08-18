package com.yjlo.yjlogre.app.model;

/**
 * Created by siwei on 19/3/14.
 */
public class SampleSentence {
    private String en;
    private String cn;

    public SampleSentence(String en, String cn){
        this.en = en;
        this.cn = cn;
    }

    public void setEn(String e){
        this.en = e;
    }

    public void setCn(String c){
        this.cn = c;
    }

    public String getEn(){
        return this.en;
    }

    public String getCn(){
        return this.cn;
    }

    public String toString(){
        return en + "\n" + cn;
    }

    public String toHtml(String keyword){
        String toReplace;
        int start;
        int end;
        String htmlString = en + "<br/>" + cn + "<br/><br/>";
        // do not add <strong> tag in Android 4.1 or 4.1.1
        String androidVersion = android.os.Build.VERSION.RELEASE;
        if (!androidVersion.equals("4.1") && !androidVersion.equals("4.1.1")
                && (en.toLowerCase()).contains(keyword.toLowerCase())){
            start = htmlString.toLowerCase().indexOf(keyword.toLowerCase());
            end = start + keyword.length();
            toReplace = htmlString.substring(start, end);
            return htmlString.replaceAll(toReplace, "<b>"+toReplace+"</b>");
        }
        return htmlString;
    }
}
