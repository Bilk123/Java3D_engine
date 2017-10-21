package core.math.geom;

import core.math.Matrix4f;
import core.math.Quaternion4f;
import core.math.Vector3f;

import java.io.*;
import java.util.ArrayList;


public class Model {
    private Vector3f vertices[];
    private int[] indices;
    private Matrix4f transform;
    public Model(String objName, Vector3f translation, Vector3f scale, Quaternion4f rotation) {
        try(BufferedReader br = new BufferedReader(new FileReader(new File(objName)))) {
            String line = br.readLine();
            ArrayList<Integer> indicesTemp = new ArrayList<>();
            ArrayList<Vector3f> verticesTemp = new ArrayList<>();
            while(line != null){
                System.out.println(line);
                String[] elements = line.split(" ");
                if(elements[0].equals("v")){
                    float x = Float.valueOf(elements[1]);
                    float y = Float.valueOf(elements[2]);
                    float z = Float.valueOf(elements[3]);
                    System.out.println(x+" : "+ y+" : "+ z);
                    Vector3f v = new Vector3f(x,y,z);
                    verticesTemp.add(v);

                }
                if(elements[0].equals("f")){
                    indicesTemp.add(Integer.parseInt(elements[1])-1);
                    indicesTemp.add(Integer.parseInt(elements[2])-1);
                    indicesTemp.add(Integer.parseInt(elements[3])-1);
                }
                line = br.readLine();
            }
            transform = Matrix4f.initTransformMatrix(translation,scale,rotation);

            vertices = new Vector3f[verticesTemp.size()];
            for(int i = 0;i<verticesTemp.size();i++){
                vertices[i] = transform.mul(verticesTemp.get(i));
            }
            indices = new int[indicesTemp.size()];
            for(int i = 0;i<indicesTemp.size();i++){
                indices[i]= indicesTemp.get(i);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Vector3f[] getVertices() {
        return vertices;
    }

    public int[] getIndices() {
        return indices;
    }
}
