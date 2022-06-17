#version 330 core

struct Light{
	int type;
	vec3 color;
	vec3 position;
	float intensity;
};

struct Material{
	int hasTexture;
	float reflectance;
};


in vec2 fTexCoords;
in vec3 fPosition;
flat in vec3 fSurfaceNormal;

uniform sampler2D uTexture;
uniform Light uLight;
uniform Material uMaterial;
uniform vec3 uAmbient;

vec4 texture;

out vec4 color;

vec4 calculateDirectionalLight(Light light, vec3 normal){
	vec3 unitLightVector = normalize(light.position);
	float diffuseFactor = max(dot(normal, unitLightVector), 0.0);
	vec4 diffuseColor = texture * vec4(light.color, 1.0) * light.intensity * diffuseFactor;
	
	vec3 cameraDirection = normalize(-fPosition);
	vec3 fromLightDirection = -light.position;
	vec3 reflectedLight = normalize(reflect(fromLightDirection, normal));
	float specularFactor = max(dot(reflectedLight, cameraDirection), 0.0);
	specularFactor = pow(specularFactor, 2);
	vec4 specularColor = texture * vec4(light.color, 1.0) * light.intensity * specularFactor * uMaterial.reflectance;

	return diffuseColor + specularColor;
}

void main(){
	texture = uMaterial.hasTexture == 0 ? vec4(1.0, 1.0, 1.0, 1.0) : texture(uTexture, fTexCoords);
	vec3 unitNormal = normalize(fSurfaceNormal);
	vec4 lightColor = calculateDirectionalLight(uLight, unitNormal);
	color = texture * vec4(uAmbient, 1.0) + lightColor;
}
