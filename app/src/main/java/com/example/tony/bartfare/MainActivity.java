package com.example.tony.bartfare;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends Activity {

    public Spinner spinner1;
    public Spinner spinner2;
    public Button button;
    public Button button2;
    public TextView textview;
    public TextView textview3;
    public TextView textview4;

    public Stations stations = new Stations();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        textview = (TextView) findViewById(R.id.textView2);
        textview3 = (TextView) findViewById(R.id.textView3);
        textview4 = (TextView) findViewById(R.id.textView4);

        //        final String[] array = getResources().getStringArray(R.array.stations_array);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_dropdown_item, array);
//        spinner1.setAdapter(adapter);
//        spinner2.setAdapter(adapter);

        final String urlKey = "&key=MW9S-E7SL-26DU-VV8V";
        String url = "http://api.bart.gov/api/stn.aspx?cmd=stns";
        final String urlMain = url +urlKey;
        new BartNameCall().execute(urlMain);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stations.setFullNa(spinner1.getSelectedItem().toString());
                stations.setFullNa2(spinner2.getSelectedItem().toString());

                String get1 = stations.change(stations.getFullNa());
                String get2 = stations.change(stations.getFullNa2());
                String url1 = get1;
                String url2 = get2;
                String url = "http://api.bart.gov/api/sched.aspx?cmd=fare";
                String finalUrl = url + "&orig="+ url1 + "&dest="+ url2 +"&date=today" + urlKey;
                String timeUrl = "http://api.bart.gov/api/etd.aspx?cmd=etd&orig=mlbr&key=MW9S-E7SL-26DU-VV8V";
                new BartApiCall().execute(finalUrl);
//                new BartTimeCall().execute(timeUrl);
            }
        });
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(this, ListActivity.class);
//                startActivity(intent);
//            }
//        });
    }
    public void submitMessage(View view) {
        Intent intent = new Intent(this,ListActivity.class);
        startActivity(intent);
    }

public void showFare(String str) {
    textview.setText(str);
}

    public void showTime(String str) {
        textview3.setText(str);
    }

    class BartNameCall extends AsyncTask<String,Void,ArrayList<String>>{

        ArrayList<String> list = new ArrayList<String>();
        ArrayList<String> list1 = new ArrayList<String>();
        ArrayList<String> list2 = new ArrayList<String>();
        String fuNa = null;
        String abbr = null;
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            String urlString= params[0];

            InputStream in = null;

            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
            } catch (Exception e ) {
                System.out.println(e.getMessage());

            }


            XmlPullParserFactory pullParserFactory;

            try {
                pullParserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = pullParserFactory.newPullParser();

                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                int eventType = parser.getEventType();


                while( eventType!= XmlPullParser.END_DOCUMENT) {
                    String name = null;

                    switch(eventType)
                    {
                        case XmlPullParser.START_TAG:
                            name = parser.getName();

                            if( name.equals("name")) {
                                fuNa = parser.nextText();
                                list1.add(fuNa);
                            }
                            else if (name.equals("abbr")) {
                                abbr = parser.nextText();
                                list2.add(abbr);
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            break;
                    }

                    eventType = parser.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return list;

        }

        @Override
        protected void onPostExecute(ArrayList<String> arrayList) {
            ArrayAdapter dataAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_spinner_item, list1);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ArrayAdapter dataAdapter1 = new ArrayAdapter(MainActivity.this,android.R.layout.simple_spinner_item, list2);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(dataAdapter);
            spinner2.setAdapter(dataAdapter);
            stations.setListA(list1);
            stations.setListB(list2);

        }
    }

//
     class BartApiCall extends AsyncTask<String,Void,String>{
        String fare = null;
    @Override
    protected String doInBackground(String... params) {
        String urlString= params[0];

        InputStream in = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
        } catch (Exception e ) {
            System.out.println(e.getMessage());

        }


        XmlPullParserFactory pullParserFactory;

        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            int eventType = parser.getEventType();


            while( eventType!= XmlPullParser.END_DOCUMENT) {
                String name = null;

                switch(eventType)
                {
                    case XmlPullParser.START_TAG:
                        name = parser.getName();

                        if( name.equals("fare")) {
                            fare = parser.nextText();
                        }
                     break;

                    case XmlPullParser.END_TAG:
                        break;
                }

                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

       return fare;
    }

        @Override
        protected void onPostExecute(String str) {
            str = fare;
            showFare(str);
        }
    }
    class BartTimeCall extends AsyncTask<String,Void,String>{
        String time = null;
        @Override
        protected String doInBackground(String... params) {
            String urlString= params[0];

            InputStream in = null;

            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
            } catch (Exception e ) {
                System.out.println(e.getMessage());

            }


            XmlPullParserFactory pullParserFactory;

            try {
                pullParserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = pullParserFactory.newPullParser();

                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                int eventType = parser.getEventType();


                while( eventType!= XmlPullParser.END_DOCUMENT) {
                    String name = null;

                    switch(eventType)
                    {
                        case XmlPullParser.START_TAG:
                            name = parser.getName();

                            if( name.equals("minutes")) {
                                time = parser.nextText();
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            break;
                    }

                    eventType = parser.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return time;
        }

        @Override
        protected void onPostExecute(String str) {
            str = time;
            showTime(str);
        }
    }
}

