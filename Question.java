/**
 * Satu soal Mystery Box. Isinya cuma data, tidak ada logika.
 *
 * Ada DUA jenis soal:
 *   PATTERN : "Perhatikan pola ... apa selanjutnya?"  (pattern + 3 pilihan)
 *   ODD     : "Cari ... yang berbeda!"  (kisi 3x5 gambar yang sama, satu kotak
 *             berbeda; pemain memilih nama gambar yang berbeda itu)
 *
 * Isi pattern / options (jenis PATTERN) boleh berupa:
 *   - nama file gambar di folder images (berakhiran .png / .jpg), atau
 *   - nama gambar + skala:  "batu.png*0.5"  (ukuran 50%), atau
 *   - teks biasa (misal "32").
 */
public class Question
{
    public static final int PATTERN = 0;
    public static final int ODD     = 1;

    public int      kind = PATTERN;
    public String   instruction;   // kalimat judul soal
    public String[] pattern;       // (PATTERN) deretan pola; kotak "?" otomatis di akhir
    public String   prompt;        // (PATTERN) pertanyaan di atas pilihan jawaban
    public String[] options;       // 3 pilihan jawaban: A, B, C
    public int      correct;       // jawaban benar: 0 = A, 1 = B, 2 = C
    public int      seconds;       // batas waktu (detik)

    public String   itemImage;     // (ODD) gambar yang mengisi hampir semua kotak
    public String   oddImage;      // (ODD) gambar yang berbeda (hanya 1 kotak, posisinya acak)

    public Question(String instruction, String[] pattern, String prompt,
                    String[] options, int correct)
    {
        this(instruction, pattern, prompt, options, correct,
             GameConfig.QUIZ_TIME_SECONDS);
    }

    public Question(String instruction, String[] pattern, String prompt,
                    String[] options, int correct, int seconds)
    {
        this.instruction = instruction;
        this.pattern = pattern;
        this.prompt = prompt;
        this.options = options;
        this.correct = correct;
        this.seconds = seconds;
    }

    /** Membuat soal jenis "Cari yang berbeda". labels = 3 nama pilihan (A, B, C). */
    public static Question odd(String instruction, String itemImage, String oddImage,
                               String[] labels, int correct)
    {
        Question q = new Question(instruction, null, "", labels, correct);
        q.kind = ODD;
        q.itemImage = itemImage;
        q.oddImage = oddImage;
        return q;
    }

    /** Salinan soal dengan urutan pilihan A/B/C diacak (kunci jawaban ikut disesuaikan). */
    public Question shuffled()
    {
        java.util.ArrayList<Integer> idx = new java.util.ArrayList<Integer>();
        for (int i = 0; i < options.length; i++) idx.add(i);
        java.util.Collections.shuffle(idx);

        String[] o = new String[options.length];
        int newCorrect = 0;
        for (int i = 0; i < idx.size(); i++)
        {
            o[i] = options[idx.get(i)];
            if (idx.get(i) == correct) newCorrect = i;
        }

        Question r = new Question(instruction, pattern, prompt, o, newCorrect, seconds);
        r.kind = kind;
        r.itemImage = itemImage;
        r.oddImage = oddImage;
        return r;
    }
}
