
precision mediump float;

uniform sampler2D u_TextureUnit;
uniform vec3 u_ChangeColor;
varying vec2 v_TextureCoordinates;
varying vec4 v_Position;
uniform bool u_IsHalf;  // 是否是处理一半

// 浮雕效果
const vec2 TexSize = vec2(100.0, 100.0);
const vec4 bkColor = vec4(0.5, 0.5, 0.5, 1.0);

void main()
{
    if (v_Position.x > 0.0 || !u_IsHalf) {
        // 1. 处理一半效果时，x > 0 时走这里
        // 2. 处理全部效果时，都走这里
        // 浮雕效果
        vec4 nColor = texture2D(u_TextureUnit, v_TextureCoordinates);
        vec2 upLeftUV = vec2(v_TextureCoordinates.x-1.0/TexSize.x, v_TextureCoordinates.y-1.0/TexSize.y);
        vec4 upLeftColor = texture2D(u_TextureUnit, upLeftUV);
        vec4 delColor = nColor - upLeftColor;
        float luminance = dot(delColor.rgb, u_ChangeColor);
        gl_FragColor = vec4(vec3(luminance), 0.0) + bkColor;
    } else {
        // 当前为处理一半模式时，x < 0 的部分都是原图效果
        vec4 nColor = texture2D(u_TextureUnit, v_TextureCoordinates);
        gl_FragColor = nColor;
    }
}
