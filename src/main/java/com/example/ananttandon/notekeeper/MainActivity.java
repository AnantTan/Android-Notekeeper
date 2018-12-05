package com.example.ananttandon.notekeeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private NoteRecyclerAdapter noteRecyclerAdapter;
    private RecyclerView recyclerNotes;
    private RecyclerView recyclerItems;
    private LinearLayoutManager notesLayoutManager;
    private CourseRecyclerAdapter courseRecyclerAdapter;
    private GridLayoutManager coursesLayoutManager;
    private NotekeeperOpenHelper mDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDbOpenHelper = new NotekeeperOpenHelper(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,NoteActivity.class));
            }
        });

        PreferenceManager.setDefaultValues(this,R.xml.pref_general,false);
        PreferenceManager.setDefaultValues(this,R.xml.pref_notification,false);
        PreferenceManager.setDefaultValues(this,R.xml.pref_data_sync,false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initializeDisplayContent();
    }

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        noteRecyclerAdapter.notifyDataSetChanged();//each time activity resume refresh it to show latest data
        updateNavHeader();
    }

    private void updateNavHeader() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);//reference to the navigation view
        View headerView = navigationView.getHeaderView(0);//reference to navigation view header
        TextView textUserName = (TextView) headerView.findViewById(R.id.text_user_name);
        TextView textEmailAddress = (TextView) headerView.findViewById(R.id.text_email_address);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = pref.getString("text_user_name","");
        String emailAddress = pref.getString("text_email_address","");
        textUserName.setText(userName);
        textEmailAddress.setText(emailAddress);
    }

    private void initializeDisplayContent() {
      //  DataManager.loadFromDatabase(mDbOpenHelper);
        recyclerItems = findViewById(R.id.list_items);
        //linear experience to the list
        notesLayoutManager = new LinearLayoutManager(this);
        coursesLayoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.course_grid_span));
        //adapter holding our list of notes and associated that adapter with our recycler view
        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        noteRecyclerAdapter = new NoteRecyclerAdapter(this,notes);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        courseRecyclerAdapter = new CourseRecyclerAdapter(this,courses);


        displayNotes();
    }

    private void displayNotes() {
        recyclerItems.setLayoutManager(notesLayoutManager);
        recyclerItems.setAdapter(noteRecyclerAdapter);
     //  SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
        selectNavigationMenuItem(R.id.nav_notes);
    }

    private void selectNavigationMenuItem(int id) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);//reference to navigation view
        Menu menu = navigationView.getMenu();//this menu has the reference to the navigation view within the menu itself
        menu.findItem(id).setChecked(true);//display the currently select option
    }

    private void displayCourses()
    {
        //associate different layout manager and adapter with our recycler view
        recyclerItems.setLayoutManager(coursesLayoutManager);
        recyclerItems.setAdapter(courseRecyclerAdapter);
        selectNavigationMenuItem(R.id.nav_courses);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


     @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notes) {
            displayNotes();
        } else if (id == R.id.nav_courses) {
            displayCourses();

        } else if (id == R.id.nav_share) {
           // handleSelection(R.string.share_message);///extracted resource string massage
            handleShare();
        } else if (id == R.id.nav_send) {
            handleSelection(R.string.send_message);///
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleShare() {
        View view = findViewById(R.id.list_items);
        //anytime user selects the value from the navigation drawer share we see the current value for that preference
        Snackbar.make(view,"Share to".concat(
                PreferenceManager.getDefaultSharedPreferences(this).getString("user_favorite_social",""))
                ,Snackbar.LENGTH_LONG).show();

    }

    private void handleSelection(int message_id) {
        //display the message
        View view = findViewById(R.id.list_items);
        //pass info in order to display it
        Snackbar.make(view,message_id,Snackbar.LENGTH_LONG).show();


    }
}
