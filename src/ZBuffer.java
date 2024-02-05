import raster.DepthBuffer;
import raster.ImageBuffer;
import transforms.Col;

public class ZBuffer {
    private final DepthBuffer depthBuffer;
    private final ImageBuffer imageBuffer;

    public ZBuffer(ImageBuffer imageBuffer){
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
    }

    public void setPixelWithZTest(int x, int y, double z, Col color) {
        // TODO: implementovat zbuffer
        // načtu z hodnotu v paměti hloubky na souřadnici x, y
        // porovnám načtenou hodnotu s hodnotu Z, která vstupuje do metody (z hodnota zpracovávaného pixelu)
        // vyhodnotím podmínku
        // pokud je vyhodnocení kladné, upravím hodnotu z v depth bufferu a obarvím pixel
    }


}
