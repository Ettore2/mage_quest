package com.example.gamequest.gameCalsses;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gamequest.*;

import com.example.gamequest.engine3D_V1.Collider3D;
import com.example.gamequest.engine3D_V1.EngineManager;
import com.example.gamequest.engine3D_V1.Point3D;

import java.util.Vector;

public class GameInstance extends Thread{
    public static final String debugStr = "debug";
    //level codification rules:
    /*
        #general codification
        [indefinite amount of powers]
        [number] [number] [number]
        [row description]
        [row description].

        #[indefinite amount of powers]
        power_id power amount power_id power_amount [ect]

        #power_amount = 0 -> infinite uses

        #[number] [number] [number]
        level_start_x_coordinate level_start_y_coordinate coins_for_win

        #[row description]
        [cell description],[cell description]

        #[cell description]
        [block descr]_[block descr]

        #[block descr]
        block_id is_tangible [activation_lines_list]

        #[activation_lines_list] (can be empty)
        1 4 6 n

        es: 0 1 -> empty block with no lines
        es: 0 1 4 6_3 1 -> empty block that contain a coin and transmit the liens 4 and 6
        es: 1 0 4 6_4 1 -> wall block intangible that transmit the liens 4 and 6 and contains a box that is tangible

        #the last block need to end with '.'
        #the last block of the line do not have ','
        #the end of a row is reported by '\n'
        #level_start_x_coordinate and level_start_y_coordinate are applied to the up-left corner going towards the down-right

        #small level example
     */
    public static final String exampleLevel =
            "1 2 3 4 2 -1\n" +
                    "5 2 1\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,\n" +
                    "X1,X1,E1_R1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,\n" +
                    "X1,X1,E1_R1,E1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,\n" +
                    "X1,X1,E1_R1,E1,X1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,\n" +
                    "X1,X1,E1_R1,E1,E1,E1_C1,E1,E1,E1,E1,E1,E1,E1,E1,X1,\n" +
                    "X1,X1,E1_R1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,E1,X1,\n" +
                    "X1,X1,E1_U1,E1_U1,E1_U1,E1,E1,E1,E1,E1_P1,E1,E1,E1,E1_M1,X1,\n" +
                    "X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X1,X10\n";

    public static final int STATE_PLAYING = 0, STATE_WON = 1, STATE_LOST = -1;
    //powers codes:
    public static final char
            ID_POWER_NOTHING = '0',
            ID_POWER_Y_CUBE = '1',
            ID_POWER_B_CUBE = '2',
            ID_POWER_TELEPORT = '3',
            ID_POWER_PHASE = '4',
            ID_POWER_GRAPPLE = '5';

    //blocks codes:
    public static final char
            ID_BLOCK_EMPTY = 'E',//bg
            ID_BLOCK_WALL = 'X',//bg
            ID_BLOCK_PLAYER = 'P',//fg
            ID_BLOCK_COIN = 'C',//fg
            ID_BLOCK_BOX = 'M',//fg
            ID_BLOCK_BUTTON = 'O',//fg
            ID_BLOCK_ACTIVATION_WALL = 'A',//fg
            ID_BLOCK_Y_CUBE = 'Y',//fg
            ID_BLOCK_B_CUBE = 'B',//fg
            ID_BLOCK_SPIKE_UP = 'U',//bg
            ID_BLOCK_SPIKE_DOWN = 'D',//bg
            ID_BLOCK_SPIKE_LEFT = 'L',//bg
            ID_BLOCK_SPIKE_RIGHT = 'R',//bg
            ID_BLOCK_NON_GRABBABLE_WALL = 'G',
            ID_BLOCK_POWER_BULLET = 'Q';//bg
    public final static int[] BACKGROUND_BLOCKS = {ID_BLOCK_EMPTY, ID_BLOCK_WALL, ID_BLOCK_NON_GRABBABLE_WALL};


