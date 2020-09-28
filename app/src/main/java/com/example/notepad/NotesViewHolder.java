package com.example.notepad;

import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.Serializable;

public class NotesViewHolder extends RecyclerView.ViewHolder implements Serializable {

    TextView titlebox;
    TextView multiLineBox;
    TextView timestamp;

    public NotesViewHolder (View view){
        super(view);

        titlebox = view.findViewById(R.id.titlebox);
        multiLineBox = view.findViewById((R.id.multiLineBox));
        timestamp = view.findViewById(R.id.dateBox);

    }
}
