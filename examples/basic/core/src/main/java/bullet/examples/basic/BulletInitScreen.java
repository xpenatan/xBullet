package bullet.examples.basic;

import bullet.BulletLoader;
import com.badlogic.gdx.ScreenAdapter;

public class BulletInitScreen extends ScreenAdapter {

    private BulletGame game;

    private boolean bulletInit = false;

    public BulletInitScreen(BulletGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        BulletLoader.init((bulletSuccess, e2) -> bulletInit = bulletSuccess);
    }

    @Override
    public void render(float delta) {
        if(bulletInit) {
            bulletInit = false;
            game.setScreen(new BulletTestScreen());
        }
    }
}
