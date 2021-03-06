package net.calzoneman.TileLand.level;

/**
 * Original Noise class written in C# by Matvei Stefarov <me@matvei.org>
 * Ported to Java by Calvin "calzoneman" Montgomery
 * Used with permission
 */

public class Noise
{
	public static final int INTERPOLATE_LINEAR = 1;
	public static final int INTERPOLATE_COSINE = 2;
	public static final int INTERPOLATE_BICUBIC = 3;
	public static final int INTERPOLATE_SPLINE = 4;
	
    int seed;
    int interpolationMode;

    public Noise(int _seed, int _interpolationMode)
    {
        seed = _seed;
        interpolationMode = _interpolationMode;
    }


    public static float InterpolateLinear(float v0, float v1, float x)
    {
        return v0 * (1 - x) + v1 * x;
    }

    public static float InterpolateLinear(float v00, float v01, float v10, float v11, float x, float y)
    {
        return InterpolateLinear(InterpolateLinear(v00, v10, x),
                                  InterpolateLinear(v01, v11, x),
                                  y);
    }


    public static float InterpolateCosine(float v0, float v1, float x)
    {
        double f = (1 - Math.cos(x * Math.PI)) * .5;
        return (float)(v0 * (1 - f) + v1 * f);
    }

    public static float InterpolateCosine(float v00, float v01, float v10, float v11, float x, float y)
    {
        return InterpolateCosine(InterpolateCosine(v00, v10, x),
                                  InterpolateCosine(v01, v11, x),
                                  y);
    }


    // Cubic and Catmull-Rom Spline interpolation methods by Paul Bourke
    // http://local.wasp.uwa.edu.au/~pbourke/miscellaneous/interpolation/
    public static float InterpolateCubic(float v0, float v1, float v2, float v3, float mu)
    {
        float a0, a1, a2, a3, mu2;
        mu2 = mu * mu;
        a0 = v3 - v2 - v0 + v1;
        a1 = v0 - v1 - a0;
        a2 = v2 - v0;
        a3 = v1;
        return (float)(a0 * mu * mu2 + a1 * mu2 + a2 * mu + a3);
    }


    public static float InterpolateSpline(float v0, float v1, float v2, float v3, float mu)
    {
        float a0, a1, a2, a3, mu2;
        mu2 = mu * mu;
        a0 = -0.5f * v0 + 1.5f * v1 - 1.5f * v2 + 0.5f * v3;
        a1 = v0 - 2.5f * v1 + 2 * v2 - 0.5f * v3;
        a2 = -0.5f * v0 + 0.5f * v2;
        a3 = v1;
        return (float)(a0 * mu * mu2 + a1 * mu2 + a2 * mu + a3);
    }


