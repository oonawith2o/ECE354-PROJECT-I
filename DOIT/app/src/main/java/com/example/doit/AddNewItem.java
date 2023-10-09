package com.example.doit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.doit.Model.Item;
import com.example.doit.Utils.DataBaseHelper;

import java.util.Objects;

public class AddNewItem extends DialogFragment {

    public static final String TAG = "AddNewTask";

    private Button saveButton;
    private EditText editSubject;
    private EditText editNote;

    private DataBaseHelper database;

    public static AddNewItem newInstance() {
        return new AddNewItem();
    }

    @NonNull
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.item_popup, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceStatus) {
        super.onViewCreated(view, savedInstanceStatus);

        saveButton = view.findViewById(R.id.saveButton);
        saveButton.setEnabled(false);
        saveButton.setBackgroundColor(R.color.dark);
        saveButton.setTextColor(Color.WHITE);
        Button cancleButton = view.findViewById(R.id.cancelButton);
        editSubject = view.findViewById(R.id.editTextSubject);
        editNote = view.findViewById(R.id.editTextNote);

        database = new DataBaseHelper(getActivity());

        boolean isUpdated = false;

        final Bundle bundle = getArguments();

        if (bundle != null) {
            isUpdated = true;
            String subject = Objects.requireNonNull(bundle.get("subject")).toString();
            String note = Objects.requireNonNull(bundle.get("note")).toString();
            editSubject.setText(subject);
            editNote.setText(note);

            if (subject.length() > 0) {
                editSubject.setEnabled(false);
            }

            if(note.length() > 0) {
                editNote.setEnabled(false);
            }
        }

        editSubject.addTextChangedListener(new TextWatcher() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    saveButton.setEnabled(false);
                    saveButton.setBackgroundColor(R.color.dark);
                    saveButton.setTextColor(Color.WHITE);
                } else {
                    saveButton.setEnabled(true);
                    saveButton.setBackgroundColor(R.color.blue);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final boolean finalIsUpdate = isUpdated;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = editSubject.getText().toString();
                String note = editNote.getText().toString();

                if (finalIsUpdate){
                    database.updateTask(bundle.getInt("id") , subject, note);
                }else{
                    Item item = new Item();
                    item.setSubject(subject);
                    item.setNote(note);
                    item.setCompleted(false);
                    database.insertTask(item);
                }
                dismiss();

            }
        });

        cancleButton.setOnClickListener(view1 -> dismiss());

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

    }
}