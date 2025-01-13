import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

// Film sınıfı
class Film {
    private String ad;
    private int sure;
    private String tur;

    public Film(String ad, int sure, String tur) {
        this.ad = ad;
        this.sure = sure;
        this.tur = tur;
    }

    public String getAd() {
        return ad;
    }

    public String getTur() {
        return tur;
    }

    public int getSure() {
        return sure;
    }

    public String toJson() {
        return "{\n" +
                "  \"ad\": \"" + ad + "\",\n" +
                "  \"sure\": " + sure + ",\n" +
                "  \"tur\": \"" + tur + "\"\n" +
                "}";
    }
}

// Müşteri sınıfı
class Musteri {
    private String isim;
    private String telefonNo;

    public Musteri(String isim, String telefonNo) {
        this.isim = isim;
        this.telefonNo = telefonNo;
    }

    public String getIsim() {
        return isim;
    }

    public String getTelefonNo() {
        return telefonNo;
    }
}

// Salon sınıfı
class Salon {
    private String name;
    private Film film;
    private boolean[] koltuklar;

    public Salon(String name, Film film, int koltukSayisi) {
        this.name = name;
        this.film = film;
        this.koltuklar = new boolean[koltukSayisi];
    }

    public String getName() {
        return name;
    }

    public Film getFilm() {
        return film;
    }

    public void bosKoltuklariGoster() {
        System.out.print("Boş Koltuklar: ");
        for (int i = 0; i < koltuklar.length; i++) {
            if (!koltuklar[i]) {
                System.out.print((i + 1) + " ");
            }
        }
        System.out.println();
    }

    public void dolulukDurumunuGoster() {
        System.out.println("Salon: " + name);
        for (int i = 0; i < koltuklar.length; i++) {
            System.out.println("Koltuk " + (i + 1) + ": " + (koltuklar[i] ? "Dolu" : "Boş"));
        }
    }

    public boolean koltukRezerveEt(int koltukNo, Musteri musteri) {
        if (koltukNo < 0 || koltukNo >= koltuklar.length) {
            System.out.println("Geçersiz koltuk numarası.");
            return false;
        }
        if (koltuklar[koltukNo]) {
            System.out.println("Bu koltuk zaten dolu.");
            return false;
        }
        koltuklar[koltukNo] = true;
        System.out.println("Rezervasyon başarılı! Koltuk No: " + (koltukNo + 1));

        // Rezervasyonu JSON'a kaydet
        saveRezervasyonToJson(musteri, koltukNo);
        return true;
    }

    private void saveRezervasyonToJson(Musteri musteri, int koltukNo) {
        String json = "{\n" +
                "  \"salon\": \"" + name + "\",\n" +
                "  \"film\": \"" + film.getAd() + "\",\n" +
                "  \"musteri\": {\n" +
                "    \"isim\": \"" + musteri.getIsim() + "\",\n" +
                "    \"telefonNo\": \"" + musteri.getTelefonNo() + "\"\n" +
                "  },\n" +
                "  \"koltukNo\": " + (koltukNo + 1) + "\n" +
                "}\n";

        try (FileWriter writer = new FileWriter("rezervasyon.json", true)) {
            writer.write(json);
            writer.write(",\n");
        } catch (IOException e) {
            System.out.println("Rezervasyon kaydedilirken bir hata oluştu: " + e.getMessage());
        }
    }
}

// Ana sınıf (Main)
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Salon> salonlar = new ArrayList<>();
        List<Film> filmler = new ArrayList<>();

        // Örnek salonlar ve filmler
        Film inception = new Film("Inception", 148, "Bilim Kurgu/Aksiyon");
        Film darkKnight = new Film("The Dark Knight", 152, "Aksiyon/Suç");
        Film titanic = new Film("Titanic", 195, "Romantik/Dram");

        filmler.add(inception);
        filmler.add(darkKnight);
        filmler.add(titanic);

        salonlar.add(new Salon("Salon 1 - Inception -> 148 Dakika", inception, 10));
        salonlar.add(new Salon("Salon 2 - The Dark Knight -> 152 Dakika", darkKnight, 10));
        salonlar.add(new Salon("Salon 3 - Titanic -> 195 Dakika", titanic, 10));

        // Filmleri JSON dosyasına kaydet
        saveFilmlerToJson(filmler);

        while (true) {
            System.out.println("1. Salon ve Seansları Listele");
            System.out.println("2. Seans ve Koltuk Seçimi");
            System.out.println("3. Tüm Koltuk Doluluk Durumlarını Göster");
            System.out.println("4. Çıkış");
            System.out.print("Lütfen bir işlem seçiniz: ");
            int secim = scanner.nextInt();

            switch (secim) {
                case 1:
                    System.out.println("\n[Salon ve Seanslar]");
                    for (int i = 0; i < salonlar.size(); i++) {
                        Salon salon = salonlar.get(i);
                        System.out.println((i + 1) + ". " + salon.getName() + " - Film: " + salon.getFilm().getAd());
                    }
                    break;

                case 2:
                    System.out.println("\n[Salon Seçimi]");
                    for (int i = 0; i < salonlar.size(); i++) {
                        System.out.println((i + 1) + ". " + salonlar.get(i).getName());
                    }
                    System.out.print("Salon numarasını seçiniz: ");
                    int salonNo = scanner.nextInt() - 1;

                    if (salonNo >= 0 && salonNo < salonlar.size()) {
                        Salon secilenSalon = salonlar.get(salonNo);
                        System.out.println("Seçilen Salon: " + secilenSalon.getName());
                        System.out.println("Film: " + secilenSalon.getFilm().getAd());
                        secilenSalon.bosKoltuklariGoster();

                        System.out.print("Rezervasyon yapmak istediğiniz koltuk numarasını giriniz: ");
                        int koltukNo = scanner.nextInt() - 1;
                        scanner.nextLine();

                        System.out.print("Adınızı giriniz: ");
                        String isim = scanner.nextLine();
                        System.out.print("Telefon numaranızı giriniz: ");
                        String telefonNo = scanner.nextLine();

                        Musteri musteri = new Musteri(isim, telefonNo);
                        secilenSalon.koltukRezerveEt(koltukNo, musteri);
                    } else {
                        System.out.println("Hatalı seçim. Lütfen geçerli bir salon seçiniz.");
                    }
                    break;

                case 3:
                    System.out.println("\n[Tüm Salonların Doluluk Durumu]");
                    for (Salon salon : salonlar) {
                        salon.dolulukDurumunuGoster();
                        System.out.println("---------------------------------------------");
                    }
                    break;

                case 4:
                    System.out.println("Çıkış yapılıyor...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Hatalı seçim. Lütfen tekrar deneyin.");
            }
        }
    }

    private static void saveFilmlerToJson(List<Film> filmler) {
        try (FileWriter writer = new FileWriter("filmler.json")) {
            writer.write("[\n");
            for (int i = 0; i < filmler.size(); i++) {
                writer.write(filmler.get(i).toJson());
                if (i < filmler.size() - 1) {
                    writer.write(",\n");
                }
            }
            writer.write("\n]");
        } catch (IOException e) {
            System.out.println("Filmler kaydedilirken bir hata oluştu: " + e.getMessage());
        }
    }
}
