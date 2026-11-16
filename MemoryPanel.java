import greenfoot.*;

/**
 * Jendela Mystery Box untuk Level 3: permainan kartu memori.
 * Klik dua kartu untuk membukanya; kalau gambarnya sama, kartu tetap terbuka.
 * Cocokkan semua pasangan sebelum waktu habis = jawaban BENAR.
 *
 * Gambar kartu dan posisinya diacak setiap kali panel ini dibuka (lihat MemoryBoard).
 * Hasilnya dikirim ke BaseLevel.finishQuiz(benar/salah) seperti kuis biasa.
 */
public class MemoryPanel extends Actor
{
    private static final int W = 640, H = 440;

    // kisi kartu
    private static final int COLS = 4, ROWS = 3;
    private static final int CARD = 76, GAP_X = 14, GAP_Y = 10;
    private static final int GRID_X = (W - (COLS * CARD + (COLS - 1) * GAP_X)) / 2;
    private static final int GRID_Y = 124;

    // bar waktu (sama dengan QuizPanel)
    private static final int BAR_X = 66, BAR_Y = 404, BAR_W = 500, BAR_H = 26;

    private static final int MISMATCH_FRAMES = 45;     // lama dua kartu salah tetap terlihat

    private final MemoryBoard board;
    private final GreenfootImage base;
    private final GreenfootImage backImg;
    private final java.util.HashMap<String, GreenfootImage> faceImgs = new java.util.HashMap<String, GreenfootImage>();
    private final float[] flip;                        // 0 = punggung, 1 = muka (untuk animasi balik)

    private final int totalFrames;
    private int framesLeft;
    private int hover = -1;
    private int mismatchTimer = 0;
    private boolean finished = false;
    private boolean correct = false;
    private int resultTimer = 0;

    public MemoryPanel()
    {
        board = new MemoryBoard(GameConfig.MEMORY_PAIRS);
        totalFrames = GameConfig.MEMORY_TIME_SECONDS * 60;
        framesLeft = totalFrames;

        backImg = Assets.get(MemoryBoard.BACK, CARD, CARD);
        for (int i = 0; i < board.size(); i++)
            if (!faceImgs.containsKey(board.card[i]))
                faceImgs.put(board.card[i], Assets.get(board.card[i], CARD, CARD));

        flip = new float[board.size()];
        base = buildBase();
        render();
    }

    // ------------------------------------------------------------ loop
    public void act()
    {
        BaseLevel level = (BaseLevel) getWorld();
        if (level == null || level.isPaused()) return;

        boolean animating = animate();

        if (finished)                                   // menampilkan hasil
        {
            resultTimer--;
            if (resultTimer <= 0) { level.finishQuiz(correct); return; }
            render();
            return;
        }

        framesLeft--;
        if (framesLeft <= 0)
        {
            finished = true;
            correct = false;
            resultTimer = GameConfig.QUIZ_RESULT_FRAMES;
            render();
            return;
        }

        // dua kartu salah: tunggu sebentar lalu tutup
        if (mismatchTimer > 0)
        {
            mismatchTimer--;
            if (mismatchTimer == 0) board.closeOpen();
        }

        // mouse
        MouseInfo m = Greenfoot.getMouseInfo();
        if (m != null) hover = cardAt(m.getX(), m.getY());
        if (Greenfoot.mouseClicked(this))
        {
            MouseInfo c = Greenfoot.getMouseInfo();
            if (c != null) click(cardAt(c.getX(), c.getY()));
        }

        if (board.isComplete())
        {
            finished = true;
            correct = true;
            resultTimer = GameConfig.QUIZ_RESULT_FRAMES;
        }

        if (animating || framesLeft % 2 == 0 || finished) render();
    }

    private void click(int i)
    {
        int r = board.pick(i);
        if (r == MemoryBoard.MISMATCH) mismatchTimer = MISMATCH_FRAMES;
    }

    /** Menggeser progres balik kartu; true kalau masih ada yang bergerak. */
    private boolean animate()
    {
        boolean moving = false;
        for (int i = 0; i < flip.length; i++)
        {
            float target = (board.isFaceUp(i) || (finished && !correct)) ? 1f : 0f;
            if (flip[i] < target) { flip[i] = Math.min(target, flip[i] + 0.2f); moving = true; }
            else if (flip[i] > target) { flip[i] = Math.max(target, flip[i] - 0.2f); moving = true; }
        }
        return moving;
    }

    private int cardAt(int mx, int my)
    {
        int rx = mx - (getX() - W / 2);
        int ry = my - (getY() - H / 2);
        for (int i = 0; i < board.size(); i++)
        {
            int x = cardX(i), y = cardY(i);
            if (rx >= x && rx < x + CARD && ry >= y && ry < y + CARD) return i;
        }
        return -1;
    }

