package org.ctu;

import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh implements Renderable {
    private int vaoID; // vertex array object
    private List<Integer> vboIDList; // vertex buffer object
    private int vertexCount;

    public Mesh(float[] positions, float[] colors, float[] indices) {
        vertexCount = indices.length;
        vboIDList = new ArrayList<>();

        // Create and bind vertex array object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create and add vertex buffer object to list
        addBufferObj(positions, GL_ARRAY_BUFFER, true);
        addBufferObj(colors, GL_ARRAY_BUFFER, true);
        addBufferObj(indices, GL_ELEMENT_ARRAY_BUFFER, false);

        // Unbind
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    private void addBufferObj(float[] vertices, int target, boolean attribEnabled) {
        int vboID = glGenBuffers();
        vboIDList.add(vboID);
        glBindBuffer(target, vboID);
        glBufferData(target, vertices, GL_STATIC_DRAW);
        if (attribEnabled) {
            int idx = vboIDList.size() - 1;
            glEnableVertexAttribArray(idx);
            glVertexAttribPointer(idx, 3, GL_FLOAT, false, 0, 0);
        }
    }

    public void render(Window window) {
        glBindVertexArray(vaoID);
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        glBindVertexArray(0);
    }

    public void cleanup() {
        vboIDList.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vaoID);
    }
}