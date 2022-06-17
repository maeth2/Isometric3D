#version 330 core
layout(location=0) in vec3 aPos;
layout(location=1) in vec2 aTexCoords;
layout(location=2) in vec3 aNormal;

out vec2 fTexCoords;
out vec3 fPosition;
flat out vec3 fSurfaceNormal;

uniform mat4 uProjection;
uniform mat4 uTransformation;
uniform mat4 uView;

void main(){
	fTexCoords = aTexCoords;
	mat4 modelViewMatrix = uView * uTransformation;
	fSurfaceNormal = (uTransformation * vec4(aNormal, 0.0)).xyz;
	vec4 mvPosition = modelViewMatrix * vec4(aPos,1.0);
	gl_Position = uProjection * mvPosition;
	fPosition = mvPosition.xyz;
}