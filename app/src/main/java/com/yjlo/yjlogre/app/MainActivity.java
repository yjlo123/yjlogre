package com.yjlo.yjlogre.app;

import android.app.Dialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yjlo.yjlogre.app.model.Definition;
import com.yjlo.yjlogre.app.model.Mode;
import com.yjlo.yjlogre.app.model.SampleSentence;
import com.yjlo.yjlogre.app.util.Converter;
import com.yjlo.yjlogre.app.util.LogUtil;
import com.yjlo.yjlogre.app.util.PackageInfoUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class MainActivity extends ActionBarActivity {

    private TextView word;
    private TextView pronunciation;
    private TextView meaning;
    private TextView sentence;
    private Button next;
    private ScrollView sentenceScroll;
    private LinearLayout mainLayout;
    private MenuItem actionModeSwitch;

    private int scopeFrom;
    private int scopeTo;
    private Mode mode;

    private static final String PROPERTY_ID = "UA-55510700-1";
    private Tracker tracker;
    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApplicationInfo appInfo = getApplicationInfo();
        if(appInfo!=null) {
            if( (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
                Toast.makeText(this, "DEV VERSION", Toast.LENGTH_SHORT).show();
            }else{
                GoogleAnalytics.getInstance(this).newTracker(PROPERTY_ID);
                GoogleAnalytics.getInstance(this).getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
                tracker = getTracker(TrackerName.APP_TRACKER);
                tracker.setScreenName("MainActivity");
                tracker.send(new HitBuilders.AppViewBuilder().build());
            }
        }

        init();

        //File dbfile = new File("/sdcard/mydb.sqlite" );
        /*
        SQLiteDatabase testdb = null;
        try{
            testdb = SQLiteDatabase.openDatabase("/sdcard/mydb.sqlite", null, SQLiteDatabase.OPEN_READONLY);
            Log.d("=======Its open? ", "true");
        }catch (SQLiteCantOpenDatabaseException e){
            Log.d("=======Its open? ", "false" );
        }
        */

        MyDatabase mDbHelper = new MyDatabase(this);
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        long numRows = DatabaseUtils.queryNumEntries(db, "data");
        Toast.makeText(this, "Number of words: "+numRows, Toast.LENGTH_SHORT).show();
        final String[] projection = {
                "id",
                "word",
                "pronunciation",
                "meaning",
                "sentence"
        };

        assert db != null;

        scopeFrom = 1;
        scopeTo = 7513;

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Random rdm = new Random();
                int index = rdm.nextInt(scopeTo - scopeFrom + 1) + scopeFrom;
                //index = 6624;

                LogUtil.Log("=========id = ", Integer.toString(index));

                Cursor c = db.query(
                        "data",  // The table to query
                        projection,                               // The columns to return
                        "id = ?",                                // The columns for the WHERE clause
                        new String[]{Integer.toString(index)},    // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        null                                 // The sort order
                );

                c.moveToFirst();
                String w = c.getString(c.getColumnIndex("word"));
                String p = c.getString(c.getColumnIndex("pronunciation"));
                String m = c.getString(c.getColumnIndex("meaning"));
                String s = c.getString(c.getColumnIndex("sentence"));

                Gson gson = new Gson();
                Type definitionType = new TypeToken<ArrayList<Definition>>(){}.getType();
                Type sampleSentenceType = new TypeToken<ArrayList<SampleSentence>>(){}.getType();
                ArrayList<Definition> def = gson.fromJson(m, definitionType);
                ArrayList<SampleSentence> sen = gson.fromJson(s, sampleSentenceType);
                sentenceScroll.fullScroll(ScrollView.FOCUS_UP);
                word.setText(w);
                pronunciation.setText(p);
                meaning.setText(Converter.definitionListToString(def));
                sentence.setText(Html.fromHtml(Converter.sampleSentenceListToHtml(sen, w)));
                c.close();
            }
        });

        next.performClick();

    }

    private void init(){
        mainLayout = (LinearLayout)findViewById(R.id.main);
        word = (TextView)findViewById(R.id.tv_word);
        pronunciation = (TextView)findViewById(R.id.tv_pronunciation);
        meaning = (TextView)findViewById(R.id.tv_meaning);
        sentence = (TextView)findViewById(R.id.tv_sentence);
        next =  (Button)findViewById(R.id.next);
        sentenceScroll = (ScrollView)findViewById(R.id.sv_sentence);

        mode = Mode.MODE_DAY;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        actionModeSwitch = menu.findItem(R.id.action_switch_mode);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_scope_1:
                scopeFrom = 1;
                scopeTo = 2000;
                break;
            case R.id.action_scope_2:
                scopeFrom = 2001;
                scopeTo = 4000;
                break;
            case R.id.action_scope_3:
                scopeFrom = 4001;
                scopeTo = 6000;
                break;
            case R.id.action_scope_4:
                scopeFrom = 6001;
                scopeTo = 7513;
                break;
            case R.id.action_scope_all:
                scopeFrom = 1;
                scopeTo = 7513;
                break;
            case R.id.action_about:
                String currentVersionName = "unknown";
                try {
                    currentVersionName = PackageInfoUtil.getVersionName(this);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                // custom dialog
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_about);
                TextView tvVersion = (TextView) dialog.findViewById(R.id.tv_version);
                tvVersion.setText(currentVersionName);
                dialog.show();
                break;
            case R.id.action_switch_mode:
                switchMode();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchMode(){
        if(mode == Mode.MODE_DAY){
            word.setTextAppearance(getBaseContext(), R.style.nightMode);
            pronunciation.setTextAppearance(getBaseContext(), R.style.nightMode);
            meaning.setTextAppearance(getBaseContext(), R.style.nightMode);
            sentence.setTextAppearance(getBaseContext(), R.style.nightMode);
            mainLayout.setBackgroundResource(R.color.nightModeBackgroundColor);
            actionModeSwitch.setIcon(R.drawable.mode_night);
            mode = Mode.MODE_NIGHT;
        }else{
            word.setTextAppearance(MainActivity.this, R.style.dayMode);
            pronunciation.setTextAppearance(getBaseContext(), R.style.dayMode);
            meaning.setTextAppearance(getBaseContext(), R.style.dayMode);
            sentence.setTextAppearance(getBaseContext(), R.style.dayMode);
            mainLayout.setBackgroundResource(R.color.dayModeBackgroundColor);
            actionModeSwitch.setIcon(R.drawable.mode_day);
            mode = Mode.MODE_DAY;
        }
    }

    synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker)
                    : analytics.newTracker(R.xml.global_tracker);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

}
