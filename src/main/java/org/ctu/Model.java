package org.ctu;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Model implements Renderable {
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
}
