package com.wordpress.zubeentolani.arjayv010;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

class frameObject {
    public String frameID;
    public String frameName;
    public long framePostion;
    public String frameDetail;


    public frameObject(String frameID, String frameName, long framePos, String frameDet){
        this.frameID = frameID;
        this.frameName = frameName;
        this.framePostion = framePos;
        this.frameDetail = frameDet ;

    }

}

public class MainActivity extends Activity implements DialogBox.DialogBoxListner {

    public boolean frameDataChangedFlag = false;

    public static Menu mainMenu;
    public static ActionBar actionBar;
    public static FrameLayout mainframeLayout;
    public static Context context;
    public static ListView mainListView ;
    public static ListView listViewSecond;
    public static ArrayList<String[]> mp3SongsList = new ArrayList<String[]>() ;
    public static ArrayList<frameObject> tagList = new ArrayList<frameObject>() ;

    public static String[] noString = {"Astr1","Hsrt2","Qstr3","Estr4","stRr5","Qstr6","dsrt7","sgtr8","astr9","sjtr10","rstr12","tsrt13","jstr14","pstr16","istr17","nstr18","gsrt19","str20","str21","str22","str23","srt24","str25","str26","str27","str28","srt29","str30","str31","str32"};

    public static String[] supportedID3Frames = new String[]{
            "TALB",
            "TPE2",
            "COMR",
            "WCOM",
            "TCOM",
            "TIT1",
            "TPE3",
            "WCOP",
            "TCOP",
            "TDAT",
            "TOWN",
            "IPLS",
            "TEXT",
            "TPE1",
            "MCDI",
            "TOAL",
            "TOPE",
            "TOFN",
            "TOLY",
            "TORY",
            "OWNE",
            "WPAY",
            "PCNT",
            "POPM",
            "TPUB",
            "TRDA",
            "TSIZ",
            "TIT3",
            "USER",
            "TIME",
            "TIT2",
            "UFID",
            "TYER",
            "TXXX"
    };

    public static String[] frameName = new String[]{
            "Album/Movie/Show title",
            "Band/orchestra/accompaniment",
            "Commercial frame",
            "Commercial information (UnEditable)",
            "Composer",
            "Content group description",
            "Conductor/performer refinement",
            "Copyright/Legal information",
            "Copyright message",
            "Date (Format DDMM)",
            "File owner/licensee",
            "Involved people list",
            "Lyricist/Text writer",
            "Lead performer(s)/Soloist(s)",
            "Music CD identifier",
            "Original album/movie/show title",
            "Original artist(s)/performer(s)",
            "Original filename",
            "Original lyricist(s)/text writer(s)",
            "Original release year",
            "Ownership frame",
            "Payment",
            "Play counter",
            "Popularimeter",
            "Publisher",
            "Recording dates",
            "Size",
            "Subtitle/Description refinement",
            "Terms of use",
            "Time (Format HHMM)",
            "Title/songname/content description",
            "Unique file identifier",
            "Year"
    };




    public static File[] files;
    public static customArrayAdapter arrAdapter = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("AlertZeek", "Inside onCreate of MainActivity");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        context = this;

        mainListView = (ListView) findViewById(R.id.Main_List_View);
        arrAdapter = new customArrayAdapter(context, R.layout.mp3_list_view, mp3SongsList);
//        arrAdapter = new customArrayAdapter(context, R.layout.mp3_list_view, noString,0);
        mainListView.setAdapter(arrAdapter);

        if (savedInstanceState == null)
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("AlertZeek", "Inside Thread to load mp3 files");

                mp3SongsList.addAll(scanForMp3());

                mainListView.post(new Runnable() {
                    @Override
                    public void run() {
                        ((ArrayAdapter)mainListView.getAdapter()).notifyDataSetChanged();
                    }
                });
                Log.i("AlertZeek", "made");
            }
        }).start();





        Log.i("Function", "Inside onCreate of MainActivity - Array created");
        Log.i("Function", String.format("inside onCreate of MainActivity- id=%d", findViewById(R.id.Main_Frame_Layout).getId()));

        mainframeLayout = (FrameLayout) findViewById(R.id.Main_Frame_Layout);

