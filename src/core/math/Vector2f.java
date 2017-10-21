package core.math;

public class Vector2f {
    public float x,  y;

    public Vector2f(float x, float y){
        this.x=x;
        this.y=y;
    }

    public Vector2f(){
        x=0;
        y=0;
    }

    public float dot(Vector2f vec) {
        return x * vec.x + y * vec.y;
    }

    public Vector2f add(Vector2f vec){
        return new Vector2f(x+vec.x, y+vec.y);
    }

    public Vector2f sub(Vector2f vec){
        return new Vector2f(x-vec.x, y-vec.y);
    }

    public Vector2f mul(float scalar){
        return new Vector2f(scalar*x, scalar*y);
    }

    public Vector2f mul(double scalar){
        return new Vector2f((float)(scalar*x), (float)(scalar*y));
    }

    public Vector2f normalise(){
        float len = len();
        x/=len;
        y/=len;
        return this;
    }

    public float len(){
        return (float)Math.sqrt(dot(this));
    }

    public float len2(){
        return dot(this);
    }

    public Vector2f cpy() {
        return new Vector2f(x,y);
    }

    public Vector2f mul(Vector2f vec){
        return new Vector2f(x*vec.x,y*vec.y);
    }

    @Override
    public String toString() {
        return "["+x+", "+y+"]";
    }

    public void set(float x, float y) {
        this.x=x;
        this.y=y;
    }
}
