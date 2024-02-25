package raster;

import Solid.Vertex;
import Zbuffer.ZBuffer;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.Optional;

import static java.lang.Math.abs;


public class LineRasterizer implements Rasterizer {
    private final ZBuffer zb;
    public LineRasterizer(ZBuffer zb){
        this.zb = zb;
    }

    public void rasterize(Vertex a, Vertex b){
        Optional<Vec3D> dA = a.getPosition().dehomog();
        Optional<Vec3D> dB = b.getPosition().dehomog();
        a = new Vertex(transformToWindow(new Point3D(dA.get())),a.getColor());
        b = new Vertex(transformToWindow(new Point3D(dB.get())),b.getColor());

        int aX = (int) Math.round(a.getPosition().getX());
        int aY = (int) Math.round(a.getPosition().getY());

        int bX = (int) Math.round(b.getPosition().getX());
        int bY = (int) Math.round(b.getPosition().getY());

        var dy = bY - aY;
        var dx = bX - aX;

        if(Math.abs(dy) < Math.abs(dx)){
            if (bX < aX) {
                Vertex temp = a;
                a = b;
                b = temp;
            }

            for (int x = Math.max(0, (int) a.getPosition().getX() + 1); x <= Math.min(zb.getImageBuffer().getWidth() - 1, b.getPosition().getX()); x++) {
                double t1 = (x - a.getPosition().getX()) / (b.getPosition().getX() - a.getPosition().getX());
                Vertex d = a.mul(1 - t1).add(b.mul(t1));

                zb.setPixelWithZTest((int) Math.round(d.getPosition().getX()), (int) Math.round(d.getPosition().getY()), d.getPosition().getZ(), d.getColor());
            }
        }
        else{
            if (bY < aY) {
                Vertex temp = a;
                a = b;
                b = temp;
            }
            for (int y = Math.max(0, (int) a.getPosition().getY() + 1); y <= Math.min(zb.getImageBuffer().getHeight() - 1, b.getPosition().getY()); y++) {
                double t1 = (y - a.getPosition().getY()) / (b.getPosition().getY() - a.getPosition().getY());
                Vertex d = a.mul(1 - t1).add(b.mul(t1));
                zb.setPixelWithZTest((int) Math.round(d.getPosition().getX()), (int) Math.round(d.getPosition().getY()), d.getPosition().getZ(), d.getColor());
            }
        }

    }

    @Override
    public Point3D transformToWindow(Point3D pos) {
        // Reflect the Y-axis to make positive Y go downwards
        double newX = pos.getX();
        double newY = -pos.getY();

        // Shift the origin to the top-left corner
        newX += 1; // Shift X by 1 to the right
        newY += 1; // Shift Y by 1 downwards

        // Scale the coordinates to fit within the window
        newX *= zb.getImageBuffer().getWidth() / 2.0; // Scale X to half the width of the window
        newY *= zb.getImageBuffer().getHeight() / 2.0; // Scale Y to half the height of the window

        return new Point3D(newX, newY, pos.getZ());
    }
}
