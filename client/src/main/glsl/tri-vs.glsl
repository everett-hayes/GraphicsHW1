#version 300 es

in vec4 vertexPosition; //#vec4# A four-element vector [x,y,z,w].; We leave z and w alone.; They will be useful later for 3D graphics and transformations. #vertexPosition# attribute fetched from vertex buffer according to input layout spec
out vec4 modelPosition;

uniform struct {
  mat4 modelMatrix;
} gameObject;

uniform struct {
	float time;
} scene;

// Multiply the position vector with the sine of its length-over-a-constant, raised to some periodically time-varying power.
void main(void) {
  modelPosition = vertexPosition;
  gl_Position = vertexPosition * gameObject.modelMatrix;
  gl_Position.xyz *= (.1 * sin(scene.time) + 1.3);
}