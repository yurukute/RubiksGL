package org.ctu;

import java.io.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int programID;

    public Shader(String vertexShaderPath, String fragmentShaderPath) {
        int vertexShaderID = loadShader(vertexShaderPath, GL_VERTEX_SHADER);
        int fragmentShaderID = loadShader(fragmentShaderPath, GL_FRAGMENT_SHADER);

        programID = glCreateProgram();
        glAttachShader(programID, vertexShaderID);
        glAttachShader(programID, fragmentShaderID);
        glLinkProgram(programID);

        if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("Failed to link shader program:");
            System.err.println(glGetProgramInfoLog(programID));
        }

        glDetachShader(programID, vertexShaderID);
        glDetachShader(programID, fragmentShaderID);
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
    }

    private int loadShader(String path, int shaderType) {
        int shaderID = glCreateShader(shaderType);

        StringBuilder shaderSource = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);

        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile shader:");
            System.err.println(glGetShaderInfoLog(shaderID));
        }

        return shaderID;
    }

    public void bind() {
        glUseProgram(programID);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        glDeleteProgram(programID);
    }

    public int getProgramId() {
        return programID;
    }
}
