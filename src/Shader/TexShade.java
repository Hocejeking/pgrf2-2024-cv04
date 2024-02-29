package Shader;

import Solid.Vertex;
import transforms.Col;
import transforms.Vec2D;

public class TexShade implements Shader {
    @Override
    public Col shade(Vertex v){
        TextureLoader texLoader = new TextureLoader();
        return texLoader.getCol(v.getTexCoords());
    }
}
