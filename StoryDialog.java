import greenfoot.*;

/** Kotak dialog Dino (panel + teks cerita). Panggil show(...) untuk mengganti teks. */
public class StoryDialog extends Actor
{
    private final GreenfootImage panel = Assets.get("dialog2dino.png");

    public StoryDialog()
    {
        show("");
    }

    public void show(String text)
    {
        GreenfootImage img = new GreenfootImage(panel);

        int y = 118;
        for (String paragraph : text.split("\n"))
        {
            for (String line : UI.wrap(paragraph, 22, 440))
            {
                UI.textLeft(img, line, 215, y, 22, Color.WHITE);
                y += 32;
            }
        }
        setImage(img);
    }
}
