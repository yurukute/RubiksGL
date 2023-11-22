package org.ctu;

import org.joml.*;

public class Model implements Renderable {
    private float step = 0.1f;
    private Vector3f scale = new Vector3f(1, 1, 1);
    private Matrix4f model = new Matrix4f();
    private Matrix4f parent = new Matrix4f();
    protected Vector3f position = new Vector3f();
    protected Quaternionf rotation = new Quaternionf();

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public void setRotation(Quaternionf rotation) {
        this.rotation = rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public void setParent(Matrix4f parent) {
        this.parent = parent;
    }

    @Override
    public Matrix4f getModel() {
        return model;
    }

    public void updateModel() {
        model = new Matrix4f(parent).mul(
                model.translationRotateScale(position, rotation, scale));
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

    public void rotate(float dx, float dy) {
        Matrix4f afterRotate = new Matrix4f();
        AxisAngle4f newAngle = new AxisAngle4f();

        afterRotate.rotateX(dy);
        afterRotate.rotateY(dx);
        afterRotate.getRotation(newAngle);

        rotation = new Quaternionf(newAngle).mul(getRotation());
    }
}
