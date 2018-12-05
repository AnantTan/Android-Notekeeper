package com.example.ananttandon.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater layoutInflater;
    private final List<CourseInfo> mCourses;//data holder

    public CourseRecyclerAdapter(Context mContext, List<CourseInfo> courses) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        this.mCourses = courses;
    }

    @NonNull
    @Override
    //receives a view and inflates the information inside the view holder
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       //creating a view available from the resource and passing it inside the viewHolder
        View itemView = layoutInflater.inflate(R.layout.item_course_list, viewGroup ,false);//false stops view from being automatically attached to the parent class
        return new ViewHolder(itemView);
    }

    @Override
    //associating data in desired position within view holder
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        CourseInfo course = mCourses.get(position);
        viewHolder.textViewCourses.setText(course.getTitle());//display course associated to the current title
        viewHolder.currentPosition = position;//hold the position each time view holder is associated with new data

    }

    @Override
    public int getItemCount() {
        return mCourses.size();//gets how much data we have
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView textViewCourses;//public because NoteRecyclerAdapter needs access
        public int currentPosition;//not final as it will change everytime

        //can receive a view get references to the textFields
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //giving out the references for the viewHolder
            textViewCourses = (TextView) itemView.findViewById(R.id.text_course);
            //whenver the user taps
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v,mCourses.get(currentPosition).getTitle(),
                            Snackbar.LENGTH_LONG).show();//bind data from courses in the layout
                }
            });
        }
    }
}
