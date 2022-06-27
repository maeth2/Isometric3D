package main;

import org.joml.Vector3f;

public class Transform {
	public Vector3f position;
	public Vector3f scale;	
	public Vector3f rotation;	
	
	public Transform() {
		init(new Vector3f(), new Vector3f(), new Vector3f(1, 1, 1));
	}

	public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
		init(position, rotation, scale);
	}
	
	public void init(Vector3f position, Vector3f rotation, Vector3f scale) {
		this.position = position;
		this.scale = scale;
		this.rotation = rotation;
	}
	
	public Transform copy() {
		Transform t = new Transform(
			new Vector3f(this.position), 
			new Vector3f(this.rotation), 
			new Vector3f(this.scale)
		);
		return t;
	}
	
	public void copy(Transform to) {
		to.position.set(this.position);
		to.rotation.set(this.rotation);
		to.scale.set(this.scale);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		if(!(o instanceof Transform)) return false;
		
		Transform t = (Transform) o;
		return t.position.equals(this.position) && t.scale == this.scale;
	}
}
