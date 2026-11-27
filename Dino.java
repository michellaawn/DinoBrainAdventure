import greenfoot.*;

/**
 * Pemain. Gerakan memakai tabrakan berbasis grid (BaseLevel.isSolid), jadi
 * tidak tembus lantai dan tidak "kependam" di tanah.
 *
 * Kontrol: kiri/kanan atau A/D, lompat: spasi / panah atas / W.
 */
public class Dino extends Actor
{
    // Hitbox (setengah lebar & setengah tinggi, dari titik tengah Dino)
    public static final int HW = 13;
    public static final int HH = 22;

    private static final int SPRITE = 56;                       // ukuran gambar Dino
    private static final int CANVAS_H = SPRITE * 2 - HH * 2;    // supaya kaki pas di dasar hitbox

    private GreenfootImage[] walkR = new GreenfootImage[4];
    private GreenfootImage[] walkL = new GreenfootImage[4];
    private GreenfootImage jumpR, jumpL, fallR, fallL, deadR, deadL;
    private GreenfootImage current;

    private int vy = 0;
    private boolean onGround = false;
    private boolean facingLeft = false;
    private boolean dead = false;
    private int deadTimer = 0;
    private int walkFrame = 0;
    private int walkTick = 0;

    public Dino()
    {
        for (int i = 0; i < 4; i++)
        {
            walkR[i] = frame("walk" + (i + 1) + ".png", false);
            walkL[i] = frame("walk" + (i + 1) + ".png", true);
        }
        jumpR = frame("jump1.png", false);  jumpL = frame("jump1.png", true);
        fallR = frame("jump3.png", false);  fallL = frame("jump3.png", true);
        deadR = frame("dead3.png", false);  deadL = frame("dead3.png", true);
        show(walkR[0]);
    }

    private GreenfootImage frame(String file, boolean mirror)
    {
        GreenfootImage canvas = new GreenfootImage(SPRITE, CANVAS_H);
        canvas.drawImage(Assets.get(file, SPRITE, SPRITE), 0, 0);
        if (mirror) canvas.mirrorHorizontally();
        return canvas;
    }

    private void show(GreenfootImage img)
    {
        if (img != current)
        {
            current = img;
            setImage(img);
        }
    }

    // ------------------------------------------------------------ loop
    public void act()
    {
        BaseLevel level = (BaseLevel) getWorld();
        if (level == null || level.isFrozen()) return;

        if (dead)
        {
            updateDead(level);
            return;
        }
        int dx = 0;
        if (Greenfoot.isKeyDown("left")  || Greenfoot.isKeyDown("a")) { dx -= GameConfig.MOVE_SPEED; facingLeft = true;  }
        if (Greenfoot.isKeyDown("right") || Greenfoot.isKeyDown("d")) { dx += GameConfig.MOVE_SPEED; facingLeft = false; }
        boolean jumpKey = Greenfoot.isKeyDown("space") || Greenfoot.isKeyDown("up") || Greenfoot.isKeyDown("w");

        onGround = blocked(level, getX(), getY() + 1);
        if (onGround && jumpKey)
        {
            vy = -GameConfig.JUMP_POWER;
            onGround = false;
        }
        moveX(level, dx);
        vy = Math.min(vy + GameConfig.GRAVITY, GameConfig.MAX_FALL_SPEED);
        moveY(level, vy);
        onGround = blocked(level, getX(), getY() + 1);

        checkHazards(level);
        if (!dead) animate(dx != 0);
    }

    // ------------------------------------------------------------ gerak
    private void moveX(BaseLevel level, int dx)
    {
        int step = dx > 0 ? 1 : -1;
        for (int i = 0; i < Math.abs(dx); i++)
        {
            if (blocked(level, getX() + step, getY())) break;
            setLocation(getX() + step, getY());
        }
    }

    private void moveY(BaseLevel level, int dy)
    {
        int step = dy > 0 ? 1 : -1;
        for (int i = 0; i < Math.abs(dy); i++)
        {
            if (blocked(level, getX(), getY() + step))
            {
                vy = 0;                 // mendarat atau kepala membentur blok
                break;
            }
            setLocation(getX(), getY() + step);
        }
    }

    private boolean blocked(BaseLevel level, int cx, int cy)
    {
        int t = GameConfig.TILE;
        int c0 = Math.floorDiv(cx - HW, t);
        int c1 = Math.floorDiv(cx + HW - 1, t);
        int r0 = Math.floorDiv(cy - HH, t);
        int r1 = Math.floorDiv(cy + HH - 1, t);
        for (int r = r0; r <= r1; r++)
            for (int c = c0; c <= c1; c++)
                if (level.isSolid(c, r)) return true;
        return false;
    }

    // ------------------------------------------------------------ bahaya
    private void checkHazards(BaseLevel level)
    {
        if (level.isGameOver()) return;
        int t = GameConfig.TILE;

        // air
        int c0 = Math.floorDiv(getX() - HW, t);
        int c1 = Math.floorDiv(getX() + HW - 1, t);
        int r0 = Math.floorDiv(getY() - HH, t);
        int r1 = Math.floorDiv(getY() + HH - 1, t);
        for (int r = r0; r <= r1; r++)
            for (int c = c0; c <= c1; c++)
                if (level.isWater(c, r)) { die(level); return; }

        // duri
        for (Object o : level.getObjects(Spike.class))
        {
            if (((Spike) o).hurts(getX(), getY(), HW, HH)) { die(level); return; }
        }

        // jatuh keluar layar
        if (getY() - HH > GameConfig.HEIGHT) die(level);
    }

    private void die(BaseLevel level)
    {
        dead = true;
        deadTimer = 60;
        vy = -11;
        show(facingLeft ? deadL : deadR);
        level.loseLife();
    }

    private void updateDead(BaseLevel level)
    {
        setLocation(getX(), getY() + vy);
        vy = Math.min(vy + 1, 14);
        deadTimer--;

        if (deadTimer <= 0 && !level.isGameOver())
        {
            setLocation(level.getStartX(), level.getStartY());   // respawn
            vy = 0;
            dead = false;
            show(walkR[0]);
        }
    }

    // ------------------------------------------------------------ animasi
    private void animate(boolean moving)
    {
        if (!onGround)
        {
            if (vy < 0) show(facingLeft ? jumpL : jumpR);
            else        show(facingLeft ? fallL : fallR);
        }
        else if (moving)
        {
            walkTick++;
            if (walkTick >= 5)
            {
                walkTick = 0;
                walkFrame = (walkFrame + 1) % 4;
            }
            show(facingLeft ? walkL[walkFrame] : walkR[walkFrame]);
        }
        else
        {
            walkFrame = 0;
            walkTick = 0;
            show(facingLeft ? walkL[0] : walkR[0]);
        }
    }

    // ------------------------------------------------------------ util
    /** Apakah hitbox Dino beririsan dengan kotak (cx, cy, setengah lebar, setengah tinggi)? */
    public boolean overlaps(int cx, int cy, int hw, int hh)
    {
        return !dead
            && Math.abs(getX() - cx) < HW + hw
            && Math.abs(getY() - cy) < HH + hh;
    }
}
