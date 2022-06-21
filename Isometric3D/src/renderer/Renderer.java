package renderer;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import components.TexturedModelRendererComponent;
import main.GameObject;
import main.Window;
import scenes.Scene;
import util.AssetManager;
import util.Shaders;

public class Renderer {
	private List<RenderBatch> batches; 
	private Scene scene;
	private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};
	private int shaderID;

	/**
	 * Initializer
	 */
	public Renderer(Scene scene) {
		this.scene = scene;
		this.shaderID = AssetManager.getShader("assets/shaders/default");
		this.batches = new ArrayList<>();
	}
	
	/**
	 * Add a Game Object to the rendering list
	 * 
	 * @param gameObject 		GameObject to add
	 */
	public void add(GameObject gameObject) {
		TexturedModelRendererComponent model = gameObject.getComponent(TexturedModelRendererComponent.class);
		if(model != null) {
			add(model);
		}
	}
	
	/**
	 * Add a Game Object to the rendering list
	 * 
	 * @param gameObject 		GameObject to add
	 */
	public void add(TexturedModelRendererComponent gameObject) {
		boolean added = false;
		for(RenderBatch b : batches) {
			if(b.getBatchModel() == gameObject.getModel()) {
				b.add(gameObject);
				added = true;
				break;
			}
		}
		if(!added) {
			RenderBatch newBatch = new RenderBatch(shaderID, gameObject.getModel());
			addBatch(newBatch);
			newBatch.add(gameObject);
		}
	}
	
	/**
	 * Add a Render Batch to render list
	 * 
	 * @param b			RenderBatch to add
	 */
	public void addBatch(RenderBatch b) {
		batches.add(b);
	}
	
	public void render() {
		Shaders.useShader(shaderID);
		Shaders.loadMatrix(shaderID, "uProjection", Window.getScene().getCamera().getProjectionMatrix());
		Shaders.loadMatrix(shaderID, "uView", Window.getScene().getCamera().getViewMatrix());		
		Shaders.loadIntArray(shaderID, "uTextures", texSlots);
		Shaders.loadLights(shaderID, "uLight", scene.getSceneLights());
		Shaders.loadInt(shaderID, "uNumOfLights", scene.getSceneLights().size());
		Shaders.loadVector3f(shaderID, "uAmbient", new Vector3f(0.2f, 0.2f, 0.2f));
		Shaders.loadFloat(shaderID, "uShineDamper", 20.0f);
		
		for(RenderBatch b : batches) {
			b.render();
		}
		
		Shaders.unbindShader();
	}
	
	public Scene getScene() {
		return this.scene;
	}
}
