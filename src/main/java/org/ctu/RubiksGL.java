package org.ctu;

import static org.lwjgl.glfw.GLFW.*;

public class RubiksGL {
    private Window window;
    private RubiksCube rubikCube;

    public void run() {
        init();
        loop();

        rubikCube.cleanup();
        window.destroy();
    }

    private void init() {
        // Create window
        window = new Window(600, 600, "Rubik's OpenGL");
        // Create object
        rubikCube = new RubiksCube(1f, 0.015f);
        // Set key event
        window.onKey((window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                this.window.close();
            }
            if (action == GLFW_REPEAT || action == GLFW_PRESS) {
                switch (key) {
                    case GLFW_KEY_UP -> rubikCube.moveUp();
                    case GLFW_KEY_DOWN -> rubikCube.moveDown();
                    case GLFW_KEY_LEFT -> rubikCube.moveLeft();
                    case GLFW_KEY_RIGHT -> rubikCube.moveRight();
                }
            }
        });
    }

    private void loop() {
        while (!window.isClosed()) {
            window.clear();

            rubikCube.update(0);
            window.render(rubikCube);

            window.swapBuffer();
            window.pollEvents();
        }
    }
}