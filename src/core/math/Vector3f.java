package core.math;

public class Vector3f {
    public float x, y, z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public float dot(Vector3f vec) {
        return x * vec.x + y * vec.y + z * vec.z;
    }

    public Vector3f cross(Vector3f vec) {
        return new Vector3f(
                y*vec.z-z*vec.y,
                -x*vec.z+z*vec.x,
                x*vec.y-y*vec.x);
    }

    public Vector3f add(Vector3f vec){
        return new Vector3f(x+vec.x, y+vec.y,z+vec.z);
    }

    public Vector3f sub(Vector3f vec){
        return new Vector3f(x-vec.x, y-vec.y,z-vec.z);
    }

    public Vector3f mul(float scalar){
        return new Vector3f(scalar*x, scalar*y, scalar*z);
    }

    public Vector3f mul(double scalar){
        return new Vector3f((float)(scalar*x), (float)(scalar*y), (float)(scalar*z));
    }

    public Vector3f normalise(){
        float len = len();
        x/=len;
        y/=len;
        z/=len;
        return this;
    }

    public float len(){
        return (float)Math.sqrt(dot(this));
    }

    public float len2(){
        return dot(this);
    }

    @Override
    public String toString() {
        return "["+x+", "+y+", "+z+"]";
    }

    public Vector3f cpy() {
        return new Vector3f(x,y,z);
    }

    public Vector3f mul(Vector3f vec){
        return new Vector3f(x*vec.x,y*vec.y,z*vec.z);
    }

    public Vector2f toVector2f(){
        return new Vector2f(x,y);
    }
}
