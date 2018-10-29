package com.untitledapps.meetasweedt;

import android.widget.ListView;

import java.util.ArrayList;

import static com.untitledapps.meetasweedt.R.id.matchesList;

/**
 * Created by User on 10/12/2016.
 */

public class Matches2Activity {
    ListView listView;

    @Override
    protected void onCreate{
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_matches);
        listView = (ListView) findViewById(matchesList);



        ArrayList<String>interests = new ArrayList<>();
        interests.add("soccer");
        tempList.add(kevin);
        tempList.add(kevin2);


    }


}
