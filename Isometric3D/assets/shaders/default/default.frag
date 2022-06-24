#version 330 core

struct Light{
	int type;
	vec3 color;
	vec3 position;
	float intensity;
	vec3 attenuation;
};

struct Material{
	int hasTexture;
	float reflectance;
};


in vec2 fTexCoords;
in vec3 fPosition;
flat in vec3 fSurfaceNormal;
in vec3 fCameraPosition;

uniform sampler2D uTexture;
uniform Light uLight[100];
uniform int uNumOfLights;
uniform Material uMaterial;
uniform vec3 uAmbient;
uniform float uShineDamper;

vec4 texture;

out vec4 color;

vec4 calculateDirectionalLight(Light light, vec3 normal){
	vec3 unitLightVector = normalize(light.position);
	float diffuseFactor = max(dot(normal, unitLightVector), 0.0);
	vec4 diffuseColor = texture * vec4(light.color, 1.0) * light.intensity * diffuseFactor;
	
	vec3 cameraDirection = normalize(fCameraPosition - fPosition);
	vec3 fromLightDirection = -unitLightVector;
	vec3 reflectedLight = normalize(reflect(fromLightDirection, normal));
	float specularFactor = max(dot(reflectedLight, cameraDirection), 0.0);
	specularFactor = pow(specularFactor,uShineDamper);
	vec4 specularColor = texture * vec4(light.color, 1.0) * light.intensity * specularFactor * uMaterial.reflectance;

	return diffuseColor + specularColor;
}

vec4 calculatePointLight(Light light, vec3 normal){
	vec3 lightDirection = light.position - fPosition;
	vec3 unitLightVector = normalize(lightDirection);
	float diffuseFactor = max(dot(normal, unitLightVector), 0.0);
	vec4 diffuseColor = texture * vec4(light.color, 1.0) * light.intensity * diffuseFactor;
	
	vec3 cameraDirection = normalize(fCameraPosition - fPosition);
	vec3 fromLightDirection = -unitLightVector;
	vec3 reflectedLight = normalize(reflect(fromLightDirection, normal));
	float specularFactor = max(dot(reflectedLight, cameraDirection), 0.0);
	specularFactor = pow(specularFactor, uShineDamper);
	vec4 specularColor = texture * vec4(light.color, 1.0) * light.intensity * specularFactor * uMaterial.reflectance;
	
	float distance = length(lightDirection);
	float attenuationInv = light.attenuation.x + light.attenuation.y * distance + light.attenuation.z * distance * distance;
		
	return (specularColor + diffuseColor) / attenuationInv;
}

void main(){
	texture = uMaterial.hasTexture == 0 ? vec4(1.0, 1.0, 1.0, 1.0) : texture(uTexture, fTexCoords);
	vec3 unitNormal = normalize(fSurfaceNormal);
	vec4 lightColor = vec4(0.0, 0.0, 0.0, 0.0);
	for(int i = 0; i < uNumOfLights; i++){
		if(uLight[i].type == 0){
			lightColor += calculateDirectionalLight(uLight[i], unitNormal);
		}else if(uLight[i].type == 1){
			lightColor += calculatePointLight(uLight[i], unitNormal);
		} 
	}
	color = texture * vec4(uAmbient, 1.0) + lightColor;
}
