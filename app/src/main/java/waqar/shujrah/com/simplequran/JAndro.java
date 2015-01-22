package waqar.shujrah.com.simplequran;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by waqar on 04/01/15.
 */


public class JAndro {

    public static final String PREFS_NAME = "MyPrefsFile";


    Context mContext;

    /** Instantiate the interface and set the context */
    JAndro(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void save_pref(String name, String value)
    {
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(name);
        editor.putString(name, value);

        editor.commit();//km

    }

    @JavascriptInterface
    public String read_pref(String name)
    {
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
        String value = settings.getString(name,null);
        return value;

    }



}
