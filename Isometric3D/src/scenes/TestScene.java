package scenes;

import org.joml.Vector3f;
import components.ModelTexture;
import components.TexturedModelRenderer;
import main.Camera;
import main.GameObject;
import main.Transform;
import util.OBJLoader;
import util.TextureLoader;

public class TestScene extends Scene{
	GameObject test;
	
	public TestScene() {
		init();
	}
	
	@Override
	public void init() {
		camera = new Camera(new Vector3f(0, 0, 2));
		this.addGameObjectToScene(camera);
		
		GameObject dragon = new GameObject(
			"dragon",
			new Transform(0.1f)
		);
		dragon.addComponent(
			new TexturedModelRenderer(
				OBJLoader.loadOBJModel("assets/models/dragon.obj"),
				new ModelTexture(TextureLoader.loadTexture("assets/textures/blank.png"))
			)
		);
		this.addGameObjectToScene(dragon);
	}

	@Override
	public void update(float dt) {
		for(GameObject g : gameObjects) {
			g.update(dt);
		}
		this.renderer.render();
	}
}
