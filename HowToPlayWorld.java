import greenfoot.*;

/** Layar "Learn": cara bermain.  */
public class HowToPlayWorld extends World
{
    // ---- font teks petunjuk ----
    private static final String  FONT_NAME = "Monospaced";   
    private static final boolean FONT_BOLD = true;

    // ---- posisi (kecilkan angka Y = lebih ke atas) ----
    private static final int PANEL_W   = 540;    // lebar papan kontrol
    private static final int PANEL_Y   = 14;     // tepi atas papan
    private static final int TITLE_Y   = 306;    // "Cara Bermain"
    private static final int LINE_Y    = 330;    // baris petunjuk pertama
    private static final int LINE_GAP  = 20;     // jarak antar baris petunjuk
    private static final int BUTTON_Y  = 408;    // tombol OK (tinggi 51 px; 450 = dasar layar)
    
    // ---- dino di sisi kiri papan kontrol ----
    private static final String DINO_IMAGE = "walk1.png";   // gambar dino
    private static final int DINO_X    = 222;           // titik tengah dino
    private static final int DINO_Y    = 165;
    private static final int DINO_SIZE = 160;           // ukuran maksimum (px)

    public HowToPlayWorld()
    {
        super(GameConfig.WIDTH, GameConfig.HEIGHT, 1);
        GreenfootImage bg = Assets.get("bg_story.png", GameConfig.WIDTH, GameConfig.HEIGHT);
        UI.dim(bg, 90);
    
        // papan kontrol (posisi x dihitung supaya tepat di tengah)
        bg.drawImage(Assets.byWidth("Tutorial.png", PANEL_W), (GameConfig.WIDTH - PANEL_W) / 2, PANEL_Y);
        // dino di sisi kiri papan kontrol
        GreenfootImage dino = Assets.fit(DINO_IMAGE, DINO_SIZE, DINO_SIZE);
        bg.drawImage(dino, DINO_X - dino.getWidth() / 2, DINO_Y - dino.getHeight() / 2);
        
        // petunjuk tambahan (edit teks di sini)
        drawText(bg, "Cara Bermain", 400, TITLE_Y, 24, UI.GOLD, UI.DARK);
        drawText(bg, "Sentuh Mystery Box dan jawab kuisnya sebelum waktu habis.", 400, LINE_Y, 14, Color.WHITE, null);
        drawText(bg, "Jawaban salah atau waktu habis = nyawa berkurang.", 400, LINE_Y + LINE_GAP, 14, Color.WHITE, null);
        drawText(bg, "Setelah kuis dijawab, capai bendera untuk lanjut. Tekan P / ESC untuk pause.", 400, LINE_Y + 2 * LINE_GAP, 14, Color.WHITE, null);
        setBackground(bg);

        addObject(new Button("tombol oke.png", 120, new Runnable() {
            public void run() { Greenfoot.setWorld(new MainMenu()); }
        }), 400, BUTTON_Y);
    }

    /** Teks rata tengah, shade = warna bayangan (null = tanpa bayangan). */
    private void drawText(GreenfootImage g, String s, int cx, int cy, int size, Color c, Color shade)
    {
        g.setFont(new Font(FONT_NAME, FONT_BOLD, false, size));
        int x = cx - (int) (s.length() * size * 0.6) / 2;    // Courier New: lebar huruf = 60% ukuran
        int y = cy + size / 3;                               // drawString memakai garis dasar huruf
        if (shade != null)
        {
            g.setColor(shade);
            g.drawString(s, x + 2, y + 2);
        }
        g.setColor(c);
        g.drawString(s, x, y);
    }
}