package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button Kaydet;
    Button Goster;
    Button Sil;
    Button Guncelle;
    EditText ad;
    EditText soyad;
    EditText yas;
    EditText sehir;
    TextView Bilgiler;
    private veritabani v1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v1 = new veritabani(this);
        Kaydet = findViewById(R.id.buttonKayit);
        Goster = findViewById(R.id.buttonGoster);
        Sil = findViewById(R.id.buttonSil);
        Guncelle = findViewById(R.id.buttonGuncelle);
        ad = findViewById(R.id.editTextAd);
        soyad = findViewById(R.id.editTextSoyad);
        yas = findViewById(R.id.editTextYas);
        sehir = findViewById(R.id.editTextSehir);
        Bilgiler = findViewById(R.id.textViewBilgiler);

        Kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    KayitEkle(ad.getText().toString(), soyad.getText().toString(), yas.getText().toString(), sehir.getText().toString());
                } finally {
                    // Don't close the database here, as it's being used elsewhere.
                }
            }
        });

        Goster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor crs = KayitGetir();
                KayitGoster(crs);
            }
        });

        Sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call KayitSil method to delete a record
                String adi = ad.getText().toString();
                KayitSil(adi);
            }
        });

        Guncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addd = ad.getText().toString();
                String soyaddd = soyad.getText().toString();
                String yasss = yas.getText().toString();
                String sehirrr = sehir.getText().toString();
                KayitGuncelle(addd, soyaddd, yasss, sehirrr);
            }
        });
    }

    private String[] sutunlar = {"ad","soyad","yas","sehir"};

    private Cursor KayitGetir() {
        SQLiteDatabase db = v1.getWritableDatabase();
        Cursor okunanlar = db.query("OgrenciBilgi", sutunlar, null, null, null, null, null);
        return okunanlar;
    }

    private void KayitGoster(Cursor goster) {
        StringBuilder builder = new StringBuilder();
        int adIndex = goster.getColumnIndex("ad");
        int soyadIndex = goster.getColumnIndex("soyad");
        int yasIndex = goster.getColumnIndex("yas");
        int sehirIndex = goster.getColumnIndex("sehir");

        if (adIndex != -1 && soyadIndex != -1 && yasIndex != -1 && sehirIndex != -1) {
            while (goster.moveToNext()) {
                String add = goster.getString(adIndex);
                String soyadd = goster.getString(soyadIndex);
                String yass = goster.getString(yasIndex);
                String sehirr = goster.getString(sehirIndex);
                builder.append("Ad: ").append(add).append("\n");
                builder.append("Soyad: ").append(soyadd).append("\n");
                builder.append("Yas: ").append(yass).append("\n");
                builder.append("Sehir: ").append(sehirr).append("\n");
                builder.append("----------------").append("\n");
            }
        } else {
            // Handle the case where one or more columns are missing
            builder.append("One or more columns are missing in the result set.");
        }

        TextView text = findViewById(R.id.textViewBilgiler);
        text.setText(builder.toString());
    }


    private void KayitEkle(String adi, String soyadi, String yasi, String sehri) {
        SQLiteDatabase db = v1.getWritableDatabase();
        ContentValues veriler = new ContentValues();
        veriler.put("ad", adi);
        veriler.put("soyad", soyadi);
        veriler.put("yas", yasi);
        veriler.put("sehir", sehri);
        db.insertOrThrow("OgrenciBilgi", null, veriler);
    }

    private void KayitSil(String adi) {
        SQLiteDatabase db = v1.getReadableDatabase();
        db.delete("OgrenciBilgi", "ad" + "=?", new String[]{adi});
    }

    private void KayitGuncelle(String addd, String soyaddd, String yasss, String sehirrr) {
        SQLiteDatabase db = v1.getWritableDatabase();
        ContentValues cvGuncelle = new ContentValues();
        cvGuncelle.put("ad", addd);
        cvGuncelle.put("soyad", soyaddd);
        cvGuncelle.put("yas", yasss);
        cvGuncelle.put("sehir", sehirrr);
        db.update("OgrenciBilgi", cvGuncelle, "ad" + "=?", new String[]{addd});
        db.close();
    }
}
