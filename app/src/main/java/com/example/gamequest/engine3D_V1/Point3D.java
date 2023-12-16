package com.example.gamequest.engine3D_V1;

import androidx.annotation.NonNull;

public class Point3D {
    public float x, y, z;

    //constructors
    public Point3D(float x, float y, float z){
        set(x, y, z);
    }
    public Point3D(){
        this(0, 0, 0);

    }
    public Point3D(Point3D p){
        this(p.x, p.y, p.z);

    }
    public Point3D(float x, float y){
        this(x, y, 0);

    }


    //other methods
    public void set(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;

    }
    public void set(Point3D p){
        set(p.x, p.y, p.z);

    }
    public Point3D copy(){
        return new Point3D(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));

    }
    public void add(Point3D v){
        this.x += x;
        this.y += y;
        this.z += z;

    }
    public Point3D sum(Point3D v){
        return new Point3D(this.x + v.x, this.y + v.y, this.z + v.z);

    }
    public Point3D diff(Point3D v){
        return new Point3D(Math.abs(this.x - v.x), Math.abs(this.y - v.y), Math.abs(this.z - v.z));

    }
    public float distanceXY(Point3D p){
        return (float) Math.sqrt(Math.pow(x - p.x,2) + Math.pow(y - p.y,2));

    }
    public Vector3D aziDistances(Point3D p){
        return new Vector3D(Math.abs(x-p.x),Math.abs(y-p.y),Math.abs(z-p.z));

    }
    public float distance(Point3D p){
        return  (float) Math.sqrt(distanceXY(p) + Math.pow(z - p.z,2));
    }
    public Point3D negative(){
        return new Point3D(-x, -y, -z);

    }


    //translation methods
    public Vector3D getVector(){
        return new Vector3D(x,y,z);

    }

    @Override
    public String toString() {
        return "x: "+x+"\ty: "+y+"\tz: "+z;
    }
}












