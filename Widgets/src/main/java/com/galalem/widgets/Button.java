package com.galalem.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.appcompat.widget.AppCompatButton;

import com.galalem.graphics.Color;

public class Button extends AppCompatButton {

    public static final int GRADIANT_BORDER_RADIUS = 8;

    public static final int GRADIENT_TYPE_LINEAR = 0x00;
    public static final int GRADIENT_TYPE_RADIAL = 0x01;

    public static final int GRADIENT_TYPE_SWEEP = 0x02;

    public static final int GRADIENT_ORIENTATION_TOP_TO_BOTTOM = 0x01;
    public static final int GRADIENT_ORIENTATION_RIGHT_TO_LEFT = 0x02;
    public static final int GRADIENT_ORIENTATION_LEFT_TO_RIGHT = 0x04;
    public static final int GRADIENT_ORIENTATION_BOTTOM_TO_TOP = 0x08;

    public static final int GRADIENT_ORIENTATION_TOP_RIGHT_TO_BOTTOM_LEFT = 0x03;
    public static final int GRADIENT_ORIENTATION_TOP_LEFT_TO_BOTTOM_RIGHT = 0x05;
    public static final int GRADIENT_ORIENTATION_BOTTOM_RIGHT_TO_TOP_LEFT = 0x0a;
    public static final int GRADIENT_ORIENTATION_BOTTOM_LEFT_TO_TOP_RIGHT = 0x0c;


    private static final int GRADIANT_DEFAULT_START_COLOR = Color.valueOf("#DEDEDE");
    private static final int GRADIANT_DEFAULT_CENTER_COLOR = Color.valueOf("#CCCCCC");
    private static final int GRADIANT_DEFAULT_END_COLOR = Color.valueOf("#ABABAB");

    public Button(@NonNull Context context) {
        super(context);
        setBackground(createSelector(
                createGradiant(1, 0, GRADIANT_DEFAULT_START_COLOR, GRADIANT_DEFAULT_CENTER_COLOR, GRADIANT_DEFAULT_END_COLOR),
                createGradiant(1, 0, GRADIANT_DEFAULT_END_COLOR, GRADIANT_DEFAULT_CENTER_COLOR, GRADIANT_DEFAULT_START_COLOR),
                12
        ));
    }

    public Button(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public Button(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }


    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Button, 0, 0);
        try {
            int orientation = array.getInt(R.styleable.Button_defaultGradientOrientation, GRADIENT_ORIENTATION_TOP_TO_BOTTOM);
            int type = array.getInt(R.styleable.Button_defaultGradientType, GRADIENT_TYPE_LINEAR);
            int colorStart = array.getColor(R.styleable.Button_defaultGradientStartColor, GRADIANT_DEFAULT_START_COLOR);
            int colorCenter = array.getColor(R.styleable.Button_defaultGradientCenterColor, GRADIANT_DEFAULT_CENTER_COLOR);
            int colorEnd = array.getColor(R.styleable.Button_defaultGradientEndColor, GRADIANT_DEFAULT_END_COLOR);

            GradientDrawable defaultGradient = createGradiant(orientation, type, colorStart, colorCenter, colorEnd);

            int holder = colorStart;

            orientation = array.getInt(R.styleable.Button_pressedGradientOrientation, GRADIENT_ORIENTATION_TOP_TO_BOTTOM);
            type = array.getInt(R.styleable.Button_pressedGradientType, GRADIENT_TYPE_LINEAR);
            colorStart = array.getColor(R.styleable.Button_pressedGradientStartColor, colorEnd);
            colorCenter = array.getColor(R.styleable.Button_pressedGradientCenterColor, colorCenter);
            colorEnd = array.getColor(R.styleable.Button_pressedGradientEndColor, holder);

            GradientDrawable pressedGradient = createGradiant(orientation, type, colorStart, colorCenter, colorEnd);

            float borderRadius = array.getDimension(R.styleable.Button_borderRadius, GRADIANT_BORDER_RADIUS);

            setBackground(createSelector(defaultGradient, pressedGradient, borderRadius));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            array.recycle();
        }
    }

    private StateListDrawable createSelector(GradientDrawable defaultBackground, GradientDrawable pressedBackground, float borderRadius) {
        defaultBackground.setCornerRadius(borderRadius);
        pressedBackground.setCornerRadius(borderRadius);
        StateListDrawable drawable = new StateListDrawable();
        drawable.setExitFadeDuration(400);
        drawable.addState(new int[]{android.R.attr.state_pressed}, pressedBackground);
        drawable.addState(new int[]{}, defaultBackground);
        return drawable;
    }

    private GradientDrawable createGradiant(int orientation, int type, @Size(max = 3) int... colors) {
        GradientDrawable drawable = new GradientDrawable(parseOrientation(orientation), colors);
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setGradientType(type);
        return drawable;
    }

    private GradientDrawable.Orientation parseOrientation(int flag) {
        switch (flag) {
            case GRADIENT_ORIENTATION_TOP_TO_BOTTOM:
                return GradientDrawable.Orientation.TOP_BOTTOM;
            case GRADIENT_ORIENTATION_BOTTOM_TO_TOP:
                return GradientDrawable.Orientation.BOTTOM_TOP;
            case GRADIENT_ORIENTATION_RIGHT_TO_LEFT:
                return GradientDrawable.Orientation.RIGHT_LEFT;
            case GRADIENT_ORIENTATION_LEFT_TO_RIGHT:
                return GradientDrawable.Orientation.LEFT_RIGHT;
            case GRADIENT_ORIENTATION_TOP_LEFT_TO_BOTTOM_RIGHT:
                return GradientDrawable.Orientation.TL_BR;
            case GRADIENT_ORIENTATION_TOP_RIGHT_TO_BOTTOM_LEFT:
                return GradientDrawable.Orientation.TR_BL;
            case GRADIENT_ORIENTATION_BOTTOM_LEFT_TO_TOP_RIGHT:
                return GradientDrawable.Orientation.BL_TR;
            case GRADIENT_ORIENTATION_BOTTOM_RIGHT_TO_TOP_LEFT:
                return GradientDrawable.Orientation.BR_TL;
            default:
                return null;
        }
    }
}
