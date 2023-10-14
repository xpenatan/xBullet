package bullet.linearmath;

import com.badlogic.gdx.math.Quaternion;

public class btQuaternion extends btQuadWord {

    public static btQuaternion TMP_1 = new btQuaternion();
    public static btQuaternion TMP_2 = new btQuaternion();
    public static btQuaternion TMP_3 = new btQuaternion();

    public static Quaternion TMP_Q1 = new Quaternion();
    public static Quaternion TMP_Q2 = new Quaternion();
    public static Quaternion TMP_Q3 = new Quaternion();

    public native float getW();
    public native void setValue(float x, float y, float z, float w);

    public static Quaternion c_1(btQuaternion in) {
        return convert(in, TMP_Q1);
    }

    public static Quaternion c_2(btQuaternion in) {
        return convert(in, TMP_Q2);
    }

    public static Quaternion c_3(btQuaternion in) {
        return convert(in, TMP_Q3);
    }

    public static btQuaternion c_1(Quaternion in) {
        return convert(in, TMP_1);
    }

    public static btQuaternion c_2(Quaternion in) {
        return convert(in, TMP_2);
    }

    public static btQuaternion c_3(Quaternion in) {
        return convert(in, TMP_3);
    }

    public static Quaternion convert(btQuaternion in, Quaternion out) {
        float x = in.getX();
        float y = in.getY();
        float z = in.getZ();
        float w = in.getW();
        out.set(x, y, z, w);
        return out;
    }

    public static btQuaternion convert(Quaternion in, btQuaternion out) {
        out.setValue(in.x, in.y, in.z, in.w);
        return out;
    }
}