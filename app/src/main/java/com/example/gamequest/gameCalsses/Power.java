package com.example.gamequest.gameCalsses;

import static com.example.gamequest.gameCalsses.GameInstance.*;

import com.example.gamequest.R;

public abstract class Power {
    private int id, imgRes, amount;// amount neg = infinite
    GameInstance game;

    public static class Nothing extends Power{
        public Nothing(int amount, GameInstance game){
            super(ID_POWER_NOTHING, amount, game, R.drawable.default_img);
        }

        @Override
        protected void active(int dirId) {

        }
    }
    public static class YellowCube extends Power{
        public YellowCube(int amount, GameInstance game){
            super(ID_POWER_Y_CUBE, amount, game, R.drawable.yellow_cube);
        }
        @Override
        protected void active(int dirId) {

        }
    }
    public static class BlackCube extends Power{
        public BlackCube(int amount, GameInstance game){
            super(ID_POWER_B_CUBE, amount, game, R.drawable.black_cube);
        }
        @Override
        protected void active(int dirId) {

        }
    }
    public static class Teleport extends Power{
        public Teleport(int amount, GameInstance game){
            super(ID_POWER_TELEPORT, amount, game, R.drawable.teleport);
        }
        @Override
        protected void active(int dirId) {

        }
    }
    public static class Grapple extends Power{
        public Grapple(int amount, GameInstance game){
            super(ID_POWER_GRAPPLE, amount, game, R.drawable.grapple);
        }
        @Override
        protected void active(int dirId) {

        }
    }
    public static class Phase extends Power{
        public Phase(int amount, GameInstance game){
            super(ID_POWER_PHASE, amount, game, R.drawable.phase);
        }

        @Override
        public boolean isUsable() {
            return true;

        }
        @Override
        protected void active(int dirId) {

        }
    }


    //constructors
    public Power(int id, int amount, GameInstance game, int ImgRes){
        this.id = id;
        this.amount = amount;
        this.game = game;
        this.imgRes = ImgRes;

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
    public static Power getPower(int id, int amount, GameInstance game){
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
    public void use(int dirId){
        if(isUsable()){
            active(dirId);
        }
        decrease();

    }
    public String toString(){
        return "power id: " + id + "\t amount: " + amount;
    }


    //function-methods
    protected abstract void active(int dirId);
    protected void decrease(){
        if(amount > 0){
            amount--;
        }
    }
}
