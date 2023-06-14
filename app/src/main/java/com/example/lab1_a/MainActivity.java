package com.example.lab1_a;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editTextImie;
    private EditText editTextNazwisko;
    private EditText editTextLiczbaOcen;
    private Button buttonZapisz;
    private Button buttonSkoncz;
    private TextView textViewWynik;

    private float srednia = 0;

    private ActivityResultLauncher<Intent> mActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextImie = findViewById(R.id.editTextImie);
        editTextNazwisko = findViewById(R.id.editTextNazwisko);
        editTextLiczbaOcen = findViewById(R.id.editTextLiczbaOcen);
        buttonZapisz = findViewById(R.id.buttonZapisz);
        buttonSkoncz = findViewById(R.id.buttonSkoncz);
        textViewWynik = findViewById(R.id.textViewWynik);

        editTextImie.addTextChangedListener(textWatcher);
        editTextNazwisko.addTextChangedListener(textWatcher);
        editTextLiczbaOcen.addTextChangedListener(textWatcher);

        buttonZapisz.setVisibility(View.GONE);
        textViewWynik.setVisibility(View.GONE);
        buttonSkoncz.setVisibility(View.GONE);

        mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts
                        .StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(
                            ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent resultIntent = result.getData();
                            Bundle bundle = resultIntent.getExtras();
                            srednia = bundle.getFloat("srednia");
                            textViewWynik.setVisibility(View.VISIBLE);
                            textViewWynik.setText("Średnia ocen: " + srednia);
                            if(srednia >= 3.0){
                                buttonSkoncz.setText("Super!");
                                buttonSkoncz.setVisibility(View.VISIBLE);
                                buttonZapisz.setVisibility(View.GONE);
                            }
                            else {
                                buttonSkoncz.setText("Niestety :(");
                                buttonSkoncz.setVisibility(View.VISIBLE);
                                buttonZapisz.setVisibility(View.GONE);
                            }
                        }
                    }});

        buttonZapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(MainActivity.this, SecondActivity.class);
                Bundle bundle = new Bundle();
                String liczba_ocen = editTextLiczbaOcen.getText().toString();
                //Toast.makeText(MainActivity.this, Integer.valueOf(liczba_ocen), Toast.LENGTH_SHORT).show();
                bundle.putInt("oceny", Integer.valueOf(liczba_ocen));
                newIntent.putExtras(bundle);

                mActivityResultLauncher.launch(newIntent);
            }
        });
        buttonSkoncz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(srednia >= 3.0)
                    Toast.makeText(MainActivity.this, "Gratulacje!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Wysyłam podanie o zaliczenie warunkowe", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        editTextLiczbaOcen.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    sprawdzPoprawnoscDanych();
                }
            }
        });

        editTextImie.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    sprawdzPoprawnoscDanych();
                }
            }
        });

        editTextNazwisko.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    sprawdzPoprawnoscDanych();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int buttonEnabled = buttonZapisz.getVisibility();
        String editTextImieError = editTextImie.getError() != null ? editTextImie.getError().toString() : null;
        String editTextNazwiskoError = editTextNazwisko.getError() != null ? editTextNazwisko.getError().toString() : null;
        String editTextLiczbaOcenError = editTextLiczbaOcen.getError() != null ? editTextLiczbaOcen.getError().toString() : null;

        outState.putInt("key_button", buttonEnabled);
        outState.putString("imie_error", editTextImieError);
        outState.putString("nazwisko_error", editTextNazwiskoError);
        outState.putString("liczba_ocen_error", editTextLiczbaOcenError);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


        int buttonEnabled = savedInstanceState.getInt("key_button");
        String editTextImieError = savedInstanceState.getString("imie_error");
        String editTextNazwiskoError = savedInstanceState.getString("nazwisko_error");
        String editTextLiczbaOcenError = savedInstanceState.getString("liczba_ocen_error");

        buttonZapisz.setVisibility(buttonEnabled);
        editTextImie.setError(editTextImieError);
        editTextNazwisko.setError(editTextNazwiskoError);
        editTextLiczbaOcen.setError(editTextLiczbaOcenError);


    }

    private boolean walidujLiczbeOcen() {
        // pobieramy tekst z pola editTextLiczbaOcen
        String tekst = editTextLiczbaOcen.getText().toString();
        Boolean poprawneDane = true;

        try {
            // próbujemy zamienić tekst na liczbę całkowitą
            int liczbaOcen = Integer.parseInt(tekst);

            if (liczbaOcen < 0) {
                //Toast.makeText(this, "Podaj liczbe dodatnia", Toast.LENGTH_SHORT).show();
                editTextLiczbaOcen.setError("Podaj liczbe dodatnia");
                poprawneDane = false;
            } else if (liczbaOcen == 0) {
                //Toast.makeText(this, "Podaj liczbe wieksza od 0", Toast.LENGTH_SHORT).show();
                editTextLiczbaOcen.setError("Podaj liczbe wieksza od 0");
                poprawneDane = false;
            }
            else if(liczbaOcen > 15){
                //Toast.makeText(this, "Podaj liczbe mniejsza od 15", Toast.LENGTH_SHORT).show();
                editTextLiczbaOcen.setError("Podaj liczbe mniejsza od 15");
                poprawneDane = false;
            }
        } catch (NumberFormatException e) {
            //Toast.makeText(this, "Niepoprawna liczba ocen", Toast.LENGTH_SHORT).show();
            editTextLiczbaOcen.setError("Niepoprawna liczba ocen");
            poprawneDane = false;
        }
        return poprawneDane;
    }

    private boolean walidujImie() {
        String imie = editTextImie.getText().toString();
        Boolean poprawneDane = true;

        if (!imie.isEmpty() && !Character.isUpperCase(imie.charAt(0))) {
            poprawneDane = false;
            //Toast.makeText(this, "Imie powinno zaczynac sie z duzej litery", Toast.LENGTH_SHORT).show();
            editTextImie.setError("Imie powinno zaczynac sie z duzej litery");
        } else if (!imie.matches("[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+")) {
            poprawneDane = false;
            //Toast.makeText(this, "Imie sklada sie tylko z liter", Toast.LENGTH_SHORT).show();
            editTextImie.setError("Imie sklada sie tylko z liter");
        } else if (imie.length() < 2) {
            poprawneDane = false;
            //Toast.makeText(this, "Imie jest za krotkie", Toast.LENGTH_SHORT).show();
            editTextImie.setError("Imie jest za krotkie");
        }
        return poprawneDane;
    }

    private boolean walidujNazwisko() {
        String nazwisko = editTextNazwisko.getText().toString();
        Boolean poprawneDane = true;

        if (!nazwisko.isEmpty() && !Character.isUpperCase(nazwisko.charAt(0))) {
            poprawneDane = false;
            //Toast.makeText(this, "Nazwisko powinno zaczynac sie z duzej litery", Toast.LENGTH_SHORT).show();
            editTextNazwisko.setError("Nazwisko powinno zaczynac sie z duzej litery");
        } else if (!nazwisko.matches("[a-zA-ZąćęłóśźżĄĆĘŁŃÓŚŹŻ]+")) {
            poprawneDane = false;
            //Toast.makeText(this, "Nazwisko sklada sie tylko z liter", Toast.LENGTH_SHORT).show();
            editTextNazwisko.setError("Nazwisko sklada sie tylko z liter");
        } else if (nazwisko.length() < 2) {
            poprawneDane = false;
            //Toast.makeText(this, "Nazwisko jest za krotkie", Toast.LENGTH_SHORT).show();
            editTextNazwisko.setError("Nazwisko jest za krotkie");
        }
        return poprawneDane;
    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            sprawdzPoprawnoscDanych();
        }
    };

    private void sprawdzPoprawnoscDanych() {

        boolean poprawneDane = walidujImie() && walidujNazwisko() && walidujLiczbeOcen();

        buttonZapisz.setVisibility(poprawneDane ? View.VISIBLE : View.GONE);
    }
}