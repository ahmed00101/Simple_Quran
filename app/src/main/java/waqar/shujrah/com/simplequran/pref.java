package waqar.shujrah.com.simplequran;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by waqar on 04/01/15.
 */


public class pref {

    public static final String PREFS_NAME = "MyPrefsFile";

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    public Float fontSize;
    public int curSura;
    public int colorMode;
    public int curFont;




    Context mContext;

    /** Instantiate the interface and set the context */
    pref(Context c) {
        mContext = c;
        settings = mContext.getSharedPreferences(PREFS_NAME, 0);

        fontSize = settings.getFloat("fontSize",16f);

        curSura = settings.getInt("curSura",0);

        colorMode = settings.getInt("colorMode", 0);

        curFont = settings.getInt("curFont",0);




    }

    /** Show a toast from the web page */
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    public void save_pref(String name, String value)
    {
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(name);
        editor.putString(name, value);

        editor.commit();//km

    }

    public String read_pref(String name)
    {
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
        String value = settings.getString(name,"");
        return value;

    }



    public void save(Float f, int sura, int color, int font)
    {
        editor = settings.edit();
        editor.remove("fontSize");
        editor.remove("curSura");
        editor.remove("colorMode");
        editor.remove("curFont");

        editor.putFloat("fontSize",f);
        editor.putInt("curSura",sura);
        editor.putInt("colorMode",color);
        editor.putInt("curFont",font);
        editor.commit();


    }

}
