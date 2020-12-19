
precision mediump float;

uniform sampler2D u_TextureUnit;
uniform int u_ChangeType;
uniform vec3 u_ChangeColor;
uniform float u_XY; // 图像宽高比
varying vec2 v_TextureCoordinates;
varying vec4 v_Position;
uniform bool u_IsHalf;  // 是否是处理一半

// 浮雕效果
const vec2 TexSize = vec2(100.0, 100.0);
const vec4 bkColor = vec4(0.5, 0.5, 0.5, 1.0);

// 马赛克效果
const vec2 TexSizeMosaic = vec2(400.0, 400.0);
const vec2 mosaicSize = vec2(8.0, 8.0);

void modifyColor(vec4 color){
    color.r=max(min(color.r,1.0),0.0);
    color.g=max(min(color.g,1.0),0.0);
    color.b=max(min(color.b,1.0),0.0);
    color.a=max(min(color.a,1.0),0.0);
}

void main()
{
    if (v_Position.x > 0.0 || !u_IsHalf) {
        // 1. 处理一半效果时，x > 0 时走这里
        // 2. 处理全部效果时，都走这里
        // 马赛克效果
        vec2 intXY = vec2(v_TextureCoordinates.x*TexSizeMosaic.x, v_TextureCoordinates.y*TexSizeMosaic.y);
        vec2 XYMosaic = vec2(floor(intXY.x/mosaicSize.x)*mosaicSize.x, floor(intXY.y/mosaicSize.y)*mosaicSize.y);
        vec2 UVMosaic = vec2(XYMosaic.x/TexSizeMosaic.x, XYMosaic.y/TexSizeMosaic.y);
        vec4 color = texture2D(u_TextureUnit, UVMosaic);
        gl_FragColor = color;
    } else {
        // 当前为处理一半模式时，x < 0 的部分都是原图效果
        vec4 nColor = texture2D(u_TextureUnit, v_TextureCoordinates);
        gl_FragColor = nColor;
    }
}
