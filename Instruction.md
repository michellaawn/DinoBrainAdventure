==========================================================
 DINO BRAIN  -  game platformer + kuis Mystery Box (Greenfoot)
==========================================================

CARA MENJALANKAN
  Buka folder ini di Greenfoot -> Compile All -> Run.
  Game otomatis mulai dari MainMenu.

ALUR GAME
  MainMenu -> StoryWorld (cerita) -> LevelIntroWorld (loading "LEVEL 01")
  -> Level1 -> LevelIntroWorld(2) -> Level2 -> LevelIntroWorld(3) -> Level3
  -> VictoryWorld.      Nyawa habis -> GameOverWorld (Coba Lagi / Menu).

  Di tiap level: sentuh Mystery Box -> jawab kuis -> bendera finish aktif.
  Jawaban benar = +100 skor. Salah / waktu habis = nyawa -1.

KONTROL
  Kiri/Kanan atau A/D  : jalan
  Spasi / Atas / W     : lompat
  P atau ESC           : pause
  Kuis: klik pilihan, atau tekan 1/2/3 atau A/B/C

----------------------------------------------------------
 YANG TINGGAL KAMU MODIFIKASI
----------------------------------------------------------
 1. SOAL KUIS   -> QuizData.java
      Satu level = satu soal. Isi pola & pilihan jawaban boleh berupa
      nama file gambar (misal "quiz_leaf.png") atau teks biasa ("32").
      Level 1 = contoh dari desain. Level 2 & 3 = placeholder (ada tanda TODO).

 2. DESAIN LEVEL -> LevelData.java
      Peta 16x9 berupa teks. Legenda ada di atas file itu
      (# tanah, ~ air, ^ duri, o koin, ? Mystery Box, S start, F finish, T pohon).
      Ganti background level lewat field "background".

 3. ANGKA GAME  -> GameConfig.java
      Kecepatan, tinggi lompat, nyawa, skor, batas waktu kuis.

 4. CERITA      -> StoryWorld.java (array STORY)
 5. WARNA UI    -> UI.java (palet warna di bagian atas)
 6. Kalau satu level butuh objek khusus -> Level1/2/3.java (prepareExtras)

----------------------------------------------------------
 STRUKTUR KODE
----------------------------------------------------------
 DATA / PENGATUR
   GameConfig   semua konstanta
   GameState    skor, nyawa, koin yang terbawa antar level
   Question     bentuk data satu soal
   QuizData     bank soal           <- edit
   LevelData    peta + background   <- edit
   Assets, UI   pemuat gambar & alat gambar (teks, panel, bintang)

 WORLD (layar)
   MainMenu, StoryWorld, HowToPlayWorld, LevelIntroWorld,
   BaseLevel (induk) -> Level1, Level2, Level3,
   GameOverWorld, VictoryWorld

 ACTOR (objek)
   Dino, Coin, Spike, MysteryBox, Flag
   QuizPanel (jendela kuis), DimLayer, HUD, PausePanel
   Button, Sprite, FloatingText, StoryDialog, DinoWalker, LoadingBar

 Nama lama -> nama baru
   bg + Beranda_Logo        -> MainMenu
   GAMESTART + dialog       -> StoryWorld + StoryDialog
   LearnGame + panel_kontrol + tombol_ok -> HowToPlayWorld
   TransitionLevel1         -> LevelIntroWorld (dipakai semua level)
   Level1World              -> BaseLevel + Level1/2/3
   Dino, DinoPlayer         -> Dino (DinoPlayer dihapus, tidak dipakai)
   Tileset, Platform, Ground, grass1/2, dggrass, Tree, time, love, tutorial
                            -> dihapus; tanah digambar langsung dari peta
   SettingsButton           -> tombol pause di BaseLevel

 ASET BARU (folder images)
   bg_level1/2/3.png, bg_story.png, bg_menu.png  background 800x450 (lebih ringan)
   dirt.png, tile_grass.png                      tile tanah
   quiz_leaf/flower/mushroom.png                 ikon soal contoh (dari desain)


==================  PEMBARUAN (kuis acak, pengaturan, level 2 & 3)  ==================

KUIS (Question.java + QuizData.java + QuizPanel.java)
  Level 1     : soal POLA, dipilih acak dari patternBank() (bentuk, tumbuhan, telur, batu).
  Level 2 & 3 : soal "CARI YANG BERBEDA" (kisi 3x5), dipilih acak dari oddBank().
                Level 3 tidak mengulang soal level 2.
  Urutan pilihan A/B/C diacak; posisi gambar yang berbeda juga acak setiap kuis dibuka.
  Tambah soal pola  : salin blok  new Question(...)  di patternBank().
  Tambah soal berbeda: salin blok  Question.odd(judul, gambarBiasa, gambarBerbeda, {A,B,C}, benar)  di oddBank().
  Gambar boleh diberi skala:  "batu.png*0.5"  (ukuran 50%).
  Semua gambar kuis ada di folder images (bentuk_*, telur_*, batu, buah_*, dino_*, bunga_*).
  Gambar-gambar itu placeholder: timpa dengan gambarmu sendiri, cukup pakai nama file yang sama.

PENGATURAN (SettingsPanel.java, Settings.java, Audio.java)
  Klik tombol gear (atau tekan P / Esc) -> panel PENGATURAN muncul: slider Musik & Efek Suara,
  tombol - / +, tombol mute, RESUME (lanjut) dan QUIT (ke Menu Utama).
  Latar panel: images/panel_pengaturan.png, tombol: btn_resume.png dan btn_quit.png.
  Suara (opsional) - taruh file di folder sounds, namanya:
    music.mp3  coin.wav  benar.wav  salah.wav
  Kalau file belum ada, game tetap jalan tanpa suara.

LEVEL (LevelData.java)
  Level 2: pulau-pulau melayang di atas danau (bg_level2_lake.png).
  Level 3: pulau melayang + duri di latar senja (bg_level3_sunset.png).
  Tanah/air digambar rapat tanpa celah (BaseLevel.drawTile / drawWater), duri diturunkan 4 px.


LEVEL 3 = KARTU MEMORI (MemoryBoard.java, MemoryPanel.java)
  Mystery Box di Level 3 membuka permainan kartu memori (bukan kuis).
  Klik dua kartu; kalau gambarnya sama kartu tetap terbuka. Cocokkan semua pasangan
  sebelum waktu habis = BENAR (+poin), waktu habis = SALAH (-1 nyawa).
  Setiap kali Mystery Box dibuka, gambar kartu yang dipakai dan posisinya diacak.
  Gambar kartu: back_6.png (punggung) + bone_1, coin_1, diamond_1, egg_2, leaf_2, meat_1, star_2.
  Atur jumlah pasangan dan waktu di GameConfig (MEMORY_PAIRS, MEMORY_TIME_SECONDS).
  Tambah gambar kartu baru: taruh di images lalu tulis namanya di MemoryBoard.FACES.
  Level 1: pohon digeser ke kanan (huruf T di LevelData level 1 dipindah dari kolom 2 ke kolom 4).
