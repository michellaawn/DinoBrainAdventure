import greenfoot.*;

/** Teks yang melayang naik lalu memudar ("+10", "Buka Mystery Box dulu!", dst). */
public class FloatingText extends Actor
{
    private int life;
    private final int maxLife;
    private final int rise;

    public FloatingText(String text, int size, Color color)
    {
        this(text, size, color, 60, 1);
    }

    public FloatingText(String text, int size, Color color, int frames, int risePerFrame)
    {
        maxLife = frames;
        life = frames;
        rise = risePerFrame;

        int w = UI.textWidth(text, size) + 12;
        GreenfootImage img = new GreenfootImage(w, size + 14);
        UI.shadow(img, text, w / 2, (size + 14) / 2, size, color, new Color(0, 0, 0, 170));
        setImage(img);
    }

    public void act()
    {
        setLocation(getX(), getY() - rise);
        life--;
        if (life < maxLife / 3)
        {
            getImage().setTransparency(Math.max(0, 255 * life / (maxLife / 3)));
        }
        if (life <= 0 && getWorld() != null)
        {
            getWorld().removeObject(this);
        }
    }
}
