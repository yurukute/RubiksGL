package org.ctu.opengl;

import org.joml.Matrix4f;

public interface Renderable {
    default Matrix4f getModel() { return new Matrix4f(); };
    default void update(long time) {}
    default void render(Window window) {}
    default void cleanup() {}
}
