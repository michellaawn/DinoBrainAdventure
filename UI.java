import greenfoot.*;

/**
 * Alat gambar kecil untuk bikin tampilan: teks, panel kayu/perkamen, dll.
 * Semua method static, panggil seperti: UI.bold(img, "Halo", 100, 50, 24, UI.INK);
 */
public class UI
{
    // Palet warna (ubah di sini untuk mengganti tema seluruh game)
    public static final Color INK       = new Color(59, 42, 26);
    public static final Color DARK      = new Color(70, 44, 28);
    public static final Color BROWN     = new Color(107, 66, 38);
    public static final Color BROWN_LT  = new Color(139, 90, 50);
    public static final Color TAN       = new Color(200, 155, 106);
    public static final Color PARCHMENT = new Color(245, 227, 195);
    public static final Color OPTION    = new Color(242, 215, 169);
    public static final Color GOLD      = new Color(255, 217, 102);
    public static final Color RED       = new Color(222, 62, 78);
    public static final Color GREEN     = new Color(72, 190, 90);
    public static final Color CLEAR     = new Color(0, 0, 0, 0);

    // ------------------------------------------------------------ teks
    /** Teks rata tengah di titik (cx, cy). */
    public static void text(GreenfootImage g, String s, int cx, int cy, int size, Color c)
    {
        if (s == null || s.length() == 0) return;
        GreenfootImage t = new GreenfootImage(s, size, c, null);
        g.drawImage(t, cx - t.getWidth() / 2, cy - t.getHeight() / 2);
    }

    /** Teks rata kiri; x = tepi kiri, cy = titik tengah vertikal. */
    public static void textLeft(GreenfootImage g, String s, int x, int cy, int size, Color c)
    {
        if (s == null || s.length() == 0) return;
        GreenfootImage t = new GreenfootImage(s, size, c, null);
        g.drawImage(t, x, cy - t.getHeight() / 2);
    }

    /** Teks "tebal" (digambar dua kali). */
    public static void bold(GreenfootImage g, String s, int cx, int cy, int size, Color c)
    {
        text(g, s, cx, cy, size, c);
        text(g, s, cx + 1, cy, size, c);
    }

    /** Teks tebal dengan bayangan, bagus untuk judul. */
    public static void shadow(GreenfootImage g, String s, int cx, int cy, int size,
                              Color c, Color shade)
    {
        text(g, s, cx + 2, cy + 2, size, shade);
        text(g, s, cx + 3, cy + 2, size, shade);
        bold(g, s, cx, cy, size, c);
    }

    public static int textWidth(String s, int size)
    {
        if (s == null || s.length() == 0) return 0;
        return new GreenfootImage(s, size, Color.WHITE, null).getWidth();
    }

    /** Memecah kalimat jadi beberapa baris sesuai lebar maksimum (px). */
    public static String[] wrap(String s, int size, int maxWidth)
    {
        java.util.ArrayList<String> lines = new java.util.ArrayList<String>();
        String[] words = s.split(" ");
        String line = "";
        for (String w : words)
        {
            String test = line.length() == 0 ? w : line + " " + w;
            if (line.length() > 0 && textWidth(test, size) > maxWidth)
            {
                lines.add(line);
                line = w;
            }
            else
            {
                line = test;
            }
        }
        if (line.length() > 0) lines.add(line);
        return lines.toArray(new String[0]);
    }

    // ------------------------------------------------------------ bentuk
    /**
     * Kotak bergaya pixel-art tiga lapis (luar - tengah - dalam) dengan sudut
     * dipotong. cornerColor = warna yang menutup sudut (UI.CLEAR = transparan).
     */
    public static void pixelBox(GreenfootImage g, int x, int y, int w, int h,
                                Color outer, Color mid, Color inner, Color cornerColor)
    {
        g.setColor(outer);
        g.fillRect(x, y, w, h);
        g.setColor(mid);
        g.fillRect(x + 4, y + 4, w - 8, h - 8);
        g.setColor(inner);
        g.fillRect(x + 8, y + 8, w - 16, h - 16);
        notch(g, x,         y,         1,  1, cornerColor);
        notch(g, x + w - 1, y,        -1,  1, cornerColor);
        notch(g, x,         y + h - 1, 1, -1, cornerColor);
        notch(g, x + w - 1, y + h - 1, -1, -1, cornerColor);
    }

    // Memotong 3 pixel di satu sudut supaya terlihat membulat ala pixel-art.
    private static void notch(GreenfootImage g, int cx, int cy, int dx, int dy, Color c)
    {
        g.setColorAt(cx, cy, c);
        g.setColorAt(cx + dx, cy, c);
        g.setColorAt(cx, cy + dy, c);
    }

    /** Garis putus-putus berbentuk persegi. */
    public static void dashedRect(GreenfootImage g, int x, int y, int w, int h, Color c)
    {
        g.setColor(c);
        for (int i = 0; i < w; i += 9)
        {
            int len = Math.min(5, w - i);
            g.fillRect(x + i, y, len, 3);
            g.fillRect(x + i, y + h - 3, len, 3);
        }
        for (int j = 0; j < h; j += 9)
        {
            int len = Math.min(5, h - j);
            g.fillRect(x, y + j, 3, len);
            g.fillRect(x + w - 3, y + j, 3, len);
        }
    }

    /** Bintang 5 sudut. */
    public static void star(GreenfootImage g, int cx, int cy, int r, Color c)
    {
        int[] xs = new int[10];
        int[] ys = new int[10];
        for (int i = 0; i < 10; i++)
        {
            double a = -Math.PI / 2 + i * Math.PI / 5;
            int rad = (i % 2 == 0) ? r : r * 2 / 5;
            xs[i] = cx + (int) Math.round(rad * Math.cos(a));
            ys[i] = cy + (int) Math.round(rad * Math.sin(a));
        }
        g.setColor(c);
        g.fillPolygon(xs, ys, 10);
    }

    /** Menggelapkan seluruh gambar dengan lapisan hitam transparan. */
    public static void dim(GreenfootImage g, int alpha)
    {
        g.setColor(new Color(0, 0, 0, alpha));
        g.fillRect(0, 0, g.getWidth(), g.getHeight());
    }
}