    private int cardX(int i) { return GRID_X + (i % COLS) * (CARD + GAP_X); }
    private int cardY(int i) { return GRID_Y + (i / COLS) * (CARD + GAP_Y); }

    // ------------------------------------------------------------ gambar dinamis
    private void render()
    {
        GreenfootImage img = new GreenfootImage(base);

        for (int i = 0; i < board.size(); i++) drawCard(img, i);

        UI.bold(img, "Pasangan: " + board.getMatchedPairs() + " / " + board.pairs
                     + "      Percobaan: " + board.getTries(), W / 2, 386, 18, UI.INK);

        if (!finished) drawTimer(img); else drawResult(img);
        setImage(img);
    }

    private void drawCard(GreenfootImage img, int i)
    {
        int x = cardX(i), y = cardY(i);
        float p = flip[i];

        GreenfootImage face;
        int w;
        if (p < 0.5f) { face = backImg;                  w = Math.round(CARD * (1f - 2f * p)); }
        else          { face = faceImgs.get(board.card[i]); w = Math.round(CARD * (2f * p - 1f)); }

        if (w >= CARD)
        {
            img.drawImage(face, x, y);
        }
        else if (w > 1)
        {
            GreenfootImage t = new GreenfootImage(face);
            t.scale(w, CARD);
            img.drawImage(t, x + (CARD - w) / 2, y);
        }

        if (!finished && board.canPick(i) && i == hover) frame(img, x, y, UI.GOLD);
        if (board.isMatched(i)) frame(img, x, y, UI.GREEN);
    }

    private void frame(GreenfootImage img, int x, int y, Color c)
    {
        img.setColor(c);
        for (int t = 0; t < 3; t++)
            img.drawRect(x - 1 - t, y - 1 - t, CARD + 2 + 2 * t - 1, CARD + 2 + 2 * t - 1);
    }

    private void drawTimer(GreenfootImage img)
    {
        img.setColor(UI.DARK);
        img.fillRect(BAR_X - 4, BAR_Y - 4, BAR_W + 8, BAR_H + 8);
        img.setColor(new Color(92, 36, 40));
        img.fillRect(BAR_X, BAR_Y, BAR_W, BAR_H);

        int w = BAR_W * framesLeft / totalFrames;
        boolean urgent = framesLeft < 600;
        img.setColor(urgent && (framesLeft / 8) % 2 == 0 ? new Color(255, 120, 120) : UI.RED);
        img.fillRect(BAR_X, BAR_Y, w, BAR_H);
        img.setColor(new Color(255, 255, 255, 70));
        img.fillRect(BAR_X, BAR_Y + 3, w, 5);

        img.drawImage(Assets.get("timee2.png", 42, 47), 14, BAR_Y - 12);
        UI.bold(img, "" + ((framesLeft + 59) / 60), BAR_X + BAR_W - 22, BAR_Y + BAR_H / 2, 20, Color.WHITE);
    }

    private void drawResult(GreenfootImage img)
    {
        img.setColor(UI.DARK);
        img.fillRect(BAR_X - 4, BAR_Y - 4, BAR_W + 8, BAR_H + 8);
        img.setColor(correct ? new Color(40, 120, 60) : new Color(140, 40, 50));
        img.fillRect(BAR_X, BAR_Y, BAR_W, BAR_H);

        String msg = correct ? "SEMUA COCOK!  +" + GameConfig.SCORE_PER_CORRECT + " poin"
                             : "WAKTU HABIS!  (-1 nyawa)";
        UI.bold(img, msg, BAR_X + BAR_W / 2, BAR_Y + BAR_H / 2, 20, Color.WHITE);
    }

    // ------------------------------------------------------------ gambar statis
    private GreenfootImage buildBase()
    {
        GreenfootImage g = new GreenfootImage(W, H);

        UI.pixelBox(g, 8, 44, W - 16, 388, UI.DARK, UI.TAN, UI.PARCHMENT, UI.CLEAR);

        UI.pixelBox(g, 120, 10, 440, 64, UI.DARK, UI.BROWN, UI.BROWN_LT, UI.CLEAR);
        UI.shadow(g, "Mistery Box!", 345, 42, 36, UI.GOLD, UI.DARK);
        g.drawImage(Assets.get("MysteryBox.png", 86, 84), 22, 2);
        sparkle(g, 118, 16, 7);
        sparkle(g, 566, 30, 6);

        UI.bold(g, "Cocokkan semua pasangan kartu!", W / 2, 102, 26, UI.INK);
        return g;
    }

    private void sparkle(GreenfootImage g, int cx, int cy, int r)
    {
        g.setColor(UI.GOLD);
        g.fillRect(cx - 1, cy - r, 3, r * 2 + 1);
        g.fillRect(cx - r, cy - 1, r * 2 + 1, 3);
    }
}
