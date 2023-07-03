package com.sszt.basis.weight;

import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.widget.TextView;

/**
 * 监听输入法输入
 */
public class EditTipInputConnection extends BaseInputConnection {
    private static final boolean DEBUG = true;
    private static final String TAG = "EditableInputConnection";

    private final TextView mTextView;
    private int mBatchEditNesting;

    public EditTipInputConnection(TextView textview) {
        super(textview, true);
        mTextView = textview;
    }

    @Override
    public Editable getEditable() {
        TextView tv = mTextView;
        if (tv != null) {
            return tv.getEditableText();
        }
        return null;
    }

    @Override
    public boolean beginBatchEdit() {
        synchronized (this) {
            if (mBatchEditNesting >= 0) {
                mTextView.beginBatchEdit();
                mBatchEditNesting++;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean endBatchEdit() {
        synchronized (this) {
            if (mBatchEditNesting > 0) {
                // When the connection is reset by the InputMethodManager and reportFinish
                // is called, some endBatchEdit calls may still be asynchronously received from the
                // IME. Do not take these into account, thus ensuring that this IC's final
                // contribution to mTextView's nested batch edit count is zero.
                mTextView.endBatchEdit();
                mBatchEditNesting--;
                return true;
            }
        }
        return false;
    }

    @Override
    public void closeConnection() {
        super.closeConnection();
        synchronized (this) {
            while (mBatchEditNesting > 0) {
                endBatchEdit();
            }
            // Will prevent any further calls to begin or endBatchEdit
            mBatchEditNesting = -1;
        }
    }

    @Override
    public boolean clearMetaKeyStates(int states) {
        final Editable content = getEditable();
        if (content == null) return false;
        KeyListener kl = mTextView.getKeyListener();
        if (kl != null) {
            try {
                kl.clearMetaKeyState(mTextView, content, states);
            } catch (AbstractMethodError e) {
                // This is an old listener that doesn't implement the
                // new method.
            }
        }
        return true;
    }

    @Override
    public boolean commitCompletion(CompletionInfo text) {
        if (DEBUG) Log.v(TAG, "commitCompletion " + text);
        mTextView.beginBatchEdit();
        mTextView.onCommitCompletion(text);
        mTextView.endBatchEdit();
        return true;
    }

    /**
     * Calls the {@link TextView#onCommitCorrection} method of the associated TextView.
     */
    @Override
    public boolean commitCorrection(CorrectionInfo correctionInfo) {
        if (DEBUG) Log.v(TAG, "commitCorrection" + correctionInfo);
        mTextView.beginBatchEdit();
        mTextView.onCommitCorrection(correctionInfo);
        mTextView.endBatchEdit();
        return true;
    }

    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        if (DEBUG) Log.v(TAG, "commitText 输入的文字" + text);
        String string = mTextView.getText().toString();
        Log.e(TAG, "commitText: " + text + "===" + string);
        iEditTipInput.tipInput(text.toString(), string);
        if (mTextView == null) {
            return super.commitText(text, newCursorPosition);
        }

        boolean success = super.commitText(text, newCursorPosition);

        return success;
    }


    @Override
    public boolean performEditorAction(int actionCode) {
        if (DEBUG) Log.v(TAG, "performEditorAction " + actionCode);
        mTextView.onEditorAction(actionCode);
        return true;
    }

    @Override
    public boolean performContextMenuAction(int id) {
        if (DEBUG) Log.v(TAG, "performContextMenuAction " + id);
        mTextView.beginBatchEdit();
        mTextView.onTextContextMenuItem(id);
        mTextView.endBatchEdit();
        return true;
    }


    @Override
    public boolean performPrivateCommand(String action, Bundle data) {
        mTextView.onPrivateIMECommand(action, data);
        return true;
    }

    private IEditTipInput iEditTipInput;

    public void setOnIEditTipInput(IEditTipInput i) {
        iEditTipInput = i;
    }

    /**
     * 监听到输入法输入的文字回调到EditTipView
     */
    interface IEditTipInput {
        void tipInput(String newStr, String oldStr);
    }

}
