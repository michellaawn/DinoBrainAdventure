import greenfoot.*;

/**
 * Level 3. Peta ada di LevelData.get(3), soal kuisnya di QuizData.get(3).
 * Kalau level ini butuh sesuatu yang khusus, tambahkan di prepareExtras().
 */
public class Level3 extends BaseLevel
{
    public Level3()
    {
        super(3);
    }

    @Override
    protected void prepareExtras()
    {
        addObject(new Sprite("pohon.png", 100, 150), 50, 278);
    }
}
