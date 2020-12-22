
precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;

void main()
{
    // 原图效果时
    vec4 nColor = texture2D(u_TextureUnit, v_TextureCoordinates);
    gl_FragColor = nColor;
}
