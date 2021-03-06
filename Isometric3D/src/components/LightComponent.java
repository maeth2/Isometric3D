package components;

import org.joml.Vector3f;

public class LightComponent extends Component {
	
	public static final int TYPE_LIGHT_DIRECTIONAL = 0;
	public static final int TYPE_LIGHT_POINT = 1;
	
	private Vector3f position;
	private Vector3f color;
	private Vector3f attenuation;
	private int lightType;
	private float intensity;
	
	public LightComponent(int lightType, float intensity, Vector3f color, Vector3f attenuation) {
		init(lightType, intensity, color, attenuation);
	}
	
	public LightComponent(int lightType, Vector3f color, Vector3f attenuation) {
		init(lightType, 1f, color, attenuation);
	}
	
	public LightComponent(int lightType, float intensity, Vector3f color) {
		init(lightType, intensity, color, new Vector3f());
	}
	
	public LightComponent(int lightType, Vector3f color) {
		init(lightType, 1f, color, new Vector3f());
	}
	
	public void init(int lightType, float intensity, Vector3f color, Vector3f attenuation) {
		this.lightType = lightType;
		this.intensity = intensity;
		this.color = color;
		this.attenuation = attenuation;
	}
	
	@Override
	public void start() {
		this.position = gameObject.transform.position;
	}
	
	@Override
	public void update(float dt) {}

	public Vector3f getColor() {
		return color;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public Vector3f getAttenuation() {
		return attenuation;
	}

	public void setAttenuation(Vector3f attenuation) {
		this.attenuation = attenuation;
	}

	public int getLightType() {
		return lightType;
	}

	public void setLightType(int lightType) {
		this.lightType = lightType;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
}
