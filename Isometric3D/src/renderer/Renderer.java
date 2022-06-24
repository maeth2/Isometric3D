package renderer;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import components.GUIComponent;
import components.TexturedModelRendererComponent;
import main.GameObject;
import main.Window;
import scenes.Scene;
import util.AssetManager;
import util.FBOHelper;
import util.Shaders;

public class Renderer {
	private final int BUFFER_WIDTH = (int)(Window.WIDTH / 4);
	private final int BUFFER_HEIGHT = (int)(Window.HEIGHT / 4);
	private List<RenderBatch> batches;
	private Scene scene;
	
	private int shaderID;
	private int depthShaderID;
	private int utilShaderID;
	private int frameBuffer;
	private int depthBuffer;
	private int linearDepthBuffer;
	private int bufferTexture;
	private int depthTexture;
	private int linearDepthTexture;

	/**
	 * Initializer
	 */
	public Renderer(Scene scene) {
		this.scene = scene;
		this.batches = new ArrayList<>();
		this.shaderID = AssetManager.getShader("assets/shaders/default");
		this.depthShaderID = AssetManager.getShader("assets/shaders/depth");
		this.utilShaderID = AssetManager.getShader("assets/shaders/util");
		this.frameBuffer = FBOHelper.createFrameBuffer(BUFFER_WIDTH, BUFFER_HEIGHT);
		this.depthBuffer = FBOHelper.createFrameBuffer(BUFFER_WIDTH, BUFFER_HEIGHT);
		this.linearDepthBuffer = FBOHelper.createFrameBuffer(BUFFER_WIDTH, BUFFER_HEIGHT);
		this.bufferTexture = AssetManager.generateTexture("buffer", BUFFER_WIDTH, BUFFER_HEIGHT, frameBuffer);
		this.depthTexture = AssetManager.generateDepthTexture("depth", BUFFER_WIDTH, BUFFER_HEIGHT, depthBuffer);
		this.linearDepthTexture = AssetManager.generateTexture("linear depth", BUFFER_WIDTH, BUFFER_HEIGHT, linearDepthBuffer);
		
		this.scene.addGameObjectToScene(
			new GameObject("test").addComponent(
				new GUIComponent(bufferTexture)
			)
		);

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
	
	public void prepare() {
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void render() {		
		
		/*
		 * Rendering Depth texture
		 */
		FBOHelper.bindFrameBuffer(depthBuffer, BUFFER_WIDTH, BUFFER_HEIGHT);
		prepare();
		Shaders.useShader(depthShaderID);
		Shaders.loadMatrix(depthShaderID, "uProjection", Window.getScene().getCamera().getProjectionMatrix());
		Shaders.loadMatrix(depthShaderID, "uView", Window.getScene().getCamera().getViewMatrix());		
		for(RenderBatch b : batches) {
			b.render(depthShaderID);
		}
		Shaders.unbindShader();
		FBOHelper.unbindFrameBuffer();

		/*
		 * Render linear depth texture
		 */
		FBOHelper.bindFrameBuffer(linearDepthTexture, BUFFER_WIDTH, BUFFER_HEIGHT);
		prepare();
		Shaders.useShader(utilShaderID);
		Shaders.loadInt(utilShaderID, "uType", 1);
		GUIRenderer.get().renderQuad(utilShaderID, depthTexture);
		Shaders.unbindShader();
		FBOHelper.unbindFrameBuffer();
		
		/*
		 * Rendering Frame texture
		 */
		FBOHelper.bindFrameBuffer(frameBuffer, BUFFER_WIDTH, BUFFER_HEIGHT);
		prepare();
		Shaders.useShader(shaderID);
		Shaders.loadMatrix(shaderID, "uProjection", Window.getScene().getCamera().getProjectionMatrix());
		Shaders.loadMatrix(shaderID, "uView", Window.getScene().getCamera().getViewMatrix());		
		Shaders.loadLights(shaderID, "uLight", scene.getSceneLights());
		Shaders.loadInt(shaderID, "uNumOfLights", scene.getSceneLights().size());
		Shaders.loadVector3f(shaderID, "uAmbient", new Vector3f(0.2f, 0.2f, 0.2f));
		Shaders.loadFloat(shaderID, "uShineDamper", 20.0f);
		for(RenderBatch b : batches) {
			b.render(shaderID);
		}
		Shaders.unbindShader();
		FBOHelper.unbindFrameBuffer();
		
		/*
		 * Rendering GUIs
		 */
		prepare();
		GUIRenderer.get().render(scene.getGUIs());
		
	}
	
	public Scene getScene() {
		return this.scene;
	}
}
