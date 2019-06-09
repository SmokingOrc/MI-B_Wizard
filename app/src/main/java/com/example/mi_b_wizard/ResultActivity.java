package com.example.mi_b_wizard;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.mi_b_wizard.Data.Game;
import com.example.mi_b_wizard.Data.Player;
import com.example.mi_b_wizard.Data.PlayerList;
import com.example.mi_b_wizard.Network.MessageHandler;
import com.example.mi_b_wizard.Network.Server;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class ResultActivity extends AppCompatActivity {

    Map<Pair<Integer, String>, String> resultMap = new HashMap<>();
    public static ResultActivity resultActivity;
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
    Button btmm;


    public void setResultMap(Map<Pair<Integer, String>, String> resultMap) {
        this.resultMap = resultMap;
    }

    public ArrayList<Integer> getIds() {
        return ids;
    }

    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    public static ResultActivity getResultActivity() {
        return resultActivity;
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
        resultLayout = findViewById(R.id.result);
        end = gameActivity.getFinalSt(); // end results..
        btmm = findViewById(R.id.backToMainMenu);


        /*
        ids.add(123123);
        ids.add(123123);
        ids.add(125241);
 //       ids.add(1234);
 //       ids.add(1234);
  //      ids.add(12355);
        playerCount=ids.size();
        */


        String sEnd = end.get(1);
        TextView test = findViewById(R.id.test);
        test.setText(end.toString());

        //randomdata2();

      //  generateNewResultList(PlayerList.getPlayers().size());

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
                    cell.setText("Round");
                    cell.setTypeface(null, Typeface.BOLD);
                    cell.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                }else if (i > 0){
                    cell.setText("Player "+ i);
                    cell.setTypeface(null, Typeface.BOLD);
                    cell.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                }
            }

        headRow.setBackgroundColor(13);

        resultLayout.addView(headRow);


        // add cells to Row
        for (int i = 1; i < maxRounds + 1 ; i++){

            TableRow row = new TableRow(ResultActivity.this);
            resultLayout.addView(row);

            for (int j = 0; j < playerCount + 1; j++){
                LinearLayout cell = new LinearLayout(ResultActivity.this);
                cell.setGravity(Gravity.CENTER_VERTICAL);
                //cell.setOrientation(LinearLayout.HORIZONTAL);
                row.addView(cell);


                TextView tv1 = new TextView(ResultActivity.this); //actual score

                switch(playerCount) {
                    case 3:
                        tv1.setPadding(50, 0, 0, 0);
                        break;
                    case 6:
                        tv1.setPadding(0, 0, 0, 0);
                        break;
                    case 4:
                        tv1.setPadding(30, 0, 0, 0);
                        break;
                    default:
                        tv1.setPadding(10, 0, 0, 0);

                }
                cell.addView(tv1);


                //first cell that shows the number of the round
                if(j == 0) {
                    tv1.setText("" + i);
                    tv1.setTypeface(null, Typeface.BOLD);
                    tv1.setGravity(Gravity.CENTER_HORIZONTAL);

                }else{
                    String s="";
                    /*
                    List<Player> playerList = PlayerList.getPlayers();

                    for (Player player:playerList) {
                        Pair<Integer, String> pair = new Pair<>(Integer.valueOf(round), player.getPlayerName());
                        s = resultMap.get(pair);

                    }
                    */
                    TextView textViewValues = new TextView(ResultActivity.this);
                    textViewValues.setText(s);
                    //textViewPoints.setText("" + points);
                    //placeHolder.setText("|");
 //                   textViewPoints.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
   //                 textViewPredictedTricks.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);


                   // LinearLayout resultContainer = new LinearLayout(ResultActivity.this);
                   // resultContainer.setOrientation(LinearLayout.HORIZONTAL);
                   // cell.addView(resultContainer);
                  //  resultContainer.setGravity(Gravity.RIGHT);



                    cell.addView(textViewValues);
                    textViewValues.setPadding(10, 0, 0, 0);


                }


            }


        }



    }

    //Random Data zum testen der Auflösung


    public void randomdata2(){

        Random rand=new Random();

        playerCount=3;
        maxRounds= 60 / playerCount;
        // ArrayList[] resultrow = new ArrayList[maxRounds + 1];

        for (int p = 0; p <= playerCount; p++) {
            ids.add(123 + p);

        }

        for (int i = 1; i < maxRounds + 1; i++) {

            for (int j = 1; j < playerCount + 1 ; j++){
                predictedTricks = rand.nextInt(7);
                points = rand.nextInt(6)*10;
                String f = Integer.toString(j);

                String s =  " " + predictedTricks + " | " + points;

                Pair<Integer, String> pair = new Pair<>(Integer.valueOf(i), f);
                resultMap.put(pair,s);
            }


        }

    }


}
