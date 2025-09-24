package bullet.gdx;

import bullet.bulletcollision.collisiondispatch.btCollisionWorld;
import bullet.linearmath.btIDebugDraw;
import bullet.linearmath.btVector3;

public abstract class BulletDebugRenderer extends btIDebugDraw {


    @Override
    protected void drawLine(btVector3 vecFrom, btVector3 vecTo, btVector3 color) {

    }

    @Override
    protected void drawContactPoint(btVector3 PointOnB, btVector3 normalOnB, float distance, int lifeTime, btVector3 color) {

    }

    public void debugDrawWorld(btCollisionWorld world) {

    }
}