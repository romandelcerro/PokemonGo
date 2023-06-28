package com.example.pokemongo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListarPokemon extends AppCompatActivity {

    SQLiteDatabase db;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_pokemon);

        db= openOrCreateDatabase("Pokemon", Context.MODE_PRIVATE, null);

        lv = findViewById(R.id.lv);
        Listar();

    }

    public void Listar(){
        String query = "SELECT * FROM Pokemon WHERE Estado = 'Capturado';";
        Cursor cursor = db.rawQuery(query, null);
        List<Pokemon> listaPokemon = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(0);
                String tipo = cursor.getString(1);
                String estado = cursor.getString(2);
                String loc = cursor.getString(3);

                Pokemon pokemon = new Pokemon(nombre, tipo, estado, loc);
                listaPokemon.add(pokemon);
            } while (cursor.moveToNext());
        }

        cursor.close();

        ArrayAdapter<Pokemon> adapter = new ArrayAdapter<Pokemon>(this, android.R.layout.simple_list_item_1, listaPokemon);
        lv.setAdapter(adapter);
    }

}