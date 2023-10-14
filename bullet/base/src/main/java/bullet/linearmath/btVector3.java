package bullet.linearmath;

import com.badlogic.gdx.math.Vector3;
import idl.IDLBase;

public class btVector3 extends IDLBase {
    public static btVector3 TMP_1 = new btVector3();
    public static btVector3 TMP_2 = new btVector3();
    public static btVector3 TMP_3 = new btVector3();

    public static Vector3 TMP_V1 = new Vector3();
    public static Vector3 TMP_V2 = new Vector3();
    public static Vector3 TMP_V3 = new Vector3();

    public native float getX();
    public native float getY();
    public native float getZ();
    public native void setValue(float x, float y, float z);

    public btVector3() {}

    public btVector3(byte b) {}

    public static Vector3 c_1(btVector3 in) {
        return convert(in, TMP_V1);
    }

    public static Vector3 c_2(btVector3 in) {
        return convert(in, TMP_V2);
    }

    public static Vector3 c_3(btVector3 in) {
        return convert(in, TMP_V3);
    }

    public static btVector3 c_1(Vector3 in) {
        return convert(in, TMP_1);
    }

    public static btVector3 c_2(Vector3 in) {
        return convert(in, TMP_2);
    }

    public static btVector3 c_3(Vector3 in) {
        return convert(in, TMP_3);
    }

    public static Vector3 convert(btVector3 in, Vector3 out) {
        float x = in.getX();
        float y = in.getY();
        float z = in.getZ();
        out.set(x, y, z);
        return out;
    }

    public static btVector3 convert(Vector3 in, btVector3 out) {
        out.setValue(in.x, in.y, in.z);
        return out;
    }
}