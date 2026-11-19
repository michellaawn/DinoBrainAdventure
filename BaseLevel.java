import greenfoot.*;

/**
 * Induk semua level. Membaca peta dari LevelData, membuat objek-objeknya,
 * dan mengatur alur: kuis Mystery Box, pause, game over, level selesai.
 *
 * Level1 / Level2 / Level3 cukup mewarisi class ini.
 */
public abstract class BaseLevel extends World
{
    protected final int levelNo;
    private final LevelData data;

    private final boolean[][] solid = new boolean[GameConfig.ROWS][GameConfig.COLS];
    private final boolean[][] water = new boolean[GameConfig.ROWS][GameConfig.COLS];
    private int startX, startY;

    private Dino dino;
    private HUD hud;
    private MysteryBox box;
    private Actor quizPanel;                 // QuizPanel (level 1-2) atau MemoryPanel (level 3)
    private DimLayer dimLayer;
    private SettingsPanel settingsPanel;
    private Button resumeBtn, quitBtn;

    private boolean quizOpen = false;
    private boolean boxOpened = false;
    private boolean paused = false;
    private boolean finished = false;
    private boolean gameOver = false;
    private boolean pauseKeyWasDown = false;
    private int frames = 0, seconds = 0, endTimer = 0, gameOverTimer = 0;

    /** Membuat level sesuai nomor (1..3). */
    public static BaseLevel create(int n)
    {
        switch (n)
        {
            case 1:  return new Level1();
            case 2:  return new Level2();
            default: return new Level3();
        }
    }

    public BaseLevel(int levelNo)
    {
        super(GameConfig.WIDTH, GameConfig.HEIGHT, 1, false);   // false = Dino boleh jatuh keluar layar
        this.levelNo = levelNo;
        this.data = LevelData.get(levelNo);

        buildLevel();
        buildUI();

        setPaintOrder(Button.class, SettingsPanel.class, QuizPanel.class, MemoryPanel.class, DimLayer.class, FloatingText.class,
                      HUD.class, Dino.class, MysteryBox.class, Flag.class,
                      Spike.class, Coin.class, Sprite.class);

        prepareExtras();
        refreshHud();
        Audio.startMusic();
    }

    // musik ikut berhenti/lanjut saat tombol Pause/Run Greenfoot ditekan
    public void started() { Audio.startMusic(); }
    public void stopped() { Audio.pauseMusic(); }

    /** Hook: override di Level1/2/3 untuk menambah objek khusus level itu. */
    protected void prepareExtras() { }

    // ------------------------------------------------------------ membangun level
    private void buildLevel()
    {
        GreenfootImage bg = Assets.get(data.background, GameConfig.WIDTH, GameConfig.HEIGHT);
        int t = GameConfig.TILE;

        drawWater(bg);          // air digambar dulu, lalu tanah menimpanya

        for (int r = 0; r < GameConfig.ROWS; r++)
        {
            for (int c = 0; c < GameConfig.COLS; c++)
            {
                char ch = data.map[r].charAt(c);
                int cx = c * t + t / 2;
                int cy = r * t + t / 2;
                int bottom = (r + 1) * t;

                switch (ch)
                {
                    case '#':
                    case 'd':
                        solid[r][c] = true;
                        boolean under = r > 0 && solid[r - 1][c];     // blok di bawah blok -> tanah polos
                        String file = (ch == 'd' || under) ? "dirt.png" : "tile_grass.png";
                        drawTile(bg, file, c, r);
                        break;
                    case '~':
                        water[r][c] = true;      // gambarnya ditangani drawWater()
                        break;
                    case '^': addObject(new Spike(), cx, bottom - Spike.HEIGHT / 2 + 4); break;
                    case 'o': addObject(new Coin(), cx, cy); break;
                    case '?': box = new MysteryBox(); addObject(box, cx, cy); break;
                    case 'F': addObject(new Flag(), cx, bottom - 25); break;
                    case 'T': addObject(new Sprite("pohon.png", 100, 150), cx, bottom - 75); break;
                    case 'S':
                        startX = cx;
                        startY = bottom - Dino.HH;
                        break;
                    default: break;
                }
            }
        }
        setBackground(bg);

        dino = new Dino();
        addObject(dino, startX, startY);
    }

