package com.untitledapps.meetasweedt;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;



import android.widget.ListView;

import com.example.NetworkShared.RequestAddMatch;
import com.example.NetworkShared.RequestAllPeople;
import com.example.NetworkShared.RequestUpdateLocation;
import com.example.NetworkShared.Response;
import com.example.NetworkShared.ResponseAllPeople;
import com.untitledapps.Client.RequestBuilder;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;

public class MatchingActivity extends AppCompatActivity {
    GridView gridView;
    static Activity context;
    static View matchingProfileView;

    //Nav variables
    private ListView mDrawerList;
    private DrawerAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    //TODO  get logged in person
    Person user = new Person(false, 19, "Arvid Hast", "sweden", 58, 13, new ArrayList<String>(Arrays.asList("computers", "staring into the abyss", "code", "stocks", "not chilling")), "asd", 21);

    ArrayList<Person> matchesList = new ArrayList<Person>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_matching);


        matchesList = getAllPeopleDb(this, user);

        /*
        Person p2 = new Person(false, 20, "Niklas Jonsson", "sweden", 57.697724f, 11.988327f, new ArrayList<String>(Arrays.asList("computers", "speakers", "wasting money", "code", "chillin")), "nj");
        Person p3 = new Person(false, 21, "Ajla Cano", "sweden", 57.677724f, 11.968327f, new ArrayList<String>(Arrays.asList("computers", "learning android studio", "code", "unknown")), "ac");
        Person p4 = new Person(true, 20, "Fredrik Lindevall", "Syria", 57.72509804f, 11.77373512f, new ArrayList<String>(Arrays.asList("computers", "code", "ida", "stocks", "chillin")), "fli");
        Person p5 = new Person(true, 20, "Daniel Hesslow", "usa", 57.687724f, 11.968327f, new ArrayList<String>(Arrays.asList("computers", "climbing", "code", "not chilling")), "dh");
        Person p6 = new Person(true, 20, "Eric Shao", "russia", 57.697724f, 11.978327f, new ArrayList<String>(Arrays.asList("computers", "djing", "code", "unknown")), "dj");
        context = this;

        matchesList.add(p2);
        matchesList.add(p3);
        matchesList.add(p4);
        matchesList.add(p5);
        matchesList.add(p6);*/

        populateMatchingView(matchesList, user);

        matchingProfileView = getLayoutInflater().inflate(R.layout.activity_matching_profile, null);
        System.out.println("hey " + matchingProfileView.findViewById(R.id.matchProcent));
        System.out.println("hey " + matchingProfileView.findViewById(R.id.matchProcent));

        initiateLocationServices(user);

        //Nav
        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setupDrawer();
    }

    public void populateMatchingView(ArrayList<Person> personArrayList, Person user) {
        gridView = (GridView) this.findViewById(R.id.gridView);
        gridView.setAdapter(new MatchViewAdapter(this, personArrayList, user));
    }

    public static void viewMatchingProfile(Person person, Person matchingPerson) {
        ((Activity) context).setContentView(R.layout.activity_matching_profile);
        //((TextView) findViewById(R.id.matchProcent)).setText(Float.toString(person.getMatchScore(matchingPerson) * 100) + "%");
        //((TextView) findViewById(R.id.distance)).setText(Float.toString(person.getDistanceTo(matchingPerson)) + "Km");
        //((TextView) context.findViewById(R.id.matchProcent)).setText(Float.toString(person.getMatchScore(matchingPerson) * 100) + "%");
        //((TextView) matchingProfileView.findViewById(R.id.distance)).setText(Float.toString(person.getDistanceTo(matchingPerson)) + "Km");
    }

    public void initiateLocationServices(final Person person) {

        LocationManager locationManager;
        LocationListener locationListener;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("matchingact", "long: " + location.getLongitude() + " lat: " + location.getLatitude());

                person.setLatitude((float)location.getLongitude());
                person.setLongitude((float)location.getLatitude());

                final RequestUpdateLocation req =
                        new RequestUpdateLocation(
                                person.getUser_id(),
                                (float)location.getLongitude(),
                                (float)location.getLatitude());

                RequestBuilder requestBuilder = new RequestBuilder(context, new RequestBuilder.Action() {
                    @Override
                    public void PostExecute() {
                        if (req.was_successfull()) {
                            System.out.println("successfully updated location of user");
                        } else {
                            System.out.println("error when updating position");
                        }
                    }
                });

                requestBuilder.addRequest(req);
                requestBuilder.execute();


                //update gui after gps coordinates are updated (to update distance)
                //assums matchesList is already populated
                populateMatchingView(matchesList, user);
            }

            @Override
            public void onProviderDisabled(String s) {
                //TODO notify
                //Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS); ??
                //startActivity(i);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //TODO maybe
            }

            @Override
            public void onProviderEnabled(String s) {
                //TODO maybe
            }


        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //TODO lower update freq after working 100%
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);

        //locationManager.removeUpdates(locationListener);
    }

    public static void requestAddMatch(Context context, int matchId, int userId){
        final RequestAddMatch req = new RequestAddMatch(matchId, userId);

        final ArrayList<Person> peopleFromDatabase = new ArrayList<>();

        RequestBuilder requestBuilder = new RequestBuilder(context, new RequestBuilder.Action() {
            @Override
            public void PostExecute() {
                if (req.was_successfull()) {
                    Response response = req.getResponse();
                    if (response != null) {
                        System.out.println("addmatch request successfull");

                        /*for(int i = 0; i < response.getIsLearner().size(); i++) {
                            String interestsString = response.getInterestsString().get(i);

                            ArrayList<String> interests = new ArrayList<>();
                            String[] parts = interestsString.split(",");

                            for(String part: parts) {
                                interests.add(part);
                            }

                            peopleFromDatabase.add(new Person(
                                    response.getIsLearner().get(i),
                                    response.getAge().get(i),
                                    response.getName().get(i),
                                    response.getOrginCountry().get(i),
                                    response.getLongitude().get(i),
                                    response.getLatitude().get(i),
                                    interests,
                                    response.getUsername().get(i),
                                    response.getUser_id().get(i)
                            ));*/

                    }

                    //System.out.println("playerStrings(1): " + peopleStrings.get(2).toString());
                } else {
                    System.out.println("fail adding match");
                }
            }
        });


        requestBuilder.addRequest(req);
        requestBuilder.execute();
    }

    public static ArrayList<Person> getAllPeopleDb(Activity activity, Person user) {
        final RequestAllPeople req = new RequestAllPeople(user.getUsername());

        final ArrayList<Person> peopleFromDatabase = new ArrayList<>();

        RequestBuilder requestBuilder = new RequestBuilder(activity, new RequestBuilder.Action() {
            @Override
            public void PostExecute() {
                if (req.was_successfull()) {
                    ResponseAllPeople response = req.getResponse();

                    if(response != null) {
                        System.out.println("getpeople request successfull");

                        ArrayList <String> peopleStrings = response.getAllPeopleString();

                        for(int i = 0; i < response.getIsLearner().size(); i++) {
                            String interestsString = response.getInterestsString().get(i);

                            ArrayList<String> interests = new ArrayList<>();
                            String[] parts = interestsString.split(",");

                            for(String part: parts) {
                                interests.add(part);
                            }

                            Person person = new Person(
                                    response.getIsLearner().get(i),
                                    response.getAge().get(i),
                                    response.getName().get(i),
                                    response.getOrginCountry().get(i),
                                    response.getLongitude().get(i),
                                    response.getLatitude().get(i),
                                    interests,
                                    response.getUsername().get(i),
                                    response.getUser_id().get(i)
                            );

                            peopleFromDatabase.add(person);



                        }

                        //System.out.println("playerStrings(1): " + peopleStrings.get(2).toString());
                    } else {
                        System.out.println("no response when fetching people from database");
                    }
                } else {
                    System.out.println("getpeople request failed");
                }
            }
        });


        requestBuilder.addRequest(req);
        requestBuilder.execute();

        return peopleFromDatabase;

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