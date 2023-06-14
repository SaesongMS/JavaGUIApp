package com.example.lab1_a;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GradesAdapter adapter;
    private int liczbaOcen;
    private Parcelable recyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Bundle extras = getIntent().getExtras();
        liczbaOcen = extras.getInt("oceny");

        String[] przedmioty = getResources().getStringArray(R.array.nazwy_przedmiotow);
        List<Subject> subjects = new ArrayList<>();
        for (int i = 0; i < liczbaOcen; i++) {
            subjects.add(new Subject(przedmioty[i]));
        }

        recyclerView = findViewById(R.id.layoutPrzedmioty);
        adapter = new GradesAdapter(this, subjects);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        // przywracanie stanu
        if (savedInstanceState != null) {
            recyclerViewState = savedInstanceState.getParcelable("recyclerViewState");
            for (int i = 0; i < subjects.size(); i++) {
                boolean radioButtonState = savedInstanceState.getBoolean("radioButtonState" + i, false);
                if (radioButtonState) {
                    int grade = savedInstanceState.getInt("grade" + i, 0);
                    subjects.get(i).setGrade(grade);
                    int rbCheckedId = savedInstanceState.getInt("rbCheckedId" + i, -1);
                    subjects.get(i).setRbCheckedId(rbCheckedId);
                }
            }
            adapter.notifyDataSetChanged();
        }

        Button button = findViewById(R.id.buttonZapisz);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Subject> subjects = adapter.getSubjects();
                float n = 0, s = 0;
                for (Subject subject : subjects) {
                    String name = subject.getName();
                    int grade = subject.getGrade();
                    if(grade != 0) {
                        n++;
                        s += grade;
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("srednia",s/n);
                setResult(RESULT_OK, intent);
                Toast.makeText(SecondActivity.this, "Srednia: "+s/n, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable("recyclerViewState", recyclerViewState);

        List<Subject> subjects = adapter.getSubjects();
        for (int i = 0; i < subjects.size(); i++) {
            Subject subject = subjects.get(i);
            outState.putBoolean("radioButtonState" + i, subject.getGrade() != 0);
            if (subject.getGrade() != 0) {
                outState.putInt("grade" + i, subject.getGrade());
            }
            outState.putInt("rbCheckedId" + i, subject.getRbCheckedId());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (recyclerViewState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }
}