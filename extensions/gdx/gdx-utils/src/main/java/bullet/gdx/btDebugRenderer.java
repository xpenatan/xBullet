package bullet.gdx;

import bullet.linearmath.btIDebugDraw;

public abstract class btDebugRenderer extends btIDebugDraw {

    private int debugMode;

    @Override
    public void setDebugMode(int debugMode) {
        this.debugMode = debugMode;
    }

    @Override
    public int getDebugMode() {
        return debugMode;
    }
}