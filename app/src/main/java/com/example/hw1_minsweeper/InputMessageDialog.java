package com.example.hw1_minsweeper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class InputMessageDialog extends AppCompatDialogFragment {
private EditText editText;
private inputNameDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new  AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view =  layoutInflater.inflate(R.layout.layout_input_dialog,null);
        builder.setView(view).setTitle("Congratulations!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name =editText.getText().toString();
                listener.applyName(name);
            }
        });
        editText=view.findViewById(R.id.inputName);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener =(inputNameDialogListener) context;
        } catch (ClassCastException e) {
           throw new ClassCastException(context.toString()+" must implement inputNameDialogListener");
        }
    }

    public interface inputNameDialogListener{
        void applyName(String name);
    }
}