    public static final String TAG_EMPTY = "empty";
    public static final String TAG_WALL = "wall";
    public static final String TAG_PLAYER = "player";
    public static final String TAG_BOX = "box";
    public static final String TAG_Y_CUBE = "y_cube";
    public static final String TAG_B_CUBE = "b_cube";
    public static final String TAG_SPIKE = "spike";
    public static final String TAG_BUTTON = "button";
    public static final String TAG_ACTIVATION_WALL = "act_wall";
    public static final String TAG_COIN = "coin";
    public static final String TAG_NON_GRABBABLE_WALL = "n_g_wall";
    public static final String TAG_POWER_BULLET = "n_g_wall";
    public static final int[][] OBJECTS_PRIORITY_LEVELS = {
            {ID_BLOCK_EMPTY},
            {ID_BLOCK_SPIKE_DOWN,ID_BLOCK_SPIKE_UP,ID_BLOCK_SPIKE_LEFT,ID_BLOCK_SPIKE_RIGHT},
            {ID_BLOCK_BUTTON},
            {ID_BLOCK_PLAYER},
            {ID_BLOCK_BOX,ID_POWER_B_CUBE,ID_BLOCK_Y_CUBE},
            {ID_BLOCK_WALL,ID_BLOCK_NON_GRABBABLE_WALL,ID_BLOCK_ACTIVATION_WALL},
            {ID_BLOCK_COIN,ID_BLOCK_POWER_BULLET}
    };
    public static final float LEVEL_JUMP = 1, PHASING_JUMP = 0.5f;
    public static final char PHASING_CODE = '0';



    public final int FIELD_WIDTH, FIELD_HEIGHT, CELL_SIZE;
    public int currState;
    public final EngineManager engineManager;
    public LevelManager.Level level;
    public LevelActivity context;
    public ViewGroup layout;
    private boolean haveInitializedThings;
    public GameObject [][] background;// [x][y] | for empty and walls
    public Player player;
    public Vector<GameObject> foreground;
    public Vector<ImageView> availableImgs;
    private int coinsForWin, coinCollected;


    //constructor
    public GameInstance(LevelActivity context, ViewGroup layout,EngineManager manager, int leveId){
        Collider3D.INVERTED_Y = true;
        this.context = context;
        this.layout = layout;
        this.haveInitializedThings = false;
        availableImgs = new Vector();

        //initialize the game manager
        this.engineManager = manager;

        CELL_SIZE = context.getResources().getDimensionPixelSize(R.dimen.cell_size);
        FIELD_WIDTH = context.getResources().getDimensionPixelSize(R.dimen.game_layout_width)/CELL_SIZE;
        FIELD_HEIGHT = context.getResources().getDimensionPixelSize(R.dimen.game_layout_height)/CELL_SIZE;
        //debug("FIELD_WIDTH:" + FIELD_WIDTH + "   FIELD_HEIGHT:" + FIELD_HEIGHT + "   CELL_SIZE:" + CELL_SIZE);

        background = new GameObject[FIELD_WIDTH][FIELD_HEIGHT];
        foreground = new Vector<>();


        //interprets the level start descr and instantiate all on screen
        //debug(":(!");
        setLevelById(leveId, true);
        //debug(":)!");

    }


    //getters
    public int getLevelId() {
        return level.id;

    }
    public int getCoinsForWin() {
        return coinsForWin;

    }
    public int getCoinCollected() {
        return coinCollected;

    }

