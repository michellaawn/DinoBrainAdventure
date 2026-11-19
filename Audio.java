import greenfoot.*;
import java.util.HashMap;

/**
 * Pemutar suara yang aman: kalau file-nya belum ada di folder "sounds",
 * tidak terjadi apa-apa (tidak error).
 *
 * Nama file yang dicari (taruh di folder sounds):
 *   music.mp3     musik latar (diputar berulang)
 *   coin.mp3      ambil koin
 *   benar.mp3     jawaban kuis benar
 *   salah.mp3     jawaban kuis salah / waktu habis
 *   gameover.mp3  kalah / nyawa habis
 *   victory.mp3   menang / selesai level
 * Volume diatur dari panel Pengaturan (Settings).
 */
public class Audio
{
    private static GreenfootSound music;
    private static boolean musicTried = false;
    private static final HashMap<String, GreenfootSound> sfx = new HashMap<String, GreenfootSound>();

    private static GreenfootSound load(String file)
    {
        if (!new java.io.File("sounds/" + file).exists()) return null;
        try { return new GreenfootSound(file); }
        catch (Exception e) { return null; }
    }

    public static void startMusic()
    {
        if (!musicTried)
        {
            musicTried = true;
            music = load("music.mp3");
        }
        if (music != null)
        {
            music.setVolume(Settings.musicLevel());
            if (!music.isPlaying()) music.playLoop();
        }
    }

    public static void pauseMusic()
    {
        if (music != null) music.pause();
    }

    /** Dipanggil setelah volume/mute berubah. */
    public static void refreshVolume()
    {
        if (music != null) music.setVolume(Settings.musicLevel());
    }

    public static void play(String file)
    {
        if (Settings.sfxLevel() <= 0) return;
        if (!sfx.containsKey(file)) sfx.put(file, load(file));
        GreenfootSound s = sfx.get(file);
        if (s == null) return;
        try
        {
            s.setVolume(Settings.sfxLevel());
            s.play();
        }
        catch (Exception e) { }
    }
}