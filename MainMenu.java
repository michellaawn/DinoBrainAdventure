import greenfoot.*;

/** Menu utama: logo + tombol Start / Learn / Exit. (Dulu bernama "bg" + "Beranda_Logo") */
public class MainMenu extends World
{
    public MainMenu()
    {
        super(GameConfig.WIDTH, GameConfig.HEIGHT, 1);
        setBackground(Assets.get("bg_menu.png", GameConfig.WIDTH, GameConfig.HEIGHT));

        addObject(new Sprite("LOGODINO.png", 440), 400, 115);

        addObject(new Button("start.png", 172, new Runnable() {
            public void run() { Greenfoot.setWorld(new StoryWorld()); }
        }), 400, 250);

        addObject(new Button("learn.png", 172, new Runnable() {
            public void run() { Greenfoot.setWorld(new HowToPlayWorld()); }
        }), 400, 300);

        addObject(new Button("exit.png", 172, new Runnable() {
            public void run() { Greenfoot.stop(); }
        }), 400, 350);
    }

    public void started() { Audio.startMusic(); }
    public void stopped() { Audio.pauseMusic(); }
}