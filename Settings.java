/** Pengaturan suara (0-100). Disimpan selama game berjalan, dipakai oleh Audio. */
public class Settings
{
    public static int musicVolume = 70;
    public static int sfxVolume = 70;
    public static boolean musicMuted = false;
    public static boolean sfxMuted = false;

    /** Volume efektif musik (0 kalau di-mute). */
    public static int musicLevel() { return musicMuted ? 0 : musicVolume; }

    /** Volume efektif efek suara (0 kalau di-mute). */
    public static int sfxLevel() { return sfxMuted ? 0 : sfxVolume; }
}