    //setters
    public void setLevelById(int levelId, boolean isDefault) {
        //debug("about to get the level "+ levelId);
        this.level = LevelManager.getInstance().getLevel(levelId, isDefault);
        //debug("about to apply following descr: "+level.descr);
        applyLevelDecr(level.descr);
        //debug("apply the descr");

    }
    public void collectCoin(){
        coinCollected++;
        if(coinCollected >= coinsForWin){
            currState = STATE_WON;
        }
        context.graphicUpdate();
    }
    //other methods
    public void applyLevelDecr(String stateDescr){
        if(Power.YellowCube.instance != null){
            destroyDynamicForegroundObj(Power.YellowCube.instance);
            Power.YellowCube.instance = null;
        }

        engineManager.removeAllObjects();//remove all objects from the game manager

        String[] subCodes = stateDescr.split("\n");
        String[] strs;
        coinCollected = 0;

        //set all background to walls
        for(int x = 0; x < background.length; x++){
            for(int y = 0; y < background[0].length; y++){
                if(background[x][y] == null){
                    background[x][y] = new Wall(new Point3D(), this, null);
                }
                if(background[x][y].id != ID_BLOCK_WALL){
                    GameObject objTmp = new Wall(new Point3D(), this, background[x][y].getImageView());
                    background[x][y] = objTmp;
                }
                //debug(CELL_SIZE * x+" "+ CELL_SIZE * y+" "+  background[x][y].getPosition().z);
                Point3D posTmp = new Point3D(CELL_SIZE * x, CELL_SIZE * y, background[x][y].getPosition().z);
                background[x][y].setPosition(posTmp);
            }
        }


        //collect available images
        if(haveInitializedThings){
            for(int i = 0; i < foreground.size(); i++){
                availableImgs.add(foreground.get(i).getImageView());
            }
        }//gather the already created images of foreground elements
        foreground = new Vector<>();

        //reset player
        if(haveInitializedThings){
            this.player.reset();

        }else{
            if(availableImgs.size() > 1){
                this.player = new Player(new Point3D(), this, availableImgs.get(0), availableImgs.get(1));
                availableImgs.remove(0);
                availableImgs.remove(0);
            }else{
                this.player = new Player(new Point3D(), this, null, null);
            }

        }

        //debug(":|");
        //elaborate the player powers
        strs = subCodes[0].split(" ");
        for(int i = 0; i < strs.length; i += 2){
            char powerId = strs[i].charAt(0);
            int powerAmount = Integer.parseInt(strs[i+1]);

            //debug("power info:"+powerId+", "+powerAmount);
            player.addPower(Power.getPower(powerId, powerAmount, this));
        }

        //debug("ddd");
        //elaborate puzzle starting position and coinsForWin
        strs = subCodes[1].split(" ");
        int xStart = Integer.parseInt(strs[0]);
        int yStart = Integer.parseInt(strs[1]);
        coinsForWin = Integer.parseInt(strs[2]);

        //debug("start to elaborate puzzle composition");

        //elaborate puzzle composition
        int offset = 2;
        for(int rowNum = 0; rowNum + offset < subCodes.length; rowNum++){
            //debug("riga "+rowNum+":"+subCodes[rowNum + offset]+" -------------------");
            strs = subCodes[rowNum + offset].split(",");//gain cells
            for(int x = 0; x < strs.length; x++){
                //debug("  "+x+" "+rowNum+" |"+"cella:"+strs[x]);
                String[] blocks = strs[x].split("_");//gain blocks
                for(int i = 0; i < blocks.length; i ++){//instantiate a block
                    //debug("    indice blocco:"+i+" |str blocco:"+blocks[i]);
                    GameObject obj = elaborateObjectsString(blocks[i], availableImgs, x, rowNum,xStart,yStart);

                    Point3D posTmp = new Point3D(CELL_SIZE * (x + xStart), CELL_SIZE * (rowNum+yStart), obj.getPosition().z);
                    obj.setPosition(posTmp);
                    //debug("block pos:"+obj.getPosition().toString()+" |obj id:"+obj.id);

                    //debug("      info blocco creato:"+obj.getTag() +"\t" + x +" " + rowNum+"\tpos z:"+obj.getPosition().z);
                }
            }
            //debug("new row");
        }

        for(int i = 0; i < availableImgs.size(); i++){
            availableImgs.get(i).setVisibility(View.INVISIBLE);

        }//make remaining views invisible


        //add new objects to the game manager
        engineManager.addObject(player);
        for(int x = 0; x < background.length; x++){
            for(int y = 0; y < background[0].length; y++){
                if(background[x][y].id == ID_BLOCK_EMPTY){
                    for(int x2 = -2; x2 < 3; x2++){
                        for(int y2 = -2; y2 < 3; y2++){
                            if(x+x2 >= 0 && x+x2 < background.length && y+y2 >= 0 && y+y2 <background[0].length && background[x+x2][y+y2].id != ID_BLOCK_EMPTY){
                                engineManager.addObject(background[x+x2][y+y2]);
                            }

                        }
                    }
                }
            }
        }
        for(int i = 0; i < foreground.size(); i++){
            engineManager.addObject(foreground.get(i));
        }

        currState = STATE_PLAYING;
        if(!haveInitializedThings){//set have haveInitializedThings
            haveInitializedThings = true;

        }else {
            context.graphicUpdate();
        }

        //debug("finish to apply level descr");
    }
    public void resetLevel(){
        applyLevelDecr(level.descr);


    }


    //other methods
    public Vector<GameObject> getCellForeground(int x, int y){
        Vector<GameObject> result = new Vector<>();

        for(int i = 0; i < foreground.size(); i++){
            if(foreground.get(i).getGreedX() == x && foreground.get(i).getGreedY() == y){
                result.add(foreground.get(i));
            }
        }

        return result;
    }
    public boolean isBackgroundFree(int x, int y){
        if(x < 0 || x >= background.length || y < 0 || y >= background.length){
            return false;
        }
        return !background[x][y].isObstacle || background[x][y].phasing;

    }
    public boolean isForegroundFree(int x, int y){
        Vector<GameObject> v = getCellForeground(x, y);
        for(int i = 0; i < v.size(); i++){
            if(v.get(i).isObstacle && !v.get(i).phasing){
                return false;
            }
        }

        return true;

    }
    public boolean isCellFree(int x, int y){
        return isBackgroundFree(x,y) && isForegroundFree(x, y);

    }


