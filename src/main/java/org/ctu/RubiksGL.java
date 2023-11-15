package org.ctu;

import static org.lwjgl.glfw.GLFW.*;

public class RubiksGL {
    private Window window;
    private Shader shader;
    private Mesh rubikCube;

    public void run() {
        init();
        loop();

        rubikCube.cleanup();
        shader.cleanup();
        window.destroy();
    }

    private void init() {
        // Define the vertices of the cube
        float[] vertices = {
                // Front face
                -0.5f,  0.5f, 0.5f,  // Top left
                -0.5f, -0.5f, 0.5f,  // Bottom left
                 0.5f, -0.5f, 0.5f,  // Bottom right
                 0.5f,  0.5f, 0.5f,  // Top right

                // Back face
                -0.5f,  0.5f, -0.5f,  // Top left
                -0.5f, -0.5f, -0.5f,  // Bottom left
                 0.5f, -0.5f, -0.5f,  // Bottom right
                 0.5f,  0.5f, -0.5f,  // Top right

                // Left face
                -0.5f,  0.5f, -0.5f,  // Top left
                -0.5f, -0.5f, -0.5f,  // Bottom left
                -0.5f, -0.5f,  0.5f,  // Bottom right
                -0.5f,  0.5f,  0.5f,  // Top right

                // Right face
                0.5f,  0.5f, -0.5f,  // Top left
                0.5f, -0.5f, -0.5f,  // Bottom left
                0.5f, -0.5f,  0.5f,  // Bottom right
                0.5f,  0.5f,  0.5f,  // Top right

                // Top face
                -0.5f, 0.5f, -0.5f,  // Top left
                -0.5f, 0.5f,  0.5f,  // Bottom left
                 0.5f, 0.5f,  0.5f,  // Bottom right
                 0.5f, 0.5f, -0.5f,  // Top right

                // Bottom face
                -0.5f, -0.5f, -0.5f, // Top left
                -0.5f, -0.5f,  0.5f, // Bottom left
                 0.5f, -0.5f,  0.5f, // Bottom right
                 0.5f, -0.5f, -0.5f  // Top right
        };

        // Create window
        window = new Window(600, 600, "Rubik's OpenGL");
        window.onKey((window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                this.window.close();
            }
        });

        // Create mesh and shader
        shader = new Shader("src/main/resources/vertex.vs", "src/main/resources/fragment.fs");
        //window.setShader(shader);

        rubikCube = new Mesh(vertices);
    }

    private void loop() {
        while (!window.isClosed()) {
            window.clear();

            window.draw(rubikCube);

            window.swapBuffer();
            window.pollEvents();
        }
    }
}