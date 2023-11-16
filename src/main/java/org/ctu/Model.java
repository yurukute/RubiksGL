package org.ctu;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Model {
    protected Matrix4f model = new Matrix4f();

    public Vector3f position = new Vector3f();

    public Quaternionf rotation = new Quaternionf();

    public void updateModel() {
        float scale = 1;
        model.translationRotateScale(position, rotation, scale);
    }
}
