package raster;

import transforms.Point3D;

public interface Rasterizer {

    public Point3D transformToWindow(Point3D pos);
}
