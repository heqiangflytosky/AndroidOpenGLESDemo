
precision mediump float;

uniform sampler2D u_TextureUnit;
uniform vec3 u_ChangeColor;
varying vec2 v_TextureCoordinates;
varying vec4 v_Position;
uniform bool u_IsHalf;  // 是否是处理一半

uniform highp float fractionalWidthOfPixel;
uniform highp float aspectRatio;
const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);

void main()
{
    if (v_Position.x > 0.0 || !u_IsHalf) {
        // 1. 处理一半效果时，x > 0 时走这里
        // 2. 处理全部效果时，都走这里
        // 半色调效果
        highp vec2 sampleDivisor = vec2(fractionalWidthOfPixel, fractionalWidthOfPixel / aspectRatio);
        highp vec2 samplePos = v_TextureCoordinates - mod(v_TextureCoordinates, sampleDivisor) + 0.5 * sampleDivisor;
        highp vec2 textureCoordinateToUse = vec2(v_TextureCoordinates.x, (v_TextureCoordinates.y * aspectRatio + 0.5 - 0.5 * aspectRatio));
        highp vec2 adjustedSamplePos = vec2(samplePos.x, (samplePos.y * aspectRatio + 0.5 - 0.5 * aspectRatio));
        highp float distanceFromSamplePoint = distance(adjustedSamplePos, textureCoordinateToUse);
        lowp vec3 sampledColor = texture2D(u_TextureUnit, samplePos).rgb;
        highp float dotScaling = 1.0 - dot(sampledColor, W);
        lowp float checkForPresenceWithinDot = 1.0 - step(distanceFromSamplePoint, (fractionalWidthOfPixel * 0.5) * dotScaling);
        gl_FragColor = vec4(vec3(checkForPresenceWithinDot), 1.0);
    } else {
        // 当前为处理一半模式时，x < 0 的部分都是原图效果
        vec4 nColor = texture2D(u_TextureUnit, v_TextureCoordinates);
        gl_FragColor = nColor;
    }
}
