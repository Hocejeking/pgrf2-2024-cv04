package Solid;

import transforms.Col;
import transforms.Point3D;

public class TestTriangle extends  Solid{
    public TestTriangle(){
        vertexBuffer.add(new Vertex(new Point3D(8, 12, 6), new Col(0, 255, 0)));
        vertexBuffer.add(new Vertex(new Point3D(12, 11, 5), new Col(0, 0, 0)));
        vertexBuffer.add(new Vertex(new Point3D(0, 0, -4), new Col(255, 255, 0)));
        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(2);
        partBuffer.add(new Part(0,1,TopologyType.TRIANGLES));
    }
}
