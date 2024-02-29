package Shader;

import Solid.Vertex;
import transforms.Col;
import transforms.Vec2D;

public class TexShade implements Shader {
    private static TextureLoader texLoader = new TextureLoader();
    @Override
    public Col shade(Vertex v){
        return texLoader.getCol(v.getTexCoords());
    }
}
