package Solid;

import transforms.Col;
import transforms.Point2D;
import transforms.Point3D;
import transforms.Vec2D;

import java.awt.*;

public class Arrow extends Solid {
    public Arrow(){
        vertexBuffer.add(new Vertex(new Point3D(5, 5, 5), new Col(255,125,75), new Vec2D(5,5))); // Arrow tip
        vertexBuffer.add(new Vertex(new Point3D(7, 5, 5), new Col(255,125,0), new Vec2D(7,5))); // Arrow base right
        vertexBuffer.add(new Vertex(new Point3D(7, 6, 5), new Col(255,0,0), new Vec2D(7,6))); // Arrow base top right
        vertexBuffer.add(new Vertex(new Point3D(9, -5, 5), new Col(0,125,255), new Vec2D(9,-5))); // Arrow base bottom right
// Define more vertices as needed for the arrow's shape
        indexBuffer.add(0); // Arrow tip
        indexBuffer.add(1); // Arrow base right
        indexBuffer.add(2); // Arrow base top right
        indexBuffer.add(0); // Arrow tip
        indexBuffer.add(3); // Arrow base bottom right
        partBuffer.add(new Part(0,1,TopologyType.TRIANGLES));
        partBuffer.add(new Part(2,1,TopologyType.LINES));
    }
}
