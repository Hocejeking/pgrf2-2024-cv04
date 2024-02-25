package Solid;

import transforms.Col;
import transforms.Point2D;
import transforms.Point3D;

import java.awt.*;

public class Arrow extends Solid {
    public Arrow(){
        vertexBuffer.add(new Vertex(new Point3D(200,300, 0.5d), new Col(0,255,0)));
        vertexBuffer.add(new Vertex(new Point3D(250,350, 0.5d), new Col(0,255,0)));
        vertexBuffer.add(new Vertex(new Point3D(200,400, 0.5d), new Col(0,255,0)));
        vertexBuffer.add(new Vertex(new Point3D(400,350,0.4d), new Col(0,0,255)));
        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(0);
        indexBuffer.add(2);
        indexBuffer.add(1);
        indexBuffer.add(2);
        indexBuffer.add(3);
        partBuffer.add(new Part(0,2,TopologyType.TRIANGLES));
        partBuffer.add(new Part(2,1,TopologyType.LINES));
    }
}
