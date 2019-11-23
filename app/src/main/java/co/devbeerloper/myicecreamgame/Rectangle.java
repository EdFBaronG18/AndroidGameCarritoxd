package co.devbeerloper.myicecreamgame;

public class Rectangle {
    public float x, y, w, h;
    public Rectangle(float posX, float posY, float weigh, float height){
        this.x = posX;
        this.y = posY;
        this.w = weigh;
        this.h = height;
    }


    public boolean intersect(Rectangle e) {
        return pointInside(e.x, e.y) || pointInside(e.x + e.w, e.y)  || pointInside(e.x, e.y + e.h)  || pointInside(e.x+e.w, e.y + e.h) ;
    }

    public boolean pointInside(float x, float y){
       return x >= this.x && x <= this.x + w && y > this.y && y <= this.y + h;
    }

}
