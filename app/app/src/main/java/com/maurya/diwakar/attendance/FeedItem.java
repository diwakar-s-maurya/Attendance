package com.maurya.diwakar.attendance;

/**
 * Created by diwakar on 4/8/15.
 */
public class FeedItem {
    boolean myAttRecord;
    String myRollString;

    FeedItem(boolean attRecord, String roll_string)
    {
        myAttRecord = attRecord;
        myRollString = roll_string;
    }
}