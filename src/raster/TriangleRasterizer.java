package raster;

import Zbuffer.ZBuffer;
import transforms.Col;
import transforms.Point3D;

public class TriangleRasterizer {
    private final ZBuffer zb;

    public TriangleRasterizer(ZBuffer zb){
        this.zb = zb;
    }

    public void rasterize(Point3D a, Point3D b, Point3D c, Col color){


        int aX = (int) Math.round(a.getX());
        int aY = (int) Math.round(a.getY());

        int bX = (int) Math.round(b.getX());
        int bY = (int) Math.round(b.getY());

        int cX = (int) Math.round(c.getX());
        int cY = (int) Math.round(c.getY());

        zb.getImageBuffer().getImg().getGraphics().drawLine(aX,aY,bX,bY);
        zb.getImageBuffer().getImg().getGraphics().drawLine(bX,bY,cX,cY);
        zb.getImageBuffer().getImg().getGraphics().drawLine(aX,aY,cX,cY);

        // seřadit body podle y
        //

        for(int y = aY; y <=bY; y++){
            double tAB = (y - aY) / (double) (bY - aY);
            int x1 =(int)Math.round ((1 - tAB) * aX + tAB * bX);
            double t2 = (y-aY)/(double)(cY-aY);
            int x2 = (int)Math.round ((1 - t2) * aX + t2 * cX);
            for(int x = x1; x <= x2; x++){
                zb.setPixelWithZTest(x,y,0.5d, color);
            }
        }

    }
}
