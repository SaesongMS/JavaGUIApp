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

public class Subject {
    private String name;
    private int grade=0;
    private int rbCheckedId = -1;

    public Subject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getRbCheckedId() {
        return rbCheckedId;
    }

    public void setRbCheckedId(int rbCheckedId) {
        this.rbCheckedId = rbCheckedId;
    }
}

