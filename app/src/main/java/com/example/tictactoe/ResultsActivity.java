package com.example.tictactoe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class ResultsActivity extends AppCompatActivity {

    private EditText emailInput;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView resultsText = findViewById(R.id.results_text);
        emailInput = findViewById(R.id.email_input);
        Button sendEmailButton = findViewById(R.id.send_email_button);
        Button shareButton = findViewById(R.id.share_button);
        Button backToGameButton = findViewById(R.id.back_to_game_button);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        Intent intent = getIntent();
        String player1Name = intent.getStringExtra("player1Name");
        String player2Name = intent.getStringExtra("player2Name");
        int player1Score = intent.getIntExtra("player1Score", 0);
        int player2Score = intent.getIntExtra("player2Score", 0);

        String results = player1Name + " : " + player1Score + "\n" +
                 player2Name + " : " + player2Score;
        resultsText.setText(results);

        sendEmailButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            sendEmail(email, results);
            vibrate();
        });


        shareButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            sendEmail(email, results);
            vibrate();
        });

        shareButton.setOnClickListener(v -> {
            sendSharedText(results);
            vibrate();
        });

        backToGameButton.setOnClickListener(v -> {finish(); vibrate(); });


        // Status Bar Color because default purple color is annooooooying!!!
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusBarColor));
    }


    public void sendEmail(String email, String results) {

        if (!email.isEmpty()) {
            String uriText = "mailto:" + Uri.encode(email) +
                    "?subject=" + Uri.encode("Subject") +
                    "&body=" + Uri.encode(results);
            Uri uri = Uri.parse(uriText);

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(uri);
            try {
                startActivity(Intent.createChooser(emailIntent, "send Email with"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(ResultsActivity.this, "אין אפליקציה לטיפול בפעולת אימייל", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ResultsActivity.this, "לא הזנת כתובת אימייל", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("QueryPermissionsNeeded")
    public void sendSharedText(String results) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, results);
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }


    private void vibrate() {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(20, 50));
        }
    }

}
