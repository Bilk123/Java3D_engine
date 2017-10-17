package util.math.geom;

import util.math.linearAlgebra.Vector3f;

public class Face3 {
    private Vector3f p1,p2,p3;

    public Face3(Vector3f p1, Vector3f p2, Vector3f p3){
        this.p1=p1;
        this.p2=p2;
        this.p3=p3;
    }

    public Vector3f calcuateNormal(){
        return p2.sub(p1).cross(p3.sub(p1));
    }


}
