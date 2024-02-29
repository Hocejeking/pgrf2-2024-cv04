package Solid;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;

public class Triangle extends Solid {
    public Triangle(){
        vertexBuffer.add(new Vertex(new Point3D(10, 10, 6), new Col(0, 125, 0), new Vec2D(10,10)));
        vertexBuffer.add(new Vertex(new Point3D(2, 6, 6), new Col(255, 125, 200), new Vec2D(2,6)));
        vertexBuffer.add(new Vertex(new Point3D(4, 5, -6), new Col(0, 125, 200), new Vec2D(4,5)));
        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(2);
        partBuffer.add(new Part(0,1,TopologyType.TRIANGLES));
    }
}
