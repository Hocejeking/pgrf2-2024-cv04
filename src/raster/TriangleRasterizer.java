package raster;

import Solid.Vertex;
import Zbuffer.ZBuffer;
import transforms.Col;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.Optional;

public class TriangleRasterizer implements Rasterizer {
    private final ZBuffer zb;

    public TriangleRasterizer(ZBuffer zb){
        this.zb = zb;
    }

    public void rasterize(Vertex a, Vertex b, Vertex c){
        Optional<Vec3D> dA = a.getPosition().dehomog();
        Optional<Vec3D> dB = b.getPosition().dehomog();
        Optional<Vec3D> dC = c.getPosition().dehomog();
        a = new Vertex(new Point3D(transformToWindow(new Point3D(dA.get()))),a.getColor());
        b = new Vertex(new Point3D(transformToWindow(new Point3D(dB.get()))),b.getColor());
        c = new Vertex(new Point3D(transformToWindow(new Point3D(dC.get()))),c.getColor());

        if(b.getPosition().getY() < a.getPosition().getY()) //pokud B je menší jak A
        {
            Vertex temp = b;
            b = a;
            a = temp;
        }
        if(c.getPosition().getY() < a.getPosition().getY()) //pokud C je menší jak A
        {
            Vertex temp = c;
            c = a;
            a = temp;
        }
        if(c.getPosition().getY() < b.getPosition().getY()) //pokud C je menší jak B
        {
            Vertex temp = c;
            c = b;
            b = temp;
        }

        int aX = (int) Math.round(a.getPosition().getX());
        int aY = (int) Math.round(a.getPosition().getY());
        double aZ = a.getPosition().getZ();

        int bX = (int) Math.round(b.getPosition().getX());
        int bY = (int) Math.round(b.getPosition().getY());
        double bZ = b.getPosition().getZ();

        int cX = (int) Math.round(c.getPosition().getX());
        int cY = (int) Math.round(c.getPosition().getY());
        double cZ = b.getPosition().getZ();

        //prvni cast
        for(int y = aY; y <=bY; y++){
            //hrana AB
            double tAB = (y - aY) / (double) (bY - aY);
            Vertex vAB = a.mul(1-tAB).add(b.mul(tAB));
            int x1 =(int) vAB.getPosition().getX();
            double z1 = vAB.getPosition().getZ();
            //hrana AC
            double tAC = (y-aY)/(double)(cY-aY);
            Vertex vAC = a.mul(1-tAC).add(c.mul(tAC));
            int x2 =(int) vAC.getPosition().getX();
            double z2 = vAC.getPosition().getZ();



            //Triangle vs Triangle-strip
            for(int x = x1; x <= x2; x++){
                double tZ = (x - x1) / (double) (x2-x1);
                double z = (1-tZ) * z1 + tZ * z2;
                zb.setPixelWithZTest(x,y,z, vAC.getColor());
            }
        }
        //zobrazovaci retezec, souradnice, kamery, Java, Rasterizace trojuhelniku, interpolace, zbuffer, Vertex, part buffer.

        //druha cast
        for(int y = bY; y <= cY; y++) {
            //hrana BC
            double tBC = (y - bY) / (double) (cY - bY);
            Vertex vBC = b.mul(1-tBC).add(c.mul(tBC));
            int x1 = (int) vBC.getPosition().getX();
            double z1 = vBC.getPosition().getZ();
            //hrana AC
            double tAC = (y - aY) / (double) (cY - aY);
            Vertex vAC = a.mul(1-tAC).add(c.mul(tAC));
            int x2 = (int) vAC.getPosition().getX();
            double z2 = vAC.getPosition().getZ();

            if (x1 > x2) {
                int tempX = x1;
                x1 = x2;
                x2 = tempX;
            }

            for (int x = x1; x <= x2; x++) {
                double tZ = (x - x1) / (double) (x2-x1);
                double z = (1 - tZ) * z1 + tZ * z2;
                zb.setPixelWithZTest(x, y, z, vAC.getColor());
            }
        }
    }

    @Override
    public Vec3D transformToWindow(Point3D pos) {
        return new Vec3D(pos)
                .mul(new Vec3D(1, -1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D(zb.getImageBuffer().getWidth() / 2f, zb.getImageBuffer().getHeight() / 2f, 1));
    }
}
