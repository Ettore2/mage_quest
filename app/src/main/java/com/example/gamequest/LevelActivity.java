package com.example.gamequest;

import static com.example.gamequest.engine3D_V1.EngineObjectModel.*;
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
import com.example.gamequest.gameCalsses.Player;
import com.example.gamequest.gameCalsses.Power;

import java.util.Vector;

public class LevelActivity extends AppCompatActivity implements Runnable{
    public static final String INTENT_EXTRA_LEVEL_ID = "level id";
    public static final String STR_WIN = "LEVEL COMPLETED", STR_LOST = "LEVEL FAILED";
    public int COLOR_NORMAL, COLOR_SPECIAL;
    public EngineManager engineManager;
    public LevelsManager levelsManager;
    public Handler handler;
    GameInstance game;
    public TextView coinsView, menuResultsView;
    public ImageButton btnMenuQuit, btnMenuRedo, btnMenuNext;
    public ImageButton btnMoveRight, btnMoveLeft, btnMoveUp, btnMoveDown, btnMoveJump;
    public ImageButton btnReset, btnQuit;
    public PowerImgButton[] btnPowers;
    public Thread managerT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// done in xml values/themes (hide up bar)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);//(hide softkey)
        setContentView(R.layout.activity_level);
        //debug("level activity");
        //debug("about to get level manager");
        levelsManager = LevelsManager.getInstance(this);
        SoundManager.getInstance(this);
        SoundManager.getInstance().startMusicPlayer();
        //debug("got level manager");

        COLOR_NORMAL = getResources().getColor(R.color.movement_buttons_background);
        COLOR_SPECIAL = getResources().getColor(R.color.selected_power_background);

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

