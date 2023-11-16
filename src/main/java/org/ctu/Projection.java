package org.ctu;

import org.joml.Matrix4f;

public class Projection {
    private static final float fov = (float) Math.toRadians(60.0f);
    private static final float z_far = 1000.f;
    private static final float z_near = 0.01f;
    private final Matrix4f projMatrix;

    public Projection(int width, int height) {
        projMatrix = new Matrix4f();
        updateProjMatrix(width, height);
    }

    public Matrix4f getProjMatrix() {
        return projMatrix;
    }

    public void updateProjMatrix(int width, int height) {
        projMatrix.setPerspective(fov, (float) width / height, z_near, z_far);
    }
}
