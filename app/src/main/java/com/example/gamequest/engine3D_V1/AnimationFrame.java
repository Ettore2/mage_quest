package com.example.gamequest.engine3D_V1;

public class AnimationFrame<E> {
    public E info;
    public float duration;

    public AnimationFrame(E info, float duration){
        this.info = info;
        this.duration = duration;

    }
}
