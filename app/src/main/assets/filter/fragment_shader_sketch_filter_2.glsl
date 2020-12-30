
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
//        vec4 nColor = texture2D(u_TextureUnit, v_TextureCoordinates);
//        float c=nColor.r*u_ChangeColor.r+nColor.g*u_ChangeColor.g+nColor.b*u_ChangeColor.b;
//        gl_FragColor=vec4(c, c, c, nColor.a);

        // 1.先把左边上下翻转
        vec2 textureCoordinatesFlip = vec2(v_TextureCoordinates.x, 1.0f - v_TextureCoordinates.y);

        // 2.再执行纹理采样
        vec2 widthStep = vec2(texelWidth, 0.0);
        vec2 heightStep = vec2(0.0, texelHeight);
        vec2 widthHeightStep = vec2(texelWidth, texelHeight);
        vec2 widthNegativeHeightStep = vec2(texelWidth, -texelHeight);

        vec2 leftTextureCoordinate = textureCoordinatesFlip.xy - widthStep;
        vec2 rightTextureCoordinate = textureCoordinatesFlip.xy + widthStep;
        vec2 topTextureCoordinate = textureCoordinatesFlip.xy - heightStep;
        vec2 topLeftTextureCoordinate = textureCoordinatesFlip.xy - widthHeightStep;
        vec2 topRightTextureCoordinate = textureCoordinatesFlip.xy + widthNegativeHeightStep;
        vec2 bottomTextureCoordinate = textureCoordinatesFlip.xy + heightStep;
        vec2 bottomLeftTextureCoordinate = textureCoordinatesFlip.xy - widthNegativeHeightStep;
        vec2 bottomRightTextureCoordinate = textureCoordinatesFlip.xy + widthHeightStep;

        float bottomLeftIntensity = texture2D(u_TextureUnit, bottomLeftTextureCoordinate).r;
        float topRightIntensity = texture2D(u_TextureUnit, topRightTextureCoordinate).r;
        float topLeftIntensity = texture2D(u_TextureUnit, topLeftTextureCoordinate).r;
        float bottomRightIntensity = texture2D(u_TextureUnit, bottomRightTextureCoordinate).r;
        float leftIntensity = texture2D(u_TextureUnit, leftTextureCoordinate).r;
        float rightIntensity = texture2D(u_TextureUnit, rightTextureCoordinate).r;
        float bottomIntensity = texture2D(u_TextureUnit, bottomTextureCoordinate).r;
        float topIntensity = texture2D(u_TextureUnit, topTextureCoordinate).r;
        float h = -topLeftIntensity - 2.0 * topIntensity - topRightIntensity + bottomLeftIntensity + 2.0 * bottomIntensity + bottomRightIntensity;
        float v = -bottomLeftIntensity - 2.0 * leftIntensity - topLeftIntensity + bottomRightIntensity + 2.0 * rightIntensity + topRightIntensity;
        float mag = 1.0 - length(vec2(h, v));
        gl_FragColor = vec4(vec3(mag), 1.0f);
    } else {
        // 当前为处理一半模式时，x < 0 的部分都是原图效果
        vec2 textureCoordinatesFlip = vec2(v_TextureCoordinates.x, 1.0f - v_TextureCoordinates.y);
        gl_FragColor = texture2D(u_TextureUnit, textureCoordinatesFlip);
    }
}
