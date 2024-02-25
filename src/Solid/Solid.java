package Solid;

import java.util.ArrayList;

public abstract class Solid {
    public final ArrayList<Vertex> vertexBuffer = new ArrayList<Vertex>();
    public final ArrayList<Integer> indexBuffer = new ArrayList<>();
    public final ArrayList<Part> partBuffer = new ArrayList<Part>();

    public ArrayList<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    public ArrayList<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public ArrayList<Part> getPartBuffer() {
        return partBuffer;
    }
}
