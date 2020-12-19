
precision mediump float;

uniform sampler2D u_TextureUnit;
uniform vec3 u_ChangeColor;
uniform float u_XY; // 图像宽高比
varying vec2 v_TextureCoordinates;
varying vec4 v_Position;
uniform bool u_IsHalf;  // 是否是处理一半

void main()
{
    // 原图效果时
    vec4 nColor = texture2D(u_TextureUnit, v_TextureCoordinates);
    gl_FragColor = nColor;
}
