package waqar.shujrah.com.simplequran;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import static android.graphics.Color.parseColor;

public class quranActivity extends Activity implements OnGestureListener {
    GestureDetector gDetector;
    TextView tv;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;


    int appendonce = 299; //Max 3 ayah to be appended at first

    pref pf;

    Context context;
    TextView quranMain;
    LinearLayout sc;
    ScrollView scrl;
    Spinner suraSelector;
    RelativeLayout menuBar;

    int curSura;
    int curFont;
    float fontSize;
    int fontDp;
    Button nextSuraB;
    Button prevSuraB;
    List<Button> btns;
    List<Typeface> fonts;

    Typeface tf;


    String bgColor = "#ffefef";
    String ayaNumColor = "#0000dd";
    String ayaColor = "#101010";

    int colorMode;



    String colors[][] = {
          //{ Name,       BgColor,        AyaNum,      AyaColor  }
            {"White",    "#ffffffff",    "#0000dd",   "#101010"  },
            {"Cream",    "#fffeeded",    "#0011dd",   "#101010"  },
            {"Sepia",    "#ffe9d8ba",    "#333311",   "#343412"  },
            {"LCD1",     "#ff929292",    "#606b7f",   "#505b6f"  },
            {"Night HC", "#ff000000",    "#dddddd",   "#EDEDED"  },
            {"Night LC", "#ff111111",    "#dcdcdc",   "#cDcDcD"  }

    };

    String fontNames[] = { "me_quran", "PDMS_Saleem", "MADDINA", "AlQalamQuran", "ar-Quran1", "QUR_STD", "noorehidayat"    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Activity","Started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quran);
        gDetector = new GestureDetector(this);

        View decorView = getWindow().getDecorView();
//      Hide both the navigation bar and the status bar.
//      SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
//      a general rule, you should design your app to hide the status bar whenever you
//      hide the navigation bar.
        //int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION| View.SYSTEM_UI_FLAG_FULLSCREEN;
        //decorView.setSystemUiVisibility(uiOptions);

        context = getApplicationContext();

        pf = new pref(context);

        curSura = pf.curSura;
        curFont = pf.curFont;
        colorMode = pf.colorMode;
        fontSize = pf.fontSize;
        //Toast.makeText(context, "curSura: "+curSura+"\ncurFont: "+curFont+"\ncolorMode: "+colorMode+"\nfontSize: "+fontSize , Toast.LENGTH_LONG).show();

        bgColor     = colors[colorMode][1];
        ayaNumColor = colors[colorMode][2];
        ayaColor    = colors[colorMode][3];

        sc = (LinearLayout) findViewById(R.id.Snd);
        scrl = (ScrollView) findViewById(R.id.scrollView);
        menuBar = (RelativeLayout) findViewById(R.id.menuBar);

        suraSelector = (Spinner) findViewById(R.id.suraSelect);



        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for(int i=0; i< quran.arnames.length; i++)
        {
            spinnerAdapter.add((+i+1)+": "+quran.arnames[i]);

        }

        spinnerAdapter.notifyDataSetChanged();

        suraSelector.post(new Runnable() {
            public void run() {
                suraSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        int s = ((int) id);
                        ((TextView) suraSelector.getChildAt(0)).setTextColor(parseColor(ayaColor));
                        print_quran(s);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });

        suraSelector.setAdapter(spinnerAdapter);

        suraSelector.setSelection(curSura);


        Typeface FA = Typeface.createFromAsset(getAssets(), "fontawesome.ttf");

        fonts = new ArrayList<Typeface>();
        fonts.add(Typeface.createFromAsset(getAssets(),"me_quran.ttf"));
        fonts.add(Typeface.createFromAsset(getAssets(),"PDMS_Saleem.ttf"));
        fonts.add(Typeface.createFromAsset(getAssets(),"MADDINA.ttf"));
        fonts.add(Typeface.createFromAsset(getAssets(),"AlQalamQuran.ttf"));
        fonts.add(Typeface.createFromAsset(getAssets(),"ar-Quran1.ttf"));
        fonts.add(Typeface.createFromAsset(getAssets(),"QUR_STD.TTF"));
        fonts.add(Typeface.createFromAsset(getAssets(),"noorehidayat.ttf"));







        tf = fonts.get(curFont);

        quranMain = (TextView) findViewById(R.id.quranMain);
        quranMain.setMovementMethod(new ScrollingMovementMethod());
        quranMain.setTypeface(tf);

        quranMain.setTextSize(fontSize);
        quranMain.setTextColor(parseColor(ayaColor));
        sc.setBackgroundColor(parseColor(bgColor));
        quranMain.setBackgroundColor(parseColor(bgColor));
        //menuBar.setBackgroundColor(parseColor(bgColor));


        quranMain.setMovementMethod(new ScrollingMovementMethod());
        quranMain.setTextIsSelectable(true);
        quranMain.setFocusable(true);
        quranMain.setFocusableInTouchMode(true);


        print_quran(curSura);

        //Resources r = getResources();
        //float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pf.fontDp, r.getDisplayMetrics());
        quranMain.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fontSize);

