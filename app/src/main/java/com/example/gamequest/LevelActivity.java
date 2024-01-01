package com.example.gamequest;

import static com.example.gamequest.gameCalsses.GameInstance.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gamequest.engine3D_V1.EngineManager;
import com.example.gamequest.gameCalsses.GameInstance;
import com.example.gamequest.gameCalsses.GameObject;
import com.example.gamequest.gameCalsses.Power;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.util.Vector;

public class LevelActivity extends AppCompatActivity implements Runnable{
    public static final String INTENT_EXTRA_LEVEL_ID = "level id";
    public static final String STR_WIN = "LEVEL COMPLETED", STR_LOST = "LEVEL FAILED";
    public EngineManager engineManager;
    public LevelManager levelManager;
    Handler handler;
    GameInstance game;
    public TextView coinsView, menuResultsView;
    public ImageButton btnMenuQuit, btnMenuRedo, btnMenuNext;
    public ImageButton btnMoveRight, btnMoveLeft, btnMoveUp, btnMoveDown, btnMoveJump;
    public ImageButton btnReset, btnQuit;
    public PowerImgButton[] btnPowers;
    public Thread managerT;
    public boolean runManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// done in xml values/themes (hide up bar)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);//(hide softkey)
        setContentView(R.layout.activity_level);
        //debug("level activity");
        //debug("about to get level manager");
        levelManager = LevelManager.getInstance(this);
        //debug("got level manager");

        //gather commands views
        btnMoveRight = findViewById(R.id.img_btm_right);
        btnMoveLeft = findViewById(R.id.img_btm_left);
        btnMoveUp = findViewById(R.id.img_btm_up);
        btnMoveDown = findViewById(R.id.img_btm_down);
        btnMoveJump = findViewById(R.id.img_btn_jump);
        btnReset = findViewById(R.id.img_btn_reset);
        btnQuit = findViewById(R.id.img_btn_quit);
        btnPowers = new PowerImgButton[5];
        btnPowers[0] = new PowerImgButton(findViewById(R.id.img_btn_power_0),findViewById(R.id.txt_power_0),null);
        btnPowers[1] = new PowerImgButton(findViewById(R.id.img_btn_power_1),findViewById(R.id.txt_power_1),null);
        btnPowers[2] = new PowerImgButton(findViewById(R.id.img_btn_power_2),findViewById(R.id.txt_power_2),null);
        btnPowers[3] = new PowerImgButton(findViewById(R.id.img_btn_power_3),findViewById(R.id.txt_power_3),null);
        btnPowers[4] = new PowerImgButton(findViewById(R.id.img_btn_power_4),findViewById(R.id.txt_power_4),null);
        //gather other views
        coinsView = findViewById(R.id.text_coins);
        menuResultsView = findViewById(R.id.txt_menu_level_results);
        btnMenuQuit = findViewById(R.id.img_btn_menu_quit);
        btnMenuRedo = findViewById(R.id.img_btn_menu_reset);
        btnMenuNext = findViewById(R.id.img_btn_menu_next);

        int levelId = getIntent().getIntExtra(INTENT_EXTRA_LEVEL_ID,1);
        handler = new Handler(Looper.getMainLooper());
        engineManager = new EngineManager(180, false);
        game = new GameInstance(this,(ViewGroup) findViewById(R.id.layout_game), engineManager,levelId);
        //debug("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");

        graphicUpdate();

