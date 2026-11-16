import greenfoot.*;

/** Kotak misteri melayang. Disentuh Dino -> membuka kuis. */
public class MysteryBox extends Actor
{
    private static final int SIZE = 44;
    private int baseY = -1;
    private int tick = 0;

    public MysteryBox()
    {
        setImage(Assets.get("MysteryBox.png", SIZE, SIZE));
    }

    public void act()
    {
        BaseLevel level = (BaseLevel) getWorld();
        if (level == null || level.isFrozen()) return;

        if (baseY < 0) baseY = getY();
        tick++;
        setLocation(getX(), baseY + (int) Math.round(3 * Math.sin(tick * 0.09)));   // melayang

        if (level.getDino().overlaps(getX(), baseY, SIZE / 2 - 2, SIZE / 2 - 2))
        {
            level.openQuiz(this);
        }
    }
}