    //function methods
    private GameObject elaborateObjectsString(String sBlock, Vector<ImageView> availableImgs, int x, int y, int xStart,int yStart){
        GameObject objTmp;
        char valTmp = sBlock.charAt(0);//valTmp = block id

        //debug("block str:"+sBlock+" block id:"+valTmp);

        if(valTmp == ID_BLOCK_PLAYER){
            //debug("player");
            if(player == null){
                if(availableImgs.size() > 1){
                    player = new Player(new Point3D(), this, availableImgs.get(0), availableImgs.get(1));
                    availableImgs.remove(0);
                    availableImgs.remove(0);
                }else{
                    player = new Player(new Point3D(), this, null, null);
                }
            }
            objTmp = player;
        }else{
            //create empty for objects that need it
            //debug("non player");

            if(isBackground(valTmp)){
                //debug("background");
                objTmp = createObjNotPlayer(valTmp, background[x+xStart][y+yStart].getImageView());
                background[x+xStart][y+yStart] = objTmp;
            }else{
                //debug("non background");
                if(availableImgs.size() > 0){
                    //debug("have resource");
                    objTmp = createObjNotPlayer(valTmp, availableImgs.get(0));
                    availableImgs.remove(0);
                }else{
                    //debug("do not have resource");
                    objTmp = createObjNotPlayer(valTmp, null);
                }

                foreground.add(objTmp);
            }
        }

        objTmp.setPhasing(sBlock.charAt(1) == PHASING_CODE);
        for(int i = 2; i < sBlock.length(); i++){
            objTmp.lineConnections.add((int)sBlock.charAt(i));

        }


        return objTmp;
    }
    private GameObject createObjNotPlayer(char id, ImageView view){
        Point3D pos = new Point3D();
        //debug("create block with id:"+id);

        switch (id){
            case ID_BLOCK_EMPTY:
                return new Empty(pos, this, view);
            case ID_BLOCK_WALL:
                return new Wall(pos, this, view);
            case ID_BLOCK_COIN:
                return new Coin(pos, this, view);
            case ID_BLOCK_BOX:
                return new Box(pos, this, view);
            case ID_BLOCK_BUTTON:
                return new Button(pos, this, view);
            case ID_BLOCK_ACTIVATION_WALL:
                return new ActivationWall(pos, this, view);
            case ID_BLOCK_Y_CUBE:
                return new YCube(pos, this, view);
            case ID_BLOCK_B_CUBE:
                return new BCube(pos, this, view);
            case ID_BLOCK_SPIKE_UP:
            case ID_BLOCK_SPIKE_DOWN:
            case ID_BLOCK_SPIKE_LEFT:
            case ID_BLOCK_SPIKE_RIGHT:
                return new Spike(pos, this, view, id);
            case ID_BLOCK_NON_GRABBABLE_WALL:
                return new NonGrabbableWall(pos, this, view);
            case ID_BLOCK_POWER_BULLET:
                return null;
                //return new PowerBullet.BulletBCube(pos,this, view, EngineObjectModel.DIR_STOP);
        }
        return null;
    }
    private boolean isBackground(char blockId){
        for(int i = 0; i < BACKGROUND_BLOCKS.length; i++){
            if(blockId == BACKGROUND_BLOCKS[i]){
                return true;
            }
        }
        return false;
    }
    public static void debug(String msg){
        Log.d(debugStr, msg);

    }
    public GameObject instantiateDynamicForegroundObj(char objId, Point3D pos){
        if(objId != ID_BLOCK_PLAYER && !isBackground(objId)){
            GameObject objTmp = createObjNotPlayer(objId, null);
            foreground.add(objTmp);
            objTmp.setPosition(pos);
            objTmp.snapToGreed();
            engineManager.addObject(objTmp);

            return objTmp;
        }
        return null;

    }
    public void destroyDynamicForegroundObj(GameObject obj){
        if(obj != null && obj.id != ID_BLOCK_PLAYER && !isBackground(obj.id)){
            if(!obj.destroyed){
                foreground.remove(obj);
                availableImgs.add(obj.getImageView());
            }
            obj.destroy();
        }
    }
}
