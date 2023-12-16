package com.example.gamequest.engine3D_V1;


public class CircularCollider extends Collider3D {
    public float radius;


    //constructors
    public CircularCollider(EngineObjectModel owner, Point3D center, String tag, float radius) {
        super(CIRCLE, owner, center, tag);
        this.radius = radius;
    }
    public CircularCollider(EngineObjectModel owner, Point3D center, float radius) {
        super(CIRCLE, owner, center);
        this.radius = radius;
    }


    //other methods
    public void setDimensions(Point3D center, float radius){
        this.center = center;
        this.radius = radius;

    }


    //Collider3D overrides
    public boolean isColliding2D(Collider3D col) {
        if(col.type == SQUARE){
            return col.isColliding2D(this);
        }
        if(col.type == CIRCLE){
            if(isActive && col.isActive){
                return getPositionAbs().distance(col.getPositionAbs()) <= this.radius + ((CircularCollider)col).radius;
            }
        }

        return false;
    }

    @Override
    public boolean isColliding(Collider3D col) {
        return false;
    }
}
