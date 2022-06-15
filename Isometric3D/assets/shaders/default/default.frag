#version 330 core

in vec2 fTexCoords;
in vec3 fSurfaceNormal;
in vec3 fToLightVector;

uniform sampler2D uTextures[8];
uniform int texID;

out vec4 color;

void main(){
	vec3 unitLightVector = normalize(fToLightVector);
	vec3 unitNormal = normalize(fSurfaceNormal);
	float diffuseFactor = dot(unitNormal, unitLightVector);
	float brightness = max(diffuseFactor,0.2);
	
	if(texID == 0){
		color = vec4(1.0, 1.0, 1.0, 1.0) * brightness;
	}else{
		color = texture(uTextures[texID], fTexCoords) * brightness;
	}
}