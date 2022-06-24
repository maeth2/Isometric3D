#version 330 core
layout(location=0) in vec3 aPos;

uniform mat4 uProjection;
uniform mat4 uTransformation;
uniform mat4 uView;

void main(){
	gl_Position = uProjection * uView * uTransformation * vec4(aPos,1.0);
}