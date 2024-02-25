package control;

import Solid.*;
import Zbuffer.ZBuffer;
import raster.Raster;
import transforms.*;
import view.Panel;
import view.Render;

import java.awt.event.*;
import java.util.ArrayList;

public class Controller3D implements Controller {
    private final Panel panel;
    private Camera cam;
    final double krok_kamery = 0.1;
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
        sceneBuff.add(new Triangle());
        sceneBuff.add(new Axis());
        sceneBuff.add(new Arrow());
    }

    public void prepZBuff(){
        bf = new ZBuffer(panel.getRaster());
    }

    private void prepMat() {

        modelMat = new Mat4Identity();

        Vec3D e = new Vec3D(10, -15, 5);
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
        MouseAdapter cameraListener = new MouseAdapter() {
            int x = -1, y = -1;
            boolean move = false;

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                    move = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                x = y = -1;
                move = false;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (move) {
                    if (x != -1 && y != -1) {
                        double daz = (e.getX() - x) / 300.0;
                        double dze = (e.getY() - y) / 300.0;
                        cam = cam.addAzimuth(daz);
                        cam = cam.addZenith(dze);
                        show();
                    }
                    x = e.getX();
                    y = e.getY();
                }
            }
        };
        panel.addMouseListener(cameraListener);
        panel.addMouseMotionListener(cameraListener);
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_UP) {
                    cam = cam.forward(krok_kamery);
                }
                if (key == KeyEvent.VK_DOWN) {
                    cam = cam.backward(krok_kamery);
                }
                if (key == KeyEvent.VK_LEFT) {
                    cam = cam.left(krok_kamery);
                }
                if (key == KeyEvent.VK_RIGHT) {
                    cam = cam.right(krok_kamery);
                }
                if (key == KeyEvent.VK_F){
                    System.out.println("switching");
                    projecMat4 = new Mat4PerspRH(Math.PI / 3, (float )bf.getImageBuffer().getHeight() / bf.getImageBuffer().getWidth(), 0.5, 30);
                }

                show();
            }
        });
    }

    private void show() {
        panel.clear();
        bf.clear();
        bf.getImageBuffer().clear();
        render = new Render(bf, modelMat,cam.getViewMatrix(),projecMat4);
        render.clear();
        render.draw(sceneBuff);
        panel.repaint();
    }
}