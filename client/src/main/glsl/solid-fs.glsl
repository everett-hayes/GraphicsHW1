#version 300 es

precision highp float;

out vec4 fragmentColor; //#vec4# A four-element vector [r,g,b,a].; Alpha is opacity, we set it to 1 for opaque.; It will be useful later for transparency.
in vec4 modelPosition;

uniform struct {
	float time;
} scene;

void main(void) {
  //float e = fract(sqrt(modelPosition.x * modelPosition.x + modelPosition.y * modelPosition.y) * 20.0);
  float e = fract(modelPosition.x * sin(scene.time) * 10.0);
  float e1 = fract(modelPosition.y * sin(scene.time) * 10.0);

  if (e < 0.75) {
    if (e1 < .75) {
      fragmentColor = vec4(0.09, .38, .46, 1); // * sin(scene.time);
    } else {
      fragmentColor = vec4(.73, .2, .31, 1); // * cos(scene.time);
    }
  } else {
    if (e1 < .75) {
      fragmentColor = vec4(.73, .2, .31, 1); // * sin(scene.time);
    } else {
      fragmentColor = vec4(0.09, .38, .46, 1); //* cos(scene.time);
    }
  }
}
