#version 330 core

in vec2 fTexCoords;

out vec4 color;

uniform sampler2D uDepth;
uniform sampler2D uColor;
uniform sampler2D uNormal;

float THRESHOLD = 0.9;
float THICKNESS = 4;
float BLEND = 0.2;

vec3 sobel(sampler2D normal, sampler2D depth, vec2 coord)
{
	float w = 1.0 / textureSize(normal, 0).x * THICKNESS;
	float h = 1.0 / textureSize(normal, 0).y * THICKNESS;
	vec4 n[9];
	n[0] = texture(normal, coord + vec2( -w, -h)) + texture(depth, coord + vec2( -w, -h)) / BLEND;
	n[1] = texture(normal, coord + vec2(0.0, -h)) + texture(depth, coord + vec2(0.0, -h)) / BLEND;
	n[2] = texture(normal, coord + vec2(  w, -h)) + texture(depth, coord + vec2(  w, -h)) / BLEND;
	n[3] = texture(normal, coord + vec2( -w, 0.0)) + texture(depth, coord + vec2( -w, 0.0)) / BLEND;
	n[4] = texture(normal, coord) + texture(depth, coord) / BLEND;
	n[5] = texture(normal, coord + vec2(  w, 0.0)) + texture(depth, coord + vec2(  w, 0.0)) / BLEND;
	n[6] = texture(normal, coord + vec2( -w, h)) + texture(depth, coord + vec2( -w, h)) / BLEND;
	n[7] = texture(normal, coord + vec2(0.0, h)) + texture(depth, coord + vec2(0.0, h)) / BLEND;
	n[8] = texture(normal, coord + vec2(  w, h)) + texture(depth, coord + vec2(  w, h)) / BLEND;
	
	vec4 sobel_edge_h = n[2] + (2.0*n[5]) + n[8] - (n[0] + (2.0*n[3]) + n[6]);
  	vec4 sobel_edge_v = n[0] + (2.0*n[1]) + n[2] - (n[6] + (2.0*n[7]) + n[8]);
	vec3 sobel = sqrt((sobel_edge_h * sobel_edge_h) + (sobel_edge_v * sobel_edge_v)).rgb;
	
	vec3 outColor = vec3(1);
	if(sobel.x > THRESHOLD || sobel.y > THRESHOLD || sobel.z > THRESHOLD) {
		outColor = vec3(0.0);
	}
	return outColor;
}

void main()
{
    color = vec4(sobel(uNormal, uDepth, fTexCoords), 1.0) * texture(uColor, fTexCoords);
    //color = vec4(sobel(uNormal, uDepth, fTexCoords), 1.0) * (texture(uNormal, fTexCoords) + texture(uDepth, fTexCoords) / BLEND);
   
} 