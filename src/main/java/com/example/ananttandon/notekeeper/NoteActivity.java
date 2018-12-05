package com.example.ananttandon.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
    public static final String NOTE_POSITION = "com.example.ananttandon.notekeeper.NOTE_PSOTION";
    public static final String ORIGINAL_COURSE_ID = "com.example.ananttandon.notekeeper.ORIGINAL_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.example.ananttandon.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.example.ananttandon.notekeeper.ORIGINAL_NOTE_TEXT";
    public static final int positonNotSet = -1;
    private NoteInfo mNote;
    private boolean misNewNote;
    private Spinner mSpinnerCourses;
    private EditText mEditNoteTitle;
    private EditText mEditNoteText;
    private int mNotePosition;
    private boolean mIsCanceling;
    private String originalNoteCourseId;
    private String originalNoteTitle;
    private String originalNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSpinnerCourses = (Spinner) findViewById(R.id.spinner_courses);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses =
                new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCourses.setAdapter(adapterCourses);

        readDisplayStateValues();
        if(savedInstanceState == null)
        {
            saveOriginalNoteValues();
        }
        else
        {
            restoreOriginalNoteValues(savedInstanceState);
        }

        mEditNoteTitle = (EditText) findViewById(R.id.title_note_text);
        mEditNoteText = (EditText) findViewById(R.id.text_note_text);
        if(!misNewNote)
        displayNote(mSpinnerCourses, mEditNoteTitle, mEditNoteText);
    }

    private void restoreOriginalNoteValues(Bundle savedInstanceState) {
        originalNoteCourseId = savedInstanceState.getString(ORIGINAL_COURSE_ID);
        originalNoteTitle = savedInstanceState.getString(ORIGINAL_NOTE_TITLE);
        originalNoteText = savedInstanceState.getString(ORIGINAL_NOTE_TEXT);
    }

    private void saveOriginalNoteValues() {
        if(misNewNote) {
            return;
        }
        originalNoteCourseId = mNote.getCourse().getCourseId();
        originalNoteTitle = mNote.getTitle();
        originalNoteText = mNote.getText();
    }

    private void displayNote(Spinner spinnerCourses, EditText editNoteTitle, EditText editNoteText) {
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int indexOfCourses = courses.indexOf(mNote.getCourse());
        spinnerCourses.setSelection(indexOfCourses);
        editNoteTitle.setText(mNote.getTitle());
        editNoteText.setText(mNote.getText());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mIsCanceling)// if canceling
        {
            if(misNewNote)// and and it is a new note
            {
                DataManager.getInstance().removeNote(mNotePosition);//remove the note
            }
            else {
                storePreviousNoteValues();
            }
        }else {
            saveNote();//else save it
        }
    }

    private void storePreviousNoteValues() {
        CourseInfo course = DataManager.getInstance().getCourse(originalNoteCourseId);
        mNote.setCourse(course);
        mNote.setTitle(originalNoteTitle);
        mNote.setText(originalNoteText);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ORIGINAL_COURSE_ID,originalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE,originalNoteTitle);
        outState.putString(ORIGINAL_NOTE_TEXT,originalNoteText);
    }

    private void saveNote() {
        mNote.setCourse((CourseInfo) mSpinnerCourses.getSelectedItem());
        mNote.setTitle(mEditNoteTitle.getText().toString());
        mNote.setText(mEditNoteText.getText().toString());
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        int position = intent.getIntExtra(NOTE_POSITION, positonNotSet);
        misNewNote = position == positonNotSet;
        if(misNewNote){
            createNewNote();
        } else{
            mNote = DataManager.getInstance().getNotes().get(position);
    }
}

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNotePosition = dm.createNewNote();
        mNote = dm.getNotes().get(mNotePosition);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }


    @Override
    //when the user selects an option from the menu
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        }
        else if(id == R.id.action_cancel)
        {
            mIsCanceling = true;
            finish();
        }else if(id == R.id.action_next)
        {
            moveNext();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_next);//gets the reference of the menu item against the id
        int lastNoteIndex = DataManager.getInstance().getNotes().size()-1;//get the last index
        item.setEnabled(mNotePosition < lastNoteIndex);//enable it until position is less than last index
        return super.onPrepareOptionsMenu(menu);
    }

    private void moveNext() {
        saveNote();//save when user goes to next note
        ++mNotePosition;//increment the position to target the next note
        mNote = DataManager.getInstance().getNotes().get(mNotePosition);//get the note that corresponds to that position form the data manager
        saveOriginalNoteValues();//before dsiplaying the new note save original values as user might make changes
        displayNote(mSpinnerCourses,mEditNoteTitle,mEditNoteText);
        invalidateOptionsMenu();//calls onPrepareOptionsMenu
    }

    private void sendEmail() {
        CourseInfo cource = (CourseInfo) mSpinnerCourses.getSelectedItem();//spinner icon will select the item
        String subject = mEditNoteTitle.getText().toString();//note title will be subject
        String text = "Checkout what i learned in the plurasight course \"" + cource.getTitle()
                +"\"\n" + mEditNoteText.getText().toString();//this will be the body in the email
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");//the standard mine type for sending msg
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(intent);
    }
}
