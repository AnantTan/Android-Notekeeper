package com.example.ananttandon.notekeeper;

import org.junit.Test;

import static org.junit.Assert.*;

public class DataManagerTest {

    @Test
    public void createNewNote() {
        DataManager dm = DataManager.getInstance();
        final CourseInfo course = dm.getCourse("android_async");
        final String noteTitle = "Text note title";
        final String noteText = "Body text of my note";

        int noteIndex = dm.createNewNote();
        NoteInfo newInfo = dm.getNotes().get(noteIndex);
        newInfo.setCourse(course);
        newInfo.setTitle(noteTitle);
        newInfo.setText(noteText);
        NoteInfo comNoteInfo = dm.getNotes().get(noteIndex);
        assertEquals(comNoteInfo.getCourse(),course);
        assertEquals(comNoteInfo.getTitle(),noteTitle);
        assertEquals(comNoteInfo.getText(),noteText);
    }
}