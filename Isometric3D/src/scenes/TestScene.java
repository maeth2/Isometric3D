package scenes;

import org.joml.Vector3f;

import components.Controller;
import components.ModelTexture;
import components.TexturedModelRenderer;
import main.Camera;
import main.GameObject;
import util.OBJLoader;
import util.TextureLoader;

public class TestScene extends Scene{
	
	int costume = 0;
	float spriteFrameTime = 0.2f;
	float spriteFrameTimeLeft = 0.0f;
	
	GameObject test;
	
	public TestScene() {
		init();
	}
	
	@Override
	public void init() {
		camera = new Camera(new Vector3f(0, 0, 2));
		
		GameObject cube = new GameObject("cube");
		test = cube;
		cube.addComponent(
			new TexturedModelRenderer(
				OBJLoader.loadOBJModel("assets/models/cube.obj"),
				new ModelTexture(TextureLoader.loadTexture("assets/textures/blank.png"))
			)
		);
		cube.addComponent(new Controller());
		this.addGameObjectToScene(camera);
		this.addGameObjectToScene(cube);
	}

	@Override
	public void update(float dt) {
		for(GameObject g : gameObjects) {
			g.update(dt);
		}
		test.transform.rotation.x += 0.5;
		test.transform.rotation.y += 0.5;
		if(test.transform.rotation.x > 360) {
			test.transform.rotation.x = 0;
		}
		if(test.transform.rotation.y > 360) {
			test.transform.rotation.y = 0;
		}
		this.renderer.render();
	}
}
