package Solid;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;
import transforms.Vec3D;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ObjLoader extends Solid {

    public  ObjLoader(String filePath) throws IOException {
        Random rand = new Random();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    String[] parts = line.split("\\s+");
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    double z = Double.parseDouble(parts[3]);
                    vertexBuffer.add(new Vertex(new Point3D(x,y,z), new Col(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255))));
                } else if (line.startsWith("f ")) {
                    String[] parts = line.split("\\s+");
                    for (int i = 1; i <= 3; i++) {
                        String[] indicesStr = parts[i].split("/");
                        int vertexIndex = Integer.parseInt(indicesStr[0]) - 1;
                        indexBuffer.add(vertexIndex);
                        partBuffer.add(new Part( 0, getVertexBuffer().size() + 7, TopologyType.TRIANGLES));
                    }

                }
            }
        }
    }

    public  void exportToOBJ(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < vertexBuffer.size(); i++) {
                Vertex vertex = vertexBuffer.get(i);
                writer.write(String.format("v %f %f %f\n", vertex.getPosition().getX(), vertex.getPosition().getY(), vertex.getPosition().getZ()));
            }

            for (int i = 0; i < indexBuffer.size(); i += 3) {
                int v1 = indexBuffer.get(i) + 1;
                int v2 = indexBuffer.get(i + 1) + 1;
                int v3 = indexBuffer.get(i + 2) + 1;
                writer.write(String.format("f %d %d %d\n", v1, v2, v3));
            }
        }
    }

}