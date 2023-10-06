package bullet.linearmath;

import com.badlogic.gdx.math.Matrix4;

public class btDefaultMotionState extends btMotionState {
    private Matrix4 transform;

    public btDefaultMotionState(Matrix4 transform) {
        this.transform = transform;
    }

    @Override
    public void getWorldTransform(Matrix4 worldTrans) {
        worldTrans.set(transform);
    }

    @Override
    public void setWorldTransform(Matrix4 worldTrans) {
        transform.set(worldTrans);
    }
}
