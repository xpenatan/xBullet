package bullet.linearmath;

import com.badlogic.gdx.math.Vector3;
import idl.IDLBase;

public class btVector3 extends IDLBase {
    public static btVector3 TMP_1 = new btVector3();
    public static btVector3 TMP_2 = new btVector3();

    public native float getX();
    public native float getY();
    public native float getZ();
    public native void setValue(float x, float y, float z);

    public static void convert(btVector3 in, Vector3 out) {
        float x = in.getX();
        float y = in.getY();
        float z = in.getZ();
        out.set(x, y, z);
    }

    public static void convert(Vector3 in, btVector3 out) {
        out.setValue(in.x, in.y, in.z);
    }
}