        //stat the game manager
        runManager = true;
        managerT = new Thread(this);
        managerT.start();
    }


    //other methods
    public void graphicUpdate(){
        //coins text
        //debug("g update LevelActivity1");
        coinsView.setText(game.getCoinCollected() +"/"+game.getCoinsForWin());
        //debug("g update LevelActivity2");

        //power buttons
        Vector<Power> playerPowers = game.player.getPowers();
        for(int i = 0; i < btnPowers.length; i++){
            if(playerPowers.size() > i){
                btnPowers[i].power = playerPowers.get(i);
            }else {
                btnPowers[i].power = null;
            }


            btnPowers[i].graphicUpdate();
        }
        //debug("g update LevelActivity3");

        int levelState = game.currLevelState;
        int visibility = levelState == LEVEL_PLAYING ? View.INVISIBLE : View.VISIBLE;
        menuResultsView.setVisibility(visibility);
        btnMenuQuit.setVisibility(visibility);
        btnMenuRedo.setVisibility(visibility);
        btnMenuNext.setVisibility(visibility);
        btnMenuQuit.setEnabled(true);
        btnMenuRedo.setEnabled(true);
        //debug("g update LevelActivity4");

        switch (levelState){
            case LEVEL_WON:
                LevelManager.getInstance().setLevelCompletion(game.getLevelId(),true,true);
                //debug(""+LevelManager.getInstance().getLastCompletedLevel(true));

                menuResultsView.setText(STR_WIN);
                break;
            case LEVEL_LOST:
                menuResultsView.setText(STR_LOST);
                break;
        }
        //debug("g update LevelActivity5");

        //debug(String.valueOf(game.level == null));
        btnMenuNext.setEnabled(LevelManager.getInstance().isCompleted(game.level.id,game.level.isDefault));
        //debug("g update LevelActivity6");

    }


    //interface inputs methods
    public void movementBtn(View view){
        /*

        if(game != null && game.currLevelState == LEVEL_PLAYING){
            Player player = game.player;

            if(view.equals(btnMoveRight)){
                player.inputMovement(GameObject.DIR_RIGHT);
            }
            if(view.equals(btnMoveLeft)){
                player.inputMovement(GameObject.DIR_LEFT);
            }
            if(view.equals(btnMoveUp)){
                player.inputMovement(GameObject.DIR_UP);
            }
            if(view.equals(btnMoveDown)){
                player.inputMovement(GameObject.DIR_DOWN);
            }
            if(view.equals(btnMoveJump)){
                player.inputJump();
            }
        }
         */

    }
    public void powerBtn(View view){}
    public void optionsBtn(View view){
        if(view.equals(btnMenuQuit) || view.equals(btnQuit)){
            if(managerT != null){
                managerT.interrupt();
            }
            runManager = false;

            startActivity(new Intent(this, LevelsSelectionActivity.class));
            finish();
        }
        if(view.equals(btnMenuRedo) || view.equals(btnReset)){
            game.resetLevel();
        }
        if(view.equals(btnMenuNext)){
            if(levelManager.isLevel(game.getLevelId()+1, true)){
                game.setLevelById(game.getLevelId()+1, true);
                game.resetLevel();
            }else {
                if(managerT != null){
                    managerT.interrupt();
                }
                runManager = false;

                startActivity(new Intent(this, LevelsSelectionActivity.class));
                finish();
            }

        }
    }


    //run
    @Override
    public void run() {
        while (runManager && ! Thread.interrupted()){
            //debug("thread start");
            if(game != null && engineManager != null && game.player != null){

                if(game.currLevelState == LEVEL_PLAYING){
                    if(btnMoveRight.isPressed()){
                        game.player.inputMovement(GameObject.DIR_RIGHT);
                    }
                    if(btnMoveLeft.isPressed()){
                        game.player.inputMovement(GameObject.DIR_LEFT);
                    }
                    if(btnMoveUp.isPressed()){
                        game.player.inputMovement(GameObject.DIR_UP);
                    }
                    if(btnMoveDown.isPressed()){
                        game.player.inputMovement(GameObject.DIR_DOWN);
                    }
                    if(btnMoveJump.isPressed()){
                        game.player.inputJump();
                    }
                }//set inputs

                if(engineManager.shouldCycle()){
                    //debug("  post");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //debug("player start pos: "+game.player.getPosition().toString());
                            engineManager.doCycle();
                            //debug("player end pos: "+game.player.getPosition().toString()+"grounded:"+game.player.grounded);
                        }
                    });
                }//execute a frame
                //debug("thread end");

            }

        }
    }
}