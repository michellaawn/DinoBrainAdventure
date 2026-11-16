import greenfoot.*;

/** Dino berjalan di tempat (animasi saja), dipakai di layar loading. */
public class DinoWalker extends Actor
{
    private final GreenfootImage[] frames = new GreenfootImage[4];
    private int tick = 0, frame = 0;

    public DinoWalker()
    {
        for (int i = 0; i < 4; i++)
            frames[i] = Assets.get("walk" + (i + 1) + ".png", 70, 70);
        setImage(frames[0]);
    }

    public void act()
    {
        tick++;
        if (tick >= 6)
        {
            tick = 0;
            frame = (frame + 1) % frames.length;
            setImage(frames[frame]);
        }
    }
}
