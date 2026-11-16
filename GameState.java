/**
 * Data yang harus "terbawa" antar World (nyawa, skor, koin, dst).
 * Semua static, jadi bisa dibaca dari mana saja: GameState.score
 */
public class GameState
{
    public static int level       = 1;
    public static int lives       = GameConfig.START_LIVES;
    public static int coins       = 0;
    public static int score       = 0;
    public static int quizCorrect = 0;
    public static int seconds     = 0;     // total waktu main (level yang sudah selesai)

    // Cadangan kondisi di awal level, dipakai kalau pemain "Coba Lagi"
    private static int snapCoins, snapScore, snapCorrect, snapSeconds;

    /** Mulai game baru dari nol. */
    public static void newGame()
    {
        level = 1;
        coins = 0;
        score = 0;
        quizCorrect = 0;
        seconds = 0;
        lives = GameConfig.START_LIVES;
        saveSnapshot();
    }

    /** Dipanggil tepat sebelum sebuah level dimulai. */
    public static void beginLevel(int n)
    {
        level = n;
        lives = GameConfig.START_LIVES;
        saveSnapshot();
    }

    /** Ulangi level yang sama (setelah Game Over / tombol Ulangi). */
    public static void retryLevel()
    {
        coins = snapCoins;
        score = snapScore;
        quizCorrect = snapCorrect;
        seconds = snapSeconds;
        lives = GameConfig.START_LIVES;
    }

    private static void saveSnapshot()
    {
        snapCoins = coins;
        snapScore = score;
        snapCorrect = quizCorrect;
        snapSeconds = seconds;
    }

    public static String formatTime(int totalSeconds)
    {
        int m = totalSeconds / 60;
        int s = totalSeconds % 60;
        return (m < 10 ? "0" : "") + m + ":" + (s < 10 ? "0" : "") + s;
    }
}
