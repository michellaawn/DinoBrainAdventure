/**
 * ====================  DESAIN LEVEL  ====================
 * Tiap level = peta 16 kolom x 9 baris (1 karakter = 1 kotak 50x50 px).
 * Baris paling atas (0) dipakai HUD, jadi biarkan kosong.
 *
 *   .  kosong              #  tanah berumput (padat, bisa diinjak)
 *   d  tanah (padat)       ~  air (jatuh = mati)
 *   ^  duri (kena = mati)  o  koin
 *   ?  Mystery Box (kuis)  S  posisi awal Dino
 *   F  bendera finish      T  pohon (hiasan saja)
 *
 * Syarat: tiap level harus punya tepat 1 S, 1 F dan 1 ?
 * Bendera baru aktif setelah Mystery Box dibuka dan kuisnya dijawab.
 */
public class LevelData
{
    public final String   name;
    public final String   tip;
    public final String   background;
    public final String[] map;

    private LevelData(String name, String tip, String background, String[] map)
    {
        this.name = name;
        this.tip = tip;
        this.background = background;
        this.map = map;
    }

    public static LevelData get(int level)
    {
        switch (level)
        {
            case 1:
                return new LevelData(
                    "THE FOREST OF KNOWLEDGE",
                    "Tip: sentuh Mystery Box lalu jawab kuisnya!",
                    "bg1.jpg",
                    new String[] {
                        //0123456789ABCDEF
                        "................",   // 0 (HUD)
                        "................",   // 1
                        "................",   // 2
                        "................",   // 3
                        ".......?........",   // 4
                        "...oo.###..ooo..",   // 5
                        ".S...o.....^..F.",    // 6
                        "###~~###########",   // 7
                        "ddd~~ddddddddddd"    // 8
                    });

            case 2:
                return new LevelData(
                    "THE SKY OF PUZZLES",
                    "Tip: loncati pulau-pulau melayang, jangan jatuh ke air!",
                    "bg2.jpg",
                    new String[] {
                        //0123456789ABCDEF
                        "................",   // 0 (HUD)
                        "................",   // 1
                        "................",   // 2
                        "................",   // 3
                        "...?....oo.ooo..",   // 4
                        "...##oo.##.###..",   // 5
                        ".S...##...o....F",   // 6
                        "#####~~~~~######",   // 7
                        "ddddd~~~~~dddddd"    // 8
                    });

            default:
                return new LevelData(
                    "THE PEAK OF LOGIC",
                    "Tip: buka Mystery Box lalu cocokkan semua pasangan kartu!",
                    "bg_level3_sunset.png",
                    new String[] {
                        //0123456789ABCDEF
                        "................",   // 0 (HUD)
                        "................",   // 1
                        "................",   // 2
                        ".............ooo",   // 3
                        ".....o..o.o?.###",   // 4
                        ".....#o.#.##....",   // 5
                        ".S...o.....^..F.",   // 6
                        "######~~########",   // 7
                        "dddddd~~dddddddd"    // 8
                    });
        }
    }
}
