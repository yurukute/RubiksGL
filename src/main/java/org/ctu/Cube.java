package org.ctu;

import org.joml.Vector3f;

public class Cube extends Model {
    private final float[] vertices = {
            -0.5f,  0.5f, 0, // Top left
             0.5f,  0.5f, 0, // Top right
             0.5f, -0.5f, 0, // Bottom right
            -0.5f, -0.5f, 0, // Bottom left

    };
    private final int[] indices =  { 0, 1, 2, 0, 2, 3 };
    private static final float HALF_PI = (float) Math.PI / 2;
    private Vector3f center;
    private float size;
    private Mesh[] faces;

    private static final Vector3f[] COLORS = new Vector3f[]{
            new Vector3f(1, 0, 0),      // Red
            new Vector3f(0, 1, 0),      // Green
            new Vector3f(0, 0, 1),      // Blue
            new Vector3f(1, 1, 0),      // Yellow
            new Vector3f(1, 1, 1),      // White
            new Vector3f(1, 0.25f, 0),  // Orange
    };

    public Cube(Vector3f center, float size) {
        this.center = center;
        this.size = size;
        generateFaces(size);
    }

    public Vector3f getCenter() {
        return center;
    }

    private void generateFaces(float size) {
        cleanup();
        Mesh[] meshes = new Mesh[6];

        for (int i = 0; i < 6; i++) {
            float[] colors = new float[4 * 3]; // rgb value for 4 points
            for (int j = 0; j < 4 * 3; j += 3) {
                colors[j]     = COLORS[i].x;
                colors[j + 1] = COLORS[i].y;
                colors[j + 2] = COLORS[i].z;
            }
            meshes[i] = new Mesh(vertices, colors, indices);
            meshes[i].setScale(new Vector3f(size));
        }
        faces = meshes;
        updateFaces();
    }

    private void updateFaces() {
        Vector3f dz = new Vector3f(0, 0, size/2);
        Vector3f dy = new Vector3f(0, size/2, 0);
        Vector3f dx = new Vector3f(size/2, 0, 0);

        faces[0].setPosition(new Vector3f(center).add(dz));
        faces[5].setPosition(new Vector3f(center).sub(dz));
        faces[1].setPosition(new Vector3f(center).add(dx));
        faces[2].setPosition(new Vector3f(center).sub(dx));
        faces[3].setPosition(new Vector3f(center).add(dy));
        faces[4].setPosition(new Vector3f(center).sub(dy));

        faces[1].getRotation().rotateY(HALF_PI);
        faces[2].getRotation().rotateY(HALF_PI);
        faces[3].getRotation().rotateX(HALF_PI);
        faces[4].getRotation().rotateX(HALF_PI);
    }

    @Override
    public void update(long time) {
        updateModel();
        for (Mesh face : faces) {
            face.setParent(getModel());
            face.update(time);
        }
    }

    @Override
    public void render(Window win) {
        for (Mesh face : faces) {
            win.render(face);
        }
    }

    @Override
    public void cleanup() {
        if (faces == null) return;
        for (Mesh face : faces) {
            if (face != null) {
                face.cleanup();
            }
        }
    }
}
