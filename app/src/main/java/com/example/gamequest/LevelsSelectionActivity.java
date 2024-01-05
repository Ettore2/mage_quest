package com.example.gamequest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class LevelsSelectionActivity extends AppCompatActivity {
    public static final String YET_TO_UNLOCK_LEVEL_HINT = "yet to unlock";
    public static final float LEVEL_HINT_PERCENTAGE_SIZE = 1.3f/100f;

    public LevelsManager levelsManager;
    public LevelSquare[][] levelsGreed;
    public ImageButton btnLeftArrow, btnRightArrow, btnPlay;
    public LevelSquare selectedLevel;
    public ImageView levelPointer;
    public int currentPage;
    public int maxPages;
    public TextView textLevelName, textLevelHint, textPagesInfo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);//(hide softkey)
        setContentView(R.layout.activity_levels_selection);

        levelsManager = LevelsManager.getInstance(this);
        SoundManager.getInstance(this);
        SoundManager.getInstance().startMusicPlayer();




        //get views
        levelsGreed = new LevelSquare[4][3];
        levelsGreed[0][0] = new LevelSquare(findViewById(R.id.img_btn_level_1),findViewById(R.id.text_level_number_1), levelsManager.getLevel(1,false),this);
        levelsGreed[1][0] = new LevelSquare(findViewById(R.id.img_btn_level_2),findViewById(R.id.text_level_number_2), levelsManager.getLevel(2,false),this);
        levelsGreed[2][0] = new LevelSquare(findViewById(R.id.img_btn_level_3),findViewById(R.id.text_level_number_3), levelsManager.getLevel(3,false),this);
        levelsGreed[3][0] = new LevelSquare(findViewById(R.id.img_btn_level_4),findViewById(R.id.text_level_number_4), levelsManager.getLevel(4,false),this);
        levelsGreed[0][1] = new LevelSquare(findViewById(R.id.img_btn_level_5),findViewById(R.id.text_level_number_5), levelsManager.getLevel(5,false),this);
        levelsGreed[1][1] = new LevelSquare(findViewById(R.id.img_btn_level_6),findViewById(R.id.text_level_number_6), levelsManager.getLevel(6,false),this);
        levelsGreed[2][1] = new LevelSquare(findViewById(R.id.img_btn_level_7),findViewById(R.id.text_level_number_7), levelsManager.getLevel(7,false),this);
        levelsGreed[3][1] = new LevelSquare(findViewById(R.id.img_btn_level_8),findViewById(R.id.text_level_number_8), levelsManager.getLevel(8,false),this);
        levelsGreed[0][2] = new LevelSquare(findViewById(R.id.img_btn_level_9),findViewById(R.id.text_level_number_9), levelsManager.getLevel(9,false),this);
        levelsGreed[1][2] = new LevelSquare(findViewById(R.id.img_btn_level_10),findViewById(R.id.text_level_number_10), levelsManager.getLevel(10,false),this);
        levelsGreed[2][2] = new LevelSquare(findViewById(R.id.img_btn_level_11),findViewById(R.id.text_level_number_11), levelsManager.getLevel(11,false),this);
        levelsGreed[3][2] = new LevelSquare(findViewById(R.id.img_btn_level_12),findViewById(R.id.text_level_number_12), levelsManager.getLevel(12,false),this);

        btnLeftArrow = findViewById(R.id.img_btn_levels_left_arrow);
        btnRightArrow = findViewById(R.id.img_btn_levels_right_arrow);


        textLevelName = findViewById(R.id.text_level_name);
        textLevelHint = findViewById(R.id.text_level_hint);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        textLevelHint.setTextSize(metrics.heightPixels*LEVEL_HINT_PERCENTAGE_SIZE);
        textPagesInfo = findViewById(R.id.text_pages_info);

        int idTmp = getIntent().getIntExtra(LevelActivity.INTENT_EXTRA_LEVEL_ID,1)-1;
        currentPage = (idTmp)/(levelsGreed.length*levelsGreed[0].length);
        maxPages = levelsManager.getLevelsAmount(true)/(levelsGreed.length*levelsGreed[0].length) + ((levelsManager.getLevelsAmount(true)%(levelsGreed.length*levelsGreed[0].length)!=0) ? 1 : 0) - 1;
        selectedLevel = levelsGreed[(idTmp%(levelsGreed.length*levelsGreed[0].length))%levelsGreed.length][(idTmp%(levelsGreed.length*levelsGreed[0].length))/levelsGreed.length];


        levelPointer = findViewById(R.id.img_level_pointer);
        levelPointer.setZ(selectedLevel.btn.getZ()+1);

        //debug("before graphic update");
        graphicUpdate();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (selectedLevel.btn.getX() == 0){}
                graphicUpdate();
            }
        }).start();
        //ensure a graphicUpdate() after gaining the position of the things ot the screen
    }



    public void levelsGreedBtn(View view){
        SoundManager.getInstance().playSound(R.raw.button_click);
        //start the level
        if(selectedLevel.level != null && selectedLevel.btn.equals(view) && levelsManager.isAvailable(selectedLevel.level.id, selectedLevel.level.isDefault)){
            Intent intent = new Intent(this, LevelActivity.class);
            intent.putExtra(LevelActivity.INTENT_EXTRA_LEVEL_ID, selectedLevel.level.id);
            startActivity(intent);
            finish();
        }

        selectedLevel = null;
        for (int x = 0; x < levelsGreed.length && selectedLevel == null; x++){
            for (int y = 0; y < levelsGreed[0].length; y++){
                if(levelsGreed[x][y].btn.equals(view)){
                    selectedLevel = levelsGreed[x][y];
                }
            }
        }

        graphicUpdate();
    }
    public void levelsArrow(View view){
        SoundManager.getInstance().playSound(R.raw.button_click);
        if(view == btnLeftArrow && currentPage > 0){
            currentPage --;
        }
        if(view == btnRightArrow && currentPage < maxPages){
            currentPage ++;
        }
        graphicUpdate();
    }
    public void backBtn(View view){
        SoundManager.getInstance().playSound(R.raw.button_click);
        startActivity(new Intent(this, StartMenuActivity.class));
        finish();

    }


    //function-methods
    private void graphicUpdate(){
        for (int y = 0; y < levelsGreed[0].length; y++){
            for (int x = 0; x < levelsGreed.length; x++){
                levelsGreed[x][y].level = levelsManager.getLevel(currentPage*(levelsGreed.length*levelsGreed[0].length) + x + y*levelsGreed.length + 1,true);

                levelsGreed[x][y].graphicUpdate(levelsManager.isAvailable(currentPage*(levelsGreed.length*levelsGreed[0].length) + x + y*levelsGreed.length + 1, true));
            }

        }

        levelPointer.setX(selectedLevel.btn.getX());
        levelPointer.setY(selectedLevel.btn.getY());
        //debug(selectedLevel.btn.getX()+"   "+selectedLevel.btn.getY());

        if(selectedLevel.level != null){
            textLevelName.setText(selectedLevel.level.name);
            if(levelsManager.isAvailable(selectedLevel.level.id, selectedLevel.level.isDefault)){
                textLevelHint.setText(selectedLevel.level.hint);
            }else {
                textLevelHint.setText(YET_TO_UNLOCK_LEVEL_HINT);
            }

        }else {
            textLevelName.setText("");
            textLevelHint.setText("");
        }

        if(currentPage == 0){
            btnLeftArrow.setColorFilter(getResources().getColor(R.color.deactivated_levels_arrow));
        }else {
            btnLeftArrow.setColorFilter(null);
        }
        if(currentPage == maxPages){
            btnRightArrow.setColorFilter(getResources().getColor(R.color.deactivated_levels_arrow));
        }else {
            btnRightArrow.setColorFilter(null);
        }

        textPagesInfo.setText((currentPage+1)+"/"+(maxPages+1));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //debug("hi");

        SoundManager.getInstance().startMusicPlayer();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //debug("goodbye");

        SoundManager.getInstance().stopMusicPlayer();
    }

}


















