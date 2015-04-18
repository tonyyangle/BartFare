package com.example.tony.bartfare;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Tony on 15/4/14.
 */
public class ListActivity extends Activity {
    ListView listView;
//    public Stations stations = new Stations();

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = (ListView) findViewById(R.id.listView);
//        String[] list = getResources().getStringArray(R.array.stations_array);

        final String urlKey = "&key=MW9S-E7SL-26DU-VV8V";
        String url = "http://api.bart.gov/api/stn.aspx?cmd=stns";
        final String urlMain = url +urlKey;
        new BartNameCall().execute(urlMain);


//        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
//        listView.setAdapter(adapter);
    }
    class BartNameCall extends AsyncTask<String,Void,ArrayList<String>> {

        ArrayList<String> list = new ArrayList<String>();
        ArrayList<String> list1 = new ArrayList<String>();
        ArrayList<String> list2 = new ArrayList<String>();
        String fuNa = null;
        String abbr = null;
        String city = null;
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
//                                list1.add(fuNa);
                            }
                            else if (name.equals("city")) {
                                city = parser.nextText();
//                                list2.add(abbr);
                                list.add(fuNa + " - (" +city + ")");
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
            ArrayAdapter dataAdapter = new ArrayAdapter(ListActivity.this,android.R.layout.simple_list_item_1, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
//            ArrayAdapter dataAdapter1 = new ArrayAdapter(ListActivity.this,android.R.layout.simple_spinner_item, list2);
//            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinner1.setAdapter(dataAdapter);
//            spinner2.setAdapter(dataAdapter);
            listView.setAdapter(dataAdapter);
//            stations.setListA(list1);
//            stations.setListB(list2);

        }
    }
}
