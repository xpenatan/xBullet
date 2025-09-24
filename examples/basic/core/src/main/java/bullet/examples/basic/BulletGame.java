package bullet.examples.basic;

import com.badlogic.gdx.Game;

public class BulletGame extends Game {

    @Override
    public void create() {
        setScreen(new BulletInitScreen(this));
    }
}
