package bullet.gdx;

import bullet.idl.helper.IDLFloatArray;
import bullet.linearmath.btMatrix3x3;
import bullet.linearmath.btQuaternion;
import bullet.linearmath.btTransform;
import bullet.linearmath.btVector3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class btGdx {
    private btGdx() {};


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

    private static IDLFloatArray tmp = new IDLFloatArray(16);

    public static btTransform convert(Matrix4 in, btTransform out) {
        // TODO improve performance
        for(int i = 0; i < in.val.length; i++) {
            float v = in.val[i];
            tmp.setValue(i, v);
        }
        out.setFromOpenGLMatrix(tmp);
        return out;
    }

    public static Matrix4 convert(btTransform in, Matrix4 out) {
        btVector3 origin = in.getOrigin();
        btMatrix3x3 matrix3x3 = in.getBasis();
        btVector3 row0 = matrix3x3.getRow(0);
        float row0X = row0.getX();
        float row0Y = row0.getY();
        float row0Z = row0.getZ();
        btVector3 row1 = matrix3x3.getRow(1);
        float row1X = row1.getX();
        float row1Y = row1.getY();
        float row1Z = row1.getZ();
        btVector3 row2 = matrix3x3.getRow(2);
        float row2X = row2.getX();
        float row2Y = row2.getY();
        float row2Z = row2.getZ();
        out.val[0] = row0X;
        out.val[1] = row1X;
        out.val[2] = row2X;
        out.val[3] = 0;
        out.val[4] = row0Y;
        out.val[5] = row1Y;
        out.val[6] = row2Y;
        out.val[7] = 0;
        out.val[8] = row0Z;
        out.val[9] = row1Z;
        out.val[10] = row2Z;
        out.val[11] = 0;
        out.val[12] = origin.getX();
        out.val[13] = origin.getY();
        out.val[14] = origin.getZ();
        out.val[15] = 1.0f;
        return out;
    }
}