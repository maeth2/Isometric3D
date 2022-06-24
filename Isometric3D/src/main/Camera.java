 package main;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import components.ControllerComponent;
import util.Maths;

public class Camera extends GameObject{
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.01f;
	private static final float FAR_PLANE = 100;
	private Matrix4f projectionMatrix;
	private GameObject target;
	
	public Camera(Vector3f position) {
		super("Camera");
		target = this;
		this.transform.position = position;
		this.projectionMatrix = new Matrix4f();
		this.addComponent(new ControllerComponent());
		createPerspectiveProjection();
		//createOrthographicProjection();
	}
	
	@Override
	public void update(float dt) {
		if(target.getComponent(ControllerComponent.class) != null) {
			target.getComponent(ControllerComponent.class).updateController(dt);
		}
		this.setPosition(target.transform.position);
	}
	
	public void createPerspectiveProjection() {
		projectionMatrix = new Matrix4f().perspective(FOV, 1920f/1000f, NEAR_PLANE, FAR_PLANE);
	}
	
	public void createOrthographicProjection() {
	    projectionMatrix = new Matrix4f().ortho(-10f, 10f, -10f * 9f / 16f, 10f * 9f / 16f, -100f, 100f);
	}
	
	public Matrix4f getViewMatrix() {
		return Maths.createViewMatrix(this.transform);
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public void setTarget(GameObject target) {
		this.target = target;
	}
	
	public void setPosition(Vector3f position) {
		this.transform.position.x = position.x;
		this.transform.position.y = position.y;
		this.transform.position.z = position.z;
	}
}
