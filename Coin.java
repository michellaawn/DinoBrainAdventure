import greenfoot.*;

/** Koin berputar. +skor dan +1 koin saat disentuh Dino. */
public class Coin extends Actor
{
    private static final int[] WIDTHS = { 22, 16, 8, 16 };   // efek berputar
    private final GreenfootImage[] frames = new GreenfootImage[WIDTHS.length];
    private int tick = 0;
    private int frame = 0;

    public Coin()
    {
        for (int i = 0; i < WIDTHS.length; i++)
            frames[i] = Assets.get("coin.png", WIDTHS[i], 34);
        setImage(frames[0]);
        tick = (int) (Math.random() * 20);   // supaya koin tidak berputar serempak
    }

    public void act()
    {
        BaseLevel level = (BaseLevel) getWorld();
        if (level == null || level.isFrozen()) return;

        tick++;
        if (tick % 7 == 0)
        {
            frame = (frame + 1) % frames.length;
            setImage(frames[frame]);
        }

        if (level.getDino().overlaps(getX(), getY(), 10, 16))
        {
            level.collectCoin(this);
        }
    }
}
