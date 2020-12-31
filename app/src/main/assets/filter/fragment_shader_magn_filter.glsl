
precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;
varying vec4 v_Position;
uniform bool u_IsHalf;  // 是否是处理一半

uniform float u_XY; // 图像宽高比
uniform float u_Radius; // 放大镜半径
const highp vec2 Center = vec2(0f, 0f); // 放大镜圆心位置

void main()
{
    if (v_Position.x > 0.0 || !u_IsHalf) {
        // 1. 处理一半效果时，x > 0 时走这里
        // 2. 处理全部效果时，都走这里
        // 放大镜效果，放大一倍
        vec4 nColor = texture2D(u_TextureUnit, v_TextureCoordinates);
        float dis=distance(vec2(v_Position.x,v_Position.y/u_XY),Center);
        if(dis<u_Radius){
            nColor=texture2D(u_TextureUnit,vec2(v_TextureCoordinates.x/2.0+0.25,v_TextureCoordinates.y/2.0+0.25));
        }
        gl_FragColor=nColor;
    } else {
        // 当前为处理一半模式时，x < 0 的部分都是原图效果
        vec4 nColor = texture2D(u_TextureUnit, v_TextureCoordinates);
        gl_FragColor = nColor;
    }
}
