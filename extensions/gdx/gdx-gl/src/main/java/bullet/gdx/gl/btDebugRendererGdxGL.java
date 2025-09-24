package bullet.gdx.gl;

import bullet.bulletcollision.collisiondispatch.btCollisionWorld;
import bullet.gdx.btDebugRenderer;
import bullet.gdx.btGdx;
import bullet.linearmath.btVector3;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class btDebugRendererGdxGL extends btDebugRenderer {

    private ShapeRenderer renderer = new ShapeRenderer();
    private Batch spriteBatch = new SpriteBatch();
    private BitmapFont font = new BitmapFont();

    private Vector3 from = new Vector3();
    private Vector3 to = new Vector3();
    private Vector3 col = new Vector3();

    @Override
    protected void drawLine(btVector3 vecFrom, btVector3 vecTo, btVector3 color) {
        btGdx.convert(vecFrom, from);
        btGdx.convert(vecTo, to);
        btGdx.convert(color, col);
        drawLine(from, to, col);
    }

    @Override
    protected void drawContactPoint(btVector3 PointOnB, btVector3 normalOnB, float distance, int lifeTime, btVector3 color) {
        btGdx.convert(PointOnB, from);
        btGdx.convert(normalOnB, to);
        btGdx.convert(color, col);
        drawContactPoint(from, to, distance, lifeTime, col);
    }

    public void debugDrawWorld(Camera camera, btCollisionWorld world) {
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        world.debugDrawWorld();
        renderer.end();
    }

    private void drawLine(Vector3 from, Vector3 to, Vector3 color) {
        renderer.setColor(color.x, color.y, color.z, 1f);
        renderer.line(from, to);
    }

    private void drawContactPoint(Vector3 pointOnB, Vector3 normalOnB, float distance, int lifeTime, Vector3 color) {
        renderer.setColor(color.x, color.y, color.z, 1f);
        renderer.point(pointOnB.x, pointOnB.y, pointOnB.z);
        renderer.line(pointOnB, normalOnB.scl(distance).add(pointOnB));
    }

    @Override
    protected void onNativeDispose() {
        spriteBatch.dispose();
        renderer.dispose();
        font.dispose();
    }
}