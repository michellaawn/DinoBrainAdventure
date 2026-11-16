import greenfoot.*;
import java.util.HashMap;

/**
 * Pemuat gambar dengan cache, supaya gambar besar tidak di-scale berulang kali.
 * Selalu mengembalikan SALINAN, jadi aman diubah-ubah.
 */
public class Assets
{
    private static final HashMap<String, GreenfootImage> cache =
        new HashMap<String, GreenfootImage>();

    /** Gambar dengan ukuran asli. */
    public static GreenfootImage get(String file)
    {
        return get(file, 0, 0);
    }

    /** Gambar di-scale persis ke lebar x tinggi. (0,0 = ukuran asli) */
    public static GreenfootImage get(String file, int w, int h)
    {
        String key = file + "@" + w + "x" + h;
        GreenfootImage img = cache.get(key);
        if (img == null)
        {
            img = new GreenfootImage(file);
            if (w > 0 && h > 0) img.scale(w, h);
            cache.put(key, img);
        }
        return new GreenfootImage(img);
    }

    /** Scale proporsional supaya muat di kotak maxW x maxH. */
    public static GreenfootImage fit(String file, int maxW, int maxH)
    {
        GreenfootImage src = get(file);
        double s = Math.min((double) maxW / src.getWidth(),
                            (double) maxH / src.getHeight());
        int w = Math.max(1, (int) (src.getWidth() * s));
        int h = Math.max(1, (int) (src.getHeight() * s));
        return get(file, w, h);
    }

    /** Scale proporsional berdasarkan lebar. */
    public static GreenfootImage byWidth(String file, int w)
    {
        GreenfootImage src = get(file);
        int h = Math.max(1, src.getHeight() * w / src.getWidth());
        return get(file, w, h);
    }
}
