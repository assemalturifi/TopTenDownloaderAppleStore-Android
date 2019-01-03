package com.example.assemalturifi.toptendownloader;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    //step 14
    private ListView listApps;

    //step33
    //in this URL i used "%d" instead of the number, this "%d" specifying an integer value
    // that will be replaced by an actual value using the string.format(), and this method
    // takes a string containing a specfial code like %d and the number of values are used to replace the format codes
    //so string.fromat will change %d with the number
    //so in on create change the URL and everywhere
    private String feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
    private int feedLimit=10;

    //step42, next step in downloadURL()
    private String feedCachedURL = "INVALIDATED";
    public static final String STATE_URL = "feedURL";
    public static final String STATE_LIMIT="feedLimit";





    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //step15
        listApps = findViewById(R.id.listView);

//        //step4
//        Log.d(TAG, "onCreate: starting AsyncTask");
//
//        DownloadData downloadData = new DownloadData();
//        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=25/xml");
//        Log.d(TAG, "onCreate: done");


        //step32
        //commented out the above code
//        downloadURL("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=25/xml");
        //next step in the fields section
        //we have 3 options in the menu bar, we are gonna add two more
        //these two options will kepp most of the URL unchanged, like one we select one of them for example
        // 10 apps, you can get fee or paid apps for 10, if you select 25 you can get free or paid apps for 25
        //these two options will kepp most of the URL unchanged,that they just actually alter the limit paramater
        //so that means we have to store the base of the URL as a class field rather than a private
        // variable on the option item selected methdod
        //we need ti store the current limit size

        //step47
        if (savedInstanceState != null) {
            feedURL = savedInstanceState.getString(STATE_URL);
            feedLimit = savedInstanceState.getInt(STATE_LIMIT);

        }

        //step34
        downloadURL(String.format(feedURL,feedLimit));



    }

    //step29
    //when its time to inflate the activities menu. That means create te menu objects from the  xml file
    // so by this method we displayed the menu option on the screen, but the menu doesnt
    // actually do anything yet so we have to write some codes specify what happens when the various items are selected
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeds_menu,menu);

        //step40
        //step41 in feed_menu, just added the refresh item
        //the step42 in the fields
        if(feedLimit==10){
            menu.findItem(R.id.top10).setChecked(true);
        }
        else{
            menu.findItem(R.id.top25).setChecked(true);
        }
        return true;
    }

    //step30
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.mnuFree:
                feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
                break;
            case R.id.mnuPaid:
                feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml";
                break;
            case R.id.mnuSongs:
                feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml";
                break;
                //step36
            case R.id.top10:
                //step37
            case R.id.top25:
                //step38
                if(!item.isChecked()){
                    item.setChecked(true);
                    feedLimit=35-feedLimit;
                    Log.d(TAG, "onOptionsItemSelected: " + item.getTitle() + " setting feedLimit to " + feedLimit);
                }
                else{
                    //tep39
                    Log.d(TAG, "onOptionsItemSelected: "+item.getTitle()+" feedLimit unchanged");
                }
                break;

                //step44

            case R.id.mnuRefresh:
                feedCachedURL = "INVALIDATED";
                break;

            default:
                return super.onOptionsItemSelected(item);


        }//step35
        downloadURL(String.format(feedURL,feedLimit));
//        downloadURL(feedURL);
        return true;

    }
    //step31
    //next step on onCreate
