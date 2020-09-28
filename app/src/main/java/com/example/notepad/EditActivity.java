package com.example.notepad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";
    TextView noteTextBox;
    TextView noteTitleBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        noteTitleBox = findViewById(R.id.titlebox2);
        noteTextBox = findViewById(R.id.multiLineBox2);
        noteTextBox.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.editactivitymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.saveButton:
                Toast.makeText(this, "Save Button Selected", Toast.LENGTH_SHORT).show();
                Intent data = new Intent();
                data.putExtra("Title", noteTitleBox.getText().toString());
                data.putExtra("Note", noteTextBox.getText().toString());
                setResult(RESULT_OK, data);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
            Log.d(TAG, "onBackPressed: ");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Your note is not saved!");
            builder.setMessage("Save note '" + noteTitleBox.getText().toString() + "'?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent data = new Intent();
                    data.putExtra("Title", noteTitleBox.getText().toString());
                    data.putExtra("Note", noteTextBox.getText().toString());
                    setResult(RESULT_OK, data);
                    Toast.makeText(EditActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(EditActivity.this, "Note Not Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            //super.onBackPressed();
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
