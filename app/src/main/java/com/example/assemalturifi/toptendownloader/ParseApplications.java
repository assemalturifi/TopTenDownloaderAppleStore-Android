package com.example.assemalturifi.toptendownloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

//step9
//in this class we will create the code to parse the XML, in other words
// we will be extracting various of fields that we are interested in and manipulate
// it so that those values can be extracted and we can store thm in the individual application.

public class ParseApplications {
    //step9
    private static final String TAG = "ParseApplications";
    private ArrayList<FeedEntry> applications;//so this list will store all the apps that it finds in the xml data
//step9
    //constructor
    public ParseApplications() {
        this.applications = new ArrayList<>();
    }
//step9
    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }


    //step10
    //this method will parse/manipulate the xml data string and create list
    // of applications that we're gonna store in our applications arrayList
    //the xml data will be sent to this method
    //this method return type boolean value, if there is a problem it will
    // return false to indicate that data coulnt be parsed
    public boolean parse(String xmlData) {
        boolean status=true;// set true, if exception is thrown, it will be false
        FeedEntry currentRecord=null;
        boolean inEntry=false;// there are two names in the apple rss feed xml we dont want that, this is for that
        String textValue="";//it stores the value of the current tag

        try {
            //the lines between XmlPull to xxp.setInput()-is responsible for setting up the
            // java xml parser that will do all the hard work of making sense of the xml for us.
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:

//                        Log.d(TAG, "parse: Starting tag for " + tagName);
                        if ("entry".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new FeedEntry();

                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
//                        Log.d(TAG, "parse: Ending tag for " + tagName);
                        if (inEntry) {
                            if ("entry".equalsIgnoreCase(tagName)) {
                                applications.add(currentRecord);
                                inEntry = false;
                            } else if ("name".equalsIgnoreCase(tagName)) {
                                currentRecord.setName(textValue);
                            } else if ("artist".equalsIgnoreCase(tagName)) {
                                currentRecord.setArtist(textValue);
                            } else if ("releaseDate".equalsIgnoreCase(tagName)) {
                                currentRecord.setReleaseData(textValue);
                            } else if ("summary".equalsIgnoreCase(tagName)) {
                                currentRecord.setSummary(textValue);
                            } else if ("image".equalsIgnoreCase(tagName)) {
                                currentRecord.setImageURL(textValue);
                            }
                        }
                        break;

                    default:
                        //nothing else to do
                }
                eventType = xpp.next();

            }
            //looping through the application list once all the xml has been processed

//            for (FeedEntry app : applications) {
//                Log.d(TAG, "**********");
//                Log.d(TAG, app.toString());
//            }
            //the next step is in mainActivity, on onPostExcute
        }
        catch (Exception e){
            status=false;
            e.printStackTrace();
        }
        return status;
    }
}
