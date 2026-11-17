import greenfoot.*;

/** Kotak dialog Dino (panel + teks cerita). Panggil show(...) untuk mengganti teks. */
public class StoryDialog extends Actor
{
    private final GreenfootImage panel = Assets.get("dialog2dino.png");
    private static final String FONT_NAME   = "Courier New";   
    private static final boolean FONT_BOLD  = true;
    private static final int    FONT_SIZE   = 18;
    private static final int    TEXT_X      = 215;
    private static final int    TEXT_Y      = 118;
    private static final int    LINE_HEIGHT = 30;
    private static final int    MAX_WIDTH   = 440;
    private final Font font = new Font(FONT_NAME, FONT_BOLD, false, FONT_SIZE);

    public StoryDialog()
    {
        show("");
    }

    public void show(String text)
    {
        GreenfootImage img = new GreenfootImage(panel);
        img.setFont(font);
        img.setColor(Color.WHITE);
        int maxChars = (int) (MAX_WIDTH / (FONT_SIZE * 0.6));

        int y = TEXT_Y;
        for (String paragraph : text.split("\n"))
        {
            for (String line : wrap(paragraph, maxChars))
            {
                img.drawString(line, TEXT_X, y + FONT_SIZE / 3);   
                y += LINE_HEIGHT;
            }
        }
        setImage(img);
    }

    /** Memecah kalimat per kata supaya tiap baris tidak lebih dari maxChars huruf. */
    private static String[] wrap(String s, int maxChars)
    {
        java.util.ArrayList<String> lines = new java.util.ArrayList<String>();
        String line = "";
        for (String w : s.split(" "))
        {
            String test = line.length() == 0 ? w : line + " " + w;
            if (line.length() > 0 && test.length() > maxChars)
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
}