        //colorMode();

        nextSuraB = (Button) findViewById(R.id.suraNext);
        nextSuraB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {  nextSura();   }
        });


        prevSuraB = (Button) findViewById(R.id.suraPrev);
        prevSuraB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                prevSura();
            }
        });


        Button fontChangeB = (Button) findViewById(R.id.fontChange);
        fontChangeB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fontChange();
            }
        });


        final Button fontPlusB = (Button) findViewById(R.id.fontPlus);
        fontPlusB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fontPlus();
            }
        });


        final Button fontMinusB = (Button) findViewById(R.id.fontMinus);
        fontMinusB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fontMinus();

            }
        });

        final Button colorModeB = (Button) findViewById(R.id.colorMode);
        colorModeB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                colorMode();

            }
        });


        quranMain.setMovementMethod(new ScrollingMovementMethod());

        btns = new ArrayList<Button>();
        btns.add(nextSuraB);
        btns.add(prevSuraB);
        btns.add(fontPlusB);
        btns.add(fontMinusB);
        btns.add(colorModeB);
        btns.add(fontChangeB);

        for(int x=0; x<btns.size(); x++)
        {
            btns.get(x).setTypeface(FA);
            btns.get(x).setTextColor(parseColor(ayaColor));
        }

        //TextView spinnerText = (TextView) suraSelector.getChildAt();
        //spinnerText.setTextColor(parseColor(ayaColor));
        System.out.println(suraSelector.getChildCount());
        //suraSelector.getChildCount();

    }






    public void fontChange(){

        if(curFont < fonts.size()-1)
            curFont++;
        else
            curFont = 0;

//        pf.save(fontSize,curSura,colorMode, curFont);
        quranMain.setTypeface(fonts.get(curFont));
        Toast.makeText(context,"Font: "+fontNames[curFont], Toast.LENGTH_SHORT).show();
        System.out.println("Font:"+Integer.toString(curFont)+" name: "+fonts.get(curFont).toString());


    }

    public void nextSura() {


        int sura = curSura + 1;
        if(sura > 113)
        {
            Toast.makeText(this,"This is last sura.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            suraSelector.setSelection(sura);
            print_quran(sura);
        }

    }

    public void prevSura() {
        int sura = curSura - 1;
        if(sura < 0)
        {
            Toast.makeText(this,"This is first sura.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            System.out.println("\nSura: " + Integer.toString(sura));
            System.out.println("\ncurSura: " + Integer.toString(curSura));


            print_quran(sura);
        }

    }

    public void fontPlus() {
        Float factor = 1.1f;
        fontSize = fontSize * factor;
        quranMain.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);
        Float lspExtra = quranMain.getLineSpacingExtra() * factor;
        Float lspMult = quranMain.getLineSpacingMultiplier() * factor;
//        Toast.makeText(this, "LSP Extra: " + lspExtra.toString(), Toast.LENGTH_SHORT).show();
       // quranMain.setLineSpacing(lspExtra, lspMult);
        System.out.println("Font Size:" + Float.toString(fontSize));

//        pf.save(fontSize,curSura,colorMode, curFont);

    }

    public void fontMinus() {
        Float factor = 0.9f;
        fontSize = fontSize * factor;
        quranMain.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);
        //Float lspExtra = quranMain.getLineSpacingExtra() * factor;
        //Float lspMult = quranMain.getLineSpacingMultiplier() * factor;
//        Toast.makeText(this, "LSP Extra: " + lspExtra.toString(), Toast.LENGTH_SHORT).show();
        //quranMain.setLineSpacing(lspExtra, lspMult);
        System.out.println("Font Size:" + Float.toString(fontSize));
//        pf.save(fontSize,curSura,colorMode, curFont);
    }

    public void colorMode(){

        if(colorMode < colors.length-1)
            colorMode++;
        else
            colorMode = 0;

        String colorModeName = colors[colorMode][0];
        bgColor     = colors[colorMode][1];
        ayaNumColor = colors[colorMode][2];
        ayaColor    = colors[colorMode][3];

//        pf.save(fontSize,curSura,colorMode, curFont);

        Toast.makeText(context, "Color Mode: "+colorModeName, Toast.LENGTH_SHORT).show();

        for(int x=0; x<btns.size(); x++)
        {
            btns.get(x).setTextColor(parseColor(ayaColor));
        }

        quranMain.setBackgroundColor(parseColor(bgColor));
        quranMain.setTextColor(parseColor(ayaColor));
        sc.setBackgroundColor(parseColor(bgColor));
        findViewById(R.id.Snd).setBackgroundColor(parseColor(bgColor));
        TextView spinnerText = (TextView) suraSelector.getChildAt(0);
        spinnerText.setTextColor(parseColor(ayaColor));
        //spinnerText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


        print_quran(curSura);

    }

    public void change_font(){
        quranMain.setTypeface(tf);

//        pf.save(fontSize,curSura,colorMode, curFont);
    }

    public void print_quran(final int sura) {
       // sc.scrollTo(0, 0);

        Thread q = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {






        curSura = sura;
//        pf.save(fontSize,curSura,colorMode, curFont);
        quran q = new quran();

        String[] verses = null;

        quranMain.setText("");
        String newText = "";

        //newText += "<font color=\"" + ayaColor + "\">" + (+sura + 1) + ": " + quran.arnames[sura] + "</font><br>";
        newText += "" + (+sura + 1) + ": " + quran.arnames[sura] + "\n";


                        if(sura > 113 | sura < 0 ){
            Toast.makeText(context,"Sura not found",Toast.LENGTH_SHORT).show();
        }else{}

        if (sura != 0 && sura != 8) {
            //newText += "<font color=\""+ayaColor+"\">"+q.arabic[0][0]+"</font><br>";

            newText += ""+q.arabic[0][0]+"\n";
        }

        System.out.println("Start!");
        try {
            verses = q.qread(sura);

            for (int x = 0; x < verses.length && x <= appendonce; x++)
            {
                //newText += "<font color=\""+ayaColor+"\">"+verses[x]+"</font><font color=\""+ayaNumColor+"\"><small>&nbsp;"+(+x+1)+"&nbsp;&nbsp;</small></font>&nbsp;";
                newText += ""+verses[x]+"  "+(+x+1)+"  ";

                //quranMain.append(Html.fromHtml("<font color=\""+ayaColor+"\">"+verses[x]+"</font><font color=\""+ayaNumColor+"\"><small>&nbsp;"+(+x+1)+"&nbsp;&nbsp;</small></font>&nbsp;" ) );

            }

            quranMain.setText(newText);
            //quranMain.setText(Html.fromHtml(newText));


        } catch (NullPointerException npe) {
            System.out.println("NPE: " + npe.getMessage());
        }



                    }
                });

            }
        });
        q.start();

    }







    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        super.dispatchTouchEvent(ev);
        return gDetector.onTouchEvent(ev);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu., menu);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        // TODO Auto-generated method stub
        //Log.d("onDown","In OnDown");
        return true;

    }

    public void rightToLeft() {   nextSura();  }
    public void lefttoRight() {   prevSura();  }

    @Override
    public boolean onFling(MotionEvent start, MotionEvent finish, float velocityX,
                           float velocityY) {
        // TODO Auto-generated method stub
        //TextView t=(TextView)findViewById(R.id.tv);
        /*if(start.getRawX() < finish.getRawX())
        {
            Log.d("onFling","in inFling");
        }
        else if(start.getRawX()>finish.getRawX())
        {
            Log.d("onFLing","in onFling");
        }*/
        if (Math.abs(start.getY() - finish.getY()) > SWIPE_MAX_OFF_PATH)
            return false;
        // right to left swipe

        if(start.getX() - finish.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

            lefttoRight();

        }
        else if (finish.getX() - start.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            rightToLeft();
        }
        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return gDetector.onTouchEvent(me);
    }

    @Override
    public void onLongPress(MotionEvent arg0) {
        // TODO Auto-generated method stub
        if(menuBar.getVisibility() == View.VISIBLE)
        {
            menuBar.setVisibility(View.GONE);
        }else
        {
            menuBar.setVisibility(View.VISIBLE);
        }

        Log.d("onLongPress","in onlongpress");
    }



    @Override
    public void onShowPress(MotionEvent arg0) {
        // TODO Auto-generated method stub
        Log.d("onShowPress","in onShowPress");

    }

    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
        // TODO Auto-generated method stub
        Log.d("onSingleTapup","in onsingletapup");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
                            float arg3) {
        // TODO Auto-generated method stub
        int ScrY = scrl.getScrollY();
        //System.out.println("Scrolling Y: "+ScrY);

        if(ScrY >= 100)
        {
            menuBar.setAlpha(0.20f);
            //menuBar.setVisibility(View.GONE);
        }else{
            menuBar.setAlpha(0.75f);
        }
        //menuBar.setVisibility(View.VISIBLE);}

        return false;
    }



    @Override
    protected void onDestroy(){

        pf.save(fontSize,curSura,colorMode, curFont);
        Log.d("Activity","onDestroy");
        super.onDestroy();

    }


}