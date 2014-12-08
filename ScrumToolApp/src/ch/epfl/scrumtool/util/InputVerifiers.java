package ch.epfl.scrumtool.util;

import java.util.Locale;

import org.apache.commons.validator.routines.EmailValidator;

import android.content.res.Resources;
import android.widget.EditText;
import ch.epfl.scrumtool.R;

/**
 * @author Cyriaque Brousse, sylb
 */
public final class InputVerifiers {
    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_ESTIMATION_VALUE = 99;

    public static boolean textEditNullOrEmpty(EditText view) {
        return (view == null) || (view.getText().length() <= 0);
    }

    public static boolean nameTooLong(EditText view) {
        return view.getText().length() >= MAX_NAME_LENGTH;
    }

    public static boolean estimationTooBig(EditText view) {
        return Float.parseFloat(view.getText().toString()) >= MAX_ESTIMATION_VALUE;
    }

    public static boolean verifyNameIsValid(EditText view, Resources res) {
        if (textEditNullOrEmpty(view)) {
            view.setError(res.getString(R.string.error_field_required));
        } else if (nameTooLong(view)) {
            view.setError(res.getString(R.string.error_name_too_long));
        } else {
            view.setError(null);
        }
        return !textEditNullOrEmpty(view) && !nameTooLong(view);
    }

    public static boolean verifyDescriptionIsValid(EditText view, Resources res) {
        if (textEditNullOrEmpty(view)) {
            view.setError(res.getString(R.string.error_field_required));
        } else {
            view.setError(null);
        }
        return !textEditNullOrEmpty(view);
    }

    public static boolean verifyEstimationIsValid(EditText view, Resources res) {
        if (textEditNullOrEmpty(view)) {
            view.setError(res.getString(R.string.error_field_required));
        } else if (estimationTooBig(view)) {
            view.setError(res.getString(R.string.error_estimation_too_big));
        } else {
            view.setError(null);
        }
        return !textEditNullOrEmpty(view) && !estimationTooBig(view);
    }

    public static boolean verifyEmailIsValid(EditText view, Resources res) {
        if (textEditNullOrEmpty(view)) {
            view.setError(res.getString(R.string.error_field_required));
        } else if (!emailIsValid(view.getText().toString())) {
            view.setError(res.getString(R.string.error_email_not_valid));
        } else {
            view.setError(null);
        }
        return !textEditNullOrEmpty(view)
                && emailIsValid(view.getText().toString());
    }

    public static String capitalize(String s) {
        s = s.toLowerCase(Locale.ENGLISH);
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public static boolean emailIsValid(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public static float sanitizeFloat(String string) {
        try {
            return Float.valueOf(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0f;
        }
    }
}