    /** Tanah: alas warna dulu, lalu tile digambar sedikit lebih besar supaya saling menimpa (tanpa celah). */
    private void drawTile(GreenfootImage bg, String file, int c, int r)
    {
        int t = GameConfig.TILE;
        int o = 1;                                      // overlap 1 px tiap sisi
        int topSkip = file.equals("dirt.png") ? 0 : 12;
        bg.setColor(new Color(150, 78, 41));            // warna dasar tanah
        bg.fillRect(c * t, r * t + topSkip, t, t - topSkip);
        bg.drawImage(Assets.get(file, t + o * 2, t + o * 2), c * t - o, r * t - o);
    }

    /** Air: satu area '~' digambar SEKALI sebagai satu gambar utuh, bukan per kotak. */
    private void drawWater(GreenfootImage bg)
    {
        int t = GameConfig.TILE;
        boolean[][] done = new boolean[GameConfig.ROWS][GameConfig.COLS];

        for (int r = 0; r < GameConfig.ROWS; r++)
        {
            for (int c = 0; c < GameConfig.COLS; c++)
            {
                if (data.map[r].charAt(c) != '~' || done[r][c]) continue;

                int w = 0;
                int h = 0;
                while (c + w < GameConfig.COLS && data.map[r].charAt(c + w) == '~') w++;
                while (r + h < GameConfig.ROWS && data.map[r + h].charAt(c) == '~') h++;
                for (int rr = r; rr < r + h; rr++)
                    for (int cc = c; cc < c + w; cc++) done[rr][cc] = true;

                int pad = 4;                            // menyelip di bawah tanah kiri-kanan
                bg.drawImage(Assets.get("air.png", w * t + pad * 2, h * t + pad), c * t - pad, r * t);
            }
        }
    }

    private void buildUI()
    {
        hud = new HUD(levelNo);
        addObject(hud, GameConfig.WIDTH / 2, HUD.HEIGHT / 2);

        addObject(new Button("setting.png", 76, new Runnable() {
            public void run() { setPaused(!paused); }
        }), 752, 28);
    }

    // ------------------------------------------------------------ loop utama
    public void act()
    {
        handlePauseKey();
        if (paused) return;

        if (finished)
        {
            endTimer--;
            if (endTimer <= 0) goNext();
            return;
        }

        if (gameOver)
        {
            gameOverTimer--;
            if (gameOverTimer <= 0) Greenfoot.setWorld(new GameOverWorld(levelNo));
            return;
        }

        if (quizOpen) return;

        frames++;
        if (frames >= 60)
        {
            frames = 0;
            seconds++;
        }
        refreshHud();
    }

    private void handlePauseKey()
    {
        boolean down = Greenfoot.isKeyDown("escape") || Greenfoot.isKeyDown("p");
        if (down && !pauseKeyWasDown && !finished && !gameOver) setPaused(!paused);
        pauseKeyWasDown = down;
    }

    private void refreshHud()
    {
        hud.update(GameState.lives, GameState.coins, seconds, boxOpened);
    }

    // ------------------------------------------------------------ pause
    private void setPaused(boolean p)
    {
        if (p == paused || finished || gameOver) return;
        paused = p;

        if (paused)
        {
            settingsPanel = new SettingsPanel();
            addObject(settingsPanel, GameConfig.WIDTH / 2, GameConfig.HEIGHT / 2);

            resumeBtn = new Button("btn_resume.png", 190, new Runnable() {
                public void run() { setPaused(false); }
            });
            quitBtn = new Button("btn_quit.png", 170, new Runnable() {
                public void run() { Greenfoot.setWorld(new MainMenu()); }
            });
            addObject(resumeBtn, SettingsPanel.PX + 170, SettingsPanel.PY + 290);
            addObject(quitBtn,   SettingsPanel.PX + 390, SettingsPanel.PY + 290);
        }
        else
        {
            removeObject(settingsPanel);
            removeObject(resumeBtn);
            removeObject(quitBtn);
        }
    }

