package com.example.notepad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Output;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";
    private ArrayList<Notes> noteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private int resCode = 1;
    private TextView noteTitleBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        notesAdapter = new NotesAdapter(noteList, this);
        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Used to get the title of the note from edit Activity under long press check line 200
        noteTitleBox = findViewById(R.id.titlebox2);

        setTitle("Multi-Notes");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void createList(){
        for(int i = 0; i < 10; i++){
            String title = "Title" + i;
            String noteBody = "Note" + i;
            long time = System.currentTimeMillis();
            Notes n = new Notes(title, noteBody, time);
            noteList.add(n);
        }
    }

    public void saveFile(View view){
        JSONArray jsonArray = new JSONArray();
        for (Notes n: noteList){
            try {
                JSONObject noteJSON = new JSONObject();
                noteJSON.put("Title: ", n.getTitle());
                noteJSON.put("Note: ", n.getNoteText());
                noteJSON.put("Time: ", n.getTimestamp());
                jsonArray.put(noteJSON);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        String jsonText = jsonArray.toString();
        Log.d(TAG, "doWrite: " + jsonText);

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("data.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(jsonText);
            outputStreamWriter.close();
            Toast.makeText(this, "File write sucess!", Toast.LENGTH_SHORT).show();
        } catch (IOException e){
            Log.d(TAG, "doWrite: File write failed: " + e.toString());
            Toast.makeText(this, "File write failed!", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadFile(View view){
        noteList.clear();
        try{
            InputStream inputStream = openFileInput("data.txt");
            if(inputStream!=null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader( inputStreamReader);
                String receiveSTR = "";
                StringBuilder stringBuilder = new StringBuilder();

                while((receiveSTR = bufferedReader.readLine()) != null){
                    stringBuilder.append(receiveSTR);
                }
                inputStream.close();
                Toast.makeText(this, "Time: " + stringBuilder.toString(), Toast.LENGTH_SHORT).show();

                String jsonText = stringBuilder.toString();
                try {
                    JSONArray jsonArray = new JSONArray(jsonText);
                    Log.d(TAG, "doRead: " + jsonArray.length());

                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String title = jsonObject.getString("Title: ");
                        String notebox = jsonObject.getString("Note: ");
                        long time = jsonObject.getLong("Time: ");
                        Notes n = new Notes(title, notebox, time);
                        noteList.add(n);
                    }
                    Log.d(TAG, "doRead: " + noteList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } catch (FileNotFoundException e){
            Log.d(TAG, "doRead: File not found: \" + e.toString()");
        } catch (IOException e){
            Log.d(TAG, "doRead: Can not read file: " + e.toString());
        }
        createList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.samplemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.aboutPage:
                Toast.makeText(this, "About Page Selected.", Toast.LENGTH_SHORT).show();
                Intent aboutPageIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutPageIntent);
                return true;
            case R.id.newNote:
                Toast.makeText(this, "New Note Page Selected", Toast.LENGTH_SHORT).show();
                Intent editPageIntent = new Intent(this, EditActivity.class);
                startActivityForResult(editPageIntent, resCode);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == resCode){
            if(resultCode == RESULT_OK){
                String title = data.getStringExtra("Title: ");
                String content = data.getStringExtra("Note: ");
                long time = data.getLongExtra("Time: ", System.currentTimeMillis());
                Notes n = new Notes(title, content, time);
                noteList.add(n);
                notesAdapter.notifyDataSetChanged();
            }
            else Log.d(TAG, "onActivityResult: result code: " + resultCode);
        }
    }

    @Override
    public void onClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);
        Notes selection = noteList.get(pos);
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Selected: " + selection.getTitle(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onLongClick(View view) {
        final int pos = recyclerView.getChildLayoutPosition(view);
        Notes selection = noteList.get(pos);
        Toast.makeText(this, "Long Clicked: " + selection.getTitle(), Toast.LENGTH_LONG).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete Note '" + noteTitleBox.getText().toString() + "'?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                noteList.remove(pos);
                notesAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

}
