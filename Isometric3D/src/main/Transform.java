package main;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Transform {
	public Vector3f position;
	public int scale;	
	public Vector3f rotation;	
	
	public Transform() {
		init(new Vector3f(), 1);
	}
	
	public Transform(Vector2f position) {
		init(new Vector3f(position.x, position.y, 0), 1);
	}
	
	public Transform(Vector3f position) {
		init(position, 1);
	}
	
	public Transform(Vector2f position, int scale) {
		init(new Vector3f(position.x, position.y, 0), scale);
	}
	
	public Transform(Vector3f position, int scale) {
		init(position, scale);
	}
	
	public void init(Vector3f position, int scale) {
		this.position = position;
		this.scale = scale;
		this.rotation = new Vector3f();
	}
	
	public Transform copy() {
		Transform t = new Transform(new Vector3f(this.position), this.scale);
		return t;
	}
	
	public void copy(Transform to) {
		to.position.set(this.position);
		to.scale = this.scale;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		if(!(o instanceof Transform)) return false;
		
		Transform t = (Transform) o;
		return t.position.equals(this.position) && t.scale == this.scale;
	}
}
