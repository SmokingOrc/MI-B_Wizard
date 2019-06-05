package com.example.mi_b_wizard.Data;

import java.util.ArrayList;
import java.util.List;

public class PlayerList {
    private static List<Player> players = new ArrayList<>();

    public static void addPlayer(Player player) {
        players.add(player);
    }

    public static List<Player> getPlayers() {
        return players;
    }

    public static String getPlayerNameById(int i){
        for (Player player : players) {
            if (player.getPlayerId() == i){
                return player.getPlayerName();
            }

        }
        return null;
    }

    public static int getMaximalRounds() {
        int numberOfPlayers = players.size();
        if (numberOfPlayers <= 6 && numberOfPlayers >= 3) {
            if (numberOfPlayers == 3) {
                return 20;
            } else if (numberOfPlayers == 4) {
                return 15;
            } else if (numberOfPlayers == 5) {
                return 12;
            } else {
                return 10;
            }
        }
        return -1;
    }
}
