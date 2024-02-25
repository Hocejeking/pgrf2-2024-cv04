package raster;

import Solid.Vertex;
import Zbuffer.ZBuffer;
import transforms.Col;
import transforms.Point3D;

public class TriangleRasterizer {
    private final ZBuffer zb;

    public TriangleRasterizer(ZBuffer zb){
        this.zb = zb;
    }

    public void rasterize(Vertex a, Vertex b, Vertex c, Col color){
        int aX = (int) Math.round(a.getPosition().getX());
        int aY = (int) Math.round(a.getPosition().getY());
        double aZ = a.getPosition().getZ();

        int bX = (int) Math.round(b.getPosition().getX());
        int bY = (int) Math.round(b.getPosition().getY());
        double bZ = b.getPosition().getZ();

        int cX = (int) Math.round(c.getPosition().getX());
        int cY = (int) Math.round(c.getPosition().getY());
        double cZ = b.getPosition().getZ();
        LineRasterizer lr = new LineRasterizer(zb);
        //lr.drawLine3D(new Point3D(aX,aY,aZ), new Point3D(bX,bY,bZ), color);
        //lr.drawLine3D(new Point3D(bX,bY,bZ), new Point3D(cX,cY,cZ), color);
        //lr.drawLine3D(new Point3D(aX,aY,aZ), new Point3D(cX,cY,cZ), color);
        //zb.getImageBuffer().getImg().getGraphics().drawLine(aX,aY,bX,bY);
        //zb.getImageBuffer().getImg().getGraphics().drawLine(bX,bY,cX,cY);
        //zb.getImageBuffer().getImg().getGraphics().drawLine(aX,aY,cX,cY);

        if(bY < aY) //pokud B je menší jak A
        {
            int tempX = bX;
            int tempY = bY;
            double tempZ = bZ;
            bY = aY;
            bX = aX;
            bZ = aZ;
            aX = tempX;
            aY = tempY;
            aZ = tempZ;
        }
        if(cY < aY) //pokud C je menší jak A
        {
            int tempX = cX;
            int tempY = cY;
            double tempZ = cZ;
            cY = aY;
            cX = aX;
            cZ = aZ;
            aX = tempX;
            aY = tempY;
            aZ = tempZ;
        }
        if(cY < bY) //pokud C je menší jak B
        {
            int tempX = cX;
            int tempY = cY;
            double tempZ = cZ;
            cY = bY;
            cX = bX;
            cZ = bZ;
            bX = tempX;
            bY = tempY;
            bZ = tempZ;
        }

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
                zb.setPixelWithZTest(x,y,z, vAB.getColor());
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
}
