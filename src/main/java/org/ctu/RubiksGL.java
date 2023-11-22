package org.ctu;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class RubiksGL {
    private Window window;
    private RubiksCube rubikCube;
    private Vector2f mousePos;
    private boolean dragging;

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
        // Set mouse click event
        window.onClick((window, button, action, mods) -> {
            if (button == GLFW_MOUSE_BUTTON_1) {
                if (action == GLFW_PRESS) {
                    dragging = true;
                    mousePos = this.window.getMousePos();
                }
                else if (action == GLFW_RELEASE) {
                    dragging = false;
                }
            }
        });
        // Set cursor move event
        window.onCursorMove((window, x, y) -> {
            if (dragging) {
                Vector2f prevMousePos = mousePos;
                mousePos = this.window.getMousePos();
                Vector2f delta = new Vector2f(mousePos).sub(prevMousePos).mul(2);
                rubikCube.rotate(delta.x, delta.y);
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