    public float StaticNoise(int x, int y)
    {
        int n = seed + x + y * Short.MAX_VALUE;
        n = (n << 13) ^ n;
        return (float)(1.0 - ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7FFFFFFF) / 1073741824d);
    }


    public float InterpolatedNoise(float x, float y)
    {
        int xInt = (int)Math.floor(x);
        float xFloat = x - xInt;

        int yInt = (int)Math.floor(y);
        float yFloat = y - yInt;

        float p00, p01, p10, p11;
        float[][] points;

        switch (interpolationMode)
        {
            case INTERPOLATE_LINEAR:
                p00 = StaticNoise(xInt, yInt);
                p01 = StaticNoise(xInt, yInt + 1);
                p10 = StaticNoise(xInt + 1, yInt);
                p11 = StaticNoise(xInt + 1, yInt + 1);
                return InterpolateLinear(InterpolateLinear(p00, p10, xFloat), InterpolateLinear(p01, p11, xFloat), yFloat);

            case INTERPOLATE_COSINE:
                p00 = StaticNoise(xInt, yInt);
                p01 = StaticNoise(xInt, yInt + 1);
                p10 = StaticNoise(xInt + 1, yInt);
                p11 = StaticNoise(xInt + 1, yInt + 1);
                return InterpolateCosine(InterpolateCosine(p00, p10, xFloat), InterpolateCosine(p01, p11, xFloat), yFloat);

            case INTERPOLATE_BICUBIC:
                points = new float[4][4];
                for (int xOffset = -1; xOffset < 3; xOffset++)
                {
                    for (int yOffset = -1; yOffset < 3; yOffset++)
                    {
                        points[xOffset + 1][yOffset + 1] = StaticNoise(xInt + xOffset, yInt + yOffset);
                    }
                }
                p00 = InterpolateCubic(points[0][0], points[1][0], points[2][0], points[3][0], xFloat);
                p01 = InterpolateCubic(points[0][1], points[1][1], points[2][1], points[3][1], xFloat);
                p10 = InterpolateCubic(points[0][2], points[1][2], points[2][2], points[3][2], xFloat);
                p11 = InterpolateCubic(points[0][3], points[1][3], points[2][3], points[3][3], xFloat);
                return InterpolateCubic(p00, p01, p10, p11, yFloat);

            case INTERPOLATE_SPLINE:
                points = new float[4][4];
                for (int xOffset = -1; xOffset < 3; xOffset++)
                {
                    for (int yOffset = -1; yOffset < 3; yOffset++)
                    {
                        points[xOffset + 1][yOffset + 1] = StaticNoise(xInt + xOffset, yInt + yOffset);
                    }
                }
                p00 = InterpolateSpline(points[0][0], points[1][0], points[2][0], points[3][0], xFloat);
                p01 = InterpolateSpline(points[0][1], points[1][1], points[2][1], points[3][1], xFloat);
                p10 = InterpolateSpline(points[0][2], points[1][2], points[2][2], points[3][2], xFloat);
                p11 = InterpolateSpline(points[0][3], points[1][3], points[2][3], points[3][3], xFloat);
                return InterpolateSpline(p00, p01, p10, p11, yFloat);
            default:
                throw new IllegalArgumentException();
        }
    }


    public float PerlinNoise(float x, float y, int startOctave, int endOctave, float decay)
    {
        float total = 0;

        float frequency = (float)Math.pow(2, startOctave);
        float amplitude = (float)Math.pow(decay, startOctave);

        for (int n = startOctave; n <= endOctave; n++)
        {
            total += InterpolatedNoise(x * frequency + frequency, y * frequency + frequency) * amplitude;
            frequency *= 2;
            amplitude *= decay;
        }
        return total;
    }


    public void PerlinNoiseMap(float[][] heightmap, int startOctave, int endOctave, float decay, int offsetX, int offsetY)
    {
        float maxDim = 0.01f; //1.0f / Math.max(heightmap.length, heightmap[0].length);
        for (int x = heightmap.length - 1; x >= 0; x--)
        {
            for (int y = heightmap[0].length - 1; y >= 0; y--)
            {
                heightmap[x][y] += PerlinNoise((x + offsetX) * maxDim, (y + offsetY) * maxDim, startOctave, endOctave, decay);
            }
        }
    }


    public static void Normalize(float[][] map)
    {
        Normalize(map, 0, 1);
    }

    public static void Normalize(float[][] map, float low, float high)
    {
        float min = Float.MAX_VALUE, max = Float.MIN_VALUE;
        for (int x = map.length - 1; x >= 0; x--)
        {
            for (int y = map[0].length - 1; y >= 0; y--)
            {
                min = Math.min(min, map[x][y]);
                max = Math.max(max, map[x][y]);
            }
        }

        float multiplier = (high - low) / (max - min);
        float constant = -min * (high - low) / (max - min) + low;

        for (int x = map.length - 1; x >= 0; x--)
        {
            for (int y = map[0].length - 1; y >= 0; y--)
            {
                map[x][y] = map[x][y] * multiplier + constant;
            }
        }
    }


    // assumes normalized input
    public static void Marble(float[][] map)
    {
        for (int x = map.length - 1; x >= 0; x--)
        {
            for (int y = map[0].length - 1; y >= 0; y--)
            {
                map[x][y] = Math.abs(map[x][y] * 2 - 1);
            }
        }
    }


    // assumes normalized input
    public static void Blend(float[][] map1, float[][] map2, float[][] blendMap)
    {
        for (int x = map1.length - 1; x >= 0; x--)
        {
            for (int y = map1[0].length - 1; y >= 0; y--)
            {
                map1[x][y] = map1[x][y] * blendMap[x][y] + map2[x][y] * (1 - blendMap[x][y]);
            }
        }
    }


    public static void Add(float[][] map1, float[][] map2)
    {
        for (int x = map1.length - 1; x >= 0; x--)
        {
            for (int y = map1[0].length - 1; y >= 0; y--)
            {
                map1[x][y] += map2[x][y];
            }
        }
    }


    public static void ApplyBias(float[][] heightmap, float c00, float c01, float c10, float c11, float midpoint)
    {
        float maxX = 2f / heightmap.length;
        float maxY = 2f / heightmap[0].length;
        int offsetX = heightmap.length / 2;
        int offsetY = heightmap[0].length / 2;

        for (int x = offsetX - 1; x >= 0; x--)
        {
            for (int y = offsetY - 1; y >= 0; y--)
            {
                heightmap[x][y] += InterpolateCosine(c00, (c00 + c01) / 2, (c00 + c10) / 2, midpoint, x * maxX, y * maxY);
                heightmap[x + offsetX][y] += InterpolateCosine((c00 + c10) / 2, midpoint, c10, (c11 + c10) / 2, x * maxX, y * maxY);
                heightmap[x][y + offsetY] += InterpolateCosine((c00 + c01) / 2, c01, midpoint, (c01 + c11) / 2, x * maxX, y * maxY);
                heightmap[x + offsetX][y + offsetY] += InterpolateCosine(midpoint, (c01 + c11) / 2, (c11 + c10) / 2, c11, x * maxX, y * maxY);
            }
        }
    }


    // assumes normalized input
    public static void ScaleAndClip(float[][] heightmap, float steepness)
    {
        for (int x = heightmap.length - 1; x >= 0; x--)
        {
            for (int y = heightmap[0].length - 1; y >= 0; y--)
            {
                heightmap[x][y] = Math.min(1, Math.max(0, heightmap[x][y] * steepness * 2 - steepness));
            }
        }
    }

    public static void Invert(float[][] heightmap)
    {
        for (int x = heightmap.length - 1; x >= 0; x--)
        {
            for (int y = heightmap[0].length - 1; y >= 0; y--)
            {
                heightmap[x][y] = 1 - heightmap[x][y];
            }
        }
    }


    public static float[][] BoxBlur(float[][] heightmap)
    {
        float divisor = 1 / 23f;
        float[][] output = new float[heightmap.length][heightmap[0].length];
        for (int x = heightmap.length - 1; x >= 0; x--)
        {
            for (int y = heightmap[0].length - 1; y >= 0; y--)
            {
                if ((x == 0) || (y == 0) || (x == heightmap.length - 1) || (y == heightmap[0].length - 1))
                {
                    output[x][y] = heightmap[x][y];
                }
                else
                {
                    output[x][y] = (heightmap[x - 1][y - 1] * 2 + heightmap[x - 1][y] * 3 + heightmap[x - 1][y + 1] * 2 +
                                    heightmap[x][y - 1] * 3 + heightmap[x][y] * 3 + heightmap[x][y + 1] * 3 +
                                    heightmap[x + 1][y - 1] * 2 + heightmap[x + 1][y] * 3 + heightmap[x + 1][y + 1] * 2) * divisor;
                }
            }
        }
        return output;
    }



    public static float[][] GaussianBlur5x5(float[][] heightmap)
    {
        float divisor = 1 / 273f;
        float[][] output = new float[heightmap.length][heightmap[0].length];
        for (int x = heightmap.length - 1; x >= 0; x--)
        {
            for (int y = heightmap[0].length - 1; y >= 0; y--)
            {
                if ((x < 2) || (y < 2) || (x > heightmap.length - 3) || (y > heightmap[0].length - 3))
                {
                    output[x][y] = heightmap[x][y];
                }
                else
                {
                    output[x][y] = (heightmap[x - 2][y - 2] + heightmap[x - 1][y - 2] * 4 + heightmap[x][y - 2] * 7 + heightmap[x + 1][y - 2] * 4 + heightmap[x + 2][y - 2] +
                                    heightmap[x - 1][y - 1] * 4 + heightmap[x - 1][y - 1] * 16 + heightmap[x][y - 1] * 26 + heightmap[x + 1][y - 1] * 16 + heightmap[x + 2][y - 1] * 4 +
                                    heightmap[x - 2][y] * 7 + heightmap[x - 1][y] * 26 + heightmap[x][y] * 41 + heightmap[x + 1][y] * 26 + heightmap[x + 2][y] * 7 +
                                    heightmap[x - 2][y + 1] * 4 + heightmap[x - 1][y + 1] * 16 + heightmap[x][y + 1] * 26 + heightmap[x + 1][y + 1] * 16 + heightmap[x + 2][y + 1] * 4 +
                                    heightmap[x - 2][y + 2] + heightmap[x - 1][y + 2] * 4 + heightmap[x][y + 2] * 7 + heightmap[x + 1][y + 2] * 4 + heightmap[x + 2][y + 2]) * divisor;
                }
            }
        }
        return output;
    }


    public static float[][] CalculateSlope(float[][] heightmap)
    {
        float[][] output = new float[heightmap.length][heightmap[0].length];

        for (int x = heightmap.length - 1; x >= 0; x--)
        {
            for (int y = heightmap[0].length - 1; y >= 0; y--)
            {
                if ((x == 0) || (y == 0) || (x == heightmap.length - 1) || (y == heightmap[0].length - 1))
                {
                    output[x][y] = 0;
                }
                else
                {
                    output[x][y] = (Math.abs(heightmap[x][y - 1] - heightmap[x][y]) * 3 +
                                    Math.abs(heightmap[x][y + 1] - heightmap[x][y]) * 3 +
                                    Math.abs(heightmap[x - 1][y] - heightmap[x][y]) * 3 +
                                    Math.abs(heightmap[x + 1][y] - heightmap[x][y]) * 3 +
                                    Math.abs(heightmap[x - 1][y - 1] - heightmap[x][y]) * 2 +
                                    Math.abs(heightmap[x + 1][y - 1] - heightmap[x][y]) * 2 +
                                    Math.abs(heightmap[x - 1][y + 1] - heightmap[x][y]) * 2 +
                                    Math.abs(heightmap[x + 1][y + 1] - heightmap[x][y]) * 2) / 20f;
                }
            }
        }

        return output;
    }
}