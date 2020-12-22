
precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;

uniform sampler2D u_TextureWater;

void main()
{
    // 水印效果时
    vec4 nColor = texture2D(u_TextureUnit, v_TextureCoordinates);
    vec4 nWater = texture2D(u_TextureWater, v_TextureCoordinates+v_TextureCoordinates);
    gl_FragColor = mix(nColor, nWater, 0.2);
}
