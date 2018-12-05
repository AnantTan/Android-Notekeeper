package com.example.ananttandon.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater layoutInflater;
    private final List<NoteInfo> mNotes;//data holder

    public NoteRecyclerAdapter(Context mContext, List<NoteInfo> mNotes) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        this.mNotes = mNotes;
    }

    @NonNull
    @Override
    //receives a view and inflates the information inside the view holder
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       //creating a view available from the resource and passing it inside the viewHolder
        View itemView = layoutInflater.inflate(R.layout.item_note_list, viewGroup ,false);//false stops view from being automatically attached to the parent class
        return new ViewHolder(itemView);
    }

    @Override
    //associating data in desired position within view holder
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        NoteInfo note = mNotes.get(position);
        viewHolder.textViewCourses.setText(note.getCourse().getTitle());//display title associated to the current note
        viewHolder.textViewTitle.setText(note.getText());
        viewHolder.currentPosition = position;//hold the position each time view holder is associated with new data
}

    @Override
    public int getItemCount() {
        return mNotes.size();//gets how much data we have
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView textViewCourses;//public because NoteRecyclerAdapter needs access
        public final TextView textViewTitle;
        public int currentPosition;//not final as it will change everytime

        //can receive a view get references to the textFields
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //giving out the references for the viewHolder
            textViewCourses = (TextView) itemView.findViewById(R.id.text_course);
            textViewTitle = (TextView) itemView.findViewById(R.id.text_title);
            //whenver the user taps
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,NoteActivity.class);
                    intent.putExtra(NoteActivity.NOTE_POSITION,currentPosition);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
