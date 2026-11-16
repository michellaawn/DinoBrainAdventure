import greenfoot.*;

/** Layar tamat setelah Level 3: ringkasan skor + bintang. */
public class VictoryWorld extends World
{
    public VictoryWorld()
    {
        super(GameConfig.WIDTH, GameConfig.HEIGHT, 1);

        GreenfootImage bg = Assets.get("bg_story.png", GameConfig.WIDTH, GameConfig.HEIGHT);
        UI.dim(bg, 120);

        UI.shadow(bg, "SELAMAT!", 400, 70, 64, UI.GOLD, UI.DARK);
        UI.bold(bg, "Dino berhasil menemukan jalan pulang!", 400, 120, 22, Color.WHITE);

        // bintang = jumlah kuis yang dijawab benar
        for (int i = 0; i < GameConfig.TOTAL_LEVELS; i++)
        {
            Color c = i < GameState.quizCorrect ? UI.GOLD : new Color(90, 90, 90);
            UI.star(bg, 400 + (i - 1) * 70, 190, 30, c);
        }

        UI.bold(bg, "Kuis benar: " + GameState.quizCorrect + " / " + GameConfig.TOTAL_LEVELS, 400, 245, 22, Color.WHITE);
        UI.bold(bg, "Skor: " + GameState.score + "     Koin: " + GameState.coins, 400, 275, 22, UI.GOLD);
        UI.bold(bg, "Waktu: " + GameState.formatTime(GameState.seconds), 400, 305, 22, Color.WHITE);
        setBackground(bg);

        addObject(new Button("Main Lagi", 200, 50, new Runnable() {
            public void run()
            {
                GameState.newGame();
                Greenfoot.setWorld(new LevelIntroWorld(1));
            }
        }), 290, 380);

        addObject(new Button("Menu Utama", 200, 50, new Runnable() {
            public void run() { Greenfoot.setWorld(new MainMenu()); }
        }), 510, 380);
    }
}
