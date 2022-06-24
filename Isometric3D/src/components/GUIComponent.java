package components;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import util.Helper;
import util.Maths;

public class GUIComponent extends Component{
	public static int vaoID = 0;
	private Matrix4f transformMatrix;
	private int texture;
	
	public static float vertices[] = {
			-1, 1,
			-1, -1,
			1, 1,
			1, -1
	};
	
	public GUIComponent(Vector2f position, Vector2f scale, int texture) {
		init(position, scale, texture);
	}
	
	public GUIComponent(int texture) {
		init(new Vector2f(), new Vector2f(1f, 1f), texture);
	}
	
	public void init(Vector2f position, Vector2f scale, int texture) {
		this.texture = texture;
		this.transformMatrix = Maths.createTransformationalMatrix(position, scale);
	}
	
	@Override
	public void update(float dt) {}
	
	public static int getVaoID() {
		if(vaoID == 0) {
			vaoID = Helper.generateVAO();
			Helper.storeDataInAttributeList(vaoID, 2, 0, vertices);
		}
		return vaoID;
	}
	
	public Matrix4f getTransformMatrix() {
		return this.transformMatrix;
	}
	
	public int getTexture() {
		return this.texture;
	}
}
