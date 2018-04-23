package com.farasyidk.pcdnoise;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.farasyidk.pcdnoise.Matriks;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button btnAdd, btnReduce;
    ImageView img1, img2, img3;
    SeekBar seekBar;
    int valSeekbar;
    TextView valSeek;
    Bitmap a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnReduce = (Button) findViewById(R.id.btnReduce);
        seekBar = (SeekBar)findViewById(R.id.seekbar);
        valSeek = (TextView) findViewById(R.id.valSeek);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valSeekbar = 100-progress;
                //int value=progress;
                valSeek.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) img1.getDrawable();
                Bitmap gambar = bitmapDrawable.getBitmap();
                img2.setImageBitmap(noise(gambar, valSeekbar));
            }
        });

        btnReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) img3.getDrawable();
                Bitmap gambar = bitmapDrawable.getBitmap();
                img2.setImageBitmap(hapus(gambar));
            }
        });
    }

    public static Bitmap hapus(Bitmap source) {
        double[][] GaussianBlurConfig = new double[][] {
                { 1, 2, 1 },
                { 2, 4, 2 },
                { 1, 2, 1 }
        };

        Matriks convMatrik = new Matriks(3);
        convMatrik.applyConfig(GaussianBlurConfig);
        convMatrik.Factor = 14;
        convMatrik.Offset = 0;
        return Matriks.computeConvolution3x3(source, convMatrik);
    }

    public static Bitmap noise(Bitmap source, int percentNoise) {
        int width = source.getWidth();
        int height = source.getHeight();
        int[] pixels = new int[width * height];
        source.getPixels(pixels, 0, width, 0, 0, width, height);
        Random random = new Random();

        int index;
        int c;
        int randColor;
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (random.nextInt(101) < percentNoise) {
                    continue;
                }
                index = y * width + x;
                c = random.nextInt(255);
                randColor = Color.rgb(c, c, c);
                pixels[index] |= randColor;
            }
        }

        Bitmap bmOut = Bitmap.createBitmap(width, height, source.getConfig());
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }

}
