package com.example.gamequest.engine3D_V1;

import java.util.Vector;

public class BaseAnimation <E> {
    private Vector<AnimationFrame> frames;
    private float timer;
    private int currFrame;


    //constructors
    public BaseAnimation(Vector<AnimationFrame> frames){
        this.frames = frames;

        timer = 0;
        currFrame = 0;
    }
    public BaseAnimation(Vector<E> info, float infoDuration){
        this(null);

        frames = new Vector<>();
        for(int i = 0; i < info.size(); i++){
            frames.add(new AnimationFrame(info.get(i),infoDuration));
        }
    }


    //other methods
    public AnimationFrame update(float deltaT){
        timer +=deltaT;

        if(timer >= frames.get(currFrame).duration){
            timer -= frames.get(currFrame).duration;
            currFrame = (currFrame + 1)%frames.size();

        }
        return frames.get(currFrame);

    }

    public AnimationFrame getCurrFrame(){
        return frames.get(currFrame);

    }
}
