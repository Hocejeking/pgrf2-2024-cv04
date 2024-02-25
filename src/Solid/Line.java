package Solid;

import transforms.Col;
import transforms.Point3D;

public class Line extends Solid {
    public Line(){
        vertexBuffer.add(new Vertex(new Point3D(200,300, 0.5d), new Col(0,255,0)));
        vertexBuffer.add(new Vertex(new Point3D(250,350, 0.5d), new Col(0,255,0)));
        indexBuffer.add(0);
        indexBuffer.add(1);
        partBuffer.add(new Part(0,1,TopologyType.LINES));
    }
}
