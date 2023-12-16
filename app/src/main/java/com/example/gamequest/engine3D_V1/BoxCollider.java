package com.example.gamequest.engine3D_V1;

public class BoxCollider extends Collider3D {
    public Vector3D size;


    //constructors
    public BoxCollider(EngineObjectModel owner, Point3D center, String tag, Vector3D size) {
        super(SQUARE, owner, center, tag);
        this.size = size;
    }
    public BoxCollider(EngineObjectModel owner, Point3D center, Vector3D size) {
        this(owner, center, "", size);
    }


    //other methods
    public float getLeftLate(){
            return this.getPositionAbs().x-(size.x/2);
    }
    public float getRightLate(){
            return this.getPositionAbs().x+(size.x/2);
    }
    public float getUpLate(){
        if(INVERTED_Y){
            return this.getPositionAbs().y-(size.y/2);
        }
        return this.getPositionAbs().y+(size.y/2);
    }
    public float getDownLate(){
        if(INVERTED_Y){
            return this.getPositionAbs().y+(this.size.y/2);
        }
        return this.getPositionAbs().y-(this.size.y/2);
    }
    public float getBackLate(){
        return this.getPositionAbs().z+(size.z/2);
    }
    public float getFrontLate(){
        return this.getPositionAbs().z-(this.size.z/2);
    }
    public Point3D pointDownLeft(){
        return new Point3D(getLeftLate(), getDownLate());

    }
    public Point3D pointUpLeft(){
        return new Point3D(getLeftLate(), getUpLate());

    }
    public Point3D pointDownRight(){
        return new Point3D(getRightLate(), getDownLate());

    }
    public Point3D pointUpRight(){
        return new Point3D(getRightLate(), getDownLate());

    }
    public void setDimensions(Point3D center, Vector3D size){
        this.center = center;
        this.size = size;

    }


    //Collider3D overrides
    public boolean isColliding2D(Collider3D col) {
        if(col.type == SQUARE){
            if(isActive && col.isActive){
                BoxCollider boxCol=(BoxCollider)col;
                return ((this.getLeftLate()-boxCol.getRightLate())*(this.getRightLate()-boxCol.getLeftLate())<0 && (this.getDownLate()-boxCol.getUpLate())*(this.getUpLate()-boxCol.getDownLate())<0);
            }
        }
        if(col.type == CIRCLE){
            if(isActive && col.isActive){
                //se il cerchio è dentro a il quadrato
                if(getLeftLate() <= col.getPositionAbs().x && getRightLate() >= col.getPositionAbs().x && getDownLate() <= col.getPositionAbs().y && getUpLate() >= col.getPositionAbs().y){
                    return true;
                }

                boolean greaterThanD1 = false,greaterThanD2 = false;
                float m,q;

                //diagonale 1
                m = (pointUpLeft().y - pointDownRight().y) / (pointUpLeft().x - pointDownRight().x);
                q = pointDownRight().y - m*pointDownRight().x;
                if(col.getPositionAbs().y >= m * col.getPositionAbs().x + q){
                    greaterThanD1 = true;
                }
                //diagonale 2
                m = (pointDownLeft().y - pointUpRight().y) / (pointDownLeft().x - pointUpRight().x);
                q = pointDownLeft().y - m*pointDownLeft().x;
                if(col.getPositionAbs().y >= m * col.getPositionAbs().x + q){
                    greaterThanD2 = true;
                }

                //selezione casi
                Point3D A = null,B = null,newCircleCenter = null;
                if(greaterThanD1 && greaterThanD2){
                    A = pointUpLeft();
                    B = pointUpRight();
                    newCircleCenter = new Point3D(col.getPositionAbs().x, col.getPositionAbs().y);
                }//cerchio è sopra
                if(greaterThanD1 && !greaterThanD2){
                    A = new Point3D(pointDownRight().y,pointDownRight().x);
                    B = new Point3D(pointUpRight().y,pointUpRight().x);
                    newCircleCenter = new Point3D(col.getPositionAbs().y, col.getPositionAbs().x);
                }//cerchio è a destra
                if(!greaterThanD1 && greaterThanD2){
                    A = new Point3D(pointDownLeft().y,pointDownLeft().x);
                    B = new Point3D(pointUpLeft().y,pointUpLeft().x);
                    newCircleCenter = new Point3D(col.getPositionAbs().y, col.getPositionAbs().x);
                }//cerchio è a sinistra
                if(!greaterThanD1 && !greaterThanD2){
                    A = pointDownLeft();
                    B = pointDownRight();
                    newCircleCenter = new Point3D(col.getPositionAbs().x, col.getPositionAbs().y);
                }//cerchio è sotto

                //x punto compresa tra AB
                if(A.x <= newCircleCenter.x && B.x >= newCircleCenter.x){
                    return Math.abs(newCircleCenter.y-A.y) <= ((CircularCollider)col).radius;
                }else{//x punto non compresa tra AB
                    float xTranslation;
                    if(newCircleCenter.x<A.x){
                        xTranslation = Math.abs(A.x-newCircleCenter.x);
                    }else{
                        xTranslation = Math.abs(B.x-newCircleCenter.x);
                    }

                    return Math.sqrt(Math.pow(xTranslation,2) + Math.pow(A.y - newCircleCenter.y,2)) <= ((CircularCollider)col).radius;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isColliding(Collider3D col) {
        return false;
    }
}












