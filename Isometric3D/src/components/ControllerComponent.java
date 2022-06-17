package components;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import listeners.KeyListener;
import listeners.MouseListener;

public class ControllerComponent extends Component {
	private float speed = 5f;
	private float lastMouseX, lastMouseY;
	private float sensitivity = 0.07f;
	public Vector3f forward, backward, right, left, up, down;
	
	public ControllerComponent() {
		lastMouseX = MouseListener.getX();
		lastMouseY = MouseListener.getY();
	}
	
	public void updateController(float dt) {
		float dx = MouseListener.getX() - lastMouseX;
		float dy = MouseListener.getY() - lastMouseY;
		
		if(MouseListener.mouseButtonDown(0)) {
			gameObject.transform.rotation.y += dx * sensitivity;
			gameObject.transform.rotation.x += dy * sensitivity;
		}
		
		float cosX = (float)Math.cos(Math.toRadians(gameObject.transform.rotation.x));
		float sinX = (float)Math.sin(Math.toRadians(gameObject.transform.rotation.x));
		float cosY = (float)Math.cos(Math.toRadians(gameObject.transform.rotation.y));
		float sinY = (float)Math.sin(Math.toRadians(gameObject.transform.rotation.y));
		
		forward = new Vector3f(cosX * sinY, -sinX, -cosX * cosY);
		backward = new Vector3f(-forward.x, -forward.y, -forward.z);
		right = new Vector3f(cosY, 0, sinY);
		left = new Vector3f(-right.x, -right.y, -right.z);
		up = new Vector3f(0, 1f, 0);
		down = new Vector3f(-up.x, -up.y, -up.z);
		
		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_W)) {
			gameObject.transform.position.add(forward.mul(dt * speed));
		}else if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_S)) {
			gameObject.transform.position.add(backward.mul(dt * speed));
		}
		
		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_A)) {
			gameObject.transform.position.add(left.mul(dt * speed));
		}else if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_D)) {
			gameObject.transform.position.add(right.mul(dt * speed));
		}
		
		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			gameObject.transform.position.add(down.mul(dt * speed));
		}else if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
			gameObject.transform.position.add(up.mul(dt * speed));
		}
		
		lastMouseX = MouseListener.getX();
		lastMouseY = MouseListener.getY();
	}
	
	@Override
	public void update(float dt) {}

}
