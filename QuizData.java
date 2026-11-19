import greenfoot.*;
import java.util.ArrayList;

/**
 * ====================  BANK SOAL (RANDOM)  ====================
 *
 *   Level 1      : soal POLA, dipilih acak dari patternBank()
 *   Level 2      : soal "CARI YANG BERBEDA", dipilih acak dari oddBank()
 *   Level 3      : bukan kuis, tapi permainan kartu memori (lihat MemoryBoard / MemoryPanel)
 *
 * Urutan pilihan A/B/C selalu diacak, posisi gambar yang berbeda juga acak.
 *
 * Tambah soal baru: salin satu blok "new Question(...)" / "Question.odd(...)",
 * beri koma di antara blok, dan taruh gambarnya di folder images.
 */
public class QuizData
{
    private static final ArrayList<Integer> usedOdd = new ArrayList<Integer>();
    private static final ArrayList<Integer> usedPattern = new ArrayList<Integer>();

    public static void reset(int level)
    {
        if (level == 1)
        {
            usedPattern.clear();
        }
        else if (level == 2)
        {
            usedOdd.clear();
        }
    }
    
    public static Question get(int level)
    {
        Question[] bank;
        ArrayList<Integer> used;

        if (level == 1)
        {
            bank = patternBank();
            used = usedPattern;
        }
        else if (level == 2)
        {
            bank = oddBank();
            used = usedOdd;
        }
        else
        {
            return null;
        }

        if (used.size() >= bank.length)
        {
            return null;
        }
        int i;
        do
        {
            i = Greenfoot.getRandomNumber(bank.length);
        }
        while (used.contains(i));
        used.add(i);
        return bank[i].shuffled();
    }

    // ------------------------------------------------------------ LEVEL 1: pola
    private static Question[] patternBank()
    {
        return new Question[] {

            // 1. Pola bentuk & warna  (jawaban: lingkaran merah)
            new Question(
                "Perhatikan pola gambar berikut!",
                new String[] { "bentuk_lingkaran_merah.png", "bentuk_kotak_hijau.png",
                               "bentuk_lingkaran_merah.png", "bentuk_kotak_hijau.png" },
                "Bentuk dan warna apa selanjutnya?",
                new String[] { "bentuk_lingkaran_merah.png", "bentuk_kotak_hijau.png",
                               "bentuk_segitiga_biru.png" },
                0),

            // 2. Pola pertumbuhan alami  (jawaban: daun)
            new Question(
                "Perhatikan pola pertumbuhan alami berikut!",
                new String[] { "quiz_leaf.png", "quiz_flower.png", "quiz_leaf.png", "quiz_flower.png" },
                "Apa yang tumbuh berikutnya?",
                new String[] { "quiz_flower.png", "quiz_leaf.png", "quiz_mushroom.png" },
                1),

            // 3. Pola jumlah telur 1,2,3,4 -> 5  (jawaban: telur_5)
            new Question(
                "Perhatikan pola jumlah telur berikut!",
                new String[] { "telur_1.png", "telur_2.png", "telur_3.png", "telur_4.png" },
                "Berapa jumlah telur selanjutnya?",
                new String[] { "telur_5.png", "telur_6.png", "telur_4.png" },
                0),

            // 4. Pola ukuran batu: kecil, kecil, BESAR, kecil, kecil -> BESAR
            //    "batu.png*0.5" = gambar batu.png diperkecil jadi 50%
            new Question(
                "Perhatikan pola ukuran berikut!",
                new String[] { "batu.png*0.5", "batu.png*0.5", "batu.png",
                               "batu.png*0.5", "batu.png*0.5" },
                "Ukuran batu apa yang selanjutnya?",
                new String[] { "batu.png", "batu.png*0.75", "batu.png*0.5" },
                0)

            // tambah soal pola lain di sini
        };
    }

    // ------------------------------------------------------------ LEVEL 2 & 3: cari yang berbeda
    // Question.odd(judul, gambarBiasa, gambarBerbeda, {pilihanA, B, C}, nomorJawabanBenar)
    private static Question[] oddBank()
    {
        return new Question[] {

            Question.odd("Cari telur yang berbeda!", "telur_utuh.png", "telur_retak.png",
                new String[] { "Telur Utuh", "Telur Retak", "Telur Emas" }, 1),

            Question.odd("Cari dino yang berbeda!", "dino_biasa.png", "dino_tidur.png",
                new String[] { "Dino Biasa", "Dino Tidur", "Dino Marah" }, 1),

            Question.odd("Cari buah yang berbeda!", "buah_sehat.png", "buah_busuk.png",
                new String[] { "Buah Sehat", "Buah Busuk", "Buah Berduri" }, 1),

            Question.odd("Cari bunga yang berbeda!", "bunga_sehat.png", "bunga_layu.png",
                new String[] { "Bunga Sehat", "Bunga Layu", "Bunga Berduri" }, 1)

            // tambah soal "cari yang berbeda" lain di sini
        };
    }
}
