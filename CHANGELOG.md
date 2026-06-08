# Changelog - NaturalVote 🗳️

Dokumentasi riwayat pembaruan, perbaikan bug, dan rilis fitur untuk plugin **NaturalVote** (Voting & Rewards Plugin).

---

## [v1.0.5] - GUI & CI/CD Update
### ✨ Fitur Baru
- **Default Vote GUI**: Perintah `/vote` kini secara default akan membuka antarmuka GUI daripada sekadar menampilkan link di chat box.
- **`/vote gui <player>` Command**: Perintah khusus bagi admin/console untuk memicu pembukaan GUI voting ke target player tertentu.
- **GUI Design Polish**: Pembaruan tata letak, warna, dan tautan (link) voting agar lebih menarik dan intuitif bagi pemain.

### ⚙️ CI/CD & Build
- **JAR Rename Protection**: Memaksa penamaan berkas akhir kompilasi agar selalu menghasilkan berkas bernama `NaturalVote.jar` guna mencegah kerancuan versi di server.
- **NuVotifier Download Automation**: Mengotomatiskan proses pengunduhan NuVotifier dari GitHub Releases resmi dan memasangnya secara lokal di lingkungan CI (GitHub Actions) untuk mempermudah pengujian.
- **GitHub Release Fix**: Perbaikan pencocokan path dan judul berkas rilis (artifact path & title) pada workflow GitHub Actions.
