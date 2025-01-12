import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// BaseEntity Sınıfı
abstract class BaseEntity {
    private int id;
    private String name;

    public BaseEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract String toJson();
}

// Müşteri Sınıfı
class Musteri extends BaseEntity {
    public Musteri(int id, String name) {
        super(id, name);
    }

    @Override
    public String toJson() {
        return String.format("{\"id\": %d, \"name\": \"%s\"}", getId(), getName());
    }
}

// Film Sınıfı
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
        return String.format("{\"ad\": \"%s\", \"sure\": %d, \"tur\": \"%s\"}", ad, sure, tur);
    }
}

// Salon Sınıfı
class Salon extends BaseEntity {
    private Film film;
    private boolean[] koltuklar;
    private List<Musteri> musteriler;

    public Salon(int id, String name, Film film, int koltukSayisi) {
        super(id, name);
        this.film = film;
        this.koltuklar = new boolean[koltukSayisi];
        this.musteriler = new ArrayList<>();
    }

    public Film getFilm() {
        return film;
    }

    public List<Musteri> getMusteriler() {
        return musteriler;
    }

    public void musteriEkle(Musteri musteri) {
        musteriler.add(musteri);
    }

    public boolean koltukRezerveEt(int koltukNo, Musteri musteri) {
        if (koltukNo < 0 || koltukNo >= koltuklar.length || koltuklar[koltukNo]) {
            System.out.println("Rezervasyon yapılamadı. Geçersiz veya dolu koltuk.");
            return false;
        }
        koltuklar[koltukNo] = true;
        musteriEkle(musteri);
        System.out.println("Rezervasyon başarılı! Koltuk No: " + (koltukNo + 1));
        return true;
    }

    public double dolulukOrani() {
        int doluKoltuk = 0;
        for (boolean koltuk : koltuklar) {
            if (koltuk) doluKoltuk++;
        }
        return (doluKoltuk / (double) koltuklar.length) * 100;
    }

    @Override
    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{\"id\": ").append(getId()).append(", \"name\": \"").append(getName()).append("\", ");
        json.append("\"film\": ").append(film.toJson()).append(", ");
        json.append("\"musteriler\": [");
        for (int i = 0; i < musteriler.size(); i++) {
            json.append(musteriler.get(i).toJson());
            if (i < musteriler.size() - 1) json.append(", ");
        }
        json.append("]}");
        return json.toString();
    }
}

// Main Sınıfı
public class Main {
    public static void main(String[] args) {
        List<Musteri> musteriler = new ArrayList<>();
        List<Film> filmler = new ArrayList<>();
        List<Salon> salonlar = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);

        // Örnek Filmler
        filmler.add(new Film("Inception", 148, "Bilim Kurgu"));
        filmler.add(new Film("The Dark Knight", 152, "Aksiyon"));
        filmler.add(new Film("Titanic", 195, "Romantik"));

        // Örnek Salonlar
        salonlar.add(new Salon(1, "Salon 1", filmler.get(0), 10));
        salonlar.add(new Salon(2, "Salon 2", filmler.get(1), 8));
        salonlar.add(new Salon(3, "Salon 3", filmler.get(2), 12));

        while (true) {
            System.out.println("\n1. Yeni Müşteri Ekle");
            System.out.println("2. Yeni Film Ekle");
            System.out.println("3. Salon Rezervasyonu Yap");
            System.out.println("4. Salon Doluluk Oranını Gör");
            System.out.println("5. Tüm Rezervasyonları Görüntüle");
            System.out.println("6. Çıkış");
            System.out.print("Seçiminizi yapın: ");
            int secim = scanner.nextInt();
            scanner.nextLine(); // Enter'ı tüketmek için

            switch (secim) {
                case 1:
                    System.out.print("Müşteri Adı: ");
                    String musteriAdi = scanner.nextLine();
                    Musteri yeniMusteri = new Musteri(musteriler.size() + 1, musteriAdi);
                    musteriler.add(yeniMusteri);
                    System.out.println("Müşteri başarıyla eklendi.");
                    break;

                case 2:
                    System.out.print("Film Adı: ");
                    String filmAdi = scanner.nextLine();
                    System.out.print("Film Süresi (dakika): ");
                    int filmSure = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Film Türü: ");
                    String filmTur = scanner.nextLine();
                    Film yeniFilm = new Film(filmAdi, filmSure, filmTur);
                    filmler.add(yeniFilm);
                    System.out.println("Film başarıyla eklendi.");
                    break;

                case 3:
                    System.out.println("Salon Seçin:");
                    for (int i = 0; i < salonlar.size(); i++) {
                        System.out.println((i + 1) + ". " + salonlar.get(i).getName());
                    }
                    int salonSecim = scanner.nextInt() - 1;
                    if (salonSecim >= 0 && salonSecim < salonlar.size()) {
                        Salon secilenSalon = salonlar.get(salonSecim);
                        System.out.print("Koltuk numarası: ");
                        int koltukNo = scanner.nextInt() - 1;
                        scanner.nextLine();
                        System.out.print("Müşteri Adı: ");
                        String isim = scanner.nextLine();
                        Musteri musteri = new Musteri(musteriler.size() + 1, isim);
                        secilenSalon.koltukRezerveEt(koltukNo, musteri);
                    } else {
                        System.out.println("Geçersiz seçim.");
                    }
                    break;

                case 4:
                    System.out.println("Salon Seçin:");
                    for (int i = 0; i < salonlar.size(); i++) {
                        System.out.println((i + 1) + ". " + salonlar.get(i).getName());
                    }
                    int dolulukSecim = scanner.nextInt() - 1;
                    if (dolulukSecim >= 0 && dolulukSecim < salonlar.size()) {
                        Salon secilenSalon = salonlar.get(dolulukSecim);
                        System.out.printf("Doluluk Oranı: %.2f%%\n", secilenSalon.dolulukOrani());
                    } else {
                        System.out.println("Geçersiz seçim.");
                    }
                    break;

                case 5:
                    System.out.println("[Tüm Rezervasyonlar]");
                    for (Salon salon : salonlar) {
                        System.out.println("Salon: " + salon.getName());
                        for (Musteri musteri : salon.getMusteriler()) {
                            System.out.println("- Müşteri: " + musteri.getName());
                        }
                    }
                    break;

                case 6:
                    System.out.println("Çıkış yapılıyor...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Geçersiz seçim.");
            }
        }
    }
}
