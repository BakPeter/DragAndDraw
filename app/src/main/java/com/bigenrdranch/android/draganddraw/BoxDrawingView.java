package com.bigenrdranch.android.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BoxDrawingView extends View {
    private static final String TAG = BoxDrawingView.class.getSimpleName();
    private static final String STATE_ARGS_SUPER_STATE = "super_state";
    private static final String STATE_ARGS_NUMBER_OF_BOXES = "state_args_number_of_boxes";
    private static final String STATE_ARGS_BOX_POINT_ORIGIN = "state_args_box_point_origin_";
    private static final String STATE_ARGS_BOX_POINT_CURRENT = "state_args_box_point_CURRENT_";

    private Box mCurrentBox;
    private List<Box> mBoxen = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    //code instantiated
    public BoxDrawingView(Context context) {
        this(context, null);
    }

    //xml instantiated
    public BoxDrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                mCurrentBox = new Box(current);
                mBoxen.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(current);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                mCurrentBox = null;
                break;
        }

        Log.i(TAG, action + " at x=" + current.x + "y=" + current.y);

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);

        for (Box box : mBoxen) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle state = new Bundle();

        state.putParcelable(STATE_ARGS_SUPER_STATE, super.onSaveInstanceState());

        int numberOfBoxes = mBoxen.size();
        state.putInt(STATE_ARGS_NUMBER_OF_BOXES, numberOfBoxes);

        for (int i = 0; i < numberOfBoxes; i++) {
            state.putParcelable(STATE_ARGS_BOX_POINT_ORIGIN + i, mBoxen.get(i).getOrigin());
            state.putParcelable(STATE_ARGS_BOX_POINT_CURRENT + i, mBoxen.get(i).getCurrent());
        }

        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle stateBundle = (Bundle) state;
        super.onRestoreInstanceState(stateBundle.getParcelable(STATE_ARGS_SUPER_STATE));

        int numberOfBoxes = stateBundle.getInt(STATE_ARGS_NUMBER_OF_BOXES);
        for (int i = 0; i < numberOfBoxes; i++) {
            PointF origin = stateBundle.getParcelable(STATE_ARGS_BOX_POINT_ORIGIN + i);
            PointF current = stateBundle.getParcelable(STATE_ARGS_BOX_POINT_CURRENT + i);
            Box box = new Box(origin, current);
            mBoxen.add(box);
        }
    }
}