//    private void downloadURL(String feedURL){
//
//        Log.d(TAG, "downloadURL: starting AsyncTask");
//
//        DownloadData downloadData = new DownloadData();
//        downloadData.execute(feedURL);
//        Log.d(TAG, "downloadURL: done");
//
//
//    }

    //step43

    private void downloadURL(String feedURL){
        if(!feedURL.equalsIgnoreCase(feedCachedURL)){
            Log.d(TAG, "downloadURL: starting AsyncTask");

            DownloadData downloadData = new DownloadData();
            downloadData.execute(feedURL);
            Log.d(TAG, "downloadURL: done");

            feedCachedURL = feedURL;
        }
        else{
            Log.d(TAG, "downloadURL: URL not changed");
        }


    }

    //step 1
    //a new class that extends AsyncTask class
    //the first paramater is type string of URL to the RSS feed,
    //it is the address of the RSS feed(the URL)
    // second paramater is used if you want to display a progress bar,
    // but our program will download very small amount of time thats
    // why Void otherwise you should give an indication to the user how long would it take
    //3.parameter contains all the xml after it's been downloaded
    //the next step in manifest file:
    //adding (android:usesCleartextTraffic="true") to manifest file
    private class DownloadData extends AsyncTask<String,Void,String>{
        private static final String TAG = "DownloadData";

        //step2
        @Override
        //the three... below is called variable-length argument(ellipsis).
        // it allows you to provide several values for the parameter or the
        // argument you provide must be of the same type, so here all of them
        // must be string,(what i mean you can provide multiple URL's and run them
        // in the background but this would not be a good idea, it is better onCreate to
        // provide different instances for eact URL)
        // we could pass in several URL's and have them downloaded
        //it like an array
        //in doInbackround all the code will run in the background
        // asynchronously in a background thread
                                            //strings=URL
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with "+strings[0]);

            String rssFeed = downloadXML(strings[0]);
            if(rssFeed==null){
                Log.e(TAG, "doInBackground: Error downloading");
            }
            return rssFeed;
        }

        //step3
        //step4 in onCreate
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Log.d(TAG, "onPostExecute: parameter is "+s);
            //step 11
            // we finished off parsing of the XML and created a list of entry
            // objects with data we wanted in the correct fields. now we need to present
            // those information in a useful way on the device screen.
            // the next step is in layout
            ParseApplications parseApplications = new ParseApplications();
            parseApplications.parse(s);

            //step16
            // now we will connect the data to the listView by using an arrayAdapter
            // our data is stored in an arrayList, which we can get from the parseApp class
            //by calling its getApp()
            //now if you run the app, you will see all the top ten apps will be listend in a scrollable list
            //until here when we run the app the top ten apps will be shown but the format is sucks, thats why we have to create our own adapter
            //next step in list_record layout
//            ArrayAdapter<FeedEntry> arrayAdapter = new ArrayAdapter<FeedEntry>(MainActivity.this,R.layout.list_item,parseApplications.getApplications());
//            listApps.setAdapter(arrayAdapter);

            //step23 we commented out the above two lines

            FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this,R.layout.list_record,parseApplications.getApplications());
            listApps.setAdapter(feedAdapter);
            //next step in FeedAdapter


        }

        //the next step is in FeedEntry class
        //step 5
        //this method will take care of downloading the feed and
        // it will return a string containing the xml
        //this method will read from a stream of data such as data over the
        // internet or data stored in a file or the local android device
        //will open a HTTP connection which we use to access the stream of data coming over
        //the internet from a URL. this connection provides inputStream
        // whenever you're dealing with a stream that coming from a slow device such
        // as disk drive or an internet connection, it is usually a good idea to use a bufferedReader
        private  String downloadXML(String urlPath) {
            //this because we're going to be appending to a string a
            // lot as we read characters from inputStream and StringBuilder as more
            // efficient than strings
            StringBuilder xmlResult = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: The response code was " + response);
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                //buffered reader will read the xml
                BufferedReader reader = new BufferedReader(inputStreamReader);
                //we could have written the 3line of code above like;
                //BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                //bufferedReader reads chars, thats why the inputBuffer is variable type of char

                int charsRead;
                char[] inputBuffer = new char[500];

                while(true){
                    charsRead = reader.read(inputBuffer);
                    //end of stream of data
                    if(charsRead<0){
                        break;
                    }
                    if(charsRead>0){
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                reader.close();

                return xmlResult.toString();

            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadXML: Invalid URL "+e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "downloadXML: IO Exception reading data: " + e.getMessage());
            }catch (SecurityException e){
                Log.e(TAG, "downloadXML: Security Exception. Needs permission? "+e.getMessage() );
//                e.printStackTrace();
            }

            //we gave permission to internet in the manifest
            //no we got some data coming from out itunes RSS feed, now it is time to start doing somehhting with that data
            //the next step is go to feedEntery class
            return null;
        }
    }
    // step45
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(STATE_URL, feedURL);
        outState.putInt(STATE_LIMIT,feedLimit);

        super.onSaveInstanceState(outState);
    }

    //step46
    // onRestoreInstanceState is called after the onCreate method, but we do the initial download in onCreate.
    //so when the device is rotated, we need to retrieve the saved Bundle in the onCreate method rather than in onRestoreInstanceState.
    //so we dont need to implement onRestoreInstanceState.
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//    }
}
