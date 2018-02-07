package core.graphics;

import core.math.Vector3f;

public class Vertex {
    private float x, y, z;
    private VertexAttribute va;

    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex(Vector3f v) {
        x = v.x;
        y = v.y;
        z = v.y;
    }

}
