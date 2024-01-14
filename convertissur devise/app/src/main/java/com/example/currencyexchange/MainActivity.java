package com.example.currencyexchange;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.currencyexchange.api.retrofitInterface;
import com.example.currencyexchange.api.RetrofitBuild;

import com.example.currencyexchange.databinding.ActivityMainBinding;
import com.google.gson.JsonObject;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerFrom, spinnerTo;
    private EditText editTextAmount;
    private Button buttonSubmit;
    private EditText currencyConverted;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        editTextAmount = findViewById(R.id.editTextAmount);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        currencyConverted = findViewById(R.id.currency_converted);

        String[] items = new String[]{"USD", "AED", "MAD","EUR", "GBP", "KWD"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        buttonSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                retrofitInterface retrofitinterface = RetrofitBuild.getRetrofitInstance().create(retrofitInterface.class);
                Call<JsonObject> call = retrofitinterface.getExchangeCurrency(spinnerFrom.getSelectedItem().toString());
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject res = response.body();
                        JsonObject rates = res.getAsJsonObject("conversion_rates");
                        double currency = Double.valueOf(editTextAmount.getText().toString());
                        double multiplier = Double.valueOf(rates.get(spinnerTo.getSelectedItem().toString()).toString());
                        double result = currency * multiplier;
                        currencyConverted.setText(String.valueOf(result));
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                    }
            });
            }
        });

    }
}