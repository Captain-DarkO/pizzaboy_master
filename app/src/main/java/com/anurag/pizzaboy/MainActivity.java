package com.anurag.pizzaboy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Pizza pizza;
    private Button btn_make, btn_fav, btn_pres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_make = (Button) findViewById(R.id.button_make);
        btn_fav = (Button) findViewById(R.id.button_preset);
        btn_pres = (Button) findViewById(R.id.button_preset);

        btn_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                MainActivity.this.startActivity(intent);

            }
        });
        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add list of favourite items
            }
        });
        btn_pres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add some preset pizza orders
            }
        });

    }

}
