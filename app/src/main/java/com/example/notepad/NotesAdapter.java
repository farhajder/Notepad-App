package com.example.notepad;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    private static final String TAG = "NotesAdapter";
    private ArrayList<Notes> nlist;
    private MainActivity mainActivity;
    RecyclerView recyclerView;

    public NotesAdapter(ArrayList<Notes> list, MainActivity mainActivity){
        nlist = list;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: CREATING NEW");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent,false);
        view.setOnClickListener(mainActivity);
        view.setOnLongClickListener(mainActivity);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: SETTING DATA");
        Notes selectedNotes = nlist.get(position);
        holder.titlebox.setText(selectedNotes.getTitle());
        holder.multiLineBox.setText(selectedNotes.getNoteText());
    }

    @Override
    public int getItemCount() {
        return nlist.size();
    }


}
