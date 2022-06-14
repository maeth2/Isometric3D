#version 330 core

in vec2 fTexCoords;

uniform sampler2D uTextures[8];
uniform int texID;

out vec4 color;

void main(){
	if(texID == 0){
		color = vec4(1.0, 1.0, 1.0, 1.0);
	}else{
		color = texture(uTextures[texID], fTexCoords);
	}
}