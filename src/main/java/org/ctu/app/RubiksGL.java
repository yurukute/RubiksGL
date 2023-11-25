package org.ctu.app;

import org.ctu.model.RubiksCube;
import org.ctu.opengl.Window;
import org.joml.Quaternionf;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class RubiksGL implements AppLogic {
    private Window window;
    private RubiksCube rubikCube;
    private Vector2f mousePos;
    private boolean dragging;
    private final float PI_OVER_8 = (float) Math.PI / 8;

    public void run() {
        init();
        loop();

        rubikCube.cleanup();
        window.destroy();
    }

    public void init() {
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
                    case GLFW_KEY_Q -> rubikCube.rotate(-PI_OVER_8, 0);
                    case GLFW_KEY_E -> rubikCube.rotate(PI_OVER_8, 0);
                    case GLFW_KEY_N -> rubikCube.rotate(0, -PI_OVER_8);
                    case GLFW_KEY_M -> rubikCube.rotate(0, PI_OVER_8);
                    case GLFW_KEY_F -> rubikCube.rotateFrontFace((mods & GLFW_MOD_SHIFT) == 0);
                    case GLFW_KEY_B -> rubikCube.rotateBackFace((mods & GLFW_MOD_SHIFT) == 0);
                    case GLFW_KEY_U -> rubikCube.rotateUpFace((mods & GLFW_MOD_SHIFT) == 0);
                    case GLFW_KEY_D -> rubikCube.rotateDownFace((mods & GLFW_MOD_SHIFT) == 0);
                    case GLFW_KEY_L -> rubikCube.rotateLeftFace((mods & GLFW_MOD_SHIFT) == 0);
                    case GLFW_KEY_R -> {
                        if (mods != GLFW_MOD_CONTROL) {
                            rubikCube.rotateRightFace((mods & GLFW_MOD_SHIFT) == 0);
                        }
                        else {
                            rubikCube.setRotation(new Quaternionf());
                            rubikCube.reset();
                        }
                    }
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

    public void loop() {
        long lastTime = System.currentTimeMillis();
        while (!window.isClosed()) {
            window.clear();

            long now = System.currentTimeMillis();
            long runtime = now - lastTime;
            lastTime = now;
            rubikCube.update(runtime);
            window.render(rubikCube);

            window.swapBuffer();
            window.pollEvents();
        }
    }
}