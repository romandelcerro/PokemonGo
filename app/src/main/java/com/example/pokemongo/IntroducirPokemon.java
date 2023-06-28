package com.example.pokemongo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class IntroducirPokemon extends AppCompatActivity {

    Button btnVolver, btnInsertar;
    EditText txtNombre, txtTipo, txtLoc;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introducir_pokemon);

        btnVolver = findViewById(R.id.btnVolver);
        btnInsertar = findViewById(R.id.btnInsertar);
        txtNombre = findViewById(R.id.txtNombre);
        txtTipo = findViewById(R.id.txtTipo);
        txtLoc = findViewById(R.id.txtLoc);

        db= openOrCreateDatabase("Pokemon", Context.MODE_PRIVATE, null);

    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnVolver:
                Animation anim = AnimationUtils.loadAnimation(this, R.anim.pulsar_boton);
                btnVolver.startAnimation(anim);

                Intent intent = new Intent(this, MainActivity.class);

                startActivity(intent);
                break;

            case R.id.btnInsertar:
                String consulta = "INSERT INTO Pokemon VALUES ('"+txtNombre.getText().toString()+"', '" +
                txtTipo.getText().toString()+"', 'Salvaje', '"+txtLoc.getText().toString()+"');";
                db.execSQL(consulta);

                txtNombre.setText("Nombre");
                txtTipo.setText("Tipo");
                txtLoc.setText("Localización");

                break;

        }

    }

}