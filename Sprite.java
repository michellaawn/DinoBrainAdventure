import greenfoot.*;

/** Aktor gambar biasa (logo, pohon, hiasan). Tidak berbuat apa-apa. */
public class Sprite extends Actor
{
    /** Skala berdasarkan lebar, tinggi mengikuti proporsi. */
    public Sprite(String file, int width)
    {
        setImage(Assets.byWidth(file, width));
    }

    /** Ukuran persis. */
    public Sprite(String file, int width, int height)
    {
        setImage(Assets.get(file, width, height));
    }
}
