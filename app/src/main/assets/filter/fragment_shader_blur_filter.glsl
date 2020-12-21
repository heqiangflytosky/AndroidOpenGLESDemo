
precision mediump float;

uniform sampler2D u_TextureUnit;
uniform vec3 u_ChangeColor;
varying vec2 v_TextureCoordinates;
varying vec4 v_Position;
uniform bool u_IsHalf;  // 是否是处理一半

void main()
{
    if (v_Position.x > 0.0 || !u_IsHalf) {
        // 1. 处理一半效果时，x > 0 时走这里
        // 2. 处理全部效果时，都走这里
        // 模糊效果
        vec4 nColor = texture2D(u_TextureUnit, v_TextureCoordinates);
        nColor+=texture2D(u_TextureUnit,vec2(v_TextureCoordinates.x-u_ChangeColor.r,v_TextureCoordinates.y-u_ChangeColor.r));
        nColor+=texture2D(u_TextureUnit,vec2(v_TextureCoordinates.x-u_ChangeColor.r,v_TextureCoordinates.y+u_ChangeColor.r));
        nColor+=texture2D(u_TextureUnit,vec2(v_TextureCoordinates.x+u_ChangeColor.r,v_TextureCoordinates.y-u_ChangeColor.r));
        nColor+=texture2D(u_TextureUnit,vec2(v_TextureCoordinates.x+u_ChangeColor.r,v_TextureCoordinates.y+u_ChangeColor.r));
        nColor+=texture2D(u_TextureUnit,vec2(v_TextureCoordinates.x-u_ChangeColor.g,v_TextureCoordinates.y-u_ChangeColor.g));
        nColor+=texture2D(u_TextureUnit,vec2(v_TextureCoordinates.x-u_ChangeColor.g,v_TextureCoordinates.y+u_ChangeColor.g));
        nColor+=texture2D(u_TextureUnit,vec2(v_TextureCoordinates.x+u_ChangeColor.g,v_TextureCoordinates.y-u_ChangeColor.g));
        nColor+=texture2D(u_TextureUnit,vec2(v_TextureCoordinates.x+u_ChangeColor.g,v_TextureCoordinates.y+u_ChangeColor.g));
        nColor+=texture2D(u_TextureUnit,vec2(v_TextureCoordinates.x-u_ChangeColor.b,v_TextureCoordinates.y-u_ChangeColor.b));
        nColor+=texture2D(u_TextureUnit,vec2(v_TextureCoordinates.x-u_ChangeColor.b,v_TextureCoordinates.y+u_ChangeColor.b));
        nColor+=texture2D(u_TextureUnit,vec2(v_TextureCoordinates.x+u_ChangeColor.b,v_TextureCoordinates.y-u_ChangeColor.b));
        nColor+=texture2D(u_TextureUnit,vec2(v_TextureCoordinates.x+u_ChangeColor.b,v_TextureCoordinates.y+u_ChangeColor.b));
        nColor/=13.0;
        gl_FragColor=nColor;
    } else {
        // 当前为处理一半模式时，x < 0 的部分都是原图效果
        vec4 nColor = texture2D(u_TextureUnit, v_TextureCoordinates);
        gl_FragColor = nColor;
    }
}
