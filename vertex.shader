#version 330 core

layout(location = 0) in vec2 pos;
layout(location = 1) in vec3 col;

out vec3 vCol;

void main() {

gl_Position = vec4 (pos , 0.0f , 1.0f);
vCol = col;
}