package com.altf4.journey.view;

//import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.altf4.journey.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ManageGoalsActivity extends AppCompatActivity {

    private FloatingActionButton addGoal;
    // Define the frame layouts in an array
    private FrameLayout[] frames = new FrameLayout[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_goals);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addGoal = findViewById(R.id.floatingActionButton);
        frames[0] = (findViewById(R.id.frameLayout1));
        frames[1] = (findViewById(R.id.frameLayout2));
        frames[2] = (findViewById(R.id.frameLayout3));
        frames[3] = (findViewById(R.id.frameLayout4));

        addGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ManageGoalsActivity.this);
                builder.setTitle("Enter the goal you would like to Achieve");

                // Set up the input
                final EditText input = new EditText(ManageGoalsActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInput = input.getText().toString();
                        // Handle user input
                        // SEND TO PROMPT HERE

                        int index = checkGoals();
                        if (index == -1) {
                            Toast.makeText(ManageGoalsActivity.this, "Max no. of goals reached", Toast.LENGTH_SHORT).show();
                        } else {
                            frames[index].setVisibility(View.VISIBLE);
                        }

                        startActivity(new Intent(ManageGoalsActivity.this, ManageTasksActivity.class));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    // Function to check goals
    private int checkGoals() {
        // Loop through the array of FrameLayouts
        for (int i = 0; i < frames.length; i++) {
            // Check if the visibility is not VISIBLE (either GONE or INVISIBLE)
            if (frames[i].getVisibility() != View.VISIBLE) {
                return i; // Return the index of the first non-visible frame
            }
        }

        // If all frames are visible, return -1
        return -1;
    }

}