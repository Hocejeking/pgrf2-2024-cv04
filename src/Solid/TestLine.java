package Solid;

import transforms.Col;
import transforms.Point3D;

public class TestLine extends  Solid {
    public TestLine(){
        vertexBuffer.add(new Vertex(new Point3D(200,300, 0.1d), new Col(0,255,0)));
        vertexBuffer.add(new Vertex(new Point3D(250,350, 0.9d), new Col(0,255,0)));
        indexBuffer.add(0);
        indexBuffer.add(1);
    }
}
