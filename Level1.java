import greenfoot.*;

/**
 * Level 1. Peta ada di LevelData.get(1), soal kuisnya di QuizData.get(1).
 * Kalau level ini butuh sesuatu yang khusus, tambahkan di prepareExtras().
 */
public class Level1 extends BaseLevel
{
    public Level1()
    {
        super(1);
    }

    @Override
    protected void prepareExtras()
    {
        addObject(new Sprite("pohon.png", 100, 150), 40, 278);

    }
}
