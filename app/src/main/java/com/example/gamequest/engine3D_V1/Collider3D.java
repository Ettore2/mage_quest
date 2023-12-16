package com.example.gamequest.engine3D_V1;


public abstract class Collider3D implements HaveTag {
    public static boolean DEFAULT_VISIBILITY = false;
    public static boolean INVERTED_Y = false;
    public static final int SQUARE = 0, CIRCLE = 1;


    public final int type;
    protected String tag;
    protected Point3D center;
    public boolean isActive,useRelativePosition,isVisible;
    protected EngineObjectModel owner;


    //constructors
    Collider3D(int type, EngineObjectModel owner, Point3D center, String tag){
        this.center = center;
        this.type = type;
        this.owner = owner;
        this.tag = tag;

        isActive = true;
        useRelativePosition = true;
        isVisible = DEFAULT_VISIBILITY;
    }
    Collider3D(int type, EngineObjectModel owner, Point3D center){
        this(type, owner, center,  "");

    }


    //setters
    public void setOwner(EngineObjectModel owner) {
        this.owner = owner;

    }


    //getters
    public EngineObjectModel getOwner() {
        return owner;

    }


    //other methods
    public abstract boolean isColliding2D(Collider3D col);
    public abstract boolean isColliding(Collider3D col);


    //HavePosition3D overrides
    public void setPositionRel(Point3D p){
        center.set(p);

    }
    public void setPositionRel(float x, float y, float z){
        center.set(x,y,z);

    }
    public void setPosX(float x){
        center.x = x;

    }
    public void setPosY(float y){
        center.y = y;

    }
    public void setPosZ(float z){
        center.z = z;

    }
    public Point3D getPositionRel(){
        return center;

    }
    public Point3D getPositionAbs(){
        return center.sum(owner.getPosition());

    }
    public float distanceXY(Point3D p){
        return getPositionAbs().distanceXY(p);

    }
    public float distance(Point3D p){
        return getPositionAbs().distance(p);

    }


    //HaveTag overrides
    public String getTag(){
        return tag;

    }
    public void setTag(String tag){
        this.tag = tag;

    }


}
