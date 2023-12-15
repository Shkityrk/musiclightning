package com.shkityrk.musiclightning;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private TextView lightTextView;
    private MediaPlayer mediaPlayer;
    private boolean isMusicPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightTextView = findViewById(R.id.lightTextView);

        // Инициализация MediaPlayer для воспроизведения музыки
        mediaPlayer = MediaPlayer.create(this, R.raw.song);
        mediaPlayer.setLooping(true); // Повторять воспроизведение

        if (lightSensor == null) {
            lightTextView.setText("Датчик освещенности не найден");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float currentLight = event.values[0];
            lightTextView.setText("Текущее освещение: " + currentLight + " люкс");

            // Проверка уровня освещенности для включения/выключения музыки
            if (currentLight <= 50 && !isMusicPlaying) {
                // Включение музыки
                mediaPlayer.start();
                isMusicPlaying = true;
            } else if (currentLight > 50 && isMusicPlaying) {
                // Выключение музыки
                mediaPlayer.pause();

                isMusicPlaying = false;
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Освобождение ресурсов MediaPlayer при уничтожении активности
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Метод, вызываемый при изменении точности датчика (не требуется в данном случае)
    }
}
