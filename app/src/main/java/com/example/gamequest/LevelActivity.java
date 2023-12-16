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

import java.util.Vector;

public class LevelActivity extends AppCompatActivity implements Runnable{
    public static final String INTENT_EXTRA_LEVEL_ID = "level id";
    public static final String STR_WIN = "LEVEL COMPLETED", STR_LOST = "LEVEL FAILED";
    public EngineManager manager;
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


        int levelId = getIntent().getIntExtra(INTENT_EXTRA_LEVEL_ID,0);
        handler = new Handler(Looper.getMainLooper());
        manager = new EngineManager(180, false);
        game = new GameInstance(this,(ViewGroup) findViewById(R.id.layout_game),manager,levelId);

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
                //TODO: check if the level was already completed (info gained from in a txt)
                btnMenuNext.setEnabled(true);
                menuResultsView.setText(STR_WIN);
                break;
            case LEVEL_LOST:
                btnMenuNext.setEnabled(false);
                menuResultsView.setText(STR_LOST);
                break;

        }
        //debug("g update LevelActivity5");
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
    public void optionsBtn(View view){}
    public void menuBtn(View view){
        if(view.equals(btnMenuQuit)){
            if(managerT != null){
                managerT.interrupt();
            }
            startActivity(new Intent(this, LevelsSelectionActivity.class));
        }
        if(view.equals(btnMenuRedo)){
            game.setLevelDecr(game.levelStartDescr);
        }
        if(view.equals(btnMenuNext)){
            game.setLevelId(game.getLevelId()+1);
            game.resetLevel();

        }
    }


    //run
    @Override
    public void run() {
        while (runManager){
            //debug("thread start");
            if(game != null && manager != null && game.player != null){

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

                if(manager.shouldCycle()){
                    //debug("  post");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            manager.doCycle();
                        }
                    });
                }//execute a frame
                //debug("thread end");

            }

        }
    }
}