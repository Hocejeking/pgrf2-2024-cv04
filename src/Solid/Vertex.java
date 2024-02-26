package Solid;

import transforms.Col;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;

public class Vertex implements Vectorizable<Vertex> {
    private final Point3D position;
    private final Col color;

    //souřadnice do textury

    public Vertex(Point3D pos, Col color){
        this.position = pos;
        this.color =color;
    }

    public Point3D getPosition(){
        return position;
    }

    public Col getColor(){
        return color;
    }

    public Vertex mul(double t) {
        // Násobení pozice a barvy vrcholu skalárem
        Point3D newPosition = position.mul(t);
        Col newColor = color.mul(t);
        return new Vertex(newPosition, newColor);
    }

    @Override
    public Vertex add(Vertex v) {
        // Sčítání pozice a barvy vrcholů
        Point3D newPosition = position.add(v.getPosition());
        Col newColor = color.add(v.getColor());
        return new Vertex(newPosition, newColor);
    }
    @Override
    public String toString(){
        System.out.println("X: " + this.getPosition().getX()+
                            "Y: " + this.getPosition().getY()+
                            "Z: "+ this.getPosition().getZ());
        return null;
    }
}
