package org.ctu.model;

import org.ctu.opengl.Model;
import org.ctu.opengl.Window;
import org.ctu.utils.Pair;
import org.joml.*;

import java.lang.Math;
import java.util.LinkedList;
import java.util.Queue;

public class RubiksCube extends Model {
    private static final float EPS = 0.00001f;
    private static final float HALF_PI = (float) Math.PI / 2;
    private static final float ROTATE_TIME = 4;
    private final Cube[] pieces;
    private final Matrix4f[] pieceRotMat; // Rotation matrix
    private float currentRotateRad = 0;
    private Pair<Vector3f, Integer> currentRotate = null;
    private final Queue<Pair<Vector3f, Integer>> rotateQueue;

    public RubiksCube(float size, float pad) {
        pieces = new Cube[27]; // 3^3
        pieceRotMat = new Matrix4f[27];
        rotateQueue = new LinkedList<>();

        float pieceSize = (size - 2 * pad) / 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    int idx = i * 3 * 3 + j * 3 + k;
                    pieces[idx] = new Cube(
                            new Vector3f(i - 1, j - 1, k - 1).mul(pieceSize + pad),
                            pieceSize);
                    pieceRotMat[idx] = new Matrix4f();
                }
            }
        }
        getPosition().z = -2;
    }

    public void rotateFrontFace(boolean clockwise) {
        rotateFace(new Vector3f(0, 0, 1), clockwise ? 1 : -1);
    }

    public void rotateBackFace(boolean clockwise) {
        rotateFace(new Vector3f(0, 0, -1), clockwise ? 1 : -1);
    }

    public void rotateUpFace(boolean clockwise) {
        rotateFace(new Vector3f(0, 1, 0), clockwise ? 1 : -1);
    }

    public void rotateDownFace(boolean clockwise) {
        rotateFace(new Vector3f(0, -1, 0), clockwise ? 1 : -1);
    }

    public void rotateLeftFace(boolean clockwise) {
        rotateFace(new Vector3f(-1, 0, 0), clockwise ? 1 : -1);
    }

    public void rotateRightFace(boolean clockwise) {
        rotateFace(new Vector3f(1, 0, 0), clockwise ? 1 : -1);
    }

    protected void rotateFace(Vector3f face, int inv) {
        rotateQueue.add(new Pair<>(face, inv));
    }

    private boolean belongsToFace(int idx, Vector3f face) {
        Matrix4f rot_mat = pieceRotMat[idx];
        Vector3f center = new Vector3f(pieces[idx].getCenter());

        Vector4f c4 = new Vector4f(center, 1);
        rot_mat.transform(c4);

        center.set(c4.x, c4.y, c4.z);

        if (face.z > EPS) {
            return center.z > EPS;
        } else if (face.z < -EPS) {
            return center.z < -EPS;
        } else if (face.x > EPS) {
            return center.x > EPS;
        } else if (face.x < -EPS) {
            return center.x < -EPS;
        } else if (face.y > EPS) {
            return center.y > EPS;
        } else if (face.y < -EPS) {
            return center.y < -EPS;
        }

        return false;
    }

    private void rotatePiece(float rad, int idx, Vector3f face) {
        Matrix4f rot_mat = new Matrix4f();

        if (face.z > EPS) {             // Font face
            rot_mat.rotateZ(-rad);
        } else if (face.z < -EPS) {     // Back face
            rot_mat.rotateZ(rad);
        } else if (face.x > EPS) {      // Right face
            rot_mat.rotateX(-rad);
        } else if (face.x < -EPS) {     // Left face
            rot_mat.rotateX(rad);
        } else if (face.y > EPS) {      // Top face
            rot_mat.rotateY(rad);
        } else if (face.y < -EPS) {     // Bottom face
            rot_mat.rotateY(-rad);
        }

        pieceRotMat[idx] = rot_mat.mul(pieceRotMat[idx]);
    }

    private void rotateFromQueue(long time) {

        if (currentRotate == null) {
            if (rotateQueue.isEmpty()) {
                return;
            }
            currentRotate = rotateQueue.poll();
            currentRotateRad = 0;
        } else {
            float rad = time / 1000f * ROTATE_TIME * currentRotate.getValue();
            Vector3f face = currentRotate.getKey();

            if (currentRotateRad + rad >= HALF_PI) {
                rad = HALF_PI - currentRotateRad;
                currentRotate = null;
            } else if (currentRotateRad + rad <= -HALF_PI) {
                rad = -HALF_PI - currentRotateRad;
                currentRotate = null;
            }
            currentRotateRad += rad;

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    for (int k = 0; k < 3; ++k) {
                        int cube_idx = i * 3 * 3 + j * 3 + k;
                        if (belongsToFace(cube_idx, face)) {
                            rotatePiece(rad, cube_idx, face);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void update(long time) {
        updateModel();
        rotateFromQueue(time);

        Matrix4f model = getModel();

        for (int i = 0; i < pieces.length; i++) {
            pieces[i].setParent(new Matrix4f(model).mul(pieceRotMat[i]));
            pieces[i].update(time);
        }
    }

    @Override
    public void render(Window win) {
        for (Cube piece : pieces) {
            win.render(piece);
        }
    }

    @Override
    public void cleanup() {
        for (Cube piece : pieces) {
            piece.cleanup();
        }
    }
}
