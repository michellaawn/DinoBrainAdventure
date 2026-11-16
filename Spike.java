import greenfoot.*;

/** Duri. Tabrakannya dicek oleh Dino (hurts), bukan oleh Spike sendiri. */
public class Spike extends Actor
{
    public static final int HEIGHT = 32;

    public Spike()
    {
        setImage(Assets.get("spike.png", GameConfig.TILE, HEIGHT));
    }

    /** Hitbox duri dibuat sedikit lebih kecil dari gambarnya agar adil. */
    public boolean hurts(int cx, int cy, int hw, int hh)
    {
        return Math.abs(cx - getX()) < hw + 18
            && Math.abs(cy - (getY() - 2)) < hh + 12;
    }
}
