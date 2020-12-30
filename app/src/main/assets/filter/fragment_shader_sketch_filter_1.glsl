
precision mediump float;

//uniform sampler2D u_TextureUnit;
uniform vec3 u_ChangeColor;
varying vec2 v_TextureCoordinates;
varying vec4 v_Position;
uniform bool u_IsHalf;  // 是否是处理一半

uniform highp float texelWidth;
uniform highp float texelHeight;

uniform sampler2D u_TextureUnit;

void main()
{
    if (v_Position.x > 0.0 || !u_IsHalf) {
        // 1. 处理一半效果时，x > 0 时走这里
        // 2. 处理全部效果时，都走这里
        // 素描效果
        // 1.先灰度处理成黑白效果
        vec4 nColor = texture2D(u_TextureUnit, v_TextureCoordinates);
        float c=nColor.r*u_ChangeColor.r+nColor.g*u_ChangeColor.g+nColor.b*u_ChangeColor.b;
        gl_FragColor=vec4(c, c, c, nColor.a);
    } else {
        // 当前为处理一半模式时，x < 0 的部分都是原图效果
        vec4 nColor = texture2D(u_TextureUnit, v_TextureCoordinates);
        gl_FragColor = nColor;
    }
}
