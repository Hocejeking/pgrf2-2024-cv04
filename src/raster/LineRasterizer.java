package raster;

import Solid.Solid;
import Solid.Vertex;
import Zbuffer.ZBuffer;
import transforms.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.abs;


public class LineRasterizer {
    private final ZBuffer zb;
    private Mat4 modelMatrix = new Mat4Identity() ;
    private Mat4 viewMatrix = new Mat4Identity();
    private Mat4 projectionMatrix = new Mat4Identity();
    public LineRasterizer(ZBuffer zb){
        this.zb = zb;
    }
    private double wmin = 0.1;

    public void rasterize(Point3D a, Point3D b, Col color){
        if (a.getW() < b.getW()) {Point3D x = a; a = b; b = x; }
        if (a.getW() < wmin)
            return;
        if (b.getW() < wmin) {
            double t = (a.getW() -wmin)/(a.getW() - b.getW());
            a = a.mul(1-t).add(b.mul(t));
        }

        Optional<Vec3D> vA = a.dehomog();
        Optional<Vec3D> vB = b.dehomog();

        Optional<Vec3D> corVA = Optional.ofNullable(vA.get().withX(0.5 * (zb.getImageBuffer().getWidth() - 1) * (vA.get().getX() + 1)))
                .map(vec -> vec.withY(0.5 * (zb.getImageBuffer().getHeight() - 1) * (1 - vec.getY())));

        Optional<Vec3D> corVB = Optional.ofNullable(vB.get().withX(0.5 * (zb.getImageBuffer().getWidth() - 1) * (vB.get().getX() + 1)))
                .map(vec -> vec.withY(0.5 * (zb.getImageBuffer().getHeight() - 1) * (1 - vec.getY())));

        //drawLine3D(new Point3D( vA.get().getX(), vA.get().getY(), vA.get().getZ()), new Point3D( vB.get().getX(), vB.get().getY(), vB.get().getZ()), color);
        drawLine3D(new Point3D( corVA.get().getX(), corVA.get().getY(), corVA.get().getZ()), new Point3D( corVB.get().getX(), corVB.get().getY(), corVB.get().getZ()), color);
    }
    public void draw(Solid s, Col barva) {
        ArrayList<Vertex> vertexList = s.getVertexBuffer();
        List<Vertex> transformedVertices = new ArrayList<>();
        modelMatrix = modelMatrix.mul(new Mat4Scale(1, 1, 1));
        modelMatrix = modelMatrix.mul(new Mat4Transl(0, 0, 0));

        Mat4 finalMatrix = modelMatrix.mul(viewMatrix.mul(projectionMatrix));

        for (Vertex v : vertexList) {
            Vertex a = new Vertex(v.getPosition().mul(finalMatrix),v.getColor()); //chyba zde
            transformedVertices.add(a);
        }

        for (int i = 0; i < s.getIndexBuffer().size() - 1; i += 2) {

            int indexA = s.getIndexBuffer().get(i);
            int indexB = s.getIndexBuffer().get(i + 1);

            Vertex point3DA = transformedVertices.get(indexA);
            Vertex point3DB = transformedVertices.get(indexB);

            rasterize(point3DA.getPosition(), point3DB.getPosition(), barva);
        }
    }

    public void drawLine3D(Point3D a, Point3D b, Col color) {
        // Převedení souřadnic bodů na celočíselné hodnoty
        int x1 = (int) a.getX();
        int y1 = (int) a.getY();
        double z1 = a.getZ();
        int x2 = (int) b.getX();
        int y2 = (int) b.getY();
        double z2 = b.getZ();

        // Inicializace seznamu bodů
        List<Point3D> points = new ArrayList<>();
        points.add(new Point3D(x1, y1, z1));

        // Výpočet rozdílů v osách
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        double dz = Math.abs(z2 - z1);

        // Určení směru v osách
        int xs = (x2 > x1) ? 1 : -1;
        int ys = (y2 > y1) ? 1 : -1;
        int zs = (z2 > z1) ? 1 : -1;

        // Určení hlavní osy podle rozdílů
        if (dx >= dy && dx >= dz) {
            // Bresenhamův algoritmus pro X-osu
            int p1 = 2 * dy - dx;
            double p2 = 2 * dz - dx;
            while (x1 != x2) {
                x1 += xs;
                if (p1 >= 0) {
                    y1 += ys;
                    p1 -= 2 * dx;
                }
                if (p2 >= 0) {
                    z1 += zs;
                    p2 -= 2 * dx;
                }
                p1 += 2 * dy;
                p2 += 2 * dz;
                points.add(new Point3D(x1, y1, z1));
            }
        } else if (dy >= dx && dy >= dz) {
            // Bresenhamův algoritmus pro Y-osu
            int p1 = 2 * dx - dy;
            double p2 = 2 * dz - dy;
            while (y1 != y2) {
                y1 += ys;
                if (p1 >= 0) {
                    x1 += xs;
                    p1 -= 2 * dy;
                }
                if (p2 >= 0) {
                    z1 += zs;
                    p2 -= 2 * dy;
                }
                p1 += 2 * dx;
                p2 += 2 * dz;
                points.add(new Point3D(x1, y1, z1));
            }
        } else {
            // Bresenhamův algoritmus pro Z-osu
            double p1 = 2 * dy - dz;
            double p2 = 2 * dx - dz;
            while (z1 != z2) {
                z1 += zs;
                if (p1 >= 0) {
                    y1 += ys;
                    p1 -= 2 * dz;
                }
                if (p2 >= 0) {
                    x1 += xs;
                    p2 -= 2 * dz;
                }
                p1 += 2 * dy;
                p2 += 2 * dx;
                points.add(new Point3D(x1, y1, z1));
            }
        }

        // Vykreslení bodů na zadaných souřadnicích s kontrolou z-bufferu
        for (Point3D p: points) {
            zb.setPixelWithZTest((int) p.getX(), (int) p.getY(), p.getZ(), color);
        }
    }


    public Mat4 getModelMatrix() {
        return modelMatrix;
    }

    public void setModelMatrix(Mat4 modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    public Mat4 getViewMatrix() {
        return viewMatrix;
    }

    public void setViewMatrix(Mat4 viewMatrix) {
        this.viewMatrix = viewMatrix;
    }

    public Mat4 getProjectionMatrix() {
        return projectionMatrix;
    }

    public void setProjectionMatrix(Mat4 projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }
}
