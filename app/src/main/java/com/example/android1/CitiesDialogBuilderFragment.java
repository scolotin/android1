package com.example.android1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CitiesDialogBuilderFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        final View view = requireActivity().getLayoutInflater().inflate(R.layout.cities_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
                                                     .setTitle(R.string.enter_city_name)
                                                     .setView(view)
                                                     .setPositiveButton(R.string.btn_apply, (dialog, which) -> {
                                                         EditText editText = view.findViewById(R.id.editText);
                                                         dismiss();
                                                         String answer = editText.getText().toString();
                                                         ((CitiesFragment)getParentFragment()).onDialogResult(answer);
                                                     });
        return builder.create();
    }
}
