package scenes;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import components.LightComponent;
import components.Material;
import components.TexturedModelRendererComponent;
import listeners.KeyListener;
import main.Camera;
import main.GameObject;
import main.Transform;
import util.AssetManager;

public class TestScene extends Scene{
	Material m;
	float coolDown = 0.5f;
	
	public TestScene() {
		init();
	}
	
	@Override
	public void init() {
		camera = new Camera(new Vector3f(0, 0, 2));
		this.addGameObjectToScene(camera);
		
		m = new Material(AssetManager.getTexture("assets/textures/blank.png"), 2f);
		for(int i = 0; i < 1; i++) {
			GameObject dragon = new GameObject(
				"dragon",
				new Transform(
						//new Vector3f((float)(Math.random() * 10.0f), (float)(Math.random() * 10.0f), (float)(Math.random() * 10.0f)),
						//new Vector3f((float)(Math.random() * 360.0f), (float)(Math.random() * 360.0f), (float)(Math.random() * 360.0f)),
						1f
				)
			);
			dragon.addComponent(
				new TexturedModelRendererComponent(
					AssetManager.getModel("assets/models/dragon.obj"),
					m
				)
			);
			this.addGameObjectToScene(dragon);
		}
	}

	@Override
	public void update(float dt) {
		for(GameObject g : gameObjects) {
			g.update(dt);
		}
		
		if(coolDown > 0) {
			coolDown -= dt;
		}else if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_R)) {
			coolDown = 0.5f;
			GameObject light = new GameObject(
				"light", 
				new Transform(
					new Vector3f(
						camera.transform.position.x, 
						camera.transform.position.y, 
						camera.transform.position.z
					),
					0.5f
				)
			);
			light.addComponent(
				new LightComponent(
					LightComponent.TYPE_LIGHT_POINT, 
					1f, 
					new Vector3f((float)Math.random(), (float)Math.random(), (float)Math.random()), 
					new Vector3f(1, 0.1f, 0.01f)
				)
			);
			light.addComponent(
				new TexturedModelRendererComponent(
					AssetManager.getModel("assets/models/cube.obj"),
					new Material(0)
				)
			);
			this.addGameObjectToScene(light);
		}

		this.renderer.render();
	}
}
