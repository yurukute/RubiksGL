package org.ctu;

import org.ctu.Mesh;
import org.ctu.Shader;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class RubiksGL {
    private long window;
    private Mesh cubeMesh;
    private Shader shader;

    public void run() {
        init();
        loop();

        cubeMesh.cleanup();
        shader.cleanup();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Set up an error callback.
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        window = glfwCreateWindow(800, 600, "Rubik's OpenGL", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback (pressed, hold, released).
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true);
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            // Get the window size
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the monitor's resolution
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // V-sync
        glfwShowWindow(window);

        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Define the vertices of the cube
        float[] vertices = {
                // Front face
                -0.5f,  0.5f, 0.5f,  // Top left
                -0.5f, -0.5f, 0.5f,  // Bottom left
                 0.5f, -0.5f, 0.5f,  // Bottom right
                 0.5f,  0.5f, 0.5f,  // Top right

                // Back face
                -0.5f,  0.5f, -0.5f,  // Top left
                -0.5f, -0.5f, -0.5f,  // Bottom left
                 0.5f, -0.5f, -0.5f,  // Bottom right
                 0.5f,  0.5f, -0.5f,  // Top right

                // Left face
                -0.5f,  0.5f, -0.5f,  // Top left
                -0.5f, -0.5f, -0.5f,  // Bottom left
                -0.5f, -0.5f,  0.5f,  // Bottom right
                -0.5f,  0.5f,  0.5f,  // Top right

                // Right face
                0.5f,  0.5f, -0.5f,  // Top left
                0.5f, -0.5f, -0.5f,  // Bottom left
                0.5f, -0.5f,  0.5f,  // Bottom right
                0.5f,  0.5f,  0.5f,  // Top right

                // Top face
                -0.5f, 0.5f, -0.5f,  // Top left
                -0.5f, 0.5f,  0.5f,  // Bottom left
                 0.5f, 0.5f,  0.5f,  // Bottom right
                 0.5f, 0.5f, -0.5f,  // Top right

                // Bottom face
                -0.5f, -0.5f, -0.5f, // Top left
                -0.5f, -0.5f,  0.5f, // Bottom left
                 0.5f, -0.5f,  0.5f, // Bottom right
                 0.5f, -0.5f, -0.5f  // Top right
        };
        cubeMesh = new Mesh(vertices);
        shader = new Shader("src/main/resources/vertex.vs", "src/main/resources/fragment.fs");
    }

    private void loop() {

        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            shader.bind();
            cubeMesh.render();
            shader.unbind();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new RubiksGL().run();
    }
}
