package org.ctu;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Model implements Renderable {
    private final float step = 0.1f;
    protected Matrix4f model = new Matrix4f();

    protected Vector3f position = new Vector3f();

    protected Quaternionf rotation = new Quaternionf();

    @Override
    public Matrix4f getModel() {
        return model;
    }

    public void updateModel() {
        float scale = 1;
        model.translationRotateScale(position, rotation, scale);
    }

    public void moveUp() {
        position.y += step;
        updateModel();
    }

    public void moveDown() {
        position.y -= step;
        updateModel();
    }

    public void moveLeft() {
        position.x -= step;
        updateModel();
    }

    public void moveRight() {
        position.x += step;
        updateModel();
    }
}
