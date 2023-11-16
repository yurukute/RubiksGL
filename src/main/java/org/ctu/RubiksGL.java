package org.ctu;

import static org.lwjgl.glfw.GLFW.*;

public class RubiksGL {
    private Window window;
    private Shader shader;
    private Projection projection;
    private Cube rubikCube;

    public void run() {
        init();
        loop();

        rubikCube.cleanup();
        shader.cleanup();
        window.destroy();
    }

    private void init() {
        // Create window
        window = new Window(600, 600, "Rubik's OpenGL");
        window.onKey((window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                this.window.close();
            }
        });

        // Create mesh and shader
        shader = new Shader("src/main/resources/vertex.vs", "src/main/resources/fragment.fs");
        window.setShader(shader);

        UniformsMap uniforms = new UniformsMap(shader.getProgramId());
        uniforms.createUniform("projection");
        uniforms.createUniform("model");

        rubikCube = new Cube();
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