package org.ctu;

public interface Renderable {
    default public void render(Window window) {}
    default public void cleanup() {}
}
