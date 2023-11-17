package org.ctu;

import org.ctu.Mesh;
import org.ctu.Renderable;
import org.ctu.Window;

public class Cube extends Model {
    private static final float[] vertices = new float[]{
            // Front (z-index = 0,5)
            -0.5f,  0.5f,  0.5f,  // Top left
             0.5f,  0.5f,  0.5f,  // Top right
             0.5f, -0.5f,  0.5f,  // Bottom right
            -0.5f, -0.5f,  0.5f,  // Bottom left

            // Back (z-index = -0,5)
            -0.5f,  0.5f, -0.5f,  // Top left
             0.5f,  0.5f, -0.5f,  // Top right
             0.5f, -0.5f, -0.5f,  // Bottom right
            -0.5f, -0.5f, -0.5f,  // Bottom left
    };

    private static final float[] colors = new float[]{
            0.5f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.5f,
            0.0f, 0.5f, 0.5f,
            0.5f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.5f,
            0.0f, 0.5f, 0.5f,
    };

    private static final int[] indices = {
            /*
                   4 +------+ 5
                   / |    / |
                0 +------+ 1|
                  |7 +---|--+ 6
                  |/     | /
                3 +------+ 2
            */
            // Clock-wise direction
            0, 1, 2, 0, 3, 2,   // Front face
            4, 5, 6, 4, 6, 7,   // Back face
            4, 5, 1, 4, 1, 0,   // Top face
            7, 6, 2, 7, 2, 3,   // Bottom face
            0, 4, 7, 0, 7, 3,   // Left face
            1, 5, 6, 1, 6, 2,   // Right face
    };

    private Mesh cubeMesh = null;

    public Cube() {
        cubeMesh = new Mesh(vertices, colors, indices);
        position.z = -2;
        updateModel();
    }

    @Override
    public void render(Window window) {
        cubeMesh.render(window);
    }

    @Override
    public void cleanup() {
        cubeMesh.cleanup();
    }
}
