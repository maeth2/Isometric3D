package scenes;

import org.joml.Vector3f;

import components.LightComponent;
import components.Material;
import components.TexturedModelRendererComponent;
import main.Camera;
import main.GameObject;
import main.Transform;
import util.AssetManager;

public class TestScene extends Scene{
	GameObject test;
	
	public TestScene() {
		init();
	}
	
	@Override
	public void init() {
		camera = new Camera(new Vector3f(0, 0, 2));
		this.addGameObjectToScene(camera);
		
		GameObject light = new GameObject("light", new Transform(new Vector3f(0, 1, 0)));
		light.addComponent(new LightComponent(1, new Vector3f(1f, 0.9f, 1f)));
		this.addGameObjectToScene(light);
		
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
					new Material(AssetManager.getTexture("assets/textures/blank.png"), 1f)
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
		this.renderer.render();
	}
}
