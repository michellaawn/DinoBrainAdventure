import greenfoot.*;

/**
 * Panel info di atas layar: nyawa, koin, level + status Mystery Box, waktu.
 * Hanya digambar ulang kalau datanya berubah (hemat performa).
 */
public class HUD extends Actor
{
    public static final int HEIGHT = 56;

    private final int levelNo;
    private int lives = -1, coins = -1, seconds = -1;
    private boolean boxOpened = false;

    private final GreenfootImage dinoIcon  = Assets.get("iconpanel.png", 56, 40);
    private final GreenfootImage pill      = Assets.get("navbar.png", 132, 40);
    private final GreenfootImage pillWide  = Assets.get("navbar.png", 170, 40);
    private final GreenfootImage heart     = Assets.get("love.png", 28, 25);
    private final GreenfootImage heartLost = Assets.get("love.png", 28, 25);
    private final GreenfootImage coinIcon  = Assets.get("coin.png", 18, 30);
    private final GreenfootImage clockIcon = Assets.get("timee2.png", 28, 31);
    private final GreenfootImage boxIcon   = Assets.get("qbox.png", 28, 28);
    private final GreenfootImage boxDone   = Assets.get("qbox.png", 28, 28);

    public HUD(int levelNo)
    {
        this.levelNo = levelNo;
        heartLost.setTransparency(60);
        boxDone.setTransparency(80);
        setImage(new GreenfootImage(GameConfig.WIDTH, HEIGHT));
        redraw();
    }

    public void update(int lives, int coins, int seconds, boolean boxOpened)
    {
        if (lives == this.lives && coins == this.coins
            && seconds == this.seconds && boxOpened == this.boxOpened) return;
        this.lives = lives;
        this.coins = coins;
        this.seconds = seconds;
        this.boxOpened = boxOpened;
        redraw();
    }

    private void redraw()
    {
        GreenfootImage img = getImage();
        img.clear();

        // kiri: ikon + nyawa
        img.drawImage(dinoIcon, 8, 8);
        img.drawImage(pill, 70, 8);
        for (int i = 0; i < GameConfig.START_LIVES; i++)
            img.drawImage(i < lives ? heart : heartLost, 82 + i * 38, 14);

        // koin
        img.drawImage(pill, 210, 8);
        img.drawImage(coinIcon, 224, 13);
        UI.bold(img, "x " + coins, 290, 28, 20, Color.WHITE);

        // tengah: level + status Mystery Box
        img.drawImage(pillWide, 350, 8);
        UI.bold(img, "LEVEL " + levelNo, 424, 28, 20, UI.GOLD);
        img.drawImage(boxOpened ? boxDone : boxIcon, 350 + 170 - 38, 14);

        // waktu
        img.drawImage(pill, 540, 8);
        img.drawImage(clockIcon, 550, 10);
        UI.bold(img, GameState.formatTime(seconds), 614, 28, 20, Color.WHITE);
    }
}
