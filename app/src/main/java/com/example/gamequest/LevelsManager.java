package com.example.gamequest;

import static com.example.gamequest.gameCalsses.GameInstance.debug;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class LevelsManager {
    public static final String PLAYER_SAVES_FILE_NAME = "saves.txt";
    /*
    level structure

    competed
    name
    hint
    descr


     */
    public static final String[] levelsDescr = {
                "level 1\n" +
                        "grab the needed coins to complete the level\n" +
                        "0 0\n" +
                        "10 3 3\n" +
                        "X1,X1,E1,E1,E1,E1,E1,E1,E1,X1,X1,\n" +
                        "E1,E1,E1,E1,E1,E1_C1,E1,E1,E1,E1,E1,\n" +
                        "E1_P1,E1,E1_C1,E1,X1,X1,X1,E1,E1_C1,E1,E1",
                "level 2\n" +
                        "not always all the coins in the level are required\n" +
                        "0 0\n" +
                        "5 5 2\n" +
                        "X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,X1,X1,\n" +
                        "X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1_C1,\n" +
                        "E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,\n" +
                        "E1,E1_P1,E1,E1,E1,E1,E1,X1,E1,E1,X1,E1,E1,E1_C1,\n" +
                        "X1,X1,X1,X1,E1,X1,X1,X1,E1_U1,E1_U1,X1,X1,X1,X1,\n" +
                        "X1,X1,X1,X1,E1_C1,X1,X1,X1,X1,X1,X1,X1,X1,X1",
            "level 3\n" +
                    "you can bush boxes; coins that get smashed between normal boxes will be lost\n" +
                    "0 0\n" +
                    "3 3 3\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,X1,X1,X1,\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,X1,\n" +
                    "X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,\n" +
                    "X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,E1,E1,E1,E1,E1,\n" +
                    "E1,E1,E1,E1,E1,X1,X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,E1_C1,E1,\n" +
                    "E1,E1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,\n" +
                    "E1_R1,E1,E1_P1,E1,E1,E1_M1,E1,E1_C1,E1_C1,E1_C1,X1,E1,E1,E1,E1,X1,X1,X1,\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,E1_U1,E1_U1,E1_U1,E1_U1,X1,X1,X1",
            "level 4\n" +
                    "as soon as you grab the needed coin you will become invulnerable\n" +
                    "0 0\n" +
                    "1 5 2\n" +
                    "X1,X1,E1_C1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,X1,X1,X1,\n" +
                    "X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,X1,X1,\n" +
                    "X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,E1,E1,E1,\n" +
                    "E1,E1_P1,E1,E1_M1,E1_M1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1_M1,E1_C1,E1,E1,\n" +
                    "X1,X1,X1,X1,X1,E1,X1,X1,X1,E1_U1,E1_U1,E1_U1,X1,X1,X1,X1,X1,E1,\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,E1,\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,E1,\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,E1_U1",
            "level 5\n" +
                    "power black cube: allow create a magic box adjacent to a wall (magic boxes smashing coins will collect them)\n" +
                    "2 1\n" +
                    "6 4 1\n" +
                    "E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,\n" +
                    "E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,\n" +
                    "E1,E1,E1,E1,X1,X1,E1,E1,E1,E1,\n" +
                    "E1,E1,E1_P1,E1,X1,X1,E1,E1_C1,E1,E1",

            "level 6\n" +
                    "level 6\n" +
                    "2 2\n" +
                    "10 4 1\n" +
                    "X1,X1,E1,E1,E1,E1,E1,E1,X1,X1,\n" +
                    "E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,\n" +
                    "E1,E1,E1,E1_C1,E1,E1,E1,E1,E1,E1,\n" +
                    "E1,X1,X1,X1,X1,E1,E1,E1,E1,E1,\n" +
                    "E1,E1,X1,E1,X1,E1,E1,E1,E1,E1,\n" +
                    "E1_P1,E1_M1,E1,E1,E1,E1,E1,E1,E1,E1",
            "level 7\n" +
                    "level 7\n" +
                    "2 3\n" +
                    "5 5 3\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,\n" +
                    "X1,X1,E1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,\n" +
                    "X1,E1,E1,E1,X1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,X1,E1,E1,E1,E1,\n" +
                    "E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,X1,E1,E1,E1,E1,X1,\n" +
                    "X1,E1,E1,E1,E1,E1,E1,E1_C1,E1,E1,E1,E1_C1,X1,X1,X1,X1,E1,E1,E1,E1,\n" +
                    "X1,X1,E1,E1,E1_P1,E1,E1,E1_M1,E1,E1,E1,X1,X1,X1,E1_C1,E1_U1,E1,E1,E1,X1",
            "level 8\n" +
                    "level 8\n" +
                    "2 2\n" +
                    "7 4 4\n" +
                    "X1,X1,E1,E1,X1,X1,X1,X1,E1,X1,X1,X1,X1,X1,X1,X1,\n" +
                    "X1,X1,E1,E1,E1,X1,X1,E1,E1,E1,X1,X1,X1,X1,X1,X1,\n" +
                    "X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,X1,\n" +
                    "E1,E1,E1,E1,E1,E1,E1,E1,E1_P1,E1,E1,E1,E1,E1,E1,X1,\n" +
                    "E1_C1,E1,E1,E1,E1,E1,E1,E1,X1,E1,E1,E1,E1,E1,E1,E1_C1,\n" +
                    "X1,X1,X1,E1,E1,E1,E1,E1,X1,E1,E1_C1,E1,E1,E1,X1,X1,\n" +
                    "X1,X1,E1,E1,E1,E1,X1,X1,X1,X1,X1,E1,E1,E1,E1_C1,X1",
            "level 9\n" +
                    "power black cube: allow create a magic box without gravity on an adjacent cell (only 1 yellow cube can exist at the time) (magic boxes smashing coins will collect them)\n" +
                    "1 -1\n" +
                    "6 5 1\n" +
                    "X1,X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,\n" +
                    "X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,\n" +
                    "X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,\n" +
                    "E1,E1,E1_P1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,E1,E1,E1,\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,X1,E1,E1,E1_C1",
            "level 10\n" +
                    "level 10\n" +
                    "1 1 2 1\n" +
                    "6 5 4\n" +
                    "X1,X1,X1,X1,E1_C1,E1,E1_P1,E1,E1,E1,E1,X1,\n" +
                    "X1,X1,X1,X1,E1,X1,X1,X1,X1,E1,E1,X1,\n" +
                    "E1,E1,E1,X1,E1,E1,E1,E1,E1,E1,E1,X1,\n" +
                    "E1_C1,E1_C1,E1,X1,E1,E1,E1,E1,E1,E1,E1,X1,\n" +
                    "X1,X1,E1,E1,E1,E1,E1,E1,E1,X1,E1,E1_C1,\n" +
                    "X1,X1,X1,X1,E1,E1,E1,E1,X1,X1,X1,X1,\n" +
                    "X1,X1,X1,X1,E1_U1,E1_U1,E1_U1,E1_U1,X1,X1,X1,X1",
            "level 11\n" +
                    "level 11\n" +
                    "1 3 2 3\n" +
                    "5 3 3\n" +
                    "X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1_C1,\n" +
                    "X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,\n" +
                    "X1,E1,E1,E1,E1,E1,E1,E1_C1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,\n" +
                    "E1,E1,E1,E1,E1,E1,E1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,\n" +
                    "E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,\n" +
                    "E1_C1,E1,E1,E1,E1,E1,E1,E1,E1,E1_P1,E1,E1,E1,E1,E1,E1,X1,X1,X1,X1,X1,X1,\n" +
                    "X1,E1_U1,E1_U1,E1_U1,E1,E1,X1,X1,X1,X1,X1,E1_U1,E1_U1,E1_U1,X1,X1,X1,X1,X1,X1,X1,X1",
            "level 12\n" +
                    "level 12\n" +
                    "1 2 2 2\n" +
                    "4 2 4\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,E1_C1,\n" +
                    "E1_C1,E1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,\n" +
                    "X1,E1,E1,X1,X1,E1_C1,E1,E1,E1,E1_P1,E1,E1,E1,E1,E1,X1,X1,\n" +
                    "X1,E1,E1,E1,X1,X1,E1,E1,E1,X1,X1,X1,X1,E1_R1_L1,X1,X1,X1,\n" +
                    "X1,E1,E1,E1,E1,X1,E1,E1,E1,E1,E1,E1,E1_D1_U1,E1,E1_C1,X1,X1,\n" +
                    "X1,X1,X1,E1,E1,E1,E1,E1,X1,X1,X1,X1,X1,E1_C1,X1,X1,X1",
            "level 13\n" +
                    "power teleport: allow to teleport to the first empty cell in the choosen direction\n" +
                    "3 2\n" +
                    "11 5 1\n" +
                    "E1,E1,E1,X1,E1,X1,E1,E1,E1,\n" +
                    "E1,E1,E1,X1,E1,X1,E1,E1,E1,\n" +
                    "E1_P1,E1,E1,X1,E1,X1,E1,E1,E1_C1",
            "level 14\n" +
                    "level 14\n" +
                    "1 -1 2 1 3 1\n" +
                    "9 2 1\n" +
                    "E1,E1,E1,E1,E1,E1,E1,E1_D1_U1,E1,X1,X1,X1,X1,X1,X1,X1,\n" +
                    "E1,E1,E1,E1,E1,X1,X1,X1,E1,X1,X1,X1,X1,X1,X1,X1,\n" +
                    "E1,E1,E1_M1,E1,E1,X1,X1,X1,E1,X1,X1,X1,X1,X1,X1,X1,\n" +
                    "E1,E1,E1_M1,E1,E1,X1,X1,X1,E1,X1,E1,E1,E1,E1,E1_D1,X1,\n" +
                    "E1_P1,E1,E1_M1,E1,E1,X1,X1,X1,E1,X1,E1,E1_M1,E1,E1,E1_U1,E1_C1,\n" +
                    "X1,X1,X1,X1,E1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1",
            "level 15\n" +
                    "level 15\n" +
                    "2 5 3 6\n" +
                    "2 4 6\n" +
                    "X1,X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,\n" +
                    "X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1_C1,X1,X1,\n" +
                    "X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,X1,X1,X1,X1,\n" +
                    "X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,E1,E1_C1,X1,X1,\n" +
                    "X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1_P1,E1,E1,X1,X1,X1,X1,X1,X1,X1,\n" +
                    "E1,E1,E1,X1,X1,X1,X1,E1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,E1_C1,\n" +
                    "X1,E1,E1,E1_C1,X1,X1,X1,E1_C1,X1,X1,X1,X1,X1,X1,X1,X1,E1,E1_C1,X1,X1",
            "level 16\n" +
                    "level 16\n" +
                    "1 7 2 7 3 5\n" +
                    "1 1 4\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,E1_C1,\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,\n" +
                    "E1,E1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,\n" +
                    "E1_C1,E1,E1,E1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,E1,E1,E1,E1,E1,X1,\n" +
                    "X1,X1,E1,E1,E1,E1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,E1,E1,E1,E1,X1,\n" +
                    "E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1_M1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,\n" +
                    "E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,E1,E1,E1,E1_M1,E1_M1,E1,E1,E1,E1,E1,E1,E1,X1,\n" +
                    "X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,E1,E1,E1,E1,E1,E1,E1,X1,\n" +
                    "X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,\n" +
                    "X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1_P1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1_U1,E1_U1,E1_U1,X1,E1,E1,E1_C1,\n" +
                    "X1,X1,X1,X1,E1_U1,E1_U1,E1_U1,E1_U1,X1,X1,X1,X1,X1,E1,E1,X1,X1,X1,E1,E1_L1,X1,X1,X1,X1,X1,X1,\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,E1_C1,X1,X1,X1,X1,X1,X1",
            "level 17\n" +
                    "power grapple: it allow you to grapple in any direction but not all walls can be grappled\n" +
                    "5 2\n" +
                    "8 2 1\n" +
                    "X1,X1,X1,X1,X1,X1,G1,G1,G1,G1,X1,X1,\n" +
                    "E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,\n" +
                    "X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,\n" +
                    "X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,\n" +
                    "X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1_C1,\n" +
                    "X1,X1,X1,X1,E1,E1,E1,E1,E1_L1,X1,X1,X1,\n" +
                    "X1,X1,X1,X1,E1,E1,E1,E1,E1_L1,X1,X1,X1,\n" +
                    "X1,X1,X1,X1,E1,E1_P1,E1_M1,E1,E1_L1,X1,X1,X1",
            "level 18\n" +
                    "if you grapple a movable block you will pull it towards you\n" +
                    "1 1 5 2\n" +
                    "3 3 1\n" +
                    "E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,E1_D1,E1_D1,E1_D1,E1_D1,E1_D1,E1_D1,X1,X1,X1,\n" +
                    "X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1_P1,E1,E1,E1,E1,E1,E1,E1,E1,E1,G1,\n" +
                    "X1,X1,E1,E1,E1,E1,E1,X1,X1,X1,X1,X1,X1,X1,X1,E1,X1,X1,X1,E1_C1,X1,\n" +
                    "X1,X1,X1,X1,E1,E1,E1,E1,E1,X1,X1,X1,X1,X1,X1,E1,X1,X1,X1,X1,X1,\n" +
                    "X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,X1,X1",
            "level 19\n" +
                    "level 19\n" +
                    "1 2 2 3 3 1 5 1\n" +
                    "8 1 3\n" +
                    "X1,X1,X1,X1,X1,X1,E1,E1,E1,X1,X1,E1,X1,X1,X1,X1,\n" +
                    "X1,X1,X1,X1,X1,E1,E1,E1,E1,X1,E1,E1,G1,G1,G1,X1,\n" +
                    "X1,X1,E1,E1,E1,E1,E1,E1,E1_C1,X1,E1,G1,E1,E1,E1,E1,\n" +
                    "X1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,E1,E1,E1,E1,E1,\n" +
                    "X1,E1,E1,E1_P1,E1,E1,E1,E1,X1,X1,X1,E1,E1,E1,E1,E1,\n" +
                    "X1,X1,X1,X1,X1,E1,E1,E1,X1,X1,G1,E1,E1,E1,E1,E1_C1,\n" +
                    "X1,X1,X1,X1,X1,X1,E1,E1,X1,X1,E1,E1,E1,E1,E1,X1,\n" +
                    "X1,X1,X1,X1,X1,X1,X1,E1,X1,X1,E1,E1,E1,E1,E1,X1,\n" +
                    "X1,X1,X1,E1,E1,E1,E1,E1,X1,G1,E1,E1,E1,X1,X1,X1,\n" +
                    "E1_C1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,\n" +
                    "X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,\n" +
                    "X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,\n" +
                    "X1,X1,X1,E1_U1,E1_U1,E1_U1,E1_U1,E1_U1,E1_U1,E1_U1,E1_U1,E1_U1,X1,X1,X1,X1",
            "level 20\n" +
                    "level 20\n" +
                    "1 1 2 1 3 1 5 1\n" +
                    "9 3 3\n" +
                    "E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,\n" +
                    "E1,E1,E1,E1,E1,E1_M1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1_C1,X1,\n" +
                    "E1_C1,X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,\n" +
                    "X1,E1,E1,E1,E1,E1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,\n" +
                    "X1,E1,E1,E1,E1_C1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,X1,X1,\n" +
                    "X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,E1,E1,E1,\n" +
                    "X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,E1_C1,E1,E1,\n" +
                    "X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,E1,E1,X1,X1,E1,\n" +
                    "X1,X1,X1,X1,E1,E1,E1_P1,E1,E1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,\n" +
                    "X1,X1,X1,X1,E1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1",
            "level 21\n" +
                    "power phasing: shoot a bullet that use a power charge to make things phasing and regain a charge by making them non phasing (can be used iven on 0 charges to de-phase things)\n" +
                    "4 1\n" +
                    "9 5 1\n" +
                    "X1,X1,X1,E1,E1,E1,E1,E1,E1,X1,E1,E1,E1,\n" +
                    "E1,E1,E1,E1,E1,E1,X1,E1,E1,X1,X1,E1,E1_C1,\n" +
                    "X1,X1,E1,E1_P1,E1,E1,X1,X1,X1,X1,X1,X1,X1",
            "level 22\n" +
                    "if a de-phasing object has his space occupied it will destroy the other object\n" +
                    "4 1\n" +
                    "8 6 2\n" +
                    "X1,E1,E1,E1,E1,E1,E1,E1,E1,X1,\n" +
                    "E1,E1,E1,E1,E1,E1,E1,E1,E1_C1,X1,\n" +
                    "E1,E1,E1,E1,E1_M1,E1_M1,X1,X1,X1,X1,\n" +
                    "E1,E1,E1,E1,E1_M1,E1_M1,X1,X1,E1,E1,\n" +
                    "E1,E1_P1,E1,E1,E1_M1,E1_M1,X1,X1,E1,E1_C1",
            "level 23\n" +
                    "level 23\n" +
                    "1 2 4 1 5 2\n" +
                    "6 2 4\n" +
                    "X1,X1,X1,X1,E1,E1,X1,X1,X1,X1,X1,X1,X1,X1,X1,\n" +
                    "X1,X1,X1,X1,E1,E1,X1,X1,X1,X1,X1,X1,X1,X1,X1,\n" +
                    "X1,X1,X1,X1,E1_C1,E1,X1,X1,X1,X1,X1,X1,X1,X1,X1,\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,\n" +
                    "X1,X1,X1,E1,E1,E1,E1,E1,X1,X1,X1,X1,X1,X1,X1,\n" +
                    "E1,E1_C1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,G1,\n" +
                    "E1,X1,E1,E1,E1,E1,E1_C1,E1,E1,E1,E1,E1,E1,E1,G1,\n" +
                    "E1,E1_P1,E1,E1,E1,E1,X1,E1,E1,E1,E1,E1,E1,E1_C1,X1,\n" +
                    "X1,X1,X1,E1_U1,E1_U1,E1_U1,E1_U1,E1_U1,E1_U1,E1_U1,E1_U1,E1_U1,X1,X1,X1",
            "level 24\n" +
                    "level 24\n" +
                    "1 -1 2 5 4 1\n" +
                    "6 2 2\n" +
                    "X1,X1,X1,X1,X1,X1,E1,X1,E1,E1,E1,E1,E1,X1,X1,\n" +
                    "X1,X1,X1,X1,E1,E1,E1,X1,X1,X1,E1,E1,E1,X1,X1,\n" +
                    "X1,X1,X1,E1,E1,X1,E1,X1,X1,X1,E1,E1,E1,X1,X1,\n" +
                    "X1,X1,X1,E1,E1_C1,X1,E1,X1,X1,E1,E1,E1,E1,X1,X1,\n" +
                    "X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1_L1,\n" +
                    "X1,E1,E1,E1,E1,E1,E1,X1,E1,E1,E1,E1,E1,E1_C1,E1_L1,\n" +
                    "E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,X1,\n" +
                    "E1_P1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,X1,\n" +
                    "X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,X1,\n" +
                    "X1,X1,E1,E1,E1,X1,X1,X1,E1,E1,E1,X1,X1,X1,X1",
            "level 25\n" +
                    "level 25\n" +
                    "1 3 2 2 3 3 5 3\n" +
                    "6 1 1\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,E1,E1,X1,X1,X1,\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,\n" +
                    "X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1_C1,\n" +
                    "X1,X1,X1,X1,E1,E1,E1,E1,E1,X1,X1,X1,X1,\n" +
                    "E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,E1_D1,E1_D1,X1,\n" +
                    "E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,\n" +
                    "X1,E1,E1_P1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,\n" +
                    "X1,X1,X1,X1,X1,E1_U1,E1_U1,E1_U1,E1_U1,E1,X1,X1,X1,\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,E1,X1,X1,X1,\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,E1,X1,X1,X1,\n" +
                    "X1,X1,E1,E1,E1,X1,X1,X1,X1,X1,X1,X1,X1,\n" +
                    "X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,E1,E1,X1",
            "level 26\n" +
                    "level 26\n" +
                    "1 -1 3 -1 4 2\n" +
                    "6 2 1\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,E1,E1,X1,X1,X1,\n" +
                    "X1,X1,E1,E1,E1,E1,E1,E1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,E1_C1,E1,X1,X1,X1,\n" +
                    "X1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,\n" +
                    "X1,E1,E1,E1_M1,E1_M1,E1_M1,E1,E1,E1,E1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,E1,E1,\n" +
                    "X1,X1,X1,X1,X1,X1,X1,E1,E1,E1,X1,X1,X1,E1,E1,E1,E1,E1,X1,X1,E1,E1,E1,E1,E1,\n" +
                    "X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,X1,X1,E1,E1,E1,E1,E1,X1,X1,E1,E1,X1,E1,E1,\n" +
                    "E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,E1,E1,E1,E1,E1,X1,X1,E1,E1,E1,E1,E1,\n" +
                    "E1,E1_P1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,X1,E1,E1,E1,E1,E1,E1,X1,E1,E1,E1,E1,E1,\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1,\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,E1,E1",
            "level 27\n" +
                    "level 27\n" +
                    "2 -1 4 2\n" +
                    "9 2 6\n" +
                    "X1,X1,E1_C1,E1,X1,X1,X1,X1,X1,E1_C1,E1,E1_D1,E1,E1,X1,X1,E1_C1,\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,E1,E1,E1,E1,X1,\n" +
                    "X1,X1,X1,E1,E1,E1,E1,X1,E1,X1,E1,E1,E1,E1,X1,X1,E1,\n" +
                    "E1_C1,X1,E1,E1,E1,X1,E1,X1,X1,E1,E1,E1,E1,X1,X1,X1,E1,\n" +
                    "X1,X1,E1,X1,X1,E1,X1,X1,E1,E1,E1,E1,E1,X1,X1,E1,E1,\n" +
                    "X1,X1,X1,E1,E1,X1,X1,E1,E1,E1_M1,E1,E1,X1,X1,E1,X1,X1,\n" +
                    "X1,X1,X1,E1,E1,X1,X1,X1,X1,X1,X1,X1,X1,X1,E1,X1,X1,\n" +
                    "X1,X1,E1,X1,X1,X1,X1,X1,E1,X1,E1,E1,X1,X1,E1_C1,X1,X1,\n" +
                    "X1,E1,E1,X1,X1,X1,E1_C1,X1,E1,X1,E1,E1,X1,X1,X1,X1,X1,\n" +
                    "X1,E1,E1_P1,X1,X1,X1,X1,X1,E1,X1,X1,X1,X1,X1,X1,X1,X1"
    };

    public static class Level{
        public final int id;
        public final String name, hint, descr;

        public final boolean isDefault;


        public Level(int id, String name, String hint, String descr, boolean isDefault){
            this.id = id;
            this.name = name;
            this.hint = hint;
            this.descr = descr;
            this.isDefault = isDefault;

        }

    }
    private static LevelsManager instance;
    private AppCompatActivity context;
    private int lastCompletedDefaultLevel;
    private int lastCompletedCustomLevel;


    //getInstance methods
    public static LevelsManager getInstance(AppCompatActivity context) {
        if (instance == null) {
            instance = new LevelsManager(context);
        }

        if(context != null && context != instance.context){
            instance.context = context;

        }

        return instance;
    }
    public static LevelsManager getInstance() {

        return getInstance(null);
    }



    //constructors
    private LevelsManager(AppCompatActivity context){
        if(context != null){
            this.context = context;
            //debug("about to get the file");
            File f = new File(context.getFilesDir()+"/"+PLAYER_SAVES_FILE_NAME);
            //debug("file: "+f.getPath());
            if (!f.exists()){
                //debug("the file does not exist");
                try {
                    f.createNewFile();
                    FileOutputStream stream = new FileOutputStream(f);
                    stream.write(0);
                    //debug("write 0 on info files");
                    stream.close();
                    //debug("file created");

                } catch (IOException e) {

                }
            }else{
                //debug("the file does already exist");
            }//enshure the file exist


            //debug("about to getLastCompletedLevelFromFile ");
            lastCompletedDefaultLevel = getLastCompletedLevelFromFile(true);
            lastCompletedCustomLevel = 0;
        }else{
            lastCompletedDefaultLevel = 0;
            lastCompletedCustomLevel = 0;
        }
    }


    //other methods
    public void setLevelCompletion(int levelId, boolean isDefaultL, boolean completed){
        File f = new File(context.getFilesDir()+"/"+PLAYER_SAVES_FILE_NAME);
        int newLastCompletedLevel = getLastCompletedLevel(isDefaultL);
        if(completed && getLastCompletedLevel(isDefaultL)<levelId){
            newLastCompletedLevel = levelId;
        }
        if(!completed){
            newLastCompletedLevel = levelId - 1;
        }
        if(isDefaultL){
            //debug("newLastCompletedLevel:" + newLastCompletedLevel);
            try {
                //debug("about to write");
                FileOutputStream stream = new FileOutputStream(f);
                stream.write(newLastCompletedLevel);
                stream.close();
                //debug("wrote");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            lastCompletedDefaultLevel = newLastCompletedLevel;
        }else {
            lastCompletedCustomLevel = newLastCompletedLevel;

        }
    }


    //other methods
    public int getLevelsAmount(boolean defaultL){
        if(defaultL){
            return levelsDescr.length;
        }
        return 0;
    }
    public int getLastCompletedLevel(boolean defaultL){
        return defaultL ? lastCompletedDefaultLevel : lastCompletedCustomLevel;

    }
    private int getLastCompletedLevelFromFile(boolean defaultL){
        if(defaultL){
            File f = new File(context.getFilesDir()+"/"+PLAYER_SAVES_FILE_NAME);
            int length = (int) f.length();
            byte[] bytes = new byte[length];
            FileInputStream in = null;

            //debug("about to get last completed level");
            try {
                in = new FileInputStream(f);
                in.read(bytes);
                in.close();
            } catch (IOException e) {
                //debug("getLastCompletedLevelFromFile return 0 exception");
                return 0;
            }
            //debug("getLastCompletedLevelFromFile return actual val:"+(int)new String(bytes).charAt(0));
            return new String(bytes).charAt(0);
        }
        //debug("getLastCompletedLevelFromFile return 0");
        return 0;
    }
    public boolean isCompleted(int id, boolean isDefaultL){
        return getLastCompletedLevel(isDefaultL) >= id;

    }
    public boolean isCompleted(Level level){
        return isCompleted(level.id, level. isDefault);


    }
    public boolean isAvailable(int id, boolean isDefaultL){
        return isCompleted(id-1, isDefaultL);


    }
    public boolean isLevel(int id, boolean isDefaultL){
        return getLevel(id, isDefaultL) != null;

    }
    public Level getLevel(int id, boolean isDefaultL){
        if(levelsDescr.length > id-1){
            //debug("the level exist");

            String[] levelDescr = levelsDescr[id-1].split("\n");

            String name = levelDescr[0];
            String hint = levelDescr[1];
            String descr = "";
            for(int i = 2 ; i < levelDescr.length; i++){
                descr = descr + levelDescr[i]+"\n";
            }
            descr = descr.substring(0, descr.length()-1);

            //System.out.println("getLevel debug:");
            //System.out.println(name);
            //System.out.println(hint);
            //System.out.println(descr);
            //System.out.println(completed);

            return new Level(id,name,hint,descr,true);


        }

        //debug("the level don't exist");

        return null;
    }
}













