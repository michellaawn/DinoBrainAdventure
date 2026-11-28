import greenfoot.*;

/** Bendera finish. Pudar/terkunci sampai Mystery Box di level itu dijawab. */
public class Flag extends Actor
{
    private final GreenfootImage active;
    private final GreenfootImage locked;
    private boolean lockedNow = true;
    private int cooldown = 0;

    public Flag()
    {
        active = Assets.get("checkpoint.png", 42, 50);
        locked = Assets.get("checkpoint.png", 42, 50);
        locked.setTransparency(110);
        setImage(locked);
    }

    public void act()
    {
        BaseLevel level = (BaseLevel) getWorld();
        if (level == null || level.isFrozen()) return;
        boolean shouldLock = !level.isBoxOpened();
        if (shouldLock != lockedNow)
        {
            lockedNow = shouldLock;
            setImage(lockedNow ? locked : active);
        }

        if (cooldown > 0) cooldown--;
        if (level.getDino().overlaps(getX(), getY(), 14, 24))
        {
            if (level.isBoxOpened()){
                level.completeLevel();}
            else if (cooldown == 0)
            {
                cooldown = 100;
                level.addObject(new FloatingText("Buka Mystery Box dulu!", 20, UI.GOLD, 80, 1),
                                getX(), getY() - 45);
            }
        }
    }
}
