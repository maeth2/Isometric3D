package components;

import org.lwjgl.glfw.GLFW;

import listeners.KeyListener;

public class Controller extends Component {
	private float speed = 1f;
	
	public void updateController(float dt) {
		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_A)) {
			gameObject.transform.position.x -= speed * dt;
		}else if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_D)) {
			gameObject.transform.position.x += speed * dt;
		}
		
		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_S)) {
			gameObject.transform.position.z += speed * dt;
		}else if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_W)) {
			gameObject.transform.position.z -= speed * dt;
		}
	}
	
	@Override
	public void update(float dt) {}

}
