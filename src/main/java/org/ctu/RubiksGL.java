package org.ctu;

import static org.lwjgl.glfw.GLFW.*;

public class RubiksGL {
    private Window window;
    private Cube rubikCube;

    public void run() {
        init();
        loop();

        rubikCube.cleanup();
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

        // Create object
        rubikCube = new Cube();
    }

    private void loop() {
        while (!window.isClosed()) {
            window.clear();

            window.render(rubikCube);

            window.swapBuffer();
            window.pollEvents();
        }
    }
}