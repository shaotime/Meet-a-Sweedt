package com.untitledapps.meetasweedt;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    //Nav variables
    private ListView mDrawerList;
    private DrawerAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etAge = (EditText) findViewById(R.id.etAge);
        final EditText etCountry = (EditText) findViewById(R.id.etCountry);
        final TextView titleMsg = (TextView) findViewById(R.id.twWelcome2);
        final TextView titleMsg2 = (TextView) findViewById(R.id.twWelcome2);

        final Button buttonMatches = (Button) findViewById(R.id.buttonMatches);
        final Button buttonChat = (Button) findViewById(R.id.buttonChat);
        final Button mapButton = (Button) findViewById(R.id.mapButton);
        final Button buttonMatch = (Button) findViewById(R.id.buttonMatch);

        //////MATCHES
        buttonMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMatchesActivity();
            }
        });

        //////MATCHES
        buttonMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMatchingActivity();
            }
        });

        //////MATCHES
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChatActivity();
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goToMapActivity();
            }
        });

        //String selCountry = SignUpActivity.spCountry.getSelectedItem().toString();


        //Nav
        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setupDrawer();



    }

    private void goToMatchingActivity() {
        Intent intent = new Intent(this, MatchingActivity.class);
        startActivity(intent);
    }

    private void goToMatchesActivity() {
        Intent intent = new Intent(this, MatchesActivity.class);
        startActivity(intent);
    }

    private void goToChatActivity() {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    private void goToMapActivity(){
        Intent intent = new Intent(this, FikaMapActivity.class);
        startActivity(intent);
    }

    //Nav classes
    private void addDrawerItems() {
        ArrayList<String> activities = new ArrayList<String>();
        activities.add("My Profile");
        activities.add("Chat");
        activities.add("Match");
        activities.add("Map");
        mAdapter = new DrawerAdapter(this, activities);
        mDrawerList.setAdapter(mAdapter);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setLogo(R.mipmap.ic_drawericon);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.intent_action) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
