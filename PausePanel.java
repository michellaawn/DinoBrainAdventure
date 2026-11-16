import greenfoot.*;

/** Layar redup + papan "PAUSE". Tombol-tombolnya dipasang oleh BaseLevel. */
public class PausePanel extends Actor
{
    public PausePanel()
    {
        GreenfootImage img = new GreenfootImage(GameConfig.WIDTH, GameConfig.HEIGHT);
        UI.dim(img, 150);
        int w = 320, h = 250;
        int x = (GameConfig.WIDTH - w) / 2, y = (GameConfig.HEIGHT - h) / 2 + 10;
        UI.pixelBox(img, x, y, w, h, UI.DARK, UI.TAN, UI.PARCHMENT, UI.CLEAR);
        UI.shadow(img, "PAUSE", GameConfig.WIDTH / 2, y + 38, 34, UI.BROWN, UI.TAN);
        setImage(img);
    }
}
