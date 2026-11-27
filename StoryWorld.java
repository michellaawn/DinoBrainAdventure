import greenfoot.*;

/** Cerita pembuka. (Dulu "GAMESTART" + "dialog") Klik Next / tekan Enter / Spasi untuk lanjut. */
public class StoryWorld extends World
{
    // Edit cerita di sini. Satu elemen = satu layar dialog. "\n" = ganti baris.
    private static final String[] STORY = {
        "I live in a beautiful forest filled\nwith trees, flowers.",
        "and wonderful creatures.\nThis forest has always been my home.",
        "I have always been curious\nabout the world around me.",
        "I love exploring new places and\nlearning about nature.",
        "But one day, I wandered too far\ninto the unknown.",
        "Oh no... everything looks unfamiliar.\nAm I lost?",
        "I'm a bit scared, but I can't give up.\nI must find my way back!",
        "It's time to be brave.\nLet my journey begin!"
    };
     
    private int index = 0;
    private final StoryDialog dialog = new StoryDialog();
    private Button nextBtn, startBtn;

    public StoryWorld()
    {
        super(GameConfig.WIDTH, GameConfig.HEIGHT, 1);
        setBackground(Assets.get("bgs.jpg", GameConfig.WIDTH, GameConfig.HEIGHT));
        addObject(dialog, 400, 250);
        dialog.show(STORY[0]);
        nextBtn = new Button("next.png", 164, new Runnable() {
            public void run() { advance(); }
        });
        startBtn = new Button("start_adventure.png", 230, new Runnable() {
            public void run() { startGame(); }
        });
        addObject(nextBtn, 400, 350);
    }

    public void act()
    {
        String k = Greenfoot.getKey();
        if ("enter".equals(k) || "space".equals(k)) advance();
    }

    private void advance()
    {
        if (index >= STORY.length - 1)
        {
            startGame();
            return;
        }
        index++;
        dialog.show(STORY[index]);

        if (index == STORY.length - 1)          
        {
            removeObject(nextBtn);
            addObject(startBtn, 400, 350);
        }
    }

    private void startGame()
    {
        GameState.newGame();
        Greenfoot.setWorld(new LevelIntroWorld(1));
    }
}
