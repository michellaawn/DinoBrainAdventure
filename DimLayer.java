import greenfoot.*;

/** Lapisan gelap transparan di belakang jendela kuis supaya pemain fokus ke soal. */
public class DimLayer extends Actor
{
    public DimLayer()
    {
        GreenfootImage img = new GreenfootImage(GameConfig.WIDTH, GameConfig.HEIGHT);
        UI.dim(img, 120);
        setImage(img);
    }
}
