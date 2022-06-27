#version 330 core
layout(location=0) in vec2 aPos;

out vec2 fTexCoords;

void main(){
	fTexCoords =  vec2((aPos.x+1.0)/2.0, (aPos.y+1.0)/2.0);
	gl_Position = vec4(aPos, 0.0, 1.0);
}