package components;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import util.Maths;

public class GUIComponent extends Component{
	private Matrix4f transformMatrix;
	private int texture;
	
	public GUIComponent(int texture) {
		init(texture);
	}
	
	public void init(int texture) {
		this.texture = texture;
	}
	
	@Override
	public void start() {
		this.transformMatrix = Maths.createTransformationalMatrix(
			new Vector2f(gameObject.transform.position.x, gameObject.transform.position.y), 
			new Vector2f(gameObject.transform.scale.x, gameObject.transform.scale.y)
		);
	}
	
	@Override
	public void update(float dt) {}
	
	public Matrix4f getTransformMatrix() {
		return this.transformMatrix;
	}
	
	public int getTexture() {
		return this.texture;
	}
}
