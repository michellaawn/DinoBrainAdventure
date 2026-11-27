import greenfoot.*;

/** Layar "LEVEL N" + loading singkat sebelum level dimulai. (Dulu "TransitionLevel1") */
public class LevelIntroWorld extends World
{
    private static final int DURATION = 150;     // frame (~3 detik)

    // ---- font layar transisi ----
    private static final String  FONT_NAME = "Courier New";   // <-- jenis font di sini
    private static final boolean FONT_BOLD = true;

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

        drawText(bg, "LEVEL " + (levelNo < 10 ? "0" : "") + levelNo, 400, 175, 46, UI.GOLD, UI.DARK);
        drawText(bg, data.name, 400, 222, 26, Color.WHITE, Color.BLACK);
        drawText(bg, data.tip, 400, 258, 17, new Color(230, 230, 230), null);
        drawText(bg, "LOADING...", 400, 285, 16, new Color(255, 220, 120), null);
        setBackground(bg);

        addObject(bar, 400, 410);
        addObject(walker, 120, 360);
    }


    private void drawText(GreenfootImage g, String s, int cx, int cy, int size, Color c, Color shade)
    {
        g.setFont(new Font(FONT_NAME, FONT_BOLD, false, size));
        int x = cx - (int) (s.length() * size * 0.6) / 2;    
        int y = cy + size / 3;                               
        if (shade != null)
        {
            g.setColor(shade);
            g.drawString(s, x + 2, y + 2);
        }
        g.setColor(c);
        g.drawString(s, x, y);
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