package com.byron.kline.base;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.byron.kline.adapter.BaseDepthAdapter;
import com.byron.kline.render.DepthRender;
import com.byron.kline.render.DepthLabelRender;
import com.byron.kline.utils.Constants;


/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.byron.kline.draw
 * @FileName     : BaseDepth.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
public class BaseDepthView extends View implements View.OnTouchListener {

    private BaseDepthAdapter dataAdapter;

    private float labelHeight = 40;

    private boolean isShowLegent = true;
    private float legentHeight = 10;
    private float defaultPadding = 8;
    private float legentTextSize = 25;

    private int leftColor = Color.GREEN;
    private int rightColor = Color.RED;
    private int legentTextColor = Color.GRAY;
    private boolean isColorSameAsLegent;
    private boolean isSelected;

    private String leftLegentText;
    private String rightLegentText;

    private int backGroundColor = Color.DKGRAY;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    protected int height;
    protected int width;
    private DepthRender depthRender;
    private DepthLabelRender labelDraw;
    private DataSetObserver observer;
    private float selectedPointX;

    public BaseDepthView(Context context) {
        super(context);
        initView(context);
    }

    public BaseDepthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BaseDepthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseDepthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    @SuppressWarnings("all")
    private void initView(Context context) {

        setOnTouchListener(this);
        labelDraw = new DepthLabelRender(5, 5);
        depthRender = new DepthRender();

        paint.setTextSize(legentTextSize);
        paint.setStyle(Paint.Style.FILL);
        observer = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                calcBaseValues();
                invalidate();
            }
        };
    }

    private void calcBaseValues() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getMeasuredHeight();
        width = getMeasuredWidth();
        labelDraw.setHeight(height);
        labelDraw.setWidth(width);
        depthRender.setWidth(width);
        depthRender.setHeight(height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        if (isShowLegent) {
            drawLegent(canvas);
        }

        if (dataAdapter != null && dataAdapter.getCount() > 0) {
            //???????????????
            float[] tempLeftDatas = dataAdapter.getTempLeftDatas();
            float[] tempRightDatas = dataAdapter.getTempRightDatas();
            //?????????
            labelDraw.drawLabels(canvas,
                    dataAdapter.getMaxIndex(), dataAdapter.getMaxValue(),
                    dataAdapter.getMinIndex(), dataAdapter.getMinValue());
            depthRender.drawDepth(canvas, tempLeftDatas, tempRightDatas,
                    dataAdapter.getMaxValue(), dataAdapter.getMinValue(),
                    dataAdapter.getMaxIndex(), dataAdapter.getMinIndex());
            //??????
            if (isSelected) {
                labelDraw.drawSelectedLables(canvas, depthRender.drawSelected(canvas, selectedPointX, tempLeftDatas, tempRightDatas, dataAdapter.getMinValue()));
            }

        }


    }

    /**
     * ?????????????????????
     *
     * @param canvas canvas
     */
    private void drawLegent(Canvas canvas) {
        float halfWidth = width >> 1;
        float legentTop;
        float leftLegentLeft;
        float leftLegentRight;
        float legentBottom;
        float rightLegentLeft = halfWidth + defaultPadding;
        float rightLegentRight = halfWidth + defaultPadding + legentHeight;
        if (TextUtils.isEmpty(rightLegentText) || TextUtils.isEmpty(leftLegentText)) {
            legentTop = defaultPadding;
            leftLegentLeft = halfWidth - legentHeight - defaultPadding;
            leftLegentRight = halfWidth - defaultPadding;
            legentBottom = legentHeight + defaultPadding;

        } else {


            float leftWidth = paint.measureText(leftLegentText);

            leftLegentLeft = halfWidth - defaultPadding * 2 - leftWidth - legentHeight;
            leftLegentRight = halfWidth - defaultPadding * 2 - leftWidth;

            legentTop = defaultPadding;
            legentBottom = defaultPadding + legentHeight;
            //??????????????????

            float rightTextX = rightLegentRight + defaultPadding;
            float leftTextX = leftLegentRight + defaultPadding;
            float textY = fixTextY(defaultPadding + legentHeight / 2);


            if (isColorSameAsLegent) {
                paint.setColor(leftColor);
                canvas.drawText(leftLegentText, leftTextX, textY, paint);
                paint.setColor(rightColor);
                canvas.drawText(rightLegentText, rightTextX, textY, paint);
            } else {
                paint.setColor(legentTextColor);
                canvas.drawText(leftLegentText, leftTextX, textY, paint);
                canvas.drawText(rightLegentText, rightTextX, textY, paint);
            }

        }
        paint.setColor(leftColor);
        canvas.drawRect(new RectF(leftLegentLeft, legentTop, leftLegentRight, legentBottom), paint);
        paint.setColor(rightColor);
        canvas.drawRect(new RectF(rightLegentLeft, legentTop, rightLegentRight, legentBottom), paint);

        depthRender.setTopPadding(40);
        labelDraw.setTopPading(40);
    }

    /**
     * ??????text???????????????
     */
    public float fixTextY(float y) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (y + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent);
    }

    /**
     * ?????????????????????
     *
     * @param canvas canvas
     */
    private void drawBackground(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(backGroundColor);
        canvas.drawRect(new RectF(0, 0, width, height), paint);
    }

    @SuppressWarnings("all")
    public void setDataAdapter(BaseDepthAdapter dataAdapter) {
        if (null != this.dataAdapter) {
            dataAdapter.unregisterDataSetObserver(observer);
        }
        this.dataAdapter = dataAdapter;
        dataAdapter.registerDataSetObserver(observer);
    }

    @SuppressWarnings("all")
    public void setShowLegent(boolean showLegent) {
        isShowLegent = showLegent;
    }

    @SuppressWarnings("all")
    public void setLegentHeight(float legentHeight) {
        this.legentHeight = legentHeight;
    }

    @SuppressWarnings("all")
    public void setDefaultPadding(float defaultPadding) {
        this.defaultPadding = defaultPadding;
    }

    @SuppressWarnings("all")
    public void setLegentTextSize(float legentTextSize) {
        this.legentTextSize = legentTextSize;
    }

    /**
     * ????????????????????????
     *
     * @param legentTextColor
     */
    @SuppressWarnings("all")
    public void setLegentTextColor(int legentTextColor) {
        this.legentTextColor = legentTextColor;
    }

    /**
     * ?????????????????????????????????
     *
     * @param colorSameAsLegent
     */
    @SuppressWarnings("all")
    public void setLegnetTextColorSameAsLegent(boolean colorSameAsLegent) {
        isColorSameAsLegent = colorSameAsLegent;
    }

    /**
     * ??????????????????
     *
     * @param leftLegentText text
     */
    public void setLeftLegentText(String leftLegentText) {
        this.leftLegentText = leftLegentText;
    }

    /**
     * ??????????????????
     *
     * @param rightLegentText text
     */
    public void setRightLegentText(String rightLegentText) {
        this.rightLegentText = rightLegentText;
    }

    /**
     * ????????????
     *
     * @param backGroundColor ????????????
     */
    public void setBackGroundColor(int backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    private float startX;
    private float startY;

    private long startTime;

    private Runnable longPressRunnable = new Runnable() {
        @Override
        public void run() {
            isSelected = changeState;
            invalidate();
        }
    };

    private boolean changeState;


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                changeState = true;
                startTime = System.currentTimeMillis();
                postDelayed(longPressRunnable, Constants.LONG_PRESS_TIME);
                startX = event.getX();
                startY = event.getY();
                selectedPointX = startX;
                break;
            case MotionEvent.ACTION_UP:
                if (System.currentTimeMillis() - startTime < Constants.LONG_PRESS_TIME) {
                    isSelected = false;
                    removeCallbacks(longPressRunnable);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                float diffX = Math.abs(x - startX);
                float diffY = Math.abs(y - startY);
                if (!isSelected && (diffX > 10 || diffY > 10 ||
                        (System.currentTimeMillis() - startTime > Constants.LONG_PRESS_TIME))) {
                    changeState = false;
                }
                startX = x;
                startY = y;
                selectedPointX = startX;
                invalidate();
                break;
        }
        return true;
    }


    /**
     * ???????????????????????????
     *
     * @param leftColor color
     */
    public void setLeftColor(int leftColor) {
        depthRender.setLeftColor(leftColor);
        this.leftColor = leftColor;
    }

    /**
     * ??????????????????
     *
     * @param leftAreaColor Color
     */
    public void setLeftAreaColor(int leftAreaColor) {
        depthRender.setLeftAreaColor(leftAreaColor);

    }

    /**
     * ????????????????????????
     *
     * @param rightColor Color
     */
    public void setRightColor(int rightColor) {
        depthRender.setRightColor(rightColor);
        this.rightColor = rightColor;

    }

    /**
     * ??????????????????
     *
     * @param rightAreaColor Color
     */
    public void setRightAreaColor(int rightAreaColor) {
        depthRender.setRightAreaColor(rightAreaColor);
    }


    /**
     * ?????????????????????
     *
     * @param selectedPointRadius ??????
     */
    public void setSelectedPointRadius(float selectedPointRadius) {
        depthRender.setSelectedPointRadius(selectedPointRadius);
    }

    /**
     * ??????????????????
     *
     * @param selectedCircleRadius ??????
     */
    public void setSelectedCircleRadius(float selectedCircleRadius) {
        depthRender.setSelectedCircleRadius(selectedCircleRadius);
    }

    /**
     * ??????????????????
     *
     * @param selectedCricleRadiusWidth ??????
     */
    public void setSelectedCricleRadiusWidth(float selectedCricleRadiusWidth) {
        depthRender.setSelectedCricleRadiusWidth(selectedCricleRadiusWidth);
    }

    /**
     * ????????????
     *
     * @param depthLineWidth ??????
     */
    public void setDepthLineWidth(float depthLineWidth) {
        depthRender.setDepthLineWidth(depthLineWidth);
    }


    /**
     * ?????????label??????
     *
     * @param selectedLabelColor Color
     */
    public void setSelectedLabelColor(int selectedLabelColor) {
        labelDraw.setSelectedLabelColor(selectedLabelColor);
    }

    /**
     * ?????????????????????
     *
     * @param selectedBoxColor Color
     */
    public void setSelectedBoxColor(int selectedBoxColor) {
        labelDraw.setSelectedBoxColor(selectedBoxColor);
    }

    /**
     * ????????????????????????
     *
     * @param selectedBoxBorderColor Color
     */
    public void setSelectedBoxBorderColor(int selectedBoxBorderColor) {
        labelDraw.setSelectedBoxBorderColor(selectedBoxBorderColor);
    }

    /**
     * ????????????padding
     *
     * @param selectedBoxPadding paddint
     */
    @SuppressWarnings("all")
    public void setSelectedBoxPadding(int selectedBoxPadding) {
        labelDraw.setSelectedBoxPadding(selectedBoxPadding);
    }

    /**
     * ???????????????label
     *
     * @param showLeftLabel ????????????
     */
    public void setShowLeftLabel(boolean showLeftLabel) {
        labelDraw.setShowLeftLabel(showLeftLabel);
    }

    /**
     * ???????????????label
     *
     * @param showRightLabel ????????????
     */
    @SuppressWarnings("all")
    public void setShowRightLabel(boolean showRightLabel) {
        labelDraw.setShowRightLabel(showRightLabel);
    }

    /**
     * ???????????????label
     *
     * @param showBottomLabel ????????????
     */
    @SuppressWarnings("all")
    public void setShowBottomLabel(boolean showBottomLabel) {
        labelDraw.setShowBottomLabel(showBottomLabel);
    }

    /**
     * ???????????????
     *
     * @param selectedBorderWitdh ??????
     */
    public void setSelectedBorderWitdh(float selectedBorderWitdh) {
        labelDraw.setSelectedBorderWitdh(selectedBorderWitdh);
    }

    public void setLabelColor(int labelColor) {
        labelDraw.setLabelColor(labelColor);
    }

    @SuppressWarnings("all")
    public void setxLabelCount(int xLabelCount) {
        labelDraw.setxLabelCount(xLabelCount);
    }

    @SuppressWarnings("all")
    public void setyLabelCount(int yLabelCount) {
        labelDraw.setyLabelCount(yLabelCount);
    }

    public void setLabelHeight(float labelHeight) {
        labelDraw.setLabelHeight(labelHeight);
        depthRender.setBottomPadding(labelHeight);
        labelDraw.setLabelHeight(labelHeight);
    }

    @SuppressWarnings("all")
    public void setColorSameAsLegent(boolean colorSameAsLegent) {
        isColorSameAsLegent = colorSameAsLegent;
    }

    public void setTextLabelTextSize(int size) {
        labelDraw.setTextSize(size);
    }
}
