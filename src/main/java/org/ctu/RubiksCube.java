package org.ctu;

import org.joml.*;

public class RubiksCube extends Model {
    private final Cube[] pieces;
    public RubiksCube(float size, float pad) {
        pieces = new Cube[27]; // 9^3
        float pieceSize = (size - 2 * pad) / 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    int idx = i * 9 + j * 3 + k;
                    pieces[idx] = new Cube(
                            new Vector3f(i - 1, j - 1, k - 1).mul(pieceSize + pad),
                            pieceSize);
                }
            }
        }
        getPosition().z = -2;
    }

    @Override
    public void update(long time) {
        updateModel();
        Matrix4f model = getModel();
        for (Cube piece : pieces) {
            piece.setParent(model);
            piece.update(time);
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
