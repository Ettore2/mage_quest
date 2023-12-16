package com.example.gamequest;

import static com.example.gamequest.gameCalsses.GameInstance.*;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.gamequest.gameCalsses.Player;
import com.example.gamequest.gameCalsses.Power;

import java.util.Vector;

public class LevelActivity extends AppCompatActivity implements Runnable{
    public EngineManager manager;
    Handler handler;
    GameInstance game;
    public TextView coinsView;
    public ImageButton btnMoveRight, btnMoveLeft, btnMoveUp, btnMoveDown, btnMoveJump;
    public ImageButton btnReset, btnQuit;
    public PowerImgButton[] btnPowers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// done in xml values/themes (hide up bar)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);//(hide softkey)
        setContentView(R.layout.activity_main);

        handler = new Handler(Looper.getMainLooper());
        manager = new EngineManager(180, false);
        game = new GameInstance(this,(ViewGroup) findViewById(R.id.layout_game),manager,0);

        //gather commands view
        coinsView = findViewById(R.id.text_coins);
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

        graphicUpdate();
        //stat the game manager
        new Thread(this).start();
    }


    //other methods
    public void graphicUpdate(){
        //coins text
        //debug("g update LevelActivity");
        coinsView.setText(game.coinCollected +"/"+game.coinsForWin);

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


    }


    //interface inputs methods
    public void movementBtn(View view){
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
    public void powerBtn(View view){}
    public void optionsBtn(View view){}

    //run
    @Override
    public void run() {
        while (true){
            //debug("thread start");
            if(game != null && manager != null && game.player != null){
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



                if(manager.shouldCycle()){
                    //debug("  post");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            manager.doCycle();
                        }
                    });
                }
                //debug("thread end");

            }
        }
    }
}