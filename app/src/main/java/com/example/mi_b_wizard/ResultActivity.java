package com.example.mi_b_wizard;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.mi_b_wizard.Network.MessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.graphics.Color.parseColor;


public class ResultActivity extends AppCompatActivity {
    TableLayout resultLayout;
    MessageHandler messageHandler;
    GameActivity gameActivity = GameActivity.getGameActivity();
    int rowcounter = 1;
    int maxRounds = 21;
    int playerCount;
    List<String> end = new ArrayList<>();
    Button btmm;


    public String getPlayernameFromList(int position){
        String name  = end.get(position);
        String[] row = name.split(" ");
        return row[1];
    }

    public String getPointsFromList(int position){
        String points = end.get(position);
        String[] row = points.split(" ");
        return row[2];

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        resultLayout = findViewById(R.id.result);
        end = gameActivity.getFinalSt(); // end results..
        btmm = findViewById(R.id.backToMainMenu);

        //Getting maxRounds from Game_activity
        Intent mIntent = getIntent();
        maxRounds = mIntent.getIntExtra("maxRounds", 0);

  //      randomdata2();
        System.out.println("Game: " + end.size());
        System.out.println("maxRounds: " + maxRounds);
        for (String s: end){
            System.out.println(s);
        }

        generateNewResultList(maxRounds);

        btmm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


    }

    private void generateNewResultList(int maxRounds){
        if (maxRounds > 0 ) {
            playerCount = 60 / maxRounds;
        }

        //Creating HeadRow
        TableRow headRow = new TableRow(ResultActivity.this);

            for(int i = 0; i < playerCount + 1 ; i++){
                TextView cell = new TextView(ResultActivity.this);

                headRow.addView(cell);

                if(i==0){
                    cell.setText(" ");
                    cell.setTextColor(parseColor("#FFFFFFFF"));
                    cell.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                }else if (i > 0){
                    String playername = getPlayernameFromList(i-1);
                    cell.setText(playername+" ");
                    cell.setTextColor(parseColor("#FFFFFFFF"));
                    cell.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                }
            }
        resultLayout.addView(headRow);

        // add cells to Row
        for (int i = 0; i < maxRounds  ; i++){

            TableRow row = new TableRow(ResultActivity.this);
            resultLayout.addView(row);
            for (int j = 0; j < playerCount + 1; j++){
                LinearLayout cell = new LinearLayout(ResultActivity.this);
                cell.setGravity(Gravity.CENTER_VERTICAL);
                row.addView(cell);

                TextView tv1 = new TextView(ResultActivity.this); //actual score

                switch(playerCount) {
                    case 3:
                        tv1.setPadding(50, 0, 0, 0);
                        break;
                    case 4:
                        tv1.setPadding(30, 0, 0, 0);
                        break;
                    default:
                        tv1.setPadding(20, 0, 0, 0);

                }
                cell.addView(tv1);

                //first cell that shows the number of the round
                if(j == 0) {
                    int c = i+1;
                    tv1.setText("" + c);
                    tv1.setTypeface(null, Typeface.BOLD);
                    tv1.setTextColor(parseColor("#FFFFFFFF"));
                    tv1.setGravity(Gravity.CENTER_HORIZONTAL);

                }else {

                    String s;
                    s = getPointsFromList(rowcounter-1);

                    TextView textViewValues = new TextView(ResultActivity.this);
                    textViewValues.setText(s);
                    textViewValues.setTypeface(null, Typeface.BOLD);
                    textViewValues.setTextColor(parseColor("#FFFFFFFF"));

                    cell.addView(textViewValues);
                    textViewValues.setPadding(10, 0, 0, 0);

                    rowcounter++;
                }


            }


        }



    }

    //Random Data zum testen der Auflösung


    public void randomdata2(){
        Random rand=new Random();

        playerCount = 6;
        maxRounds= 60 / playerCount;
        for (int i = 1; i < maxRounds + 1; i++) {

            for (int j = 0; j <playerCount; j++) {
                end.add("Round" + i + " groups" + j + ": " + i * rand.nextInt(10) + " points");


            }


        }

    }


}
