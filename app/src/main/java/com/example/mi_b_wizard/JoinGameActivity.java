package com.example.mi_b_wizard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class JoinGameActivity extends AppCompatActivity {
    private String[] availableGames = null;
    private ListView lvAvailableGames = null;
    private ArrayAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        availableGames = new String[]{"Game 1", "Game 2", "Game 3", "Game 4"};
        lvAvailableGames = (ListView)findViewById(R.id.lvAvailableGames);
        adapter = new ArrayAdapter<String>(this, R.layout.layoutfile, this.availableGames);
        lvAvailableGames.setAdapter(adapter);
    }
}
