package com.example.lab1_a;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.ViewHolder> {

    private Context context;
    private List<Subject> subjects;

    // Zapisuje referencję do listy przedmiotów i kontekstu
    public GradesAdapter(Context context, List<Subject> subjects) {
        this.context = context;
        this.subjects = subjects;
    }

    // Tworzy nowy obiekt ViewHoldera, który reprezentuje elementy listy
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new ViewHolder(view, subjects);
    }

    // Ustawia dane w elemencie listy, dla wiersza o danej pozycji
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subject subject = subjects.get(position);
        holder.tvName.setText(subject.getName());
        if (subject.getRbCheckedId() != -1) {
            holder.rgGrades.check(subject.getRbCheckedId());
        }
        holder.rgGrades.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = (RadioButton) group.findViewById(checkedId);
                if (selectedRadioButton != null) {
                    int grade = Integer.parseInt(selectedRadioButton.getText().toString());
                    subject.setGrade(grade);
                    subject.setRbCheckedId(checkedId);
                }
            }
        });
    }

    // Zwraca liczbę elementów listy
    @Override
    public int getItemCount() {
        return subjects.size();
    }

    // Zwraca listę przedmiotów
    public List<Subject> getSubjects() {
        return subjects;
    }


    // Klasa ViewHoldera, która reprezentuje elementy listy, przechowuje referencje do elementów interfejsu
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        RadioGroup rgGrades;
        RadioButton rbGrade2;
        RadioButton rbGrade3;
        RadioButton rbGrade4;
        RadioButton rbGrade5;

        public ViewHolder(@NonNull View itemView, List<Subject> subjects) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            rgGrades = itemView.findViewById(R.id.rgGrades);
            rbGrade2 = itemView.findViewById(R.id.rbGrade2);
            rbGrade3 = itemView.findViewById(R.id.rbGrade3);
            rbGrade4 = itemView.findViewById(R.id.rbGrade4);
            rbGrade5 = itemView.findViewById(R.id.rbGrade5);
        }


    }
}