        //start the game manager
        startManagerThread();
    }


    //other methods
    public void graphicUpdate(){
        //coins text
        //debug("g update LevelActivity1???????????????????????????????????????????");
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

        int levelState = game.currState;
        int visibility = levelState == STATE_PLAYING ? View.INVISIBLE : View.VISIBLE;
        menuResultsView.setVisibility(visibility);
        btnMenuQuit.setVisibility(visibility);
        btnMenuRedo.setVisibility(visibility);
        btnMenuNext.setVisibility(visibility);
        btnMenuQuit.setEnabled(true);
        btnMenuRedo.setEnabled(true);
        //debug("g update LevelActivity4");

        switch (levelState){
            case STATE_WON:
                SoundManager.getInstance().playSound(R.raw.win);
                LevelsManager.getInstance().setLevelCompletion(game.getLevelId(),true,true);
                //debug(""+LevelManager.getInstance().getLastCompletedLevel(true));

                menuResultsView.setText(STR_WIN);
                break;
            case STATE_LOST:
                menuResultsView.setText(STR_LOST);
                break;
        }
        //debug("g update LevelActivity5");

        //debug(String.valueOf(game.level == null));
        btnMenuNext.setEnabled(LevelsManager.getInstance().isCompleted(game.level.id,game.level.isDefault));
        //debug("g update LevelActivity6");


        for(int i = 0; i < btnPowers.length; i++){
            btnPowers[i].button.setBackgroundColor(COLOR_NORMAL);

        }


        Power selectedPower = game.player.getSelectedPower();
        for(int i = 0; i < btnPowers.length; i++){
            if(btnPowers[i].power != null && btnPowers[i].power == selectedPower){
                btnPowers[i].button.setBackgroundColor(COLOR_SPECIAL);
            }else {
                btnPowers[i].button.setBackgroundColor(COLOR_NORMAL);
            }
        }
        if(selectedPower != null){

            Vector<Integer> availableDirs = selectedPower.getAvailableDirs();
            for(int i = 0; i < availableDirs.size(); i++){
                if(availableDirs.get(i) == DIR_UP){
                    btnMoveUp.setBackgroundColor(COLOR_SPECIAL);
                }
                if(availableDirs.get(i) == DIR_DOWN){
                    btnMoveDown.setBackgroundColor(COLOR_SPECIAL);
                }
                if(availableDirs.get(i) == DIR_LEFT){
                    btnMoveLeft.setBackgroundColor(COLOR_SPECIAL);
                }
                if(availableDirs.get(i) == DIR_RIGHT){
                    btnMoveRight.setBackgroundColor(COLOR_SPECIAL);
                }
            }

        }else {
            btnMoveUp.setBackgroundColor(COLOR_NORMAL);
            btnMoveDown.setBackgroundColor(COLOR_NORMAL);
            btnMoveLeft.setBackgroundColor(COLOR_NORMAL);
            btnMoveRight.setBackgroundColor(COLOR_NORMAL);

        }
        //debug("g update LevelActivity7!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

    }


    //function-methods
    private void startManagerThread(){
        if(managerT == null){
            managerT = new Thread(this);
            managerT.start();
        }
    }
    private void stopManagerThread(){
        if(managerT != null){
            managerT.interrupt();
            managerT = null;
        }
    }


    //interface inputs methods
    public void movementBtn(View view){
        /*done in run

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
    public void powerBtn(View view){
        if(game.currState == STATE_PLAYING && game.player.bullet == null && game.player.grounded){

            Power selectedPower = game.player.getSelectedPower();
            Power newSelectedPower = null;
            for(int i = 0; i < btnPowers.length; i++){
                if(btnPowers[i].button.equals(view) && btnPowers[i].power.isUsable()){
                    newSelectedPower = btnPowers[i].power;
                }
            }

            if(selectedPower == null){
                //SoundManager.getInstance().playSound(R.raw.power_select);
                game.player.setSelectedPower(newSelectedPower);

            }else{
                if(selectedPower == newSelectedPower){
                    //SoundManager.getInstance().playSound(R.raw.power_deselect);
                    game.player.setSelectedPower(null);
                }else {
                    //SoundManager.getInstance().playSound(R.raw.power_select);
                    game.player.setSelectedPower(newSelectedPower);

                }
            }


            graphicUpdate();

        }
    }
    public void optionsBtn(View view){
        SoundManager.getInstance().playSound(R.raw.button_click);
        if(view.equals(btnMenuQuit) || view.equals(btnQuit)){
            stopManagerThread();

            Intent intent = new Intent(this, LevelsSelectionActivity.class);
            intent.putExtra(INTENT_EXTRA_LEVEL_ID, game.level.id);
            startActivity(intent);
            finish();
        }
        if(view.equals(btnMenuRedo) || view.equals(btnReset)){
            game.resetLevel();
        }
        if(view.equals(btnMenuNext)){
            if(levelsManager.isLevel(game.getLevelId()+1, true)){
                game.setLevelById(game.getLevelId()+1, true);
                game.resetLevel();
            }else {
                stopManagerThread();

                Intent intent = new Intent(this, LevelsSelectionActivity.class);
                intent.putExtra(INTENT_EXTRA_LEVEL_ID, game.level.id);
                startActivity(intent);
                finish();
            }

        }
    }


    //run
    @Override
    public void run() {
        //debug("manager thread start");
        while (!Thread.interrupted()){
            if(game != null && engineManager != null && game.player != null){
                Player player = game.player;
                Power selectedPower = player.getSelectedPower();

                if(game.currState == STATE_PLAYING){
                    if(btnMoveRight.isPressed()){
                        game.player.inputMovement(DIR_RIGHT);

                    }
                    if(btnMoveLeft.isPressed()){
                        game.player.inputMovement(DIR_LEFT);

                    }
                    if(btnMoveUp.isPressed()){
                        game.player.inputMovement(DIR_UP);

                    }
                    if(btnMoveDown.isPressed()){
                        game.player.inputMovement(DIR_DOWN);

                    }
                    if(btnMoveJump.isPressed()){
                        game.player.inputJump();

                    }
                }//set inputs

                if(engineManager.shouldCycle()){
                    //debug("do a post ");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //debug("about to engineManager.doCycle()");
                            engineManager.doCycle();
                            //debug("done engineManager.doCycle()");

                            /*
                            int count = 0;
                            Vector<EngineObjectModel> v = engineManager.getManagedObjects();
                            for(int i = 0; i < v.size(); i++){
                                if(v.get(i).tag.equals(TAG_B_CUBE)){
                                    count++;
                                }
                            }
                            debug(""+count);

                             */
                            //debug(game.availableImgs.size()+" | "+game.foreground.size());
                        }
                    });//need to be done by the "primary" thread
                }//execute a frame

                //debug("mT do work");
            }
            //debug("manage thread run");

        }
        //debug("manager thread stop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //debug("hi");

        SoundManager.getInstance().startMusicPlayer();
        startManagerThread();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //debug("goodbye");

        SoundManager.getInstance().stopMusicPlayer();
        stopManagerThread();

    }
}





















