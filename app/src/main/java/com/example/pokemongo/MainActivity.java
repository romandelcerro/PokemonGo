package com.example.pokemongo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
    Button btnCapturar, btnListar, btnIntroducir;

    private boolean botonPulsado = false;
    SQLiteDatabase db;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    LocationManager manejador;
    Double latitud;
    Double longitud;
    String proveedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCapturar = findViewById(R.id.btnCapturar);
        btnListar = findViewById(R.id.btnListar);
        btnIntroducir = findViewById(R.id.btnIntroducir);

        db = openOrCreateDatabase("Pokemon", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Pokemon(Nombre VARCHAR, Tipo VARCHAR, Estado VARCHAR, Localizacion VARCHAR)");

        manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Comprobar si el gps está habilitado
        if (!manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, R.string.txtGps, Toast.LENGTH_SHORT).show();
        }
        // Solicitar permisos de ubicación
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            Criteria criteria = new Criteria();
            proveedor = manejador.getBestProvider(criteria, false);

            manejador.requestLocationUpdates(proveedor, 1000, 10, new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            latitud = location.getLatitude();
                            longitud = location.getLongitude();

                            System.out.println("Latitud: " + latitud + ", Longitud: " + longitud);

                            Pokemon pokemon = comparaLoc(location);

                            if (pokemon == null) {
                                Toast.makeText(getApplicationContext(), R.string.txtPokeCercano, Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(getApplicationContext(), R.string.txtPokeAlLado, Toast.LENGTH_SHORT).show();
                            }
                        };
            });
        }

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor rotacion = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener((SensorEventListener) this, rotacion, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCapturar:

                Animation anim = AnimationUtils.loadAnimation(this, R.anim.pulsar_boton);
                btnCapturar.startAnimation(anim);
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                }

                Criteria criteria = new Criteria();
                proveedor = manejador.getBestProvider(criteria, false);
                manejador.requestLocationUpdates(proveedor, 1000, 10, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        latitud = location.getLatitude();
                        longitud = location.getLongitude();

                        Pokemon pokemon = comparaLoc(location);

                        if (pokemon == null) {
                            Toast.makeText(getApplicationContext(), R.string.txtNoCap, Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(getApplicationContext(), R.string.txtCap, Toast.LENGTH_SHORT).show();

                            String update = "UPDATE Pokemon SET Estado = 'Capturado' WHERE Nombre ='"+pokemon.getNombre()+"';";
                            db.execSQL(update);

                        }

                    };
                });

                botonPulsado = false;

            break;

            case R.id.btnListar:
                Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.pulsar_boton);
                btnListar.startAnimation(anim2);

                Intent intent = new Intent(this, ListarPokemon.class);
                startActivity(intent);

                break;

            case R.id.btnIntroducir:
                Animation anim3 = AnimationUtils.loadAnimation(this, R.anim.pulsar_boton);
                btnIntroducir.startAnimation(anim3);

                Intent intent2 = new Intent(this, IntroducirPokemon.class);
                startActivity(intent2);

            break;
        }
    }

    private Pokemon comparaLoc(Location location) {

        Cursor cursor = db.rawQuery("SELECT * FROM Pokemon WHERE Estado = 'Salvaje'", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Pokemon pokemon = new Pokemon(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));

            String [] loc = pokemon.getLocalizacion().split(",");
            String latitudPokemon = loc[0];
            String longitudPokemon = loc[1];

            float[] distancia = new float[1];

            Location.distanceBetween(latitud, longitud, Double.parseDouble(latitudPokemon), Double.parseDouble(longitudPokemon), distancia);
            System.out.println(distancia[0]);
            if (distancia[0] <= 200) {
                return pokemon;
            }

            cursor.moveToNext();
        }

        return null;
    }

    public void onSensorChanged(SensorEvent event) {

        float y = event.values[1];

        if (y < 6.0 && !botonPulsado) {

            btnCapturar.performClick();
            botonPulsado = true;

        } else if (y > 6.0 && botonPulsado) {
            botonPulsado = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}