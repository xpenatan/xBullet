package bullet.linearmath;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

public class DebugDrawer extends btIDebugDraw {

    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private SpriteBatch spriteBatch;
    private BitmapFont font;

    private boolean ownsShapeRenderer = true, ownsSpriteBatch = true, ownsFont = true;

    private Camera camera;
    private Viewport viewport;
    private int debugMode = 0;

    private Vector3 from = new Vector3();
    private Vector3 to = new Vector3();
    private Vector3 col = new Vector3();

    @Override
    protected void drawLine(btVector3 vecFrom, btVector3 vecTo, btVector3 color) {
        btVector3.convert(vecFrom, from);
        btVector3.convert(vecTo, to);
        btVector3.convert(color, col);
        drawLine(from, to, col);
    }

    @Override
    protected void drawContactPoint(btVector3 PointOnB, btVector3 normalOnB, float distance, int lifeTime, btVector3 color) {
        btVector3.convert(PointOnB, from);
        btVector3.convert(normalOnB, to);
        btVector3.convert(color, col);
        drawContactPoint(from, to, distance, lifeTime, col);
    }

    public void drawLine(Vector3 from, Vector3 to, Vector3 color) {
        shapeRenderer.setColor(color.x, color.y, color.z, 1f);
        shapeRenderer.line(from, to);
    }

    public void drawContactPoint(Vector3 pointOnB, Vector3 normalOnB, float distance, int lifeTime, Vector3 color) {
        shapeRenderer.setColor(color.x, color.y, color.z, 1f);
        shapeRenderer.point(pointOnB.x, pointOnB.y, pointOnB.z);

        shapeRenderer.line(pointOnB, normalOnB.scl(distance).add(pointOnB));
    }

    public void reportErrorWarning(String warningString) {
        Gdx.app.error("Bullet", warningString);
    }

    public void draw3dText(Vector3 location, String textString) {
        if(spriteBatch == null) {
            spriteBatch = new SpriteBatch();
        }
        if(font == null) {
            font = new BitmapFont();
        }

        // this check is necessary to avoid "mirrored" instances of the text
        if(camera.frustum.pointInFrustum(location)) {
            if(viewport != null) {
                camera.project(location, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(),
                        viewport.getScreenHeight());
            }
            else {
                camera.project(location);
            }

            shapeRenderer.end();
            spriteBatch.begin();

            // the text will be centered on the position
            font.draw(spriteBatch, textString, location.x, location.y, 0, textString.length(), 0, Align.center, false);

            spriteBatch.end();
            shapeRenderer.begin(ShapeType.Line);
        }
    }

    public void setDebugMode(int debugMode) {
        this.debugMode = debugMode;
    }

    @Override
    public int getDebugMode() {
        return debugMode;
    }

    /**
     * Use this in case no {@code glViewport} is in use. Otherwise please supply the used {@link Viewport} to
     * {@link #begin(Viewport)}.
     *
     * @param camera The (perspective) camera to be used when doing the debug rendering.
     */
    public void begin(Camera camera) {
        this.camera = camera;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Line);
    }

    /**
     * This has to be used in case the camera to be used is managed via a {@link Viewport}.
     *
     * @param viewport The currently used viewport with its managed (perspective) camera.
     */
    public void begin(Viewport viewport) {
        this.viewport = viewport;
        begin(viewport.getCamera());
    }

    /**
     * Ends the debug rendering process and leads to a flush.
     */
    public void end() {
        shapeRenderer.end();
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    /**
     * Switches the {@link ShapeRenderer}. The given shape renderer won't be disposed when {@link #dispose()} is called.
     */
    public void setShapeRenderer(ShapeRenderer shapeRenderer) {
        if(ownsShapeRenderer) {
            this.shapeRenderer.dispose();
        }
        this.shapeRenderer = shapeRenderer;
        ownsShapeRenderer = false;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    /**
     * Switches the {@link SpriteBatch}. The given sprite batch won't be disposed when {@link #dispose()} is called.
     */
    public void setSpriteBatch(SpriteBatch spriteBatch) {
        if(ownsSpriteBatch && this.spriteBatch != null) {
            this.spriteBatch.dispose();
        }
        this.spriteBatch = spriteBatch;
        ownsSpriteBatch = false;
    }

    public BitmapFont getFont() {
        return font;
    }

    /**
     * Switches the {@link BitmapFont}. The given font won't be disposed when {@link #dispose()} is called.
     */
    public void setFont(BitmapFont font) {
        if(ownsFont && this.font != null) {
            this.font.dispose();
        }
        this.font = font;
        ownsFont = false;
    }

    @Override
    public void dispose() {
        super.dispose();
        if(ownsShapeRenderer) {
            shapeRenderer.dispose();
        }
        if(ownsSpriteBatch && spriteBatch != null) {
            spriteBatch.dispose();
        }
        if(ownsFont && font != null) {
            font.dispose();
        }
    }

}