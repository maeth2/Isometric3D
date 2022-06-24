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
import util.BufferHelper;
import util.ShaderLoader;
import util.TextureLoader;

public class Renderer {
	private final int FRAME_WIDTH = (int)(Window.WIDTH / 5);
	private final int FRAME_HEIGHT = (int)(Window.HEIGHT / 5);
	private final int DEPTH_WIDTH = (int)(Window.WIDTH / 4);
	private final int DEPTH_HEIGHT = (int)(Window.HEIGHT / 4);
	private List<RenderBatch> batches;
	private Scene scene;
	
	private int shaderID;
	private int depthShaderID;
	private int utilShaderID;
	private int frameBuffer;
	private int depthBuffer;
	private int linearDepthBuffer;
	private int frameTexture;
	private int depthTexture;
	private int linearDepthTexture;

	/**
	 * Initializer
	 */
	public Renderer(Scene scene) {
		this.scene = scene;
		this.batches 			= new ArrayList<>();
		this.shaderID 			= AssetManager.getShader("assets/shaders/default");
		this.depthShaderID 		= AssetManager.getShader("assets/shaders/depth");
		this.utilShaderID 		= AssetManager.getShader("assets/shaders/util");
		this.frameBuffer 		= BufferHelper.createFrameBuffer(FRAME_WIDTH, FRAME_HEIGHT);
		this.depthBuffer 		= BufferHelper.createFrameBuffer(DEPTH_WIDTH, DEPTH_HEIGHT);
		this.linearDepthBuffer 	= BufferHelper.createFrameBuffer(DEPTH_WIDTH, DEPTH_HEIGHT);
		this.frameTexture 		= AssetManager.generateTexture("buffer", FRAME_WIDTH, FRAME_HEIGHT, frameBuffer);
		this.depthTexture 		= AssetManager.generateDepthTexture("depth", DEPTH_WIDTH, DEPTH_HEIGHT, depthBuffer);
		this.linearDepthTexture = AssetManager.generateTexture("linear depth", DEPTH_WIDTH, DEPTH_HEIGHT, linearDepthBuffer);
		
		this.scene.addGameObjectToScene(
			new GameObject("test").addComponent(
				new GUIComponent(frameTexture)
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
		BufferHelper.bindFrameBuffer(
			depthBuffer, 
			(int)TextureLoader.getDimensions(depthTexture).x, 
			(int)TextureLoader.getDimensions(depthTexture).y
		);
		prepare();
		ShaderLoader.useShader(depthShaderID);
		ShaderLoader.loadMatrix(depthShaderID, "uProjection", Window.getScene().getCamera().getProjectionMatrix());
		ShaderLoader.loadMatrix(depthShaderID, "uView", Window.getScene().getCamera().getViewMatrix());		
		for(RenderBatch b : batches) {
			b.render(depthShaderID);
		}
		ShaderLoader.unbindShader();
		BufferHelper.unbindFrameBuffer();

		/*
		 * Render linear depth texture
		 */
		BufferHelper.bindFrameBuffer(
			linearDepthBuffer, 
			(int)TextureLoader.getDimensions(linearDepthTexture).x, 
			(int)TextureLoader.getDimensions(linearDepthTexture).y
		);
		prepare();
		ShaderLoader.useShader(utilShaderID);
		ShaderLoader.loadInt(utilShaderID, "uType", 1);
		GUIRenderer.get().renderQuad(utilShaderID, depthTexture);
		ShaderLoader.unbindShader();
		BufferHelper.unbindFrameBuffer();
		
		/*
		 * Rendering Frame texture
		 */
		BufferHelper.bindFrameBuffer(
			frameBuffer, 
			(int)TextureLoader.getDimensions(frameTexture).x, 
			(int)TextureLoader.getDimensions(frameTexture).y
		);		
		prepare();
		ShaderLoader.useShader(shaderID);
		ShaderLoader.loadMatrix(shaderID, "uProjection", Window.getScene().getCamera().getProjectionMatrix());
		ShaderLoader.loadMatrix(shaderID, "uView", Window.getScene().getCamera().getViewMatrix());		
		ShaderLoader.loadLights(shaderID, "uLight", scene.getSceneLights());
		ShaderLoader.loadInt(shaderID, "uNumOfLights", scene.getSceneLights().size());
		ShaderLoader.loadVector3f(shaderID, "uAmbient", new Vector3f(0.2f, 0.2f, 0.2f));
		ShaderLoader.loadFloat(shaderID, "uShineDamper", 20.0f);
		for(RenderBatch b : batches) {
			b.render(shaderID);
		}
		ShaderLoader.unbindShader();
		BufferHelper.unbindFrameBuffer();
		
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
