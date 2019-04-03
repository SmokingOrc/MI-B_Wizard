package com.example.mi_b_wizard;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class JoinGameActivity extends AppCompatActivity {
    private String[] availableGames = null;
    private ListView lvAvailableGames = null;
    private ArrayAdapter adapter = null;
    private Button btnStartGame = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        findViewsByID();
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStartGame_Click();
            }
        });
        availableGames = new String[]{"Game 1", "Game 2", "Game 3", "Game 4"};

        adapter = new ArrayAdapter<String>(this, R.layout.layoutfile, this.availableGames);
        lvAvailableGames.setAdapter(adapter);
    }

    private void findViewsByID() {
        lvAvailableGames = (ListView)findViewById(R.id.lvAvailableGames);
        btnStartGame = (Button)findViewById(R.id.btnStartGame);
    }

    private void btnStartGame_Click() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("You selected the game: ");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        /*builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });*/

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
