import greenfoot.*;

/** Layar Game Over: Coba Lagi (ulang level yang sama) atau kembali ke menu. */
public class GameOverWorld extends World
{
    public GameOverWorld(final int levelNo)
    {
        super(GameConfig.WIDTH, GameConfig.HEIGHT, 1);

        GreenfootImage bg = Assets.get(LevelData.get(levelNo).background,
                                       GameConfig.WIDTH, GameConfig.HEIGHT);
        UI.dim(bg, 170);
        UI.shadow(bg, "GAME OVER", 400, 120, 70, UI.RED, Color.BLACK);
        UI.bold(bg, "Nyawamu habis di Level " + levelNo, 400, 185, 22, Color.WHITE);
        UI.bold(bg, "Skor: " + GameState.score + "     Koin: " + GameState.coins, 400, 220, 22, UI.GOLD);
        setBackground(bg);

        addObject(new Button("Coba Lagi", 240, 54, new Runnable() {
            public void run()
            {
                GameState.retryLevel();
                Greenfoot.setWorld(BaseLevel.create(levelNo));
            }
        }), 400, 300);

        addObject(new Button("Menu Utama", 240, 54, new Runnable() {
            public void run() { Greenfoot.setWorld(new MainMenu()); }
        }), 400, 366);
    }
}
