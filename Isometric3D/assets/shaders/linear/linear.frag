#version 330 core

in vec2 fTexCoords;

uniform sampler2D uTexture;

float near = 0.1; 
float far  = 100.0; 

out vec4 color;

float linearizeDepth(float depth) 
{
    float z = depth * 2.0 - 1.0; // back to NDC 
    float d = (2.0 * near * far) / (far + near - z * (far - near));		
    
    return d / far;
}

void main(){
	vec4 tex = texture(uTexture, fTexCoords);
	float depth = linearizeDepth(tex.x);
	color = vec4(vec3(depth), 0.0);
}