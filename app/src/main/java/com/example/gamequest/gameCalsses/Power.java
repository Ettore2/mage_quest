package com.example.gamequest.gameCalsses;

import static com.example.gamequest.engine3D_V1.EngineObjectModel.*;
import static com.example.gamequest.gameCalsses.GameInstance.*;

import com.example.gamequest.R;
import com.example.gamequest.engine3D_V1.Point3D;
import com.example.gamequest.engine3D_V1.Vector3D;

import java.util.Vector;

public abstract class Power {
    private char id;
    int imgRes, amount;// amount neg = infinite
    Vector<Integer> availableDirs;
    GameInstance game;

    public static class Nothing extends Power{
        public Nothing(int amount, GameInstance game){
            super(ID_POWER_NOTHING, amount, game, R.drawable.default_img);
        }

        @Override
        protected void active(int dir) {
            decreaseAmount();
        }
    }
    public static class YellowCube extends Power{
        public static GameObject instance;
        public YellowCube(int amount, GameInstance game){
            super(ID_POWER_Y_CUBE, amount, game, R.drawable.yellow_cube);
            availableDirs.add(DIR_UP);
            availableDirs.add(DIR_DOWN);
            availableDirs.add(DIR_LEFT);
            availableDirs.add(DIR_RIGHT);
        }
        @Override
        protected void active(int dir) {
            decreaseAmount();
            if(instance != null){
                game.destroyDynamicForegroundObj(instance);
                instance = null;
            }

            if(dir == DIR_UP){
                if(game.isCellFree(game.player.getGreedX(), game.player.getGreedY()-1)){
                    Point3D pos = game.player.getPosition().sum(new Point3D(0,-game.CELL_SIZE,0));
                    instance = game.instantiateDynamicForegroundObj(ID_BLOCK_Y_CUBE, pos);
                }
            }
            if(dir == DIR_DOWN){
                if(game.isCellFree(game.player.getGreedX(), game.player.getGreedY()+1)){
                    Point3D pos = game.player.getPosition().sum(new Point3D(0,game.CELL_SIZE,0));
                    instance = game.instantiateDynamicForegroundObj(ID_BLOCK_Y_CUBE, pos);
                }
            }
            if(dir == DIR_LEFT){
                if(game.isCellFree(game.player.getGreedX()-1, game.player.getGreedY())){
                    Point3D pos = game.player.getPosition().sum(new Point3D(-game.CELL_SIZE,0,0));
                    instance = game.instantiateDynamicForegroundObj(ID_BLOCK_Y_CUBE, pos);
                }
            }
            if(dir == DIR_RIGHT){
                if(game.isCellFree(game.player.getGreedX()+1, game.player.getGreedY())){
                    Point3D pos = game.player.getPosition().sum(new Point3D(game.CELL_SIZE,0,0));
                    instance = game.instantiateDynamicForegroundObj(ID_BLOCK_Y_CUBE, pos);
                }
            }
        }
    }
    public static class BlackCube extends Power{
        public BlackCube(int amount, GameInstance game){
            super(ID_POWER_B_CUBE, amount, game, R.drawable.black_cube);
            availableDirs.add(GameObject.DIR_LEFT);
            availableDirs.add(GameObject.DIR_RIGHT);
        }
        @Override
        protected void active(int dir) {
            decreaseAmount();
            PowerBullet bullet = new PowerBullet.BulletBCube(game.player.getPosition(), game,null,dir);
            game.player.bullet = bullet;
            game.engineManager.addObject(bullet);

        }
    }
    public static class Teleport extends Power{
        public Teleport(int amount, GameInstance game){
            super(ID_POWER_TELEPORT, amount, game, R.drawable.teleport);
            availableDirs.add(DIR_UP);
            availableDirs.add(GameObject.DIR_DOWN);
            availableDirs.add(GameObject.DIR_LEFT);
            availableDirs.add(GameObject.DIR_RIGHT);
        }
        @Override
        protected void active(int dir) {
            decreaseAmount();
            int x = game.player.getGreedX(), y = game.player.getGreedY();
            boolean found = false;
            while (! found && x >= 0 && x < game.background.length && y >= 0 && y < game.background.length){
                if(dir == DIR_UP){
                    y--;
                }
                if(dir == DIR_DOWN){
                    y++;
                }
                if(dir == DIR_LEFT){
                    x--;
                }
                if(dir == DIR_RIGHT){
                    x++;
                }

                if(x >= 0 && x < game.background.length && y >= 0 && y < game.background[0].length && game.isCellFree(x,y)){
                    found = true;
                }
            }

            if(! found){
                x = game.player.getGreedX();
                y = game.player.getGreedY();
            }

            Point3D pos = game.player.getPosition().sum(new Point3D((x-game.player.getGreedX())*game.CELL_SIZE,(y-game.player.getGreedY())* game.CELL_SIZE,0));
            game.player.setPosition(pos);

        }
    }
    public static class Grapple extends Power{
        public Grapple(int amount, GameInstance game){
            super(ID_POWER_GRAPPLE, amount, game, R.drawable.grapple);
            availableDirs.add(DIR_UP);
            availableDirs.add(GameObject.DIR_LEFT);
            availableDirs.add(GameObject.DIR_RIGHT);
        }
        @Override
        protected void active(int dir) {

        }
    }
    public static class Phase extends Power{
        public Phase(int amount, GameInstance game){
            super(ID_POWER_PHASE, amount, game, R.drawable.phase);
            availableDirs.add(DIR_UP);
            availableDirs.add(GameObject.DIR_DOWN);
            availableDirs.add(GameObject.DIR_LEFT);
            availableDirs.add(GameObject.DIR_RIGHT);
        }

