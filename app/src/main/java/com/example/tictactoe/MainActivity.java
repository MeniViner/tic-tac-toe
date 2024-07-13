package com.example.tictactoe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText player1Name, player2Name;
    private TextView player1Score, player2Score;
    private final Button[][] buttons = new Button[3][3];

    private boolean player1Turn = true;
    private int roundCount;
    private int player1Points, player2Points;

    private Vibrator vibrator;

    @SuppressLint("DiscouragedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        player1Name = findViewById(R.id.player1_name);
        player2Name = findViewById(R.id.player2_name);
        player1Score = findViewById(R.id.player1_score);
        player2Score = findViewById(R.id.player2_score);
        Button resetButton = findViewById(R.id.reset_button);
        Button saveButton = findViewById(R.id.save_button);


        buttons[0][0] = findViewById(R.id.button1);
        buttons[0][1] = findViewById(R.id.button2);
        buttons[0][2] = findViewById(R.id.button3);
        buttons[1][0] = findViewById(R.id.button4);
        buttons[1][1] = findViewById(R.id.button5);
        buttons[1][2] = findViewById(R.id.button6);
        buttons[2][0] = findViewById(R.id.button7);
        buttons[2][1] = findViewById(R.id.button8);
        buttons[2][2] = findViewById(R.id.button9);

        buttons[0][0].setOnClickListener(this);
        buttons[0][1].setOnClickListener(this);
        buttons[0][2].setOnClickListener(this);
        buttons[1][0].setOnClickListener(this);
        buttons[1][1].setOnClickListener(this);
        buttons[1][2].setOnClickListener(this);
        buttons[2][0].setOnClickListener(this);
        buttons[2][1].setOnClickListener(this);
        buttons[2][2].setOnClickListener(this);


        resetButton.setOnClickListener(v -> {resetAll(); vibrate();});
        saveButton.setOnClickListener(v -> {
            vibrate();
            String player1 = player1Name.getText().toString();
            String player2 = player2Name.getText().toString();
            if (player1.isEmpty() || player2.isEmpty()) {
                Toast.makeText(MainActivity.this, "נא להזין את שני שמות השחקנים", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
            intent.putExtra("player1Name", player1);
            intent.putExtra("player2Name", player2);
            intent.putExtra("player1Score", player1Points);
            intent.putExtra("player2Score", player2Points);
            startActivity(intent);
        });

        // Status Bar Color because default purple color is annooooooying!!!
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));

    }

    @Override
    public void onClick(View v) {
        Button clickedButton = (Button) v;

        if (!clickedButton.getText().toString().isEmpty()) {
            return;
        }

        vibrate();

        if (player1Turn) {
            clickedButton.setText("X");
        } else {
            clickedButton.setText("O");
        }

        roundCount++;

        if (checkForWin()) {
            if (player1Turn) {
                player1Wins();
            } else {
                player2Wins();
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            player1Turn = !player1Turn;
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }


        // בדיקןת לרןחב
        if (field[0][0].equals(field[0][1]) && field[0][0].equals(field[0][2]) && !field[0][0].isEmpty()) return true;
        if (field[1][0].equals(field[1][1]) && field[1][0].equals(field[1][2]) && !field[1][0].isEmpty()) return true;
        if (field[2][0].equals(field[2][1]) && field[2][0].equals(field[2][2]) && !field[2][0].isEmpty()) return true;

        // בדיקןת לאורך
        if (field[0][0].equals(field[1][0]) && field[0][0].equals(field[2][0]) && !field[0][0].isEmpty()) return true;
        if (field[0][1].equals(field[1][1]) && field[0][1].equals(field[2][1]) && !field[0][1].isEmpty()) return true;
        if (field[0][2].equals(field[1][2]) && field[0][2].equals(field[2][2]) && !field[0][2].isEmpty()) return true;

        //  אלכסוניות
        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].isEmpty()) return true;
        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].isEmpty()) return true;

        return false;
    }

    private void player1Wins() {
        player1Points++;
        String player1 = player1Name.getText().toString();
        Toast.makeText(this, player1 + " ניצח!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void player2Wins() {
        player2Points++;
        String player2 = player2Name.getText().toString();
        Toast.makeText(this, player2 + " ניצח!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void draw() {
        Toast.makeText(this, "תיקו.", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void updatePointsText() {
        player1Score.setText(String.valueOf(player1Points));
        player2Score.setText(String.valueOf(player2Points));
    }

    // Reset Board Functionality
    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        roundCount = 0;
        player1Turn = true;
    }

    // Reset All Functionality
    private void resetAll() {
        resetBoard();
        player1Points = 0;
        player2Points = 0;
        player1Name.setText("");
        player2Name.setText("");
        updatePointsText();
    }


    private void vibrate() {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(20, 50));
        }
    }


}
