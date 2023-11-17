package org.ctu;

import org.joml.Matrix4f;

public interface Renderable {
    default public Matrix4f getModel() { return new Matrix4f(); };
    default public void render(Window window) {}
    default public void cleanup() {}
}
