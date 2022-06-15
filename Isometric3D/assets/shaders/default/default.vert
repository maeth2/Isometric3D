#version 330 core
layout(location=0) in vec3 aPos;
layout(location=1) in vec2 aTexCoords;
layout(location=2) in vec3 aNormal;

out vec2 fTexCoords;
out vec3 fToLightVector;
out vec3 fSurfaceNormal;

uniform vec3 uLightPosition;
uniform mat4 uProjection;
uniform mat4 uTransformation;
uniform mat4 uView;

void main(){
	fTexCoords = aTexCoords;
	vec4 worldPosition = uTransformation * vec4(aPos,1.0);
	gl_Position = uProjection * uView * uTransformation * vec4(aPos,1.0);
	fSurfaceNormal = aNormal;
	fToLightVector = uLightPosition - worldPosition.xyz;
}