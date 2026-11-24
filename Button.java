import greenfoot.*;

/**
- Tombol serbaguna. Ada dua jenis:
-   new Button("start.png", 170, () -> aksi())          -> tombol bergambar
-   new Button("Lanjutkan", 220, 46, () -> aksi())      -> tombol teks bergaya kayu
- Membesar sedikit saat disorot mouse.
 */
public class Button extends Actor
{
    private final GreenfootImage normal;
    private final GreenfootImage hover;
    private final Runnable action;
    private boolean hovered = false;

    /** Tombol bergambar (lebar ditentukan, tinggi proporsional). */
    public Button(String imageFile, int width, Runnable action)
    {
        this.action = action;
        normal = Assets.byWidth(imageFile, width);
        hover = Assets.byWidth(imageFile, (int) (width * 1.06));
        setImage(normal);
    }

    /** Tombol teks. */
    public Button(String label, int width, int height, Runnable action)
    {
        this.action = action;
        normal = draw(label, width, height, UI.OPTION);
        hover = draw(label, width, height, UI.GOLD);
        setImage(normal);
    }

    private GreenfootImage draw(String label, int w, int h, Color fill)
    {
        GreenfootImage img = new GreenfootImage(w, h);
        UI.pixelBox(img, 0, 0, w, h, UI.DARK, UI.TAN, fill, UI.CLEAR);
        UI.bold(img, label, w / 2, h / 2, Math.max(14, h / 2 - 2), UI.INK);
        return img;
    }

    public void act()
    {
        MouseInfo m = Greenfoot.getMouseInfo();
        if (m != null)
        {
            boolean over = (m.getActor() == this);
            if (over != hovered)
            {
                hovered = over;
                setImage(hovered ? hover : normal);
            }
        }

        if (Greenfoot.mouseClicked(this))
        {
            Audio.play("click.mp3");
            action.run();
        }
    }
}