//        str.add(new String[]{noString[0]," "}) ;
//        str.add(new String[]{noString[1]," "}) ;
//        mainListView.deferNotifyDataSetChanged();



        Log.i("Function", "Inside onCreate of MainActivity - Adapter");

//        mainListView.setAdapter(adap);

//        mainTable = (TableLayout)findViewById(R.id.tbLay);
//        TableRow tblRw = (TableRow) tb.getChildAt(0);
//        TextView txtView = (TextView) tblRw.getChildAt(1);
//        txtView.setText("Changed");

//        scanFor();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        mainMenu = menu;

        menu.findItem(R.id.item_more).setVisible(false);
        menu.findItem(R.id.item_commitChanges).setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("AlertZeek","optionButton clicked id = " + item.getTitle());

        switch (item.getItemId()){
            case (R.id.item_refresh):

                Log.i("AlertZeek","refresh button clicked");
                if (mainListView.getVisibility() == View.VISIBLE){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("AlertZeek", "Inside Thread to load mp3 files");

                            mp3SongsList.addAll(scanForMp3());

                            mainListView.post(new Runnable() {
                                @Override
                                public void run() {
                                    ((ArrayAdapter)mainListView.getAdapter()).notifyDataSetChanged();
                                }
                            });
                            Log.i("AlertZeek", "made");
                        }
                    }).start();
                }else{

                }
            break;
            case (R.id.item_onlyExistingFrames):
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i =0; i < tagList.size(); i++){
                            if (tagList.get(i).framePostion == -1){
                                tagList.remove(i);
                                i--;
                            }
                        }
                        listViewSecond.post(new Runnable() {
                            @Override
                            public void run() {
                                ((ArrayAdapter)listViewSecond.getAdapter()).notifyDataSetChanged();
                            }
                        });
                    }
                }).start();
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void callDialogBox(int position, int option){
        Log.i("AlertZeek", "Calling DialogBox");
        DialogBox frag = new DialogBox();

        if (option == 1) { //it is normal TEXT frame
            frag.setValues(frameName[position], tagList.get(position).frameDetail, position);
        }
        frag.show(((Activity) context).getFragmentManager(), "ManagerZeek");
    }



    public ArrayList<String []> scanForMp3(){
        Log.i("AlertZeek","inside scanForMp3 in MainActivity");

        ArrayList<String[]> arr = new ArrayList<String[]>();
        Cursor list = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{ MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA},
                String.format("%s!=0", MediaStore.Audio.Media.IS_MUSIC),
                null,
                null);

        list.moveToFirst();
        while(!list.isAfterLast()) {
//            Log.i("ZeekMP3",String.format("Title=%s \n Data=%s",list.getString(0),list.getString(1)));
            arr.add(new String[]{list.getString(0), list.getString(1)});
            list.moveToNext();
//            Log.i("ZeekMP3", String.format("Title=%s \n Data=%s", arr.get(i)[0], arr.get(i)[1
        }

        list.close();
        return arr;
    }




    public static void buttonPressed_loadDetailView(final View rowView){

        Log.i("AlertZeek", "inside buttonPressed of MainActivity");


//        tagList.clear();
//        for (int i =0; i < frameName.length; i++) {
//            tagList.add(new frameObject(supportedID3Frames[i],frameName[i],-1,null));
//        }

        if (fileRead(mp3SongsList.get(rowView.getId())[1]) == true) {

            mainListView.setEnabled(false);

            String st;
            final View v = ((LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.detail_view, null);
            TextView tx = (TextView) v.findViewById(R.id.detail_view_rwTextView);
            ImageView im = (ImageView) v.findViewById(R.id.detail_view_rwImageView);
            Button btn = (Button) v.findViewById(R.id.detail_view_rwButton);
            final ListView listView = (ListView) v.findViewById(R.id.detail_view_rwDetailListView);
            listViewSecond = listView;

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("AlertZeek","Detail list click postition =" + position);
                    callDialogBox(position,1);
                }
            });


            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View btV) {

                    Log.i("AlertZeek", "inside onClick to reach back to mp3List");



                    mainListView.setVisibility(View.VISIBLE);

                    Animation DetailListAnim = new AnimationUtils().loadAnimation(context, R.anim.remove_details_anim);
                    Animation DetailViewAnim = new TranslateAnimation(
                            Animation.RELATIVE_TO_PARENT, 0.0f,
                            Animation.RELATIVE_TO_PARENT, 0.0f,
                            Animation.ABSOLUTE, -rowView.getY(),
                            Animation.RELATIVE_TO_PARENT, 0.0f);
                    DetailViewAnim.setDuration(500);
                    DetailViewAnim.setFillAfter(true);

                    v.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                    v.setY(rowView.getY());
                    v.startAnimation(DetailViewAnim);
                    listView.startAnimation(DetailListAnim);
                    android.os.Handler hand = new android.os.Handler();
                    hand.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mainframeLayout.removeView(v);
                            mainListView.setEnabled(true);
                            tagList.clear();
                            mainMenu.findItem(R.id.item_more).setVisible(false);
                            mainMenu.findItem(R.id.item_commitChanges).setVisible(false);
                            tagList.clear();
                            for (int i =0; i < frameName.length; i++) {
                                tagList.add(new frameObject(supportedID3Frames[i],frameName[i],-1,null));
                            }
                        }
                    }, 500);
                }
            });


            v.setX(rowView.getX());
            v.setY(0);
            st = ((TextView) rowView.findViewById(R.id.mp3_list_view_rwTextView)).getText().toString();
            tx.setText(st);
            im.setImageResource(im.getContext().getResources().getIdentifier("_" + st.substring(0, 1).toLowerCase(), "drawable", im.getContext().getPackageName()));



            mainframeLayout.addView(v);

            Animation DetailViewAnim;
            Animation DetailListAnim = new AnimationUtils().loadAnimation(context, R.anim.show_details_anim);


            DetailViewAnim = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.ABSOLUTE, rowView.getY(),
                    Animation.RELATIVE_TO_PARENT, 0.0f);
            DetailViewAnim.setDuration(500);
            DetailViewAnim.setFillAfter(true);

            listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));


