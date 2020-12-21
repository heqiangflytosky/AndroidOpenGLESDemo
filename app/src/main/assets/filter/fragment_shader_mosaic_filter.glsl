
precision mediump float;

uniform sampler2D u_TextureUnit;
uniform vec3 u_ChangeColor;
varying vec2 v_TextureCoordinates;
varying vec4 v_Position;
uniform bool u_IsHalf;  // 是否是处理一半

// 马赛克效果
const vec2 TexSizeMosaic = vec2(400.0, 400.0);
const vec2 mosaicSize = vec2(8.0, 8.0);

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
