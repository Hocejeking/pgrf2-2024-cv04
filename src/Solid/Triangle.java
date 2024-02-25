package Solid;

import transforms.Col;
import transforms.Point3D;

public class Triangle extends Solid {
    public Triangle(){
        /*vertexBuffer.add(new Vertex( new Point3D(400,0, 0.5), new Col(255,0,0)));
        vertexBuffer.add(new Vertex(new Point3D(0, 300, 0.5), new Col(255,0,0)));
        vertexBuffer.add(new Vertex(new Point3D(799,599,0.5), new Col(255,0,0)));*/
        vertexBuffer.add(new Vertex(new Point3D(10, 10, 6d), new Col(0, 125, 0)));
        vertexBuffer.add(new Vertex(new Point3D(-2, 6, -4d), new Col(255, 125, 200)));
        vertexBuffer.add(new Vertex(new Point3D(5, 7, -2d), new Col(0, 125, 200)));
        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(2);
        partBuffer.add(new Part(0,1,TopologyType.TRIANGLES));
    }
}
