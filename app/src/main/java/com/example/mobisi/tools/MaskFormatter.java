package com.example.mobisi.tools;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import kotlin.text.CharCategory;

import android.text.TextWatcher;

public class MaskFormatter implements TextWatcher {
    private final String mask;
    private final EditText editText;

    public MaskFormatter(String mask, EditText editText) {
        this.mask = mask;
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();
        String maskedText = applyMask(text);
        editText.removeTextChangedListener(this);
        editText.setText(maskedText);
        editText.setSelection(maskedText.length());
        editText.addTextChangedListener(this);
    }

    private String applyMask(String input) {
        StringBuilder result = new StringBuilder();
        int maskIndex = 0;
        String mascaraMutavel = mask;

        for (int i = 0; i < mascaraMutavel.length(); i++) {
            char maskChar = mascaraMutavel.charAt(i);
            if (maskIndex < input.length()) {
                if (maskChar == input.charAt(maskIndex)) {
                    result.append(maskChar);
                    maskIndex++;
                    continue;
                } else if (maskChar == '#') {
                    result.append(input.charAt(maskIndex));
                    maskIndex++;
                } else {
                    result.append(maskChar);
                }
            } else {
                break;
            }
        }
        return result.toString();
    }

    public boolean isMaskMatching() {
        String text = editText.getText().toString();
        String mascaraMutavel = mask;

        if (text.length() != mask.length())
            return false;

        for (int i = 0; i < text.length(); i++) {
            char characterText = text.charAt(i);
            char maskCharacter = mascaraMutavel.charAt(i);

            if (maskCharacter == '#')
                continue;
            else if (maskCharacter == characterText)
                continue;
            else
                return false;
        }

        return true;
    }

    public String RemoveMask(){
        String input = editText.getText().toString();

        if(input.length() != mask.length())
            return input;

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++){
            Character textChar = input.charAt((i));
            Character maskChar = mask.charAt((i));

            if(textChar != maskChar)
                result.append(textChar);
        }

        return result.toString();
    }
}
