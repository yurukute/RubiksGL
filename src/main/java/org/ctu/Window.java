package org.ctu;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private long window;
    private Projection projection;
    private Shader shader = null;
    private UniformsMap uniforms;


    public Window(int width, int height, String name) {
        GLFWErrorCallback.createPrint(System.err);

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Optionals
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(width, height, name, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);

        display();

        GL.createCapabilities();
        glClearColor(0, 0, 0, 0);

        projection = new Projection(width, height);
        glfwSetFramebufferSizeCallback(window, (win, w, h) -> resize(w, h));

        String shader_path = "src/main/resources/shader/";
        shader = new Shader(shader_path + "vertex.vs", shader_path + "fragment.fs");

        uniforms = new UniformsMap(shader.getProgramId());
        uniforms.createUniform("projection");
        uniforms.createUniform("model");
    }

    private void resize(int width, int height) {
        projection.updateProjMatrix(width, height);
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public void onKey(GLFWKeyCallbackI callback) {
        glfwSetKeyCallback(window, callback);
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    public void close() {
        glfwSetWindowShouldClose(window, true);
    }

    public boolean isClosed() {
        return glfwWindowShouldClose(window);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void swapBuffer() {
        glfwSwapBuffers(window);
    }

    public void display() {
        glfwShowWindow(window);
    }

    public void render(Renderable renderer) {
        shader.bind();
        uniforms.setUniform("projection", projection.getProjMatrix());
        uniforms.setUniform("model", renderer.getModel());
        renderer.render(this);
        shader.unbind();
    }

    public void destroy() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    }
}