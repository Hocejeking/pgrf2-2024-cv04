package Shader;

import transforms.Col;
import transforms.Vec2D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TextureLoader {
    private BufferedImage image;
    public TextureLoader(){
        try {
            image = ImageIO.read(new File("/Users/tobiashocevar/IdeaProjects/pgrf2-2024-cv04/src/texture.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Col getCol(Vec2D texCoords) {
        return new Col(image.getRGB((int) texCoords.getX(), (int) texCoords.getY()));
    }
}