        @Override
        public boolean isUsable() {
            return true;

        }
        @Override
        protected void active(int dir) {
            decreaseAmount();
        }
    }


    //constructors
    public Power(char id, int amount, GameInstance game, int ImgRes){
        this.id = id;
        this.amount = amount;
        this.game = game;
        this.imgRes = ImgRes;
        availableDirs = new Vector<>();

    }


    //getters
    public int getId(){
        return id;

    }
    public int getImgRes() {
        return imgRes;

    }
    public int getAmount() {
        return amount;

    }
    public static Power getPower(char id, int amount, GameInstance game){
        switch (id){
            case ID_POWER_B_CUBE: return new BlackCube(amount,game);
            case ID_POWER_TELEPORT: return new Teleport(amount,game);
            case ID_POWER_PHASE: return new Phase(amount,game);
            case ID_POWER_GRAPPLE: return new Grapple(amount,game);
            case ID_POWER_Y_CUBE: return new YellowCube(amount,game);
            default: return new Nothing(amount,game);
        }
    }


    //setters
    public void setAmount(int amount) {
        this.amount = amount;

    }


    //other methods
    public boolean isUsable() {
        return amount != 0;

    }
    public boolean isInfinite() {
        return amount < 0;

    }
    public void addAmount(int amount) {
        if(this.amount >= 0){
            this.amount += amount;

            if(this.amount < 0){
                this.amount = 0;
            }
        }else {
            this.amount = -1;
        }


    }
    public void decreaseAmount(){
        if(amount > 0){
            amount--;
        }
    }
    public void use(int dir){
        //debug("power use start");
        game.context.handler.post(new Runnable() {
            @Override
            public void run() {
                debug("power active start");
                active(dir);
                debug("power active end");
            }
        });

        //debug("power use end");
    }
    public String toString(){
        return "power id: " + id + "\t amount: " + amount;

    }
    public Vector<Integer> getAvailableDirs() {
        return availableDirs;

    }
    public boolean isAcceptableDir(int dir){
        return availableDirs.contains(dir);

    }

    //function-methods
    protected abstract void active(int dir);
}
