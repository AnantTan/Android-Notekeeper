package com.example.ananttandon.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {
    private NoteRecyclerAdapter noteRecyclerAdapter;

    //private ArrayAdapter<NoteInfo> mAdapterNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoteListActivity.this,NoteActivity.class));
            }
        });
        initializeDisplayContent();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        noteRecyclerAdapter.notifyDataSetChanged();//each time activity resume refresh it to show latest data
    }

    private void initializeDisplayContent() {
  final RecyclerView recyclerNotes =  findViewById(R.id.list_notes);
            //linear experience to the list
            final LinearLayoutManager notesLayoutManager = new LinearLayoutManager(this);
            recyclerNotes.setLayoutManager(notesLayoutManager);
           //adapter holding our list of notes and associated that adapter with our recycler view
            List<NoteInfo> notes = DataManager.getInstance().getNotes();
        noteRecyclerAdapter = new NoteRecyclerAdapter(this,notes);
            recyclerNotes.setAdapter(noteRecyclerAdapter);
     }

}
