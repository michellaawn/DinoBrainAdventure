import greenfoot.*;

/**
 * Jendela Mystery Box (desain mengikuti mock-up):
 * banner judul, instruksi, deretan pola + kotak "?", pertanyaan,
 * 3 pilihan A/B/C, dan bar waktu merah di bawah.
 *
 * Jawab dengan klik mouse, atau tekan 1/2/3 atau A/B/C.
 * Semua isi (teks, gambar, kunci jawaban) berasal dari objek Question.
 */
public class QuizPanel extends Actor
{
    private static final int W = 640, H = 440;

    // posisi elemen di dalam gambar panel
    private static final int OPT_X = 45, OPT_Y = 316, OPT_W = 170, OPT_H = 76, OPT_GAP = 20;
    private static final int BAR_X = 66, BAR_Y = 404, BAR_W = 500, BAR_H = 26;

    private static final int NONE = -2, TIMEOUT = -1;

    // kisi soal "cari yang berbeda"
    private static final int ODD_COLS = 5, ODD_ROWS = 3;

    private final Question q;
    private final int oddIndex;          // (soal ODD) kotak ke-berapa (0-14) yang berbeda
    private final GreenfootImage base;
    private final int totalFrames;
    private int framesLeft;
    private int hover = -1;
    private int chosen = NONE;
    private boolean correct = false;
    private int resultTimer = 0;

    public QuizPanel(Question q)
    {
        this.q = q;
        oddIndex = Greenfoot.getRandomNumber(ODD_COLS * ODD_ROWS);
        totalFrames = q.seconds * 60;
        framesLeft = totalFrames;
        base = buildBase();
        render();
    }

    // ------------------------------------------------------------ loop
    public void act()
    {
        BaseLevel level = (BaseLevel) getWorld();
        if (level == null || level.isPaused()) return;

        if (chosen != NONE)                       // sedang menampilkan hasil
        {
            resultTimer--;
            if (resultTimer <= 0) level.finishQuiz(correct);
            return;
        }

        framesLeft--;
        if (framesLeft <= 0)
        {
            chosen = TIMEOUT;
            correct = false;
            resultTimer = GameConfig.QUIZ_RESULT_FRAMES;
            render();
            return;
        }

        // mouse
        MouseInfo m = Greenfoot.getMouseInfo();
        if (m != null)
        {
            hover = optionAt(m.getX(), m.getY());
        }
        if (Greenfoot.mouseClicked(this))
        {
            MouseInfo c = Greenfoot.getMouseInfo();
            if (c != null)
            {
                int i = optionAt(c.getX(), c.getY());
                if (i >= 0) { answer(i); return; }
            }
        }

        // keyboard
        String k = Greenfoot.getKey();
        if (k != null)
        {
            if (k.equals("1") || k.equals("a")) { answer(0); return; }
            if (k.equals("2") || k.equals("b")) { answer(1); return; }
            if (k.equals("3") || k.equals("c")) { answer(2); return; }
        }

        if (framesLeft % 2 == 0) render();
    }

    private void answer(int index)
    {
        chosen = index;
        correct = (index == q.correct);
        resultTimer = GameConfig.QUIZ_RESULT_FRAMES;
        render();
    }

    private int optionAt(int mx, int my)
    {
        int rx = mx - (getX() - W / 2);
        int ry = my - (getY() - H / 2);
        for (int i = 0; i < 3; i++)
        {
            int x0 = OPT_X + i * (OPT_W + OPT_GAP);
            if (rx >= x0 && rx < x0 + OPT_W && ry >= OPT_Y && ry < OPT_Y + OPT_H) return i;
        }
        return -1;
    }

    // ------------------------------------------------------------ gambar dinamis
    private void render()
    {
        GreenfootImage img = new GreenfootImage(base);

        if (chosen == NONE)
        {
            if (hover >= 0) outline(img, hover, UI.GOLD, 0);
            drawTimer(img);
        }
        else
        {
            outline(img, q.correct, UI.GREEN, 70);                       // jawaban benar
            if (chosen >= 0 && chosen != q.correct) outline(img, chosen, UI.RED, 70);
            drawResult(img);
        }
        setImage(img);
    }

