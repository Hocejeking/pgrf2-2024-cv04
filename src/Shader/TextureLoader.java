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
            image = ImageIO.read(new File("C:\\Users\\Administrator\\IdeaProjects\\pgrf2-2024-cv04\\src\\texture.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Col getCol(Vec2D texCoords) {
        System.out.println("x: " + texCoords.getX() + " " + "y: " + texCoords.getY());
        return new Col(image.getRGB((int) texCoords.getX() * 80, (int) texCoords.getY() * 80));
    }
}
