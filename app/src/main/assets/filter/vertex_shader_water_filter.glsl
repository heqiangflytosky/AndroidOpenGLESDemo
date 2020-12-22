
attribute vec4 a_Position;
attribute vec2 a_TextureCoordinates;
attribute vec2 a_TextureWaterCoordinates;

varying vec2 v_TextureCoordinates;
varying vec4 v_Position;
varying vec2 v_TextureWateCoordinates;
void main()
{
    v_TextureCoordinates = a_TextureCoordinates;
    gl_Position = a_Position;
    v_Position = a_Position;
    v_TextureWateCoordinates = a_TextureWaterCoordinates;
}
