package renderer;

import static org.lwjgl.opengl.GL30.*;



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
import util.Quad;
import util.ShaderLoader;
import util.TextureLoader;

public class Renderer {
	private final int FRAME_WIDTH = (int)(Window.WIDTH);
	private final int FRAME_HEIGHT = (int)(Window.HEIGHT);
	private final int PIXEL_WIDTH = FRAME_WIDTH / 5;
	private final int PIXEL_HEIGHT = FRAME_HEIGHT / 5;
	private List<RenderBatch> batches;
	private Scene scene;
	
	private int sceneShaderID;
	private int outlineShaderID;
	private int linearShaderID;
	
	private int sceneBuffer;
	private int outlineBuffer;
	private int linearBuffer;
	private int pixelBuffer;
	
	private int colorTexture;
	private int normalTexture;
	private int depthTexture;
	private int outlineTexture;
	private int linearTexture;
	private int pixelTexture;

	/**
	 * Initializer
	 */
	public Renderer(Scene scene) {
		this.scene = scene;
		this.batches 				= new ArrayList<>();
		this.sceneShaderID 			= AssetManager.getShader("assets/shaders/default");
		this.outlineShaderID 		= AssetManager.getShader("assets/shaders/outline");
		this.linearShaderID 		= AssetManager.getShader("assets/shaders/linear");
		this.sceneBuffer 			= BufferHelper.createFrameBuffer(FRAME_WIDTH, FRAME_HEIGHT, 2);
		this.outlineBuffer 			= BufferHelper.createFrameBuffer(FRAME_WIDTH, FRAME_HEIGHT, 1);
		this.linearBuffer 			= BufferHelper.createFrameBuffer(FRAME_WIDTH, FRAME_HEIGHT, 1);
		this.pixelBuffer 			= BufferHelper.createFrameBuffer(FRAME_WIDTH, FRAME_HEIGHT, 1);
		this.colorTexture 			= AssetManager.generateTexture("color", sceneBuffer, FRAME_WIDTH, FRAME_HEIGHT, GL_COLOR_ATTACHMENT0);
		this.normalTexture 			= AssetManager.generateTexture("normal", sceneBuffer, FRAME_WIDTH, FRAME_HEIGHT, GL_COLOR_ATTACHMENT1);
		this.depthTexture 			= AssetManager.generateDepthTexture("depth", sceneBuffer, FRAME_WIDTH, FRAME_HEIGHT);
		this.outlineTexture 		= AssetManager.generateTexture("outline", outlineBuffer, FRAME_WIDTH, FRAME_HEIGHT, GL_COLOR_ATTACHMENT0);
		this.linearTexture 			= AssetManager.generateTexture("linear", linearBuffer, FRAME_WIDTH, FRAME_HEIGHT, GL_COLOR_ATTACHMENT0);
		this.pixelTexture 			= AssetManager.generateTexture("pixel", pixelBuffer, PIXEL_WIDTH, PIXEL_HEIGHT, GL_COLOR_ATTACHMENT0);
		
		this.scene.addGameObjectToScene(
			new GameObject("test").addComponent(
				new GUIComponent(pixelTexture)
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
			RenderBatch newBatch = new RenderBatch(gameObject.getModel());
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
	
	//TODO Modulize all these frame buffer code
	public void render() {
		/*
		 * Rendering Frame texture
		 */
		BufferHelper.bindFrameBuffer(
			sceneBuffer, 
			(int)TextureLoader.getDimensions(colorTexture).x, 
			(int)TextureLoader.getDimensions(colorTexture).y
		);		
		prepare();
		ShaderLoader.useShader(sceneShaderID);
		ShaderLoader.loadMatrix(sceneShaderID, "uProjection", Window.getScene().getCamera().getProjectionMatrix());
		ShaderLoader.loadMatrix(sceneShaderID, "uView", Window.getScene().getCamera().getViewMatrix());		
		ShaderLoader.loadLights(sceneShaderID, "uLight", scene.getSceneLights());
		ShaderLoader.loadInt(sceneShaderID, "uNumOfLights", scene.getSceneLights().size());
		ShaderLoader.loadVector3f(sceneShaderID, "uAmbient", new Vector3f(0.2f, 0.2f, 0.2f));
		ShaderLoader.loadFloat(sceneShaderID, "uShineDamper", 20.0f);
		for(RenderBatch b : batches) {
			b.render(sceneShaderID);
		}
		ShaderLoader.unbindShader();
		BufferHelper.unbindFrameBuffer();

		/*
		 * Render linear texture
		 */
		BufferHelper.bindFrameBuffer(
			linearBuffer, 
			(int)TextureLoader.getDimensions(linearTexture).x, 
			(int)TextureLoader.getDimensions(linearTexture).y
		);
		prepare();
		ShaderLoader.useShader(linearShaderID);
		Quad.renderQuad(linearShaderID, depthTexture);
		ShaderLoader.unbindShader();
		BufferHelper.unbindFrameBuffer();
		
		/*
		 * Render outline texture
		 */
		BufferHelper.bindFrameBuffer(
			outlineBuffer, 
			(int)TextureLoader.getDimensions(outlineTexture).x, 
			(int)TextureLoader.getDimensions(outlineTexture).y
		);
		prepare();
		int textures[] = {linearTexture, colorTexture, normalTexture};
		ShaderLoader.useShader(outlineShaderID);
		TextureLoader.bindTextureToShader(outlineShaderID, "uDepth", 0);
		TextureLoader.bindTextureToShader(outlineShaderID, "uColor", 1);
		TextureLoader.bindTextureToShader(outlineShaderID, "uNormal", 2);
		Quad.renderQuad(outlineShaderID, textures);
		ShaderLoader.unbindShader();
		BufferHelper.unbindFrameBuffer();
		
		/*
		 * Render pixelized texture
		 */
		BufferHelper.bindFrameBuffer(
			pixelBuffer, 
			(int)TextureLoader.getDimensions(pixelTexture).x, 
			(int)TextureLoader.getDimensions(pixelTexture).y
		);
		prepare();
		Quad.renderQuad(outlineTexture);
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
