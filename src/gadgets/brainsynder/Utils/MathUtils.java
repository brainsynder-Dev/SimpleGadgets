package gadgets.brainsynder.Utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.Random;

public class MathUtils {
    public static final float nanoToSec = 1.0E-9F;
    public static final float FLOAT_ROUNDING_ERROR = 1.0E-6F;
    public static final float PI = 3.1415927F;
    public static final float PI2 = 6.2831855F;
    public static final float SQRT_3 = 1.7320508F;
    public static final float E = 2.7182817F;
    private static final int SIN_BITS = 14;
    private static final int SIN_MASK = 16383;
    private static final int SIN_COUNT = 16384;
    private static final float radFull = 6.2831855F;
    private static final float degFull = 360.0F;
    private static final float radToIndex = 2607.5945F;
    private static final float degToIndex = 45.511112F;
    public static final float radiansToDegrees = 57.295776F;
    public static final float radDeg = 57.295776F;
    public static final float degreesToRadians = 0.017453292F;
    public static final float degRad = 0.017453292F;
    private static final int ATAN2_BITS = 7;
    private static final int ATAN2_BITS2 = 14;
    private static final int ATAN2_MASK = 16383;
    private static final int ATAN2_COUNT = 16384;
    static final int ATAN2_DIM = (int)Math.sqrt(16384.0D);
    private static final float INV_ATAN2_DIM_MINUS_1;
    public static Random random;
    private static final int BIG_ENOUGH_INT = 16384;
    private static final double BIG_ENOUGH_FLOOR = 16384.0D;
    private static final double CEIL = 0.9999999D;
    private static final double BIG_ENOUGH_CEIL = 16384.999999999996D;
    private static final double BIG_ENOUGH_ROUND = 16384.5D;

    public MathUtils() {
    }

    public static final float sin(float radians) {
        return MathUtils.Sin.table[(int)(radians * 2607.5945F) & 16383];
    }

    public static final float cos(float radians) {
        return MathUtils.Sin.table[(int)((radians + 1.5707964F) * 2607.5945F) & 16383];
    }

    public static final float sinDeg(float degrees) {
        return MathUtils.Sin.table[(int)(degrees * 45.511112F) & 16383];
    }

    public static final float cosDeg(float degrees) {
        return MathUtils.Sin.table[(int)((degrees + 90.0F) * 45.511112F) & 16383];
    }

