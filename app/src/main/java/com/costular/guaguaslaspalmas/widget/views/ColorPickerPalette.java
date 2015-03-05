package com.costular.guaguaslaspalmas.widget.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.costular.guaguaslaspalmas.R;


/**
 * A color picker custom view which creates an grid of color squares.  The number of squares per
 * row (and the padding between the squares) is determined by the user.
 */
public class ColorPickerPalette extends TableLayout {

    public interface OnColorSelectedListener {

        /**
         * Called when a specific color square has been selected.
         */
        public void onColorSelected(int color);
    }

    public static final int SIZE_SMALL = 1;
    public static final int SIZE_LARGE = 2;

    public OnColorSelectedListener mOnColorSelectedListener;

    private String mDescription;
    private String mDescriptionSelected;

    private int mSwatchLength;
    private int mMarginSize;
    private int mNumColumns;

    public ColorPickerPalette(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorPickerPalette(Context context) {
        super(context);
    }

    /**
     * Initialize the size, columns, and listener.  Size should be a pre-defined size (SIZE_LARGE
     * or SIZE_SMALL) from ColorPickerDialogFragment.
     */
    public void init(int size, int columns, OnColorSelectedListener listener) {
        mNumColumns = columns;
        Resources res = getResources();
        if (size == SIZE_LARGE) {
            mSwatchLength = res.getDimensionPixelSize(R.dimen.color_swatch_large);
            mMarginSize = res.getDimensionPixelSize(R.dimen.color_swatch_margins_large);
        } else {
            mSwatchLength = res.getDimensionPixelSize(R.dimen.color_swatch_small);
            mMarginSize = res.getDimensionPixelSize(R.dimen.color_swatch_margins_small);
        }
        mOnColorSelectedListener = listener;

        //mDescription = res.getString(R.string.color_swatch_description);
        //mDescriptionSelected = res.getString(R.string.color_swatch_description_selected);
        mDescription = "";
        mDescriptionSelected = "";
    }

    private TableRow createTableRow() {
        TableRow row = new TableRow(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(params);
        return row;
    }

    /**
     * Adds swatches to table in a serpentine format.
     */
    public void drawPalette(int[] colors, int selectedColor) {
        if (colors == null) {
            return;
        }

        this.removeAllViews();
        int tableElements = 0;
        int rowElements = 0;
        int rowNumber = 0;

        // Fills the table with swatches based on the array of colors.
        TableRow row = createTableRow();
        for (int color : colors) {
            tableElements++;

            View colorSwatch = createColorSwatch(color, selectedColor);
            setSwatchDescription(rowNumber, tableElements, rowElements, color == selectedColor,
                    colorSwatch);
            addSwatchToRow(row, colorSwatch, rowNumber);

            rowElements++;
            if (rowElements == mNumColumns) {
                addView(row);
                row = createTableRow();
                rowElements = 0;
                rowNumber++;
            }
        }

        // Create blank views to fill the row if the last row has not been filled.
        if (rowElements > 0) {
            while (rowElements != mNumColumns) {
                addSwatchToRow(row, createBlankSpace(), rowNumber);
                rowElements++;
            }
            addView(row);
        }
    }

    /**
     * Appends a swatch to the end of the row for even-numbered rows (starting with row 0),
     * to the beginning of a row for odd-numbered rows.
     */
    private void addSwatchToRow(TableRow row, View swatch, int rowNumber) {
        if (rowNumber % 2 == 0) {
            row.addView(swatch);
        } else {
            row.addView(swatch, 0);
        }
    }

    /**
     * Add a content description to the specified swatch view. Because the colors get added in a
     * snaking form, every other row will need to compensate for the fact that the colors are added
     * in an opposite direction from their left->right/top->bottom order, which is how the system
     * will arrange them for accessibility purposes.
     */
    private void setSwatchDescription(int rowNumber, int index, int rowElements, boolean selected,
            View swatch) {
        int accessibilityIndex;
        if (rowNumber % 2 == 0) {
            // We're in a regular-ordered row
            accessibilityIndex = index;
        } else {
            // We're in a backwards-ordered row.
            int rowMax = ((rowNumber + 1) * mNumColumns);
            accessibilityIndex = rowMax - rowElements;
        }

        String description;
        if (selected) {
            description = String.format(mDescriptionSelected, accessibilityIndex);
        } else {
            description = String.format(mDescription, accessibilityIndex);
        }
        swatch.setContentDescription(description);
    }

    /**
     * Creates a blank space to fill the row.
     */
    private ImageView createBlankSpace() {
        ImageView view = new ImageView(getContext());
        TableRow.LayoutParams params = new TableRow.LayoutParams(mSwatchLength, mSwatchLength);
        params.setMargins(mMarginSize, mMarginSize, mMarginSize, mMarginSize);
        view.setLayoutParams(params);
        return view;
    }

    /**
     * Creates a color swatch.
     */
    private ColorPickerSwatch createColorSwatch(int color, int selectedColor) {

        ColorPickerSwatch view = new ColorPickerSwatch(getContext(), color,
                color == selectedColor, mOnColorSelectedListener);
        TableRow.LayoutParams params = new TableRow.LayoutParams(mSwatchLength, mSwatchLength);
        params.setMargins(mMarginSize, mMarginSize, mMarginSize, mMarginSize);
        view.setLayoutParams(params);
        return view;
    }

    private class ColorPickerSwatch extends FrameLayout implements View.OnClickListener {
        private int mColor;
        private ImageView mSwatchImage;
        private ImageView mCheckmarkImage;
        private OnColorSelectedListener mOnColorSelectedListener;

        /**
         * Interface for a callback when a color square is selected.
         */


        public ColorPickerSwatch(Context context, int color, boolean checked,
                                 OnColorSelectedListener listener) {
            super(context);
            mColor = color;
            mOnColorSelectedListener = listener;

            LayoutInflater.from(context).inflate(R.layout.calendar_color_picker_swatch, this);
            mSwatchImage = (ImageView) findViewById(R.id.color_picker_swatch);
            mCheckmarkImage = (ImageView) findViewById(R.id.color_picker_checkmark);
            setColor(color);
            setChecked(checked);
            setOnClickListener(this);
        }

        protected void setColor(int color) {
            Drawable[] colorDrawable = new Drawable[]
                    {getContext().getResources().getDrawable(R.drawable.calendar_color_picker_swatch)};
            mSwatchImage.setImageDrawable(new ColorStateDrawable(colorDrawable, color));
        }

        private void setChecked(boolean checked) {
            if (checked) {
                mCheckmarkImage.setVisibility(View.VISIBLE);
            } else {
                mCheckmarkImage.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            if (mOnColorSelectedListener != null) {
                mOnColorSelectedListener.onColorSelected(mColor);
            }
        }
    }

    /**
     * A drawable which sets its color filter to a color specified by the user, and changes to a
     * slightly darker color when pressed or focused.
     */
    private class ColorStateDrawable extends LayerDrawable {

        private static final float PRESSED_STATE_MULTIPLIER = 0.70f;

        private int mColor;

        public ColorStateDrawable(Drawable[] layers, int color) {
            super(layers);
            mColor = color;
        }

        @Override
        protected boolean onStateChange(int[] states) {
            boolean pressedOrFocused = false;
            for (int state : states) {
                if (state == android.R.attr.state_pressed || state == android.R.attr.state_focused) {
                    pressedOrFocused = true;
                    break;
                }
            }

            if (pressedOrFocused) {
                super.setColorFilter(getPressedColor(mColor), PorterDuff.Mode.SRC_ATOP);
            } else {
                super.setColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
            }

            return super.onStateChange(states);
        }

        /**
         * Given a particular color, adjusts its value by a multiplier.
         */
        private int getPressedColor(int color) {
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            hsv[2] = hsv[2] * PRESSED_STATE_MULTIPLIER;
            return Color.HSVToColor(hsv);
        }

        @Override
        public boolean isStateful() {
            return true;
        }
    }
}