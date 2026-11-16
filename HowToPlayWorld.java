import greenfoot.*;

/** Layar "Learn": cara bermain. (Dulu "LearnGame" + "panel_kontrol" + "tombol_ok") */
public class HowToPlayWorld extends World
{
    public HowToPlayWorld()
    {
        super(GameConfig.WIDTH, GameConfig.HEIGHT, 1);

        GreenfootImage bg = Assets.get("bg_story.png", GameConfig.WIDTH, GameConfig.HEIGHT);
        UI.dim(bg, 90);

        // papan kontrol
        bg.drawImage(Assets.byWidth("Tutorial.png", 560), 120, 20);

        // petunjuk tambahan (edit teks di sini)
        UI.shadow(bg, "Cara Bermain", 400, 322, 26, UI.GOLD, UI.DARK);
        UI.bold(bg, "Sentuh Mystery Box dan jawab kuisnya sebelum waktu habis.", 400, 352, 17, Color.WHITE);
        UI.bold(bg, "Jawaban salah atau waktu habis = nyawa berkurang.", 400, 374, 17, Color.WHITE);
        UI.bold(bg, "Setelah kuis dijawab, capai bendera untuk lanjut. Tekan P / ESC untuk pause.", 400, 396, 17, Color.WHITE);
        setBackground(bg);

        addObject(new Button("tombol oke.png", 120, new Runnable() {
            public void run() { Greenfoot.setWorld(new MainMenu()); }
        }), 400, 425);
    }
}
