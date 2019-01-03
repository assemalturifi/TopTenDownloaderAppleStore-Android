package com.example.assemalturifi.toptendownloader;

//step6
// in this class we are gonna parse out(cozumlemek) the individual fields from the xml
// we are going to hold in info that coming out of this xml
//the next step in ParseApplication
//
public class FeedEntry {
    private String name;
    private String artist;
    private String releaseData;
    private String summary;
    private String imageURL;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getReleaseData() {
        return releaseData;
    }

    public void setReleaseData(String releaseData) {
        this.releaseData = releaseData;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    //step7
    //next step in parseApp class
    @Override
    public String toString() {
        return "name=" + name + '\n' +
                ", artist=" + artist + '\n' +
                ", releaseData=" + releaseData + '\n' +
                ", imageURL=" + imageURL + '\n';
    }
    // here entry class is created and every time we get a
    // new entry in the data we're gonna create a new feedEntry object
    //and set the values that wre find in that XML data
}

