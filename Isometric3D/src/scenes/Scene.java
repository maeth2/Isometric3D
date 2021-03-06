package scenes;

import java.util.ArrayList;
import java.util.List;

import components.GUIComponent;
import components.LightComponent;
import main.Camera;
import main.GameObject;
import renderer.Renderer;

public abstract class Scene {	
	public static int MAX_SCENE_LIGHTS = 10;
	protected Camera camera;
	private boolean isRunning = false;
	protected List<LightComponent> lightObjects = new ArrayList<LightComponent>();
	protected List<GameObject> gameObjects = new ArrayList<GameObject>();
	protected List<GUIComponent> guis = new ArrayList<GUIComponent>();
	protected Renderer renderer = new Renderer(this);
	
	public Scene() {}
	
	/**
	 * Initializes the scene
	 */
	public void init() {}
	
	/**
	 * Updates the scene
	 * 
	 * @param dt		Time passed per frame
	 */
	public abstract void update(float dt);
	
	/**
	 * Adds a game object to the scene
	 * 
	 * @param g			Game object to add
	 */
	public void addGameObjectToScene(GameObject g) {
		gameObjects.add(g);
		if(g.getComponent(LightComponent.class) != null && lightObjects.size() <= Scene.MAX_SCENE_LIGHTS) {
			lightObjects.add(g.getComponent(LightComponent.class));
		}
		if(g.getComponent(GUIComponent.class) != null) {
			guis.add(g.getComponent(GUIComponent.class));
		}
		if(isRunning) {
			g.start();
			renderer.add(g);
		}
	}
	
	/**
	 * Start function
	 */
	public void start() {
		for(GameObject g : gameObjects) {
			g.start();
			renderer.add(g);
		}
		isRunning = true;
	}
	
	/**
	 * Get scene camera
	 * 
	 * @return			Scene camera
	 */
	public Camera getCamera() {
		return this.camera;
	}
	
	/**
	 * Get scene lights
	 * 
	 * @return
	 */
	public List<LightComponent> getSceneLights(){
		return this.lightObjects;
	}
	
	/**
	 * Get GUIs
	 * 
	 * @return
	 */
	public List<GUIComponent> getGUIs(){
		return this.guis;
	}
}
