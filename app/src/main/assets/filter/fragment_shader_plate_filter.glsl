
precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;
varying vec4 v_Position;
uniform bool u_IsHalf;  // 是否是处理一半

void main()
{
    if (v_Position.x > 0.0 || !u_IsHalf) {
        // 1. 处理一半效果时，x > 0 时走这里
        // 2. 处理全部效果时，都走这里
        // 底片效果
        vec4 nColor = texture2D(u_TextureUnit, v_TextureCoordinates);
        gl_FragColor = vec4(1.0 - nColor.r, 1.0 - nColor.g, 1.0 - nColor.b, nColor.a);;
    } else {
        // 当前为处理一半模式时，x < 0 的部分都是原图效果
        vec4 nColor = texture2D(u_TextureUnit, v_TextureCoordinates);
        gl_FragColor = nColor;
    }
}
