import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Logika permainan kartu memori (tanpa gambar, hanya aturan).
 * Dipakai oleh MemoryPanel yang menggambar dan menerima klik.
 *
 * Setiap papan baru: gambar kartu dipilih ACAK dari FACES, lalu posisinya diacak.
 */
public class MemoryBoard
{
    /** Gambar punggung kartu. */
    public static final String BACK = "back_6.png";

    /** Semua gambar muka kartu yang bisa keluar. Tambah nama file baru di sini. */
    public static final String[] FACES = {
        "bone_1.png", "coin_1.png", "diamond_1.png", "egg_2.png",
        "leaf_2.png", "meat_1.png", "star_2.png"
    };

    // hasil pick()
    public static final int NONE = 0, FIRST = 1, MATCH = 2, MISMATCH = 3;

    public final int pairs;
    public final String[] card;          // gambar tiap kartu, sudah diacak
    private final boolean[] matched;
    private int open1 = -1, open2 = -1;  // kartu yang sedang terbuka (belum dinilai)
    private int matchedPairs = 0;
    private int tries = 0;

    public MemoryBoard(int pairs)
    {
        if (pairs > FACES.length) pairs = FACES.length;
        this.pairs = pairs;

        ArrayList<String> pool = new ArrayList<String>(Arrays.asList(FACES));
        Collections.shuffle(pool);                       // gambar apa yang dipakai: acak
        ArrayList<String> deck = new ArrayList<String>();
        for (int i = 0; i < pairs; i++)
        {
            deck.add(pool.get(i));
            deck.add(pool.get(i));
        }
        Collections.shuffle(deck);                       // posisi kartu: acak

        card = deck.toArray(new String[0]);
        matched = new boolean[card.length];
    }

    public int size() { return card.length; }

    /** Kartu menghadap ke atas (sudah cocok atau sedang dibuka)? */
    public boolean isFaceUp(int i) { return matched[i] || i == open1 || i == open2; }

    public boolean isMatched(int i) { return matched[i]; }

    /** Boleh diklik? Tidak boleh kalau sudah terbuka/cocok, atau dua kartu sedang menunggu ditutup. */
    public boolean canPick(int i)
    {
        return i >= 0 && i < card.length && !matched[i] && i != open1 && i != open2 && open2 == -1;
    }

    public int pick(int i)
    {
        if (!canPick(i)) return NONE;

        if (open1 == -1)
        {
            open1 = i;
            return FIRST;
        }

        open2 = i;
        tries++;
        if (card[open1].equals(card[open2]))
        {
            matched[open1] = true;
            matched[open2] = true;
            matchedPairs++;
            open1 = -1;
            open2 = -1;
            return MATCH;
        }
        return MISMATCH;                                 // dua kartu terbuka; panggil closeOpen() nanti
    }

    /** Menutup dua kartu yang tidak cocok. */
    public void closeOpen()
    {
        open1 = -1;
        open2 = -1;
    }

    public boolean isComplete() { return matchedPairs == pairs; }
    public int getMatchedPairs() { return matchedPairs; }
    public int getTries() { return tries; }
}
