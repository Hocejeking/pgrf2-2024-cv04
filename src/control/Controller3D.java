package control;

import Solid.*;
import Zbuffer.ZBuffer;
import raster.LineRasterizer;
import raster.Raster;
import raster.TriangleRasterizer;
import transforms.*;
import view.Panel;
import view.Render;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Controller3D implements Controller {
    private final Panel panel;
    private Camera cam;
    private final ArrayList<Solid> sceneBuff = new ArrayList<>();
    private Mat4 modelMat;
    private Mat4 projecMat4;
    ZBuffer bf;
    Render render;
    public Controller3D(Panel panel) {
        this.panel = panel;
        initObjects(panel.getRaster());
        prepMat();
        initScene();
        initListeners();
        prepZBuff();
        show();
    }

    public void initObjects(Raster<Col> raster) {
        raster.setDefaultValue(new Col(0x101010));
    }

    //pridam objekty do sceny
    public void initScene(){
        sceneBuff.add(new Arrow());
    }

    public void prepZBuff(){
        bf = new ZBuffer(panel.getRaster());
    }

    private void prepMat() {

        modelMat = new Mat4Identity();

        Vec3D e = new Vec3D(5, -10, 5);
        double azimuth = Math.toRadians(100);
        double zenith = Math.toRadians(-15);
        cam = createCamera(e, azimuth, zenith);

        double fov = Math.PI / 3;
        double aspectRatio = (double) panel.getRaster().getHeight() / panel.getRaster().getWidth();
        double near = 0.5;
        double far = 150;
        projecMat4 = createPerspectiveProjection(fov, aspectRatio, near, far);
    }

    private Camera createCamera(Vec3D position, double azimuth, double zenith) {
        return new Camera()
                .withPosition(position)
                .withAzimuth(azimuth)
                .withZenith(zenith);
    }

    private Mat4 createPerspectiveProjection(double fov, double aspectRatio, double near, double far) {
        return new Mat4PerspRH(fov, aspectRatio, near, far);
    }

    @Override
    public void initListeners() {
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize();
                initObjects(panel.getRaster());
            }
        });
    }

    private void show() {
        panel.clear();
        render = new Render(bf,modelMat,cam.getViewMatrix(),projecMat4);
        render.draw(sceneBuff);
        panel.repaint();
    }
}

 /*lr.draw(new TestLine(), new Col(0,255,0));
        lr.draw(new Line(),new Col(255,0,0));
        lr.draw(new Arrow(),new Col(255,255,0));
        tr.rasterize(
                new Vertex( new Point3D(400,0, 0.5), new Col(255,0,0)),
                new Vertex(new Point3D(0, 300, 0.5), new Col(255,0,0)),
                new Vertex(new Point3D(799,599,0.5), new Col(255,0,0)),
                new Col(255,0,0)
                );
        tr.rasterize(
                new Vertex(new Point3D(500,0,0.3), new Col(0,255,0)),
                new Vertex(new Point3D(0,350,0.7),new Col(0,255,0)),
                new Vertex(new Point3D(400,599,0.7),new Col(0,255,0)),
                new Col(0,255,0)
        );*/