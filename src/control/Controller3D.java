package control;

import Zbuffer.ZBuffer;
import raster.Raster;
import raster.TriangleRasterizer;
import transforms.Col;
import transforms.Point3D;
import view.Panel;

import java.awt.event.*;

public class Controller3D implements Controller {
    private final Panel panel;

    public Controller3D(Panel panel) {
        this.panel = panel;
        initObjects(panel.getRaster());
        initListeners();
        redraw();
    }

    public void initObjects(Raster<Col> raster) {
        raster.setDefaultValue(new Col(0x101010));
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

    private void redraw() {
        panel.clear();

        ZBuffer bf = new ZBuffer(panel.getRaster());
        TriangleRasterizer tr = new TriangleRasterizer(bf);
        tr.rasterize(
                new Point3D(400,1, 0.5d),
                new Point3D(1, 300, 0.5d),
                new Point3D(500,500,0.5d),
                new Col(255,0,0)
                );
        panel.repaint();
    }
}