//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,noString);

            detail_list_class arrayAdapter = new detail_list_class(context, R.layout.detail_list_view, tagList);
            listView.setAdapter(arrayAdapter);
            listView.setVisibility(View.VISIBLE);

            v.startAnimation(DetailViewAnim);
            listView.startAnimation(DetailListAnim);
            android.os.Handler hand = new android.os.Handler();

            hand.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mainListView.setVisibility(View.INVISIBLE);
                    mainMenu.findItem(R.id.item_more).setVisible(true);
                    mainMenu.findItem(R.id.item_commitChanges).setVisible(true);
                }
            }, 500);

        }else{

        }
    }




    public static boolean fileRead (String address){

        Log.i("AlertZeek","Inside fileRead of MainActivity with address " + address);
        File fileToRead = new File(address);
        InputStream streamInput = null;
        boolean flag = false;
        byte[] bytesRead = new byte[3] ;
        int numberOfBytes;
        char[] readString;
        int i ;
        try{
            streamInput = new BufferedInputStream( new FileInputStream(address) ) ;
            streamInput.read(bytesRead, 0, 3);
            if ((new String(bytesRead)).substring(0,3).compareTo("ID3") == 0){
                Log.i("AlertZeek","Contains compatible ID3 data --" + (new String(bytesRead)).substring(0,3));
                flag =true;
                readID3Tags(new BufferedInputStream( new FileInputStream(address) ) );
            }
            else{
                Log.i("AlertZeek","Contains IN-compatible ID3 data --"+ (new String(bytesRead)).substring(0,3));
                flag = false;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }




    public static void readID3Tags(InputStream inStream) throws IOException {

        Log.i("AlertZeek","Inside readID3Tags in MainActivity");
        byte[] readByte = new byte[1] ;
        long goneThruoghBytes = 0 ;
        byte extendedHeaderPresentBit;
        long tagSize = 0;

        byte [] frameID = new byte[4];
        long frameSize ;
        byte frameFlags ;
        int frameIndex ;

        goneThruoghBytes += inStream.skip(5);


        //read header flags especially extended header bit
        extendedHeaderPresentBit =(byte) ((byte)inStream.read() & (1<<7) );



        //read total tag size - 10 (size of header)
        tagSize |= ((byte)inStream.read());
        tagSize = tagSize << 7;
        tagSize |= ((byte)inStream.read());
        tagSize = tagSize << 7;
        tagSize |= ((byte)inStream.read());
        tagSize = tagSize << 7;
        tagSize |= ((byte)inStream.read());



        if (extendedHeaderPresentBit != 0)
        {
            Log.i("AlertZeek","extended header IS present and tagSize = " + tagSize);

        }else{
            Log.i("AlertZeek","extended header NOT Present and tagSize = " + tagSize);
        }

        while(goneThruoghBytes < tagSize){

            goneThruoghBytes += inStream.read(frameID);

            frameSize = 0;

            frameSize |= ((long)inStream.read());
            frameSize = frameSize << 8;

            frameSize |= ((long)inStream.read());
            frameSize = frameSize << 8;

            frameSize |= ((long)inStream.read());
            frameSize = frameSize << 8;

            frameSize |= ((long)inStream.read());


            goneThruoghBytes += inStream.skip(1);

            frameFlags = (byte) inStream.read();

            goneThruoghBytes += 5;

            frameIndex = isSupportedFrame (frameID,frameFlags);
            if ( frameIndex != -1){
                byte[] frameDescription = new byte[(int) frameSize];

                tagList.get(frameIndex).framePostion = goneThruoghBytes;
                tagList.get(frameIndex).frameDetail = "";
                goneThruoghBytes += inStream.read(frameDescription);
                parseFrame (new String(frameID),frameSize,frameDescription,frameIndex);

            } else{
                goneThruoghBytes += inStream.skip( frameSize );

            }

        }

    }



    public static int isSupportedFrame(byte[] frameID,byte frameFlags){

        String IDToCompare = new String(frameID);
        Log.i("AlertZeek","FrameID encountered is = " + IDToCompare);
        for (int i =0; i < supportedID3Frames.length; i++){
            if (supportedID3Frames[i].compareTo(IDToCompare) == 0){
                if ( ((frameFlags & (1<<7)) | (frameFlags & (1<<6))) == 0) {
                    Log.i("AlertZeek","Frame is supported");
                    return i;
                }else{
                    Log.i("AlertZeek","Compressed or encrypted frame");
                }
            }
        }
        return -1;
    }



    public static void  parseFrame (String frameID, long frameSize,  byte[] frameDescription, int frameIndex){

        Log.i("AlertZeek", "frame ID is = " + new String(frameID) + " frame size is = " + frameSize + " frame description = " + frameDescription);
        if(frameID.compareTo("TXXX") == 0) {
            Log.i("AlertZeek", "Reading "+ frameID + " frame");
        }
        if(frameID.charAt(0) == 'T') {
            Log.i("AlertZeek", "Reading " + frameID + " frame");
            tagList.get(frameIndex).frameDetail = textInformationParser(frameDescription);
        }

    }

    public static String textInformationParser (byte[] description) {

        if (description[0] == 1) {
            Log.i("AlertZeek","Text data encoded in 1");
            int length = (description.length - 3 ) / 2;
            if (length > 0) {
                byte[] transformed = new byte[length];
                for (int i = 0; i < length; i++) {
                    transformed[i] = description[(i * 2) + 3];
                }
                return new String(transformed);
            }
            else {
                return null;
            }

        } else if (description[0] == 0) {
            Log.i("AlertZeek","Text data encoded in 0");
            int length = (description.length - 1);
            if (length > 0) {
                byte[] transformed = new byte[length];
                for (int i = 0; i < length; i++) {
                    transformed[i] = description[i + 1];
                }
                return new String(transformed);
            } else {
                return null;
            }
        }

        return null;

    }


    @Override
    public void onDialogSaveClick(int position,String frameDetail) {
        Log.i("AlertZeek","Dialog frame data changed");
        tagList.get(position).frameDetail = frameDetail;
        listViewSecond.deferNotifyDataSetChanged();
        frameDataChangedFlag = true;
        Toast.makeText(this,"Change added to list",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogCancelClick(int position) {
        Log.i("AlertZeek","Dialog cancel click");
    }

    //    void openUpDetails (View v){
//
//        Log.i("Function", "Inside openDetails of MainActivity");
//
//        TextView txtView = (TextView) mainTable.getChildAt((v.getId() * 3)+1);
//
//        if (txtView.getVisibility() == View.VISIBLE){
//            txtView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0));
//            txtView.setVisibility(View.INVISIBLE);
//        }
//        else {
//            txtView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//            txtView.setVisibility(View.VISIBLE);
//        }
//
//    }




    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}




////            imVw = (ImageView) findViewById(R.id.rwImageView);
////            txView = (TextView) findViewById(R.id.rwTextView);
////            btn = (Button) findViewById(R.id.rwButton);
//
//        mainTable.setBackgroundColor(parseColor("white"));
//
//        for(int i=0; i<15; i++){
//
//            TableRow mainTableRow = new TableRow(this);
//            ImageView imVw = new ImageView(this);
//            TextView txView = new TextView(this);
//            Button btn = new Button(this);
//
//            mainTableRow.addView(findViewById(R.id.viewForRw));
////            ImageView imVw = (ImageView) findViewById(R.id.rwImageView);
////            TextView tx3 = (TextView) findViewById(R.id.rwTextView);
////            View vw = new View(this);
////            Button bt = (Button) findViewById(R.id.rwButton);
//
//
////            TableRow tbrew = new TableRow(this);
////            tbrw = tbrw_;
//
////            mainTableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
////            mainTableRow.setBackgroundColor(parseColor("white"));
//
//
//            txView.setText(str[i]);
////            txView.setLayoutParams(((TextView)findViewById(R.id.rwTextView)).getLayoutParams());
////            txView.setTextSize(((TextView)findViewById(R.id.rwTextView)).getTextSize());
////
////            imVw.setBackgroundColor(parseColor("black"));
//            imVw.setImageResource(R.drawable.character_a);
////            imVw.setScaleType(((ImageView) findViewById(R.id.rwImageView)).getScaleType());
////            imVw.setLayoutParams(findViewById(R.id.rwImageView).getLayoutParams());
////            imVw.setLayoutParams(new TableRow.MarginLayoutParams(5, 5));
////
////
////            vw.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
////            vw.setBackgroundColor(parseColor("black"));
////            vw.setPadding(20, 20, 20, 20);
////
////            btn.setText("butt");
////            btn.setLayoutParams(findViewById(R.id.rwButton).getLayoutParams());
////            btn.setBackgroundColor(findViewById(R.id.rwButton).getSolidColor());
//            btn.setId(i);
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    openUpDetails(v);
//                }
//            });
//
//
////            mainTableRow.addView(imVw);
////            mainTableRow.addView(txView);
////            mainTableRow.addView(btn);
//            mainTable.addView(mainTableRow);
