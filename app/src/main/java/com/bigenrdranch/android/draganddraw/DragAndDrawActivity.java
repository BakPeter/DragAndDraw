package com.bigenrdranch.android.draganddraw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class DragAndDrawActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return DragAndDrawFragment.newInstance();
    }
}