    // ------------------------------------------------------------ kuis
    public void openQuiz(MysteryBox b)
    {
        if (quizOpen || boxOpened || gameOver || finished) return;
        quizOpen = true;
        box = b;
        dimLayer = new DimLayer();
        addObject(dimLayer, GameConfig.WIDTH / 2, GameConfig.HEIGHT / 2);
        if (levelNo == 3) quizPanel = new MemoryPanel();      // level 3 = kartu memori
        else              quizPanel = new QuizPanel(QuizData.get(levelNo));
        addObject(quizPanel, GameConfig.WIDTH / 2, GameConfig.HEIGHT / 2 + 8);
    }

    /** Dipanggil QuizPanel setelah hasil jawaban selesai ditampilkan. */
    public void finishQuiz(boolean correct)
    {
        quizOpen = false;
        boxOpened = true;

        if (quizPanel != null)
        {
            removeObject(quizPanel);
            quizPanel = null;
        }
        if (dimLayer != null)
        {
            removeObject(dimLayer);
            dimLayer = null;
        }

        int bx = GameConfig.WIDTH / 2, by = GameConfig.HEIGHT / 2;
        if (box != null)
        {
            bx = box.getX();
            by = box.getY();
            removeObject(box);
            box = null;
        }

        Audio.play(correct ? "benar.mp3" : "salah.mp3");
        if (correct)
        {
            GameState.score += GameConfig.SCORE_PER_CORRECT;
            GameState.quizCorrect++;
            addObject(new FloatingText("+" + GameConfig.SCORE_PER_CORRECT, 28, UI.GOLD, 70, 1), bx, by);
        }
        else
        {
            loseLife();
            addObject(new FloatingText("-1 nyawa", 24, UI.RED, 70, 1), bx, by);
        }
        refreshHud();
    }

    // ------------------------------------------------------------ event gameplay
    public void collectCoin(Coin c)
    {
        GameState.coins++;
        Audio.play("coin.mp3");
        GameState.score += GameConfig.SCORE_PER_COIN;
        addObject(new FloatingText("+" + GameConfig.SCORE_PER_COIN, 18, UI.GOLD, 40, 1),
                  c.getX(), c.getY() - 10);
        removeObject(c);
        refreshHud();
    }

    public void loseLife()
    {
        if (gameOver) return;
        GameState.lives--;
        if (GameState.lives <= 0)
        {
            GameState.lives = 0;
            gameOver = true;
            gameOverTimer = 80;
            Audio.pauseMusic();
            Audio.play("gameover.mp3");
        }
        refreshHud();
    }

    public void completeLevel()
    {
        if (finished || gameOver) return;
        finished = true;
        endTimer = 110;
        Audio.pauseMusic();
        Audio.play("victory.mp3");
        addObject(new FloatingText("LEVEL SELESAI!", 44, UI.GOLD, 110, 0),
                  GameConfig.WIDTH / 2, GameConfig.HEIGHT / 2 - 20);
    }

    private void goNext()
    {
        GameState.seconds += seconds;
        if (levelNo >= GameConfig.TOTAL_LEVELS)
            Greenfoot.setWorld(new VictoryWorld());
        else
            Greenfoot.setWorld(new LevelIntroWorld(levelNo + 1));
    }

    // ------------------------------------------------------------ getter (dipakai actor lain)
    public boolean isSolid(int col, int row)
    {
        if (col < 0 || col >= GameConfig.COLS) return true;     // dinding kiri & kanan
        if (row < 0 || row >= GameConfig.ROWS) return false;
        return solid[row][col];
    }

    public boolean isWater(int col, int row)
    {
        if (col < 0 || col >= GameConfig.COLS || row < 0 || row >= GameConfig.ROWS) return false;
        return water[row][col];
    }

    /** True kalau gameplay harus berhenti (pause, kuis terbuka, atau level selesai). */
    public boolean isFrozen()    { return paused || quizOpen || finished; }
    public boolean isPaused()    { return paused; }
    public boolean isGameOver()  { return gameOver; }
    public boolean isBoxOpened() { return boxOpened; }
    public Dino getDino()        { return dino; }
    public int getStartX()       { return startX; }
    public int getStartY()       { return startY; }
}