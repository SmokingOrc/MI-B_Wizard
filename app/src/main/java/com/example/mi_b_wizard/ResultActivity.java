package com.example.mi_b_wizard;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.mi_b_wizard.Data.Game;
import com.example.mi_b_wizard.Data.Player;
import com.example.mi_b_wizard.Network.MessageHandler;
import com.example.mi_b_wizard.Network.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class ResultActivity extends AppCompatActivity {

    private String idsPoints;
    private String idsPredictedTricks;

    private HashMap<String, Integer> mapOfIds;

    TableLayout resultLayout;
    MessageHandler messageHandler;
    GameActivity gameActivity = GameActivity.getGameActivity();
    Server server;
    ArrayList<Integer> ids = new ArrayList<Integer>();
    int predictedTricks;
    int points;
    int round=1,maxRounds;
    int playerCount;
    List<String> end = new ArrayList<>();

    public void addRound(){
        this.round++;
    }
    public void setIds(){
        this.ids=messageHandler.getId();
        this.playerCount=ids.size();
    }

    public void setPredictedTricks(int predictedTricks){
        this.predictedTricks=predictedTricks;
    }

    public void setPoints(int points){
        this.points=points;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mapOfIds = new HashMap<>();
        resultLayout = findViewById(R.id.result);
        end = gameActivity.getFinalSt(); // end results..

        /*
        ids.add(123);
        ids.add(124);
        ids.add(125);
        ids.add(126);
        playerCount=ids.size();
       */
        randomdata();
        generateNewResultList(playerCount);

    }

    private void generateNewResultList(int playercount){
        if (playercount < 3){
            playercount = 3;
        }else if(playercount > 6){
            playercount = 6;
        }
        maxRounds= 60 / playercount;


        //Creating HeadRow
        TableRow headRow = new TableRow(ResultActivity.this);

            for(int i = 0; i < playercount + 1 ; i++){
                TextView cell = new TextView(ResultActivity.this);

                headRow.addView(cell);

                if (i > 0){
                    int id =ids.get(i-1);
                    cell.setText("" + id);
                    cell.setTypeface(null, Typeface.BOLD);
                    cell.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                }
            }

        headRow.setBackgroundColor(13);

        resultLayout.addView(headRow);


        // add cells to Row
        for (int i = 1; i <= maxRounds; i++){

            TableRow row = new TableRow(ResultActivity.this);
            resultLayout.addView(row);

            for (int j = 0; j < playercount + 1; j++){
                LinearLayout cell = new LinearLayout(ResultActivity.this);
                //cell.setGravity(Gravity.CENTER_VERTICAL);
                //cell.setOrientation(LinearLayout.HORIZONTAL);
                row.addView(cell);


                TextView tv1 = new TextView(ResultActivity.this); //actual score
                tv1.setPadding(10, 0, 0, 0);
                cell.addView(tv1);


                //first cell that shows the number of the round
                if(j == 0) {
                    tv1.setText("" + i);
                    tv1.setTypeface(null, Typeface.BOLD);
                    tv1.setGravity(Gravity.CENTER_HORIZONTAL);

                }else{
                    TextView textViewPredictedTricks = new TextView(ResultActivity.this);
                    TextView textViewPoints = new TextView(ResultActivity.this);
                    TextView placeHolder = new TextView(ResultActivity.this);
                    textViewPredictedTricks.setText("" + predictedTricks);
                    textViewPoints.setText("" + points);
                    placeHolder.setText("|");
 //                   textViewPoints.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
   //                 textViewPredictedTricks.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);


                    LinearLayout resultContainer = new LinearLayout(ResultActivity.this);
                    resultContainer.setOrientation(LinearLayout.HORIZONTAL);
                    cell.addView(resultContainer);
                  //  resultContainer.setGravity(Gravity.RIGHT);



                    resultContainer.addView(textViewPredictedTricks);
                    resultContainer.addView(placeHolder);
                    resultContainer.addView(textViewPoints);


                }


            }


        }



    }

    public void randomdata(){
        Random rand=new Random();

        playerCount=3;

        for (int r=0;r < round;r++) {


            for (int p = 0; p <= playerCount; p++) {
                ids.add(123 + p);
                predictedTricks = rand.nextInt(7);
                points = rand.nextInt(6)*10;


            }


        }

    }


}
