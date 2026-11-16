/**
 * Semua angka penting game ada di sini.
 * Mau ubah kecepatan, lompatan, jumlah nyawa, waktu kuis, dll? Cukup edit file ini.
 */
public class GameConfig
{
    // ---- Ukuran layar & grid level -------------------------------------
    public static final int WIDTH  = 800;
    public static final int HEIGHT = 450;
    public static final int TILE   = 50;              // 1 kotak = 50 x 50 px
    public static final int COLS   = WIDTH / TILE;    // 16 kolom
    public static final int ROWS   = HEIGHT / TILE;   // 9 baris

    // ---- Alur game -----------------------------------------------------
    public static final int TOTAL_LEVELS = 3;
    public static final int START_LIVES  = 3;         // nyawa di awal TIAP level

    // ---- Skor ----------------------------------------------------------
    public static final int SCORE_PER_COIN    = 10;
    public static final int SCORE_PER_CORRECT = 100;  // jawaban kuis benar

    // ---- Fisika Dino ---------------------------------------------------
    public static final int MOVE_SPEED     = 5;       // px per frame
    public static final int JUMP_POWER     = 16;      // makin besar = lompat makin tinggi
    public static final int GRAVITY        = 1;
    public static final int MAX_FALL_SPEED = 14;

    // ---- Kuis ----------------------------------------------------------
    public static final int QUIZ_TIME_SECONDS = 15;   // batas waktu default per soal
    public static final int QUIZ_RESULT_FRAMES = 100; // lama "BENAR/SALAH" tampil

    // Level 3: permainan kartu memori
    public static final int MEMORY_PAIRS = 6;          // jumlah pasangan (maks 7 = jumlah gambar kartu)
    public static final int MEMORY_TIME_SECONDS = 60;  // batas waktu mencocokkan semua pasangan
}
