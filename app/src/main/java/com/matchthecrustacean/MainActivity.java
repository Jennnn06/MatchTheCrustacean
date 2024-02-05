package com.matchthecrustacean;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // Keep track of clicked positions
    private int[] clickedPositions = {-1, -1};
    // Array to store crustacean images
    private int[] crustaceanImages;
    // Array to store duplicate crustacean images
    private int[] duplicateCrustaceanImages;
    // Array to store image views
    private ImageView[] crustaceanSqr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Import crustacean images
        crustaceanImages = new int[]{
                R.drawable.hermit, R.drawable.starfish, R.drawable.isopod, R.drawable.shrimp,
                R.drawable.crab, R.drawable.lobster
        };

        // Initialize the image views
        crustaceanSqr = new ImageView[12];
        crustaceanSqr[0] = findViewById(R.id.sqr1);
        crustaceanSqr[1] = findViewById(R.id.sqr2);
        crustaceanSqr[2] = findViewById(R.id.sqr3);
        crustaceanSqr[3] = findViewById(R.id.sqr4);
        crustaceanSqr[4] = findViewById(R.id.sqr5);
        crustaceanSqr[5] = findViewById(R.id.sqr6);
        crustaceanSqr[6] = findViewById(R.id.sqr7);
        crustaceanSqr[7] = findViewById(R.id.sqr8);
        crustaceanSqr[8] = findViewById(R.id.sqr9);
        crustaceanSqr[9] = findViewById(R.id.sqr10);
        crustaceanSqr[10] = findViewById(R.id.sqr11);
        crustaceanSqr[11] = findViewById(R.id.sqr12);

        // Shuffle the crustacean images
        shuffleCrustaceanImages();

        // Set initial image resources for all image views
        for (int i = 0; i < crustaceanSqr.length; i++) {
            crustaceanSqr[i].setImageResource(R.drawable.square);
        }

        // Set click listeners for the image views
        for (int i = 0; i < crustaceanSqr.length; i++) {
            final int position = i;
            crustaceanSqr[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = crustaceanSqr[position];

                    if (clickedPositions[0] == -1) {
                        // First click
                        clickedPositions[0] = position;
                        imageView.setImageResource(duplicateCrustaceanImages[clickedPositions[0]]);
                    } else if (clickedPositions[1] == -1 && clickedPositions[0] != position) {
                        // Second click on a different ImageView
                        clickedPositions[1] = position;
                        imageView.setImageResource(duplicateCrustaceanImages[clickedPositions[1]]);

                        // Compare the clicked images
                        if (duplicateCrustaceanImages[clickedPositions[0]] != duplicateCrustaceanImages[clickedPositions[1]]) {
                            // Images are different, reset them after a delay
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    crustaceanSqr[clickedPositions[0]].setImageResource(R.drawable.square);
                                    crustaceanSqr[clickedPositions[1]].setImageResource(R.drawable.square);
                                    resetClickedPositions();
                                }
                            }, 1000);
                        } else {
                            // Images are the same, reset the clicked positions immediately
                            resetClickedPositions();
                        }
                        // Check if all image views are flipped
                        if (isAllFlipped()) {
                            // Show AlertDialog to ask for retry
                            showRetryDialog();
                        }
                    }
                }
            });
        }

    }

    private boolean isAllFlipped() {
        for (ImageView imageView : crustaceanSqr) {
            if (imageView.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.square).getConstantState()) {
                return false;
            }
        }
        return true;
    }

    private void showRetryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Congratulations! You flipped all the images. Do you want to retry?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        anotherRetryForFlippingEverything();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void anotherRetryForFlippingEverything(){
        // Restart the app by launching the launcher activity
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        // Finish the current activity
        finish();
    }
    public void retry(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to retry?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Restart the app by launching the launcher activity
                        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        // Finish the current activity
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void shuffleCrustaceanImages() {
        //Duplicator of the crustacean images
        duplicateCrustaceanImages = new int[12];

        //Duplicate the crustacean images
        for (int i = 0; i < duplicateCrustaceanImages.length; i++) {
            duplicateCrustaceanImages[i] = crustaceanImages[i / 2 % crustaceanImages.length];
        }

        //Shuffle the duplicated crustacean images
        Random random = new Random();
        for (int i = duplicateCrustaceanImages.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = duplicateCrustaceanImages[index];
            duplicateCrustaceanImages[index] = duplicateCrustaceanImages[i];
            duplicateCrustaceanImages[i] = temp;
        }
    }

    private void resetClickedPositions() {
        clickedPositions[0] = -1;
        clickedPositions[1] = -1;
    }
}
