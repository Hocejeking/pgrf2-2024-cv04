package view;

import Solid.*;
import Zbuffer.ZBuffer;
import raster.LineRasterizer;
import raster.TriangleRasterizer;
import transforms.Mat4;

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

    private void SortTriangle(Vertex a, Vertex b, Vertex c){
        Vertex aTrans = new Vertex(a.getPosition().mul(modelMat).mul(view).mul(projecMat4), a.getColor());
        Vertex bTrans = new Vertex(b.getPosition().mul(modelMat).mul(view).mul(projecMat4), b.getColor());
        Vertex cTrans = new Vertex(c.getPosition().mul(modelMat).mul(view).mul(projecMat4), c.getColor());
        if(checkTriangleOutOfBounds(aTrans,bTrans,cTrans))
            return;
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

    public void draw(ArrayList<Solid> solids){
        for(Solid s : solids){
            for(Part p : s.getPartBuffer()){
                TopologyType topT = p.getType();
                switch (topT) {
                    case TRIANGLES: // Trojúhelníková topologie
                        // Procházíme všechny trojúhelníky v části
                        IntStream.range(0, p.getCount())
                                .mapToObj(i -> Arrays.asList(3 * i, 3 * i + 1, 3 * i + 2))
                                .forEach(indices -> {
                                    //je potřeba určit jestli se má trojuhelnik vykreslit nebo ne.
                                    SortTriangle(s.getVertexBuffer().get(s.getIndexBuffer().get(p.getStart() + indices.get(0))),
                                                 s.getVertexBuffer().get(s.getIndexBuffer().get(p.getStart() + indices.get(1))),
                                                 s.getVertexBuffer().get(s.getIndexBuffer().get(p.getStart() + indices.get(2))));
                                });
                        break;
                    case LINE:
                    case AXIS:
                        break;
                }
            }
        }
    }
}
