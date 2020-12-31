
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
    //gl_FragColor = nColor + nWater; //这种方式水印颜色不对，会呈现混合效果
    gl_FragColor = mix(nColor, nWater, nWater.a);
}
