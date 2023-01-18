package org.school.housing.fragments.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.school.housing.R;
import org.school.housing.interfaces.dialog.DialogListener;

public class DeleteConfirmationDialog extends DialogFragment {
    private DialogListener dialogListener;
    private Button btn_dialog_submit;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dialogListener = (DialogListener) context ;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement DialogListener "+e);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = findViews(inflater,container);

        btn_dialog_submit.setOnClickListener(view_confirm -> {
            if (dialogListener != null) {
                //its okay i called this method for getting faster (i should have created another one)
                dialogListener.onConfirmClicked();
                dismiss();
            }
        });
        return view;
    }

    @SuppressLint("MissingInflatedId")
    private View findViews(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.dialog_delete_confirmation, container, false);
        btn_dialog_submit = view.findViewById(R.id.btn_dialog_okay);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dialogListener = null;
    }
}
