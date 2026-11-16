import greenfoot.*;

/**
 * Panel PENGATURAN (muncul saat tombol gear / tombol P ditekan).
 * Latar kayu berasal dari images/panel_pengaturan.png. Slider, tombol - / + dan
 * tombol mute digambar dinamis di sini. Tombol RESUME dan QUIT dipasang oleh BaseLevel.
 *
 * Cara pakai: klik/seret slider, klik tombol - atau +, klik ikon speaker untuk mute.
 *
 * Koordinat di bawah (relatif terhadap pojok kiri-atas panel) HARUS sama dengan
 * angka di gambar panel_pengaturan.png. Kalau gambar diganti, sesuaikan angkanya.
 */
public class SettingsPanel extends Actor
{
    public static final int PW = 560, PH = 360;
    public static final int PX = (GameConfig.WIDTH - PW) / 2;
    public static final int PY = (GameConfig.HEIGHT - PH) / 2;

    private static final int ROW_MUSIC = 122, ROW_SFX = 192;
    private static final int MINUS_X = 278, PLUS_X = 434, MUTE_X = 482;
    private static final int TRACK_X0 = 298, TRACK_W = 114;
    private static final int STEP = 10;

    private final GreenfootImage base;
    private int dragging = 0;                 // 0 = tidak, 1 = slider musik, 2 = slider efek
    private int shownMusic = -1, shownSfx = -1;
    private boolean shownMusicMuted, shownSfxMuted;

    public SettingsPanel()
    {
        base = new GreenfootImage(GameConfig.WIDTH, GameConfig.HEIGHT);
        UI.dim(base, 150);
        base.drawImage(Assets.get("panel_pengaturan.png"), PX, PY);
        setImage(new GreenfootImage(base));
        redraw();
    }

    public void act()
    {
        MouseInfo m = Greenfoot.getMouseInfo();

        if (m != null && Greenfoot.mousePressed(this)) press(m.getX() - PX, m.getY() - PY);

        if (dragging != 0)
        {
            if (m != null && Greenfoot.mouseDragged(this)) setFromX(dragging, m.getX() - PX);
            if (Greenfoot.mouseDragEnded(this) || Greenfoot.mouseClicked(null)) dragging = 0;
        }

        redraw();
    }

    // ------------------------------------------------------------ input
    private void press(int lx, int ly)
    {
        for (int row = 1; row <= 2; row++)
        {
            int y = rowY(row);
            if (near(lx, ly, MINUS_X, y)) { change(row, -STEP); return; }
            if (near(lx, ly, PLUS_X, y))  { change(row, +STEP); return; }
            if (near(lx, ly, MUTE_X, y))  { toggleMute(row);    return; }
            if (lx >= TRACK_X0 - 8 && lx <= TRACK_X0 + TRACK_W + 8 && Math.abs(ly - y) <= 14)
            {
                dragging = row;
                setFromX(row, lx);
                return;
            }
        }
    }

    private boolean near(int x, int y, int cx, int cy)
    {
        return (x - cx) * (x - cx) + (y - cy) * (y - cy) <= 18 * 18;
    }

    private int rowY(int row) { return row == 1 ? ROW_MUSIC : ROW_SFX; }

    private void setFromX(int row, int lx)
    {
        // lx = posisi mouse dalam koordinat panel (x dunia dikurangi PX)
        int v = Math.round((lx - TRACK_X0) * 100f / TRACK_W);
        set(row, v);
    }

    private void change(int row, int delta)
    {
        int cur = (row == 1) ? Settings.musicVolume : Settings.sfxVolume;
        set(row, cur + delta);
    }

    private void set(int row, int v)
    {
        v = Math.max(0, Math.min(100, v));
        if (row == 1) { Settings.musicVolume = v; Settings.musicMuted = (v == 0); }
        else          { Settings.sfxVolume = v;   Settings.sfxMuted = (v == 0); }
        Audio.refreshVolume();
    }

    private void toggleMute(int row)
    {
        if (row == 1) Settings.musicMuted = !Settings.musicMuted;
        else          Settings.sfxMuted = !Settings.sfxMuted;
        Audio.refreshVolume();
    }

    // ------------------------------------------------------------ gambar
    private void redraw()
    {
        if (shownMusic == Settings.musicVolume && shownSfx == Settings.sfxVolume
            && shownMusicMuted == Settings.musicMuted && shownSfxMuted == Settings.sfxMuted) return;
        shownMusic = Settings.musicVolume;
        shownSfx = Settings.sfxVolume;
        shownMusicMuted = Settings.musicMuted;
        shownSfxMuted = Settings.sfxMuted;

        GreenfootImage g = new GreenfootImage(base);
        drawSlider(g, ROW_MUSIC, Settings.musicVolume, Settings.musicMuted);
        drawSlider(g, ROW_SFX, Settings.sfxVolume, Settings.sfxMuted);
        setImage(g);
    }

    private void drawSlider(GreenfootImage g, int row, int vol, boolean muted)
    {
        int x0 = PX + TRACK_X0;
        int y = PY + row;
        int fill = TRACK_W * vol / 100;
        if (muted) fill = 0;

        // alur slider
        g.setColor(new Color(45, 28, 15));
        g.fillRect(x0 - 3, y - 9, TRACK_W + 6, 18);
        g.setColor(new Color(100, 62, 32));
        g.fillRect(x0, y - 6, TRACK_W, 12);

        // isi hijau
        g.setColor(new Color(96, 196, 60));
        g.fillRect(x0, y - 6, fill, 12);
        g.setColor(new Color(170, 235, 120));
        g.fillRect(x0, y - 6, fill, 3);

        // tombol geser
        int kx = x0 + fill;
        g.setColor(new Color(45, 28, 15));
        g.fillRect(kx - 6, y - 13, 12, 26);
        g.setColor(new Color(230, 212, 176));
        g.fillRect(kx - 4, y - 11, 8, 22);

        // tanda mute
        if (muted)
        {
            int mx = PX + MUTE_X;
            g.setColor(new Color(220, 50, 50));
            for (int d = 0; d < 3; d++)
            {
                g.drawLine(mx - 9 + d, y - 9, mx + 9 + d, y + 9);
                g.drawLine(mx - 9 + d, y + 9, mx + 9 + d, y - 9);
            }
        }
    }
}
