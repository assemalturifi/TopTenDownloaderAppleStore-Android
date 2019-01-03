package com.example.assemalturifi.toptendownloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

//step18
public class FeedAdapter extends ArrayAdapter {
    //step19
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    //LayoutInflater instantiate a layout xml file into its corrosponding view objects
    private final LayoutInflater layoutInflater;
    private List<FeedEntry> applications;

    //step20
    public FeedAdapter(Context context, int resource, List<FeedEntry> applications) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.applications = applications;
    }

    //step21
    @Override
    public int getCount() {
        return applications.size();
    }

//    //step22
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = layoutInflater.inflate(layoutResource, parent, false);
//        TextView textName = view.findViewById(R.id.tvName);
//        TextView textArtist = view.findViewById(R.id.tvArtist);
//        TextView textSummary = view.findViewById(R.id.tvSummary);
//
//        FeedEntry currentApp = applications.get(position);
//
//        textName.setText(currentApp.getName());
//        textArtist.setText(currentApp.getArtist());
//        textSummary.setText(currentApp.getSummary());
//        return view;
//        //nextStep in mainActivity in onPostExecte
//    }


    //step24
    //this custom adapter lets us use our own layout so that we could display different widgets in
    // the listView rather than having to display all the data in the single textView widget that the
    // basic arrayAdapter allows. and this gives us more control and appears in eeach line of the ListView
    //our adapter isnt very efficient. now if you look at the getView(), it inflates a new view every time its called.
    //that means that if we used it to display a thousand items it would create one thousand views and thats
    // very costly in terms of both time and memory. the findViewById is very slow because it has to scan the layout
    //from the start each time its called, checking to see if each widget is the one we want and it gets worse than that, if
    //you start scrolling up and down those thousand items itwill create a new view everytime.
    //so in next step we will make FeedAdapter more efficient, you can check the memory and CPu when you click profiler next to the Logcat

//    //step25
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        if(convertView==null){
//            convertView = layoutInflater.inflate(layoutResource, parent, false);
//        }
//
//        TextView textName = convertView.findViewById(R.id.tvName);
//        TextView textArtist = convertView.findViewById(R.id.tvArtist);
//        TextView textSummary = convertView.findViewById(R.id.tvSummary);
//
//        FeedEntry currentApp = applications.get(position);
//
//        textName.setText(currentApp.getName());
//        textArtist.setText(currentApp.getArtist());
//        textSummary.setText(currentApp.getSummary());
//        return convertView;
//
//    }

    //step26
    // we will make the getVew more efficient
    private static class ViewHolder{
       final  TextView name;
       final TextView artist;
       final TextView summary;


       ViewHolder(View v){
           this.name = v.findViewById(R.id.tvName);
           this.artist = v.findViewById(R.id.tvArtist);
           this.summary = v.findViewById(R.id.tvSummary);
       }
    }
    //step27
    //now it is more efficient and more faster and use less memory
    //next step creating a menu folder in res diractory
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;


        if(convertView==null){
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        FeedEntry currentApp = applications.get(position);

        viewHolder.name.setText(currentApp.getName());
        viewHolder.artist.setText(currentApp.getArtist());
        viewHolder.summary.setText(currentApp.getSummary());
        return convertView;

    }
}