    private void outline(GreenfootImage img, int i, Color c, int fillAlpha)
    {
        int x = OPT_X + i * (OPT_W + OPT_GAP);
        if (fillAlpha > 0)
        {
            img.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), fillAlpha));
            img.fillRect(x, OPT_Y, OPT_W, OPT_H);
        }
        img.setColor(c);
        for (int t = 0; t < 4; t++)
            img.drawRect(x - t, OPT_Y - t, OPT_W + 2 * t - 1, OPT_H + 2 * t - 1);
    }

    private void drawTimer(GreenfootImage img)
    {
        // wadah bar
        img.setColor(UI.DARK);
        img.fillRect(BAR_X - 4, BAR_Y - 4, BAR_W + 8, BAR_H + 8);
        img.setColor(new Color(92, 36, 40));
        img.fillRect(BAR_X, BAR_Y, BAR_W, BAR_H);

        // isi bar (berkedip kalau sisa < 5 detik)
        int w = BAR_W * framesLeft / totalFrames;
        boolean urgent = framesLeft < 300;
        img.setColor(urgent && (framesLeft / 8) % 2 == 0 ? new Color(255, 120, 120) : UI.RED);
        img.fillRect(BAR_X, BAR_Y, w, BAR_H);
        img.setColor(new Color(255, 255, 255, 70));
        img.fillRect(BAR_X, BAR_Y + 3, w, 5);

        // jam + sisa detik
        img.drawImage(Assets.get("timee2.png", 42, 47), 14, BAR_Y - 12);
        UI.bold(img, "" + ((framesLeft + 59) / 60), BAR_X + BAR_W - 22, BAR_Y + BAR_H / 2, 20, Color.WHITE);
    }

    private void drawResult(GreenfootImage img)
    {
        img.setColor(UI.DARK);
        img.fillRect(BAR_X - 4, BAR_Y - 4, BAR_W + 8, BAR_H + 8);
        img.setColor(correct ? new Color(40, 120, 60) : new Color(140, 40, 50));
        img.fillRect(BAR_X, BAR_Y, BAR_W, BAR_H);

        char letter = (char) ('A' + q.correct);
        String msg;
        if (correct)               msg = "BENAR!  +" + GameConfig.SCORE_PER_CORRECT + " poin";
        else if (chosen == TIMEOUT) msg = "WAKTU HABIS!  Jawaban: " + letter;
        else                        msg = "SALAH!  Jawaban benar: " + letter + "   (-1 nyawa)";
        UI.bold(img, msg, BAR_X + BAR_W / 2, BAR_Y + BAR_H / 2, 20, Color.WHITE);
    }

    // ------------------------------------------------------------ gambar statis
    private GreenfootImage buildBase()
    {
        GreenfootImage g = new GreenfootImage(W, H);

        // badan panel
        UI.pixelBox(g, 8, 44, W - 16, 388, UI.DARK, UI.TAN, UI.PARCHMENT, UI.CLEAR);

        // banner judul + ikon kotak
        UI.pixelBox(g, 120, 10, 440, 64, UI.DARK, UI.BROWN, UI.BROWN_LT, UI.CLEAR);
        UI.shadow(g, "Mistery Box!", 345, 42, 36, UI.GOLD, UI.DARK);
        g.drawImage(Assets.get("MysteryBox.png", 86, 84), 22, 2);
        sparkle(g, 118, 16, 7);
        sparkle(g, 566, 30, 6);

        if (q.kind == Question.ODD)
        {
            // judul + kisi gambar + pilihan teks
            String[] lines = UI.wrap(q.instruction, 28, W - 80);
            for (int i = 0; i < lines.length; i++)
                UI.bold(g, lines[i], W / 2, 106 + i * 30, 28, UI.INK);
            drawGrid(g);
            for (int i = 0; i < 3 && i < q.options.length; i++)
                drawOddOption(g, i);
            return g;
        }

        // instruksi
        String[] lines = UI.wrap(q.instruction, 24, W - 80);
        for (int i = 0; i < lines.length; i++)
            UI.bold(g, lines[i], W / 2, 118 + i * 30, 24, UI.INK);

        // deretan pola + kotak "?"
        drawPattern(g, 198);

        // pertanyaan
        String[] pl = UI.wrap(q.prompt, 26, W - 80);
        for (int i = 0; i < pl.length; i++)
            UI.bold(g, pl[i], W / 2, 276 + i * 30, 26, UI.INK);

        // pilihan jawaban
        for (int i = 0; i < 3 && i < q.options.length; i++)
            drawOption(g, i);

        return g;
    }

    private void drawPattern(GreenfootImage g, int cy)
    {
        int n = (q.pattern == null) ? 0 : q.pattern.length;
        int slot = 66, gap = 22;
        int total = (n + 1) * slot + n * gap;
        int x = (W - total) / 2;

        for (int i = 0; i < n; i++)
        {
            drawContent(g, q.pattern[i], x + slot / 2, cy, slot - 4, 40);
            x += slot + gap;
        }

        // kotak "?"
        UI.dashedRect(g, x, cy - slot / 2, slot, slot, UI.INK);
        UI.bold(g, "?", x + slot / 2, cy, 44, UI.INK);
    }

    /** Kisi 3 x 5: semua kotak gambar biasa, satu kotak (acak) gambar yang berbeda. */
    private void drawGrid(GreenfootImage g)
    {
        int dx = 64, dy = 54, size = 46;
        int x0 = W / 2 - (ODD_COLS - 1) * dx / 2;
        int y0 = 152;
        for (int r = 0; r < ODD_ROWS; r++)
        {
            for (int c = 0; c < ODD_COLS; c++)
            {
                String f = (r * ODD_COLS + c == oddIndex) ? q.oddImage : q.itemImage;
                GreenfootImage img = Assets.fit(f, size, size);
                g.drawImage(img, x0 + c * dx - img.getWidth() / 2, y0 + r * dy - img.getHeight() / 2);
            }
        }
    }

    /** Pilihan berupa teks (boleh 2 baris), mis. "Telur" / "Retak". */
    private void drawOddOption(GreenfootImage g, int i)
    {
        int x = OPT_X + i * (OPT_W + OPT_GAP);
        UI.pixelBox(g, x, OPT_Y, OPT_W, OPT_H, UI.TAN, UI.TAN, UI.OPTION, UI.PARCHMENT);
        UI.bold(g, (char) ('A' + i) + ".", x + 30, OPT_Y + OPT_H / 2, 28, UI.INK);

        String[] lines = UI.wrap(q.options[i], 22, 96);
        int n = lines.length;
        for (int k = 0; k < n; k++)
        {
            int ly = OPT_Y + OPT_H / 2 + (int) ((k - (n - 1) / 2.0) * 26);
            UI.bold(g, lines[k], x + 112, ly, 22, UI.INK);
        }
    }

    private void drawOption(GreenfootImage g, int i)
    {
        int x = OPT_X + i * (OPT_W + OPT_GAP);
        UI.pixelBox(g, x, OPT_Y, OPT_W, OPT_H, UI.TAN, UI.TAN, UI.OPTION, UI.PARCHMENT);
        UI.bold(g, (char) ('A' + i) + ".", x + 30, OPT_Y + OPT_H / 2, 28, UI.INK);
        drawContent(g, q.options[i], x + 106, OPT_Y + OPT_H / 2, 62, 32);
    }

    /**
     * Menggambar isi: berakhiran .png/.jpg -> gambar, selain itu -> teks.
     * Gambar boleh diberi skala: "batu.png*0.5" = ukuran 50%.
     */
    private void drawContent(GreenfootImage g, String content, int cx, int cy, int box, int textSize)
    {
        String name = content;
        double scale = 1.0;
        int star = content.lastIndexOf('*');
        if (star > 0 && isImage(content.substring(0, star)))
        {
            name = content.substring(0, star);
            scale = Double.parseDouble(content.substring(star + 1));
        }

        if (isImage(name))
        {
            GreenfootImage img = Assets.fit(name, box, box);
            if (scale != 1.0)
                img = Assets.get(name, Math.max(1, (int) (img.getWidth() * scale)),
                                       Math.max(1, (int) (img.getHeight() * scale)));
            g.drawImage(img, cx - img.getWidth() / 2, cy - img.getHeight() / 2);
        }
        else
        {
            UI.bold(g, content, cx, cy, textSize, UI.INK);
        }
    }

    private boolean isImage(String s)
    {
        String l = s.toLowerCase();
        return l.endsWith(".png") || l.endsWith(".jpg") || l.endsWith(".gif");
    }

    private void sparkle(GreenfootImage g, int cx, int cy, int r)
    {
        g.setColor(UI.GOLD);
        g.fillRect(cx - 1, cy - r, 3, r * 2 + 1);
        g.fillRect(cx - r, cy - 1, r * 2 + 1, 3);
    }
}
