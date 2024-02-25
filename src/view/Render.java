package view;

import Solid.*;
import Zbuffer.ZBuffer;
import raster.LineRasterizer;
import raster.TriangleRasterizer;
import transforms.Col;
import transforms.Mat4;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Render {
    private ZBuffer bf;
    private TriangleRasterizer triangleRasterizer;
    private LineRasterizer lineRasterizer;
    private Mat4 modelMat, view, projecMat4;

    public Render(ZBuffer bf, Mat4 modelMat, Mat4 view, Mat4 projecMat4) {
        this.bf = bf;
        triangleRasterizer = new TriangleRasterizer(bf);
        lineRasterizer = new LineRasterizer(bf);
        this.modelMat = modelMat;
        this.view = view;
        this.projecMat4 = projecMat4;
    }

    private void SortAxisLine(Vertex a, Vertex b){
        a = new Vertex(a.getPosition().mul(view).mul(projecMat4), a.getColor());
        b = new Vertex(b.getPosition().mul(view).mul(projecMat4), b.getColor());
        if(checkAxisOutOfBounds(a,b))
            return;
        if(a.getPosition().getZ() <b.getPosition().getZ()){
            Vertex temp = a;
            a = b;
            b = temp;
        }
        boolean vertexBehindNearPlane = a.getPosition().getZ() < 0 || b.getPosition().getZ() < 0;
        if(vertexBehindNearPlane){
            boolean aBehind = a.getPosition().getZ()<0;
            boolean bBehind = b.getPosition().getZ()<0;
            if(aBehind){
                return;
            }else{
                double t1 = (0 - a.getPosition().getZ()) / (b.getPosition().getZ() - a.getPosition().getZ());
                Vertex ab = a.mul(1 - t1).add(b.mul(t1));
                lineRasterizer.rasterize(a,ab);
            }
        }
        else{
            lineRasterizer.rasterize(a,b);
        }
    }

    private void SortTriangle(Vertex a, Vertex b, Vertex c){
        a = new Vertex(a.getPosition().mul(modelMat).mul(view).mul(projecMat4), a.getColor());
        b = new Vertex(b.getPosition().mul(modelMat).mul(view).mul(projecMat4), b.getColor());
        c = new Vertex(c.getPosition().mul(modelMat).mul(view).mul(projecMat4), c.getColor());
        if(checkTriangleOutOfBounds(a,b,c))
            return;

        //je potřeba saeřadit strany podle z.
        if (a.getPosition().getZ() < b.getPosition().getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        if (b.getPosition().getZ() < c.getPosition().getZ()) {
            Vertex temp = b;
            b = c;
            c = temp;
        }
        if (a.getPosition().getZ() < b.getPosition().getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        boolean vertexBehindNearPlane = a.getPosition().getZ() < 0 || b.getPosition().getZ() < 0 || c.getPosition().getZ() < 0;

        if (vertexBehindNearPlane) {
            // Determine which vertices are behind the near plane
            boolean aBehind = a.getPosition().getZ() < 0;
            boolean bBehind = b.getPosition().getZ() < 0;
            boolean cBehind = c.getPosition().getZ() < 0;

            if (aBehind && !bBehind && !cBehind) {
                // jsou videt b + c
                double t1 = (0 - a.getPosition().getZ()) / (b.getPosition().getZ() - a.getPosition().getZ());
                Vertex ab = a.mul(1 - t1).add(b.mul(t1));

                double t2 = -a.getPosition().getZ() / (c.getPosition().getZ() - a.getPosition().getZ());
                Vertex ac = a.mul(1 - t2).add(c.mul(t2));
                triangleRasterizer.rasterize(a,ab,ac);
            } else if (!aBehind && bBehind && !cBehind) {
                // jsou videt a + c
                double t1 = -a.getPosition().getZ() / (c.getPosition().getZ() - a.getPosition().getZ());
                Vertex ac = a.mul(1 - t1).add(c.mul(t1));

                double t2 = -b.getPosition().getZ() / (c.getPosition().getZ() - b.getPosition().getZ());
                Vertex bc = b.mul(1 - t2).add(c.mul(t2));
                triangleRasterizer.rasterize(a,b,bc);
                triangleRasterizer.rasterize(a,ac,bc);
            } else {
                System.out.println("unable to render");
                //nemuzeme rendrovat jelikoz 2 hrany nejsou viditelne
            }
        } else {
            //ve je videt
            triangleRasterizer.rasterize(a,b,c);
        }
    }

    private boolean checkTriangleOutOfBounds(Vertex a, Vertex b, Vertex c){
        boolean triangleOutOfRightBounds = a.getPosition().getX() > a.getPosition().getW() && b.getPosition().getX() > b.getPosition().getW() && c.getPosition().getX() > c.getPosition().getW();
        boolean triangleOutOfLeftBounds = a.getPosition().getX() < -a.getPosition().getW() && b.getPosition().getX() < -b.getPosition().getW() && c.getPosition().getX() < -c.getPosition().getW();
        boolean triangleOutOfTopBounds = a.getPosition().getY() > a.getPosition().getW() && b.getPosition().getY() > b.getPosition().getW() && c.getPosition().getY() > c.getPosition().getW();
        boolean triangleOutOfBottomBounds = a.getPosition().getY() < -a.getPosition().getW() && b.getPosition().getY() < -b.getPosition().getW() && c.getPosition().getY() < -c.getPosition().getW();
        boolean triangleOutOfFrontBounds = a.getPosition().getZ() > a.getPosition().getW() && b.getPosition().getZ() > b.getPosition().getW() && c.getPosition().getZ() > c.getPosition().getW();
        boolean triangleOutOfBackBounds = a.getPosition().getZ() < 0 && b.getPosition().getZ() < 0 && c.getPosition().getZ() < 0;

        if (triangleOutOfRightBounds || triangleOutOfLeftBounds || triangleOutOfTopBounds ||
                triangleOutOfBottomBounds || triangleOutOfFrontBounds || triangleOutOfBackBounds) {
            return true;
        }
        else{
            return false;
        }
    }

    private boolean checkAxisOutOfBounds(Vertex a, Vertex b){
        boolean AxisOutOfRightBounds = a.getPosition().getX() > a.getPosition().getW() && b.getPosition().getX() > b.getPosition().getW();
        boolean AxisOutOfLeftBounds = a.getPosition().getX() < -a.getPosition().getW() && b.getPosition().getX() < -b.getPosition().getW();
        boolean AxisOutOfTopBounds = a.getPosition().getY() > a.getPosition().getW() && b.getPosition().getY() > b.getPosition().getW();
        boolean AxisOutOfDownBounds = a.getPosition().getY() < -a.getPosition().getW() && b.getPosition().getY() < -b.getPosition().getW();
        boolean AxisOutOfFrontBounds = a.getPosition().getZ() > a.getPosition().getW() && b.getPosition().getZ() > b.getPosition().getW();
        boolean AxisOutOfBackBounds = a.getPosition().getZ() < 0 && b.getPosition().getZ() < 0;

        if(AxisOutOfRightBounds || AxisOutOfLeftBounds || AxisOutOfTopBounds || AxisOutOfDownBounds || AxisOutOfFrontBounds || AxisOutOfBackBounds){
            return true;
        }
        else
        {
            return false;
        }
    }

    public void draw(ArrayList<Solid> solids){
        for(Solid s : solids){
            for(Part p : s.getPartBuffer()){
                TopologyType topT = p.getType();
                switch (topT) {
                    case TRIANGLES:
                        // Procházíme všechny trojúhelníky v části
                        IntStream.range(0, p.getCount())
                                .mapToObj(i -> Arrays.asList(3 * i, 3 * i + 1, 3 * i + 2))
                                .forEach(indices -> {
                                    //je potřeba určit jestli se má trojuhelnik vykreslit nebo ne.
                                    SortTriangle(s.getVertexBuffer().get(s.getIndexBuffer().get(p.getStart() + indices.get(0))),
                                                 s.getVertexBuffer().get(s.getIndexBuffer().get(p.getStart() + indices.get(1))),
                                                 s.getVertexBuffer().get(s.getIndexBuffer().get(p.getStart() + indices.get(2))));
                                });
                    case AXIS:
                        IntStream.range(0, p.getCount())
                                .mapToObj(i -> Arrays.asList(2 * i, 2 * i + 1))
                                .forEach(indices -> {
                                    // Determine whether to draw the line or not
                                    SortAxisLine(s.getVertexBuffer().get(s.getIndexBuffer().get(p.getStart() + indices.get(0))),
                                            s.getVertexBuffer().get(s.getIndexBuffer().get(p.getStart() + indices.get(1))));
                                });
                }
            }
        }
    }
}
