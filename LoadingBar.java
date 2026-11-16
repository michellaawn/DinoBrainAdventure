import greenfoot.*;

/** Bar loading. Panggil setProgress(0..100). */
public class LoadingBar extends Actor
{
    private static final int W = 440, H = 18;
    private int progress = -1;

    public LoadingBar()
    {
        setProgress(0);
    }

    public void setProgress(int p)
    {
        p = Math.max(0, Math.min(100, p));
        if (p == progress) return;
        progress = p;

        GreenfootImage img = new GreenfootImage(W, H);
        img.setColor(new Color(30, 35, 35));
        img.fillRect(0, 0, W, H);
        img.setColor(new Color(255, 195, 65));
        img.fillRect(2, 2, (W - 4) * p / 100, H - 4);
        img.setColor(Color.WHITE);
        img.drawRect(0, 0, W - 1, H - 1);
        setImage(img);
    }
}