    public static boolean isInteger(Object object) {
        try {
            Integer.parseInt(object.toString());
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static boolean isDouble(Object object) {
        try {
            Double.parseDouble(object.toString());
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static final float atan2(float y, float x) {
        float add;
        float mul;
        if(x < 0.0F) {
            if(y < 0.0F) {
                y = -y;
                mul = 1.0F;
            } else {
                mul = -1.0F;
            }

            x = -x;
            add = -3.1415927F;
        } else {
            if(y < 0.0F) {
                y = -y;
                mul = -1.0F;
            } else {
                mul = 1.0F;
            }

            add = 0.0F;
        }

        float invDiv = 1.0F / ((x < y?y:x) * INV_ATAN2_DIM_MINUS_1);
        if(invDiv == 1.0F / 0.0) {
            return ((float)Math.atan2((double)y, (double)x) + add) * mul;
        } else {
            int xi = (int)(x * invDiv);
            int yi = (int)(y * invDiv);
            return (MathUtils.Atan2.table[yi * ATAN2_DIM + xi] + add) * mul;
        }
    }

    public static final int random(int range) {
        return random.nextInt(range + 1);
    }

    public static final int random(int start, int end) {
        return start + random.nextInt(end - start + 1);
    }

    public static final boolean randomBoolean() {
        return random.nextBoolean();
    }

    public static final boolean randomBoolean(float chance) {
        return random() < chance;
    }

    public static final float random() {
        return random.nextFloat();
    }

    public static final float random(float range) {
        return random.nextFloat() * range;
    }

    public static final float random(float start, float end) {
        return start + random.nextFloat() * (end - start);
    }

    public static int nextPowerOfTwo(int value) {
        if(value == 0) {
            return 1;
        } else {
            --value;
            value |= value >> 1;
            value |= value >> 2;
            value |= value >> 4;
            value |= value >> 8;
            value |= value >> 16;
            return value + 1;
        }
    }

    public static boolean isPowerOfTwo(int value) {
        return value != 0 && (value & value - 1) == 0;
    }

    public static int clamp(int value, int min, int max) {
        return value < min?min:(value > max?max:value);
    }

    public static short clamp(short value, short min, short max) {
        return value < min?min:(value > max?max:value);
    }

    public static float clamp(float value, float min, float max) {
        return value < min?min:(value > max?max:value);
    }

    public static int floor(float x) {
        return (int)((double)x + 16384.0D) - 16384;
    }

    public static int floorPositive(float x) {
        return (int)x;
    }

    public static int ceil(float x) {
        return (int)((double)x + 16384.999999999996D) - 16384;
    }

    public static int ceilPositive(float x) {
        return (int)((double)x + 0.9999999D);
    }

    public static int round(float x) {
        return (int)((double)x + 16384.5D) - 16384;
    }

    public static int roundPositive(float x) {
        return (int)(x + 0.5F);
    }

    public static boolean isZero(float value) {
        return Math.abs(value) <= 1.0E-6F;
    }

    public static boolean isZero(float value, float tolerance) {
        return Math.abs(value) <= tolerance;
    }

    public static boolean isEqual(float a, float b) {
        return Math.abs(a - b) <= 1.0E-6F;
    }

    public static boolean isEqual(float a, float b, float tolerance) {
        return Math.abs(a - b) <= tolerance;
    }

    public static final Vector rotateAroundAxisX(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double y = v.getY() * cos - v.getZ() * sin;
        double z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    public static final Vector rotateAroundAxisY(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    public static final Vector rotateAroundAxisZ(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos - v.getY() * sin;
        double y = v.getX() * sin + v.getY() * cos;
        return v.setX(x).setY(y);
    }

    public static final Vector rotateVector(Vector v, double angleX, double angleY, double angleZ) {
        rotateAroundAxisX(v, angleX);
        rotateAroundAxisY(v, angleY);
        rotateAroundAxisZ(v, angleZ);
        return v;
    }

    public static final double angleToXAxis(Vector vector) {
        return Math.atan2(vector.getX(), vector.getY());
    }

    public static Vector getRandomVector() {
        double x = random.nextDouble() * 2.0D - 1.0D;
        double y = random.nextDouble() * 2.0D - 1.0D;
        double z = random.nextDouble() * 2.0D - 1.0D;
        return (new Vector(x, y, z)).normalize();
    }

    public static void applyVelocity(final Entity ent, Vector v) {
        if(!ent.hasMetadata("NPC")) {
            ent.setVelocity(v);
        }
    }

    public static Vector getRandomCircleVector() {
        double rnd = random.nextDouble() * 2.0D * 3.141592653589793D;
        double x = Math.cos(rnd);
        double z = Math.sin(rnd);
        return new Vector(x, 0.0D, z);
    }

    public static Material getRandomMaterial(Material[] materials) {
        return materials[random.nextInt(materials.length)];
    }

    public static double getRandomAngle() {
        return random.nextDouble() * 2.0D * 3.141592653589793D;
    }

    public static double randomDouble(double min, double max) {
        return Math.random() < 0.5D?(1.0D - Math.random()) * (max - min) + min:Math.random() * (max - min) + min;
    }

    public static float randomRangeFloat(float min, float max) {
        return (float)(Math.random() < 0.5D?(1.0D - Math.random()) * (double)(max - min) + (double)min:Math.random() * (double)(max - min) + (double)min);
    }

    public static byte randomByte(int max) {
        return (byte)random.nextInt(max + 1);
    }

    public static int randomRangeInt(int min, int max) {
        return (int)(Math.random() < 0.5D?(1.0D - Math.random()) * (double)(max - min) + (double)min:Math.random() * (double)(max - min) + (double)min);
    }

    public static double offset(Entity a, Entity b) {
        return offset((Vector)a.getLocation().toVector(), (Vector)b.getLocation().toVector());
    }

    public static double offset(Location a, Location b) {
        return offset((Vector)a.toVector(), (Vector)b.toVector());
    }

    public static double offset(Vector a, Vector b) {
        return a.subtract(b).length();
    }

    static {
        INV_ATAN2_DIM_MINUS_1 = 1.0F / (float)(ATAN2_DIM - 1);
        random = new Random();
    }

    private static class Atan2 {
        static final float[] table = new float[16384];

        private Atan2() {
        }

        static {
            for(int i = 0; i < MathUtils.ATAN2_DIM; ++i) {
                for(int j = 0; j < MathUtils.ATAN2_DIM; ++j) {
                    float x0 = (float)i / (float)MathUtils.ATAN2_DIM;
                    float y0 = (float)j / (float)MathUtils.ATAN2_DIM;
                    table[j * MathUtils.ATAN2_DIM + i] = (float)Math.atan2((double)y0, (double)x0);
                }
            }

        }
    }

    private static class Sin {
        static final float[] table = new float[16384];

        private Sin() {
        }

        static {
            int i;
            for(i = 0; i < 16384; ++i) {
                table[i] = (float)Math.sin((double)(((float)i + 0.5F) / 16384.0F * 6.2831855F));
            }

            for(i = 0; i < 360; i += 90) {
                table[(int)((float)i * 45.511112F) & 16383] = (float)Math.sin((double)((float)i * 0.017453292F));
            }

        }
    }
}
