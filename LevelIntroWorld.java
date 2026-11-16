import greenfoot.*;

/** Layar "LEVEL N" + loading singkat sebelum level dimulai. (Dulu "TransitionLevel1") */
public class LevelIntroWorld extends World
{
    private static final int DURATION = 150;     // frame (~3 detik)

    private final int levelNo;
    private int timer = 0;
    private final LoadingBar bar = new LoadingBar();
    private final DinoWalker walker = new DinoWalker();

    public LevelIntroWorld(int levelNo)
    {
        super(GameConfig.WIDTH, GameConfig.HEIGHT, 1);
        this.levelNo = levelNo;
        LevelData data = LevelData.get(levelNo);

        GreenfootImage bg = Assets.get("pagi.png", GameConfig.WIDTH, GameConfig.HEIGHT);
        UI.dim(bg, 150);
        bg.drawImage(Assets.byWidth("LOGODINO.png", 300), 250, 20);

        UI.shadow(bg, "LEVEL " + (levelNo < 10 ? "0" : "") + levelNo, 400, 205, 46, UI.GOLD, UI.DARK);
        UI.shadow(bg, data.name, 400, 252, 26, Color.WHITE, Color.BLACK);
        UI.bold(bg, data.tip, 400, 288, 17, new Color(230, 230, 230));
        UI.bold(bg, "LOADING...", 400, 378, 16, new Color(255, 220, 120));
        setBackground(bg);

        addObject(bar, 400, 410);
        addObject(walker, 120, 335);
    }

    public void act()
    {
        timer++;
        int p = Math.min(100, timer * 100 / DURATION);
        bar.setProgress(p);
        walker.setLocation(120 + p * 560 / 100, 335);

        String k = Greenfoot.getKey();
        boolean skip = timer > 30 && (k != null || Greenfoot.mouseClicked(null));

        if (timer >= DURATION || skip)
        {
            GameState.beginLevel(levelNo);
            Greenfoot.setWorld(BaseLevel.create(levelNo));
        }
    }
}
