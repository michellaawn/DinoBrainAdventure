
import greenfoot.*;
public class EndingWorld extends World
{
    private static final String[] STORY = {
        "I did it! I can see my forest\nagain, right over there!",
        "The Mystery Boxes taught me\nso many new things on my way.",
        "In the Forest of Knowledge,\nI learned to spot patterns.",
        "In the Sky of Puzzles,\nI learned to stay focused.",
        "On the Peak of Logic,\nI learned to remember.",
        "My family is waiting for me.\nI'm finally home!",
        "Getting lost wasn't so scary.\nIt made me smarter and braver.",
        "Thank you for helping me\nfind my way home!  THE END"
    };

    private int index = 0;
    private final StoryDialog dialog = new StoryDialog(700, 115);

    private Button nextBtn;

    public EndingWorld()
    {
        super(GameConfig.WIDTH, GameConfig.HEIGHT, 1);

        setBackground(Assets.get(
            "dino_home.jpg",
            GameConfig.WIDTH,
            GameConfig.HEIGHT
        ));

        // Posisi box diturunkan
        addObject(dialog, 400, 350);
        dialog.show(STORY[0]);

        nextBtn = new Button("next.png", 164, new Runnable() {
            public void run() {
                advance();
            }
        });

        // Tombol Next di bawah box
        addObject(nextBtn, 627, 405);
    }

    public void act()
    {
        String k = Greenfoot.getKey();

        if ("enter".equals(k) || "space".equals(k)) {
            advance();
        }
    }

    private void advance()
    {
        if (index >= STORY.length - 1) return;

        index++;

        dialog.show(STORY[index]);

        if (index == STORY.length - 1)
        {
            removeObject(nextBtn);
            showEndButtons();
        }
    }

    private void showEndButtons()
    {
    addObject(new Button("btn_play_again.png", 200, new Runnable() {
        public void run()
        {
            GameState.newGame();
            Greenfoot.setWorld(new LevelIntroWorld(1));
        }
        }), 420, 405);

    addObject(new Button("btn_main_menu.png", 200, new Runnable() {
        public void run()
        {
            Greenfoot.setWorld(new MainMenu());
        }
        }), 625, 405);
    }
}
