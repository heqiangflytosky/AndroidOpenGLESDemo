
precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;

uniform sampler2D u_TextureWater;
varying vec2 v_TextureWateCoordinates;

void main()
{
    // 水印效果时
    vec4 nColor = texture2D(u_TextureUnit, v_TextureCoordinates);
    vec4 nWater = texture2D(u_TextureWater, v_TextureWateCoordinates);
    //gl_FragColor = mix(nColor, nWater, 0.5);
    //gl_FragColor = nWater;
    gl_FragColor = nColor + nWater;
}
