package main;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;

import listeners.KeyListener;
import listeners.MouseListener;
import scenes.Scene;
import scenes.TestScene;
import util.Helper;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;

public class Window {
	private static Window instance;
	private String name;
	public static int WIDTH, HEIGHT;
	private long glfwWindow;
	
	public float r, g, b, c;
	
	private static Scene currentScene;
	
	public Window() {
		this.name = "OTHS";
		WIDTH = 1920;
		HEIGHT = 1000;
		r = 1;
		g = 1;
		b = 1;
		c = 1;
	}
	
	public static void changeScene(int newScene) {
		switch(newScene) {
			case 0:
				currentScene = new TestScene();
				currentScene.start();
				break;
			default:
				assert false : "Unknown scene" + newScene;
				break;
		}
	}
	
	public static Window get() {
		if(Window.instance == null) {
			Window.instance = new Window();
		}
		return instance;
	}
	
	public void run() {
		System.out.println("LWJGL VERSION: " + Version.getVersion());
		
		init();
		loop();
		
		//Free memory
		glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);
		
		//Terminate GLFW
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public void init() {
		System.out.println("CREATING WINDOW...");

		// Setting up error callback
		GLFWErrorCallback.createPrint(System.err).set();
		
		// Initialize GLFW
		if(!glfwInit()) {
			throw new IllegalStateException("Failed to initialize GLFW");
		}
		
		// Configure GLFW
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
		
		// Create the window
		glfwWindow = glfwCreateWindow(WIDTH, HEIGHT, this.name, 0, 0);
		
		if(glfwWindow == 0) {
			throw new IllegalStateException("Failed to initialize window");
		}
		
		//Sets Callbacks
		glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
		glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
		glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
		glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

		//Make the OpenGL context current
		glfwMakeContextCurrent(glfwWindow);
		
		//Enable v-sync
		glfwSwapInterval(1);
		
		//Make the window visible
		glfwShowWindow(glfwWindow);
				
		//Create Capabilities
		createCapabilities();
		
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_DEPTH_TEST);
		
		System.out.println("WINDOW CREATED!");
		
		Window.changeScene(0);
	}
	
	public void loop() {
		float beginTime = (float)glfwGetTime();
		float endTime = (float)glfwGetTime();
		float dt = -1;
		
		while(!glfwWindowShouldClose(glfwWindow)) {
			// Poll events
			glfwPollEvents();
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			if(dt >= 0) {
				currentScene.update(dt);
			}
			
			glfwSwapBuffers(glfwWindow);

			if(KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
				System.out.println("FPS: " + 1/dt);
			}
			
			endTime = (float)glfwGetTime();
			dt = endTime - beginTime;
			beginTime = endTime;
		}
		
		Helper.dispose();
	}
	
	public static Scene getScene() {
		return Window.currentScene;
	}
}
