# Autograde

Selamat datang di Autograde - aplikasi ujian atau tes daring yang revolusioner! Aplikasi ini memanfaatkan teknologi LLM (Large Language Model) untuk secara otomatis menilai jawaban isian dan esai, memberikan penilaian yang akurat dan objektif. Dibangun dengan Android Studio menggunakan Java, aplikasi seluler ini memungkinkan anda untuk bergabung dalam ruang tes, menjawab soal essay, dan mendapatkan hasil penilaian otomatis secara real-time.

# Daftar Isi

- [Fitur](#fitur)
- [Penggunaan](#penggunaan)
- [Tangkapan Layar](#tangkapan-layar)

## Fitur

- **Join Test Room**: Memungkinkan anda untuk bergabung ke dalam ruang tes menggunakan kode unik yang diberikan oleh administrator
- **Timed Test**: Sistem timer otomatis yang menghitung waktu ujian dan memberikan notifikasi ketika waktu hampir habis
- **Essay Questions**: Interface yang user-friendly untuk menjawab soal-soal essay dengan text area yang responsive
- **LLM-Powered Auto Grading**: Teknologi Large Language Model yang canggih untuk menilai jawaban isian dan esai secara otomatis dengan akurasi tinggi
- **Navigation System**: Fitur navigasi antar soal dengan kemampuan untuk menandai dan membatalkan tanda pada soal tertentu
- **Real-time Submission**: Sistem pengiriman jawaban secara real-time ke server untuk pemrosesan dan penilaian otomatis
- **Automated Scoring**: Penilaian otomatis yang objektif dan konsisten menggunakan teknologi AI
- **Test Results**: Tampilan hasil ujian yang komprehensif dengan skor dan feedback dari sistem AI
- **Back Navigation**: Kemudahan untuk kembali ke halaman utama setelah menyelesaikan ujian

## Penggunaan

### Instalasi

Untuk mendapatkan salinan lokal dan menjalankannya, ikuti langkah-langkah sederhana berikut.

#### Prasyarat

- Android Studio terinstal di komputer Anda
- Java Development Kit (JDK) versi 8 atau lebih tinggi
- Android SDK dengan API level yang sesuai

#### Setup

1. **Kloning repositori:**
   ```sh
   git clone https://github.com/Yousran/final-mobile-2025.git
   ```

2. **Buka proyek di Android Studio:**
   - Buka Android Studio
   - Klik "Open an existing Android Studio project"
   - Navigasikan ke repositori yang telah dikloning dan klik OK

3. **Bangun proyek:**
   - Tunggu hingga build Gradle selesai
   - Selesaikan setiap dependensi jika diminta
   - Pastikan semua library yang diperlukan sudah terdownload

4. **Jalankan aplikasi:**
   - Pilih perangkat atau emulator Android
   - Klik tombol Run (tombol hijau)
   - Aplikasi akan ter-install dan berjalan di perangkat target

#### Cara Menggunakan Aplikasi

1. **Bergabung ke Test Room:**
   - Buka aplikasi Autograde
   - Masukkan kode ruang tes yang diberikan oleh administrator. untuk uji coba, anda bisa menggunakan kode berikut `` 24FUVG ``
   - Klik "Join" untuk masuk ke ruang ujian

2. **Mengikuti Ujian:**
   - Baca instruksi ujian dengan teliti
   - Klik "Start Test" untuk memulai ujian
   - Timer akan otomatis berjalan
   - Jawab setiap soal essay pada text area yang tersedia

3. **Navigasi Soal:**
   - Gunakan tombol "Next" dan "Previous" untuk berpindah antar soal
   - Mark soal yang ingin ditinjau kembali dengan tombol "Mark"
   - Unmark soal jika sudah yakin dengan jawaban

4. **Menyelesaikan Ujian:**
   - Pastikan semua soal sudah dijawab
   - Submit jawaban sebelum waktu habis
   - Sistem AI akan otomatis menilai jawaban anda
   - Lihat hasil penilaian dan feedback di halaman Test Result

## Tangkapan Layar

<img src="https://picsum.photos/300/600?random=1" width="200">
<img src="https://picsum.photos/300/600?random=2" width="200">
<img src="https://picsum.photos/300/600?random=3" width="200">
<img src="https://picsum.photos/300/600?random=4" width="200">
<img src="https://picsum.photos/300/600?random=5" width="200">

## Teknologi yang Digunakan

- **Platform**: Android
- **Bahasa Pemrograman**: Java
- **IDE**: Android Studio
- **UI Components**: Android Views dan Fragments
- **AI Technology**: Large Language Model (LLM) untuk penilaian otomatis yang di implementasikan di API
- **Networking**: HTTP requests untuk komunikasi dengan server dan API LLM
- **Database**: SQLite untuk penyimpanan lokal (jika diperlukan)

## Repository

Proyek ini tersedia di GitHub: [https://github.com/Yousran/final-mobile-2025.git](https://github.com/Yousran/final-mobile-2025.git)

## Kontribusi

Kontribusi selalu diterima! Silakan buat pull request atau buka issue untuk saran dan perbaikan.

## Lisensi

Proyek ini dibuat untuk keperluan akademik di Universitas Hasanuddin, Semester 4, Lab Pemrograman Mobile.
