package com.example.mi_b_wizard;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.example.mi_b_wizard.Data.Game;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.parseColor;


public class ResultActivity extends AppCompatActivity {
    TableLayout resultLayout;
    GameActivity gameActivity = GameActivity.getGameActivity();
    int rowcounter = 1;
    int maxRounds = 20;
    int playerCount = 3;
    List<String> end = new ArrayList<>();
    ImageView btmm;

    public String getPlayernameFromList(int position){
        String name  = end.get(position);
        String[] row = name.split(" ");
        return row[2];
    }

    public String getPointsFromList(int position){
        String points = end.get(position);
        String[] row = points.split(" ");
        return row[3];

    }

    public void cleanMyList(List<String> end){
        for (int i=0; i < end.size();i++ ){
           String s  = end.get(i);
           String[] row = s.split(" ");
           if (row.length <= 3){
               end.remove(i);
               i--;
           }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        resultLayout = findViewById(R.id.result);
        end = gameActivity.getFinalSt(); // end results..
        btmm = findViewById(R.id.home);



        //Getting maxRounds from Game_activity
        Intent mIntent = getIntent();
        maxRounds = mIntent.getIntExtra("maxRounds", 0);
        cleanMyList(end);
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
            playerCount = 60 / maxRounds;

        //Creating HeadRow
        TableRow headRow = new TableRow(ResultActivity.this);

            for(int i = 0; i < playerCount + 1 ; i++){
                TextView cell = new TextView(ResultActivity.this);

                headRow.addView(cell);

                if(i==0){
                    cell.setText(" ");
                }else if (i > 0){   //setting Playernames
                    String playername = getPlayernameFromList(i-1);
                    cell.setText(playername+" ");
                    cell.setTextColor(parseColor("#FFFFFFFF"));
 //                   cell.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                }
            }
        resultLayout.addView(headRow);

        // add cells to Row
        for (int i = 0; i < maxRounds  ; i++){

            TableRow row = new TableRow(ResultActivity.this);
            resultLayout.addView(row);
            for (int j = 0; j < playerCount + 1; j++){
                LinearLayout cell = new LinearLayout(ResultActivity.this);
//                cell.setGravity(Gravity.CENTER_VERTICAL);
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

                    String res;
                    res = getPointsFromList(rowcounter-1);
                    TextView textViewValues = new TextView(ResultActivity.this);
                    textViewValues.setText(res);
                    textViewValues.setTypeface(null, Typeface.BOLD);
                    textViewValues.setTextColor(parseColor("#FFFFFFFF"));
                    cell.addView(textViewValues);
                    textViewValues.setPadding(10, 0, 0, 0);

                    rowcounter++;
                }


            }


        }



    }

}
