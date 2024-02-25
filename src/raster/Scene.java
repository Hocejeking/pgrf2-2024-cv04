package raster;

import Solid.Solid;
import transforms.Col;
import transforms.Mat4;

import java.util.ArrayList;

public class Scene {
    private ArrayList<Solid> solids;
    private Mat4 projection;
    private Mat4 view;
    private Col color;

    public ArrayList<Solid> getSolids() {
        return solids;
    }

    public void setSolids(ArrayList<Solid> solids) {
        this.solids = solids;
    }

    public Mat4 getProjection() {
        return projection;
    }

    public void setProjection(Mat4 projection) {
        this.projection = projection;
    }

    public Mat4 getView() {
        return view;
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    public Col getColor() {
        return color;
    }

    public void setColor(Col color) {
        this.color = color;
    }
}
