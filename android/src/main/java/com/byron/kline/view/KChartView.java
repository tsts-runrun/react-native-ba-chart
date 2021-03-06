package com.byron.kline.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.byron.kline.R;
import com.byron.kline.adapter.KLineChartAdapter;
import com.byron.kline.base.BaseKChartView;
import com.byron.kline.base.BaseRender;
import com.byron.kline.formatter.IDateTimeFormatter;
import com.byron.kline.formatter.IValueFormatter;
import com.byron.kline.render.EmaRender;
import com.byron.kline.render.KDJRender;
import com.byron.kline.render.MACDRender;
import com.byron.kline.render.MainRender;
import com.byron.kline.render.RSIRender;
import com.byron.kline.render.VolumeRender;
import com.byron.kline.render.WRRender;
import com.byron.kline.utils.DpUtil;
import com.byron.kline.utils.SlidListener;
import com.byron.kline.utils.Status;

import java.util.Set;

/*************************************************************************
 * Description   :
 *
 * @PackageName  : com.byron.kline.utils
 * @FileName     : KLineChartView.java
 * @Author       : chao
 * @Date         : 2019/4/8
 * @Email        : icechliu@gmail.com
 * @version      : V1
 *************************************************************************/
@SuppressWarnings("unused")
public class KChartView extends BaseKChartView {

    private View progressBar;

    public KChartView(Context context) {
        this(context, null);
        initView(context);
    }

    public KChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttrs(attrs, context);
    }

    private void initView(Context context) {
        mainRender = new MainRender(context);
        volumeRender = new VolumeRender(context);
        addIndexDraw(Status.IndexStatus.MACD.getStatu(), new MACDRender(context));
        addIndexDraw(Status.IndexStatus.KDJ.getStatu(), new KDJRender(context));
        addIndexDraw(Status.IndexStatus.RSI.getStatu(), new RSIRender(context));
        addIndexDraw(Status.IndexStatus.WR.getStatu(), new WRRender(context));
        addIndexDraw(Status.IndexStatus.EMA.getStatu(), new EmaRender(context));
    }

    private void initAttrs(AttributeSet attrs, Context context) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.KChartView);
        parseAttrs(array, context);
    }

    public void parseAttrs(TypedArray array, Context context) {
        if (null != array) {
            try {
                setLogoResouce(array.getResourceId(R.styleable.KChartView_chartLogo, 0));
                //???????????????
                setLimitTextSize(array.getDimension(R.styleable.KChartView_limitTextSize, DpUtil.Dp2Px(context, 10)));
                setLimitTextColor(array.getColor(R.styleable.KChartView_limitTextColor, Color.parseColor("#6D87A8")));
                //??????
                setBetterX(array.getBoolean(R.styleable.KChartView_betterXLabel, true));
                setBetterSelectedX(array.getBoolean(R.styleable.KChartView_betterSelectedXLabel, true));
                setyLabelMarginBorder(array.getDimension(R.styleable.KChartView_yLabelMarginBorder, 10));
                setCrossFollowTouch(array.getBoolean(R.styleable.KChartView_closeFollowTouch, false) ? Status.CrossTouchModel.FOLLOW_FINGERS : Status.CrossTouchModel.SHOW_CLOSE);
                setLineWidth(array.getDimension(R.styleable.KChartView_lineWidth, DpUtil.Dp2Px(context, 0.8f)));
                setCommonTextSize(array.getDimension(R.styleable.KChartView_commonTextSize, DpUtil.Dp2Px(context, 10)));
                setMacdStrockWidth(array.getDimension(R.styleable.KChartView_macdStrokeWidth, DpUtil.Dp2Px(context, 0.8f)));
                setXLabelTextSize(array.getDimension(R.styleable.KChartView_labelTextSize, DpUtil.Dp2Px(context, 10)));
                setYLabelTextSize(array.getDimension(R.styleable.KChartView_labelTextSize, DpUtil.Dp2Px(context, 10)));
                setCommonTextColor(array.getColor(R.styleable.KChartView_commonTextColor, Color.parseColor("#6D87A8")));
                setXlabelTextColor(array.getColor(R.styleable.KChartView_labelTextColor, Color.parseColor("#6D87A8")));
                setYLabelTextColor(array.getColor(R.styleable.KChartView_labelTextColor, Color.parseColor("#6D87A8")));
                setYlabelAlign(array.getBoolean(R.styleable.KChartView_yLabelAlign, false));
                setChartPaddingTop(array.getDimension(R.styleable.KChartView_paddingTop, DpUtil.Dp2Px(context, 20)));
                setChildPaddingTop(array.getDimension(R.styleable.KChartView_childPaddingTop, DpUtil.Dp2Px(context, 10)));
                setChartPaddingBottom(array.getDimension(R.styleable.KChartView_paddingBottom, 0));
                //??????
                setGridLineWidth(array.getDimension(R.styleable.KChartView_gridLineWidth, DpUtil.Dp2Px(context, 0.8f)));
                setGridColumns(array.getInteger(R.styleable.KChartView_gridLineColumns, 5));
                setGridRows(array.getInteger(R.styleable.KChartView_gridLineRows, 5));
                setGridLineColor(array.getColor(R.styleable.KChartView_gridLineColor, Color.parseColor("#1ACFD3A9")));
                //??????
                setVolLegendColor(array.getColor(R.styleable.KChartView_volLegendColor, Color.parseColor("#6D87A8")));
                setMainLegendMarginTop(array.getDimension(R.styleable.KChartView_mainLegendMarginTop, 10f));
                setLegendMarginLeft(array.getDimension(R.styleable.KChartView_legendMarginLeft, 0f));
                setVolLegendMarginTop(array.getDimension(R.styleable.KChartView_volLegendMarginTop, 10f));
                //?????????
                setVolLineChartColor(array.getColor(R.styleable.KChartView_volLineChartColor, Color.parseColor("#4B85A6")));

                //?????????
                setPriceLineWidth(array.getDimension(R.styleable.KChartView_priceLineWidth, DpUtil.Dp2Px(context, 0.8f)));
                setPriceLineColor(array.getColor(R.styleable.KChartView_priceLineColor, Color.parseColor("#6D87A8")));
                setPriceLineRightColor(array.getColor(R.styleable.KChartView_priceLineRightColor, Color.parseColor("#4B85A6")));
                setPriceLabelRightTextColor(array.getColor(R.styleable.KChartView_priceLabelRightTextColor, Color.parseColor("#4B85A6")));
                setPriceLabelRightBackgroundColor(array.getColor(R.styleable.KChartView_priceLabelRightBackgroundColor, Color.parseColor("#131F30")));
                setPriceLabelInLineBoxBackgroundColor(array.getColor(R.styleable.KChartView_priceLabelInLineBoxBackgroundColor, Color.parseColor("#CFD3E9")));
                setPriceLabelInLineBoxBorderColor(array.getColor(R.styleable.KChartView_priceLabelInLineBoxBorderColor, Color.parseColor("#CFD3E9")));
                setPriceLabelInLineBoxBorderWidth(array.getDimension(R.styleable.KChartView_priceLabelInLineBoxBorderWidth, 0.8f));
                setPriceLabelInLineBoxPadding(array.getDimension(R.styleable.KChartView_priceLabelInLineBoxPadding, 20));
                setPriceLabelInLineMarginRight(array.getDimension(R.styleable.KChartView_priceLabelInLineBoxMarginRight, 120));
                setPriceLabelInLineBoxHeight(array.getDimension(R.styleable.KChartView_priceLabelInLineBoxHeight, 40));
                setPriceLabelInLineBoxRadius(array.getDimension(R.styleable.KChartView_priceLabelInLineBoxRadius, 20));
                setPriceLabelInLineShapeWidth(array.getDimension(R.styleable.KChartView_priceLabelInLineShapeWidth, 10));
                setPriceLabelInLineShapeHeight(array.getDimension(R.styleable.KChartView_priceLabelInLineShapeHeight, 20));
                setPriceLabelInLineShapeTextMargin(array.getDimension(R.styleable.KChartView_priceLabelInLineBoxShapeTextMargin, 10));
                setPriceLabelInLineClickable(array.getBoolean(R.styleable.KChartView_priceLabelInLineClickable, false));
                setPriceLineDotSolidWidth(array.getDimension(R.styleable.KChartView_priceLineDotSolidWidth, 8f));
                setPriceLineDotStrokeWidth(array.getDimension(R.styleable.KChartView_priceLineDotStrokeWidth, 4f));
                setPriceLabelRightBackgroundAlpha(array.getInt(R.styleable.KChartView_priceLineRightLabelBackGroundAlpha, 255));
                setPriceLabelInLineTextColor(array.getInt(R.styleable.KChartView_priceLabelInLineTextColor, Color.parseColor("#6D87A8")));
                setPriceLabelInLineTextSize(array.getDimension(R.styleable.KChartView_priceLabelInLineTextSize, DpUtil.Dp2Px(context, 10)));

                //?????????
                setSelectedCrossBigColor(array.getColor(R.styleable.KChartView_selectedCrossBigColor, Color.parseColor("#9ACFD3E9")));
                setSelectedPointColor(array.getColor(R.styleable.KChartView_selectedCrossPointColor, Color.WHITE));
                setSelectedPointRadius(array.getDimension(R.styleable.KChartView_selectedCrossPointRadius, DpUtil.Dp2Px(context, 0.8f)));
                setSelectedShowCrossPoint(array.getBoolean(R.styleable.KChartView_selectedShowCrossPoint, true));
                setSelectedXLineWidth(array.getDimension(R.styleable.KChartView_selectedXLineWidth, DpUtil.Dp2Px(context, 0.8f)));
                setSelectedXLabelBorderWidth(array.getDimension(R.styleable.KChartView_selectedXLabelBorderWidth, DpUtil.Dp2Px(context, 0.8f)));
                setSelectedXLabelBorderColor(array.getColor(R.styleable.KChartView_selectedXLabelBorderColor, Color.WHITE));
                setSelectedXLabelBackgroundColor(array.getColor(R.styleable.KChartView_selectedXLabelBackgroundColor, Color.parseColor("#CFD3E9")));
                setSelectedYLineWidth(array.getDimension(R.styleable.KChartView_selectedYLineWidth, 7));
                setSelectedXLineColor(array.getColor(R.styleable.KChartView_selectedXLineColor, Color.parseColor("#CFD3E9")));
                setSelectedYLineColor(array.getColor(R.styleable.KChartView_selectedYLineColor, Color.parseColor("#1ACFD3E9")));
                setSelectedYColor(array.getColor(R.styleable.KChartView_selectedYColor, Color.parseColor("#CFD3E9")));
                setSelectedPriceBoxBackgroundColor(array.getColor(R.styleable.KChartView_selectedPriceBoxBackgroundColor, Color.parseColor("#081724")));
                setSelectedPriceBoxHorizentalPadding(array.getDimension(R.styleable.KChartView_selectedPriceBoxHorizontalPadding, DpUtil.Dp2Px(context, 5)));
                setSelectedPriceboxVerticalPadding(array.getDimension(R.styleable.KChartView_selectedPriceBoxVerticalPadding, DpUtil.Dp2Px(context, 0.8f)));
                setSelectedDateBoxHorizontalPadding(array.getDimension(R.styleable.KChartView_selectedDateBoxHorizontalPadding, DpUtil.Dp2Px(context, 5)));
                setSelectedDateBoxVerticalPadding(array.getDimension(R.styleable.KChartView_selectedDateBoxVerticalPadding, DpUtil.Dp2Px(context, 0.8f)));
                setSelectInfoBoxMargin(array.getDimension(R.styleable.KChartView_selectedInfoBoxMargin, DpUtil.Dp2Px(context, 5)));
                setSelectInfoBoxColors(
                        array.getColor(R.styleable.KChartView_selectedInfoBoxTextColor, Color.WHITE),
                        array.getColor(R.styleable.KChartView_selectedInfoBoxBorderColor, Color.WHITE),
                        array.getColor(R.styleable.KChartView_selectedInfoBoxBackgroundColor, Color.DKGRAY)
                );
                setSelectedInfoTextSize(array.getDimension(R.styleable.KChartView_selectedInfoTextSize, DpUtil.Dp2Px(context, 10)));
                setSelectInfoBoxPadding(array.getDimension(R.styleable.KChartView_selectedInfoBoxPadding, 4));
                setSelectedInfoLabels((String[]) array.getTextArray(R.styleable.KChartView_selectedInfoLabels));
                setSelectedXLabelTextColor(array.getColor(R.styleable.KChartView_selectedXLabelTextColor, Color.parseColor("#6D87A8")));
                setSelectedXLabelTextSize(array.getDimension(R.styleable.KChartView_selectedXLabelTextSize, DpUtil.Dp2Px(context, 10)));
                //K???
                setIncreaseColor(array.getColor(R.styleable.KChartView_increaseColor, Color.GREEN));
                setDecreaseColor(array.getColor(R.styleable.KChartView_decreaseColor, Color.RED));
                setChartItemWidth(array.getDimension(R.styleable.KChartView_itemWidth, 25));
                setCandleWidth(array.getDimension(R.styleable.KChartView_candleWidth, 20));
                setCandleLineWidth(array.getDimension(R.styleable.KChartView_candleLineWidth, DpUtil.Dp2Px(context, 0.8f)));

                //?????????????????????
                setBackGroundFillTopColor(array.getColor(R.styleable.KChartView_backgroundFillTopColor, Color.TRANSPARENT));
                setBackGroundFillBottomColor(array.getColor(R.styleable.KChartView_backgroundFillBottomColor, Color.TRANSPARENT));

                // time line
                setTimeLineColor(array.getColor(R.styleable.KChartView_timeLineColor, Color.parseColor("#4B85D6")));
                setTimeLineFillTopColor(array.getColor(R.styleable.KChartView_timeLineFillTopColor, Color.parseColor("#404B85D6")));
                setTimeLineFillBottomColor(array.getColor(R.styleable.KChartView_timeLineFillBottomColor, Color.parseColor("#004B85D6")));
                setTimeLineEndColor(array.getColor(R.styleable.KChartView_timeLineEndPointColor, Color.WHITE));
                setTimeLineEndRadius(array.getDimension(R.styleable.KChartView_timeLineEndRadius, DpUtil.Dp2Px(context, 4)));
                setTimeLineEndMultiply(array.getFloat(R.styleable.KChartView_timeLineEndMultiply, 3f));


                //macd
                setMacdChartColor(array.getColor(R.styleable.KChartView_macdIncreaseColor, Color.parseColor("#03C087")),
                        array.getColor(R.styleable.KChartView_macdDecreaseColor, Color.parseColor("#FF605A")));
                setMACDWidth(array.getDimension(R.styleable.KChartView_macdWidth, 10));
                setDIFColor(array.getColor(R.styleable.KChartView_difColor, Color.parseColor("#F6DC93")));
                setDEAColor(array.getColor(R.styleable.KChartView_deaColor, Color.parseColor("#61D1C0")));
                setMACDColor(array.getColor(R.styleable.KChartView_macdColor, Color.parseColor("#CB92FE")));
                //kdj
                setKColor(array.getColor(R.styleable.KChartView_kColor, Color.parseColor("#F6DC93")));
                setDColor(array.getColor(R.styleable.KChartView_dColor, Color.parseColor("#61D1C0")));
                setJColor(array.getColor(R.styleable.KChartView_jColor, Color.parseColor("#CB92FE")));
                //wr
                setR1Color(array.getColor(R.styleable.KChartView_wr1Color, Color.parseColor("#F6DC93")));
                setR2Color(array.getColor(R.styleable.KChartView_wr2Color, Color.parseColor("#61D1C0")));
                setR3Color(array.getColor(R.styleable.KChartView_wr3Color, Color.parseColor("#CB92FE")));
                //rsi
                setRSI1Color(array.getColor(R.styleable.KChartView_rsi1Color, Color.parseColor("#F6DC93")));
                setRSI2Color(array.getColor(R.styleable.KChartView_rsi2Color, Color.parseColor("#61D1C0")));
                setRSI3Color(array.getColor(R.styleable.KChartView_ris3Color, Color.parseColor("#CB92FE")));
                //main
                setMa1Color(array.getColor(R.styleable.KChartView_ma1Color, Color.parseColor("#F6DC93")));
                setMa2Color(array.getColor(R.styleable.KChartView_ma2Color, Color.parseColor("#61D1C0")));
                setMa3Color(array.getColor(R.styleable.KChartView_ma3Color, Color.parseColor("#CB92FE")));

                setVolMa1Color(array.getColor(R.styleable.KChartView_volMa1Color, Color.parseColor("#F6DC93")));
                setVolMa2Color(array.getColor(R.styleable.KChartView_volMa2Color, Color.parseColor("#61D1C0")));

                setCandleSolid(Status.HollowModel.getStrokeModel(array.getInteger(R.styleable.KChartView_candleSolid, 0)));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                array.recycle();
            }
        }
        animInvalidate();
    }

    /**
     * ????????????X??????????????????
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setSelectedXLabelTextColor(int color) {
        selectedXLabelPaint.setColor(color);
        return this;
    }

    /**
     * ?????????????????????????????????
     *
     * @param size size
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineTextSize(float size) {
        labelInPriceLinePaint.setTextSize(size);
        return this;
    }

    /**
     * ?????????????????????????????????
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineTextColor(int color) {
        labelInPriceLinePaint.setColor(color);
        return this;
    }

    /**
     * ????????????X??????????????????
     *
     * @param size size
     * @return {@link KChartView}
     */
    public KChartView setSelectedXLabelTextSize(float size) {
        selectedXLabelPaint.setTextSize(size);
        return this;
    }

    /**
     * ?????????????????????/?????????????????????
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setLimitTextColor(int color) {
        mainRender.setLimitTextColor(color);
        return this;
    }

    /**
     * ?????????????????????/?????????????????????
     *
     * @param dimension textSize
     * @return {@link KChartView}
     */
    public KChartView setLimitTextSize(float dimension) {
        mainRender.setLimitTextSize(dimension);
        return this;
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param showCrossPoint default true
     * @return {@link KChartView}
     */
    public KChartView setSelectedShowCrossPoint(boolean showCrossPoint) {
        this.showCrossPoint = showCrossPoint;
        return this;
    }

    /**
     * ?????????X??????????????????
     *
     * @param width width
     * @return {@link KChartView}
     */
    public KChartView setSelectedXLabelBorderWidth(float width) {
        selectorXFramePaint.setStrokeWidth(width);
        return this;
    }

    /**
     * ?????????X??????????????????
     *
     * @param color width
     * @return {@link KChartView}
     */
    public KChartView setSelectedXLabelBackgroundColor(int color) {
        selectorXBackgroundPaint.setColor(color);
        return this;
    }


    /**
     * ??????????????????padding
     *
     * @param childViewPaddingTop padding
     * @return {@link KChartView}
     */
    public KChartView setChildPaddingTop(float childViewPaddingTop) {
        this.childViewPaddingTop = childViewPaddingTop;
        return this;
    }

    /**
     * ?????????X?????????????????????
     *
     * @param color color
     * @return {@link KChartView}
     */
    public KChartView setSelectedXLabelBorderColor(int color) {
        selectorXFramePaint.setColor(color);
        return this;
    }

    /**
     * ???????????????????????????
     *
     * @param width width
     * @return {@link KChartView}
     */
    public KChartView setPriceLineDotSolidWidth(float width) {
        priceDotLineItemWidth = width;
        return this;
    }


    /**
     * ?????????????????????
     *
     * @param width width
     * @return {@link KChartView}
     */
    public KChartView setPriceLineDotStrokeWidth(float width) {
        priceDotLineItemSpace = width;
        return this;
    }


    /**
     * ????????????????????????
     *
     * @param width width
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineBoxBorderWidth(float width) {
        priceLineBoxPaint.setStrokeWidth(width);
        return this;
    }

    /**
     * ????????????????????????????????????
     *
     * @param color color
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineBoxBorderColor(int color) {
        priceLineBoxPaint.setColor(color);
        return this;
    }

    /**
     * ??????macd ???????????????
     *
     * @param inColor ????????????
     * @param deColor ????????????
     */
    public KChartView setMacdChartColor(int inColor, int deColor) {
        BaseRender baseRender = indexRenders.get(Status.IndexStatus.MACD.getStatu());
        if (baseRender instanceof MACDRender) {
            ((MACDRender) baseRender).setMacdChartColor(inColor, deColor);
        }
        return this;
    }

    /**
     * ??????macd ???????????????
     *
     * @param model {@link Status.HollowModel}
     * @return {@link KChartView}
     */
    public KChartView setMacdStrokeModel(Status.HollowModel model) {
        BaseRender baseRender = indexRenders.get(Status.IndexStatus.MACD.getStatu());
        if (baseRender instanceof MACDRender) {
            ((MACDRender) baseRender).setStrokeModel(model);
        }
        return this;
    }

    /**
     * macd???????????????
     *
     * @param lineWidth ?????? defaut 0.8dp
     * @return {@link KChartView}
     */
    public KChartView setMacdStrockWidth(float lineWidth) {
        BaseRender baseRender = indexRenders.get(Status.IndexStatus.MACD.getStatu());
        if (baseRender instanceof MACDRender) {
            ((MACDRender) baseRender).setMacdStrokeWidth(lineWidth);
        }
        return this;
    }

    /**
     * ?????????LoadingView????????????K???,?????????????????????????????????setProgressBar ?????????????????????progressBar
     *
     * @return {@link KChartView}
     */
    public KChartView showLoading() {
        if (null != progressBar) {
            if (progressBar.getVisibility() != View.VISIBLE) {
                post(() -> {
                    progressBar.setVisibility(View.VISIBLE);
                    isAnimationLast = false;
                });
            }
        } else {
            setLoadingView(new ProgressBar(getContext()));
            showLoading();
        }
        return this;
    }

    /**
     * ?????????LoadingView?????????K???
     *
     * @return {@link KChartView}
     */
    public KChartView justShowLoading() {
        showLoading();
        isShowLoading = true;
        return this;
    }


    /**
     * ????????????????????????????????????
     * ??????500??????????????????????????????????????????
     *
     * @return {@link KChartView}
     */
    public KChartView hideLoading() {
        if (null != progressBar) {
            postDelayed(() -> {
                progressBar.setVisibility(View.GONE);
                isShowLoading = false;
            }, 500);
        }
        return this;
    }

    /**
     * ?????????????????????
     *
     * @return {@link KChartView}
     */
    public KChartView hideSelectData() {
        showSelected = false;
        return this;
    }


    /**
     * ????????????????????????
     *
     * @param scaleEnable
     */

    public void setScaleEnable(boolean scaleEnable) {
        super.setScaleEnable(scaleEnable);
    }


    /**
     * ?????????????????? ??????
     *
     * @param scrollEnable
     */
    public void setScrollEnable(boolean scrollEnable) {
        super.setScrollEnable(scrollEnable);
    }

    /**
     * ??????????????????????????????
     *
     * @param autoFixScrollEnable
     */
    public void setAutoFixScrollEnable(boolean autoFixScrollEnable) {
        this.autoFixScrollEnable = autoFixScrollEnable;
    }

    /**
     * ??????DIF??????
     *
     * @return {@link KChartView}
     */
    public KChartView setDIFColor(int color) {
        BaseRender baseRender = indexRenders.get(Status.IndexStatus.MACD.getStatu());
        if (baseRender instanceof MACDRender) {
            ((MACDRender) baseRender).setDIFColor(color);
        }
        return this;
    }

    /**
     * ??????DEA??????
     *
     * @return {@link KChartView}
     */
    public KChartView setDEAColor(int color) {
        BaseRender baseRender = indexRenders.get(Status.IndexStatus.MACD.getStatu());
        if (baseRender instanceof MACDRender) {
            ((MACDRender) baseRender).setDEAColor(color);
        }
        return this;
    }

    /**
     * ??????MACD??????
     *
     * @return {@link KChartView}
     */
    public KChartView setMACDColor(int color) {
        BaseRender baseRender = indexRenders.get(Status.IndexStatus.MACD.getStatu());
        if (baseRender instanceof MACDRender) {
            ((MACDRender) baseRender).setMACDColor(color);
        }
        return this;
    }

    /**
     * ??????MACD?????????
     *
     * @param MACDWidth width
     * @return {@link KChartView}
     */
    public KChartView setMACDWidth(float MACDWidth) {
        BaseRender baseRender = indexRenders.get(Status.IndexStatus.MACD.getStatu());
        if (baseRender instanceof MACDRender) {
            ((MACDRender) baseRender).setMACDWidth(MACDWidth);
        }
        return this;
    }

    /**
     * ??????K??????
     *
     * @return {@link KChartView}
     */
    public KChartView setKColor(int color) {
        BaseRender baseRender = indexRenders.get(Status.IndexStatus.KDJ.getStatu());
        if (baseRender instanceof KDJRender) {
            ((KDJRender) baseRender).setKColor(color);
        }
        return this;
    }

    /**
     * ??????D??????
     *
     * @return {@link KChartView}
     */
    public KChartView setDColor(int color) {
        BaseRender baseRender = indexRenders.get(Status.IndexStatus.KDJ.getStatu());
        if (baseRender instanceof KDJRender) {
            ((KDJRender) baseRender).setDColor(color);
        }
        return this;
    }

    /**
     * ??????J??????
     *
     * @return {@link KChartView}
     */
    public KChartView setJColor(int color) {
        BaseRender baseRender = indexRenders.get(Status.IndexStatus.KDJ.getStatu());
        if (baseRender instanceof KDJRender) {
            ((KDJRender) baseRender).setJColor(color);
        }
        return this;
    }

    /**
     * ??????R??????
     *
     * @return {@link KChartView}
     */
    public KChartView setR1Color(int color) {
        BaseRender baseRender = indexRenders.get(Status.IndexStatus.WR.getStatu());
        if (baseRender instanceof WRRender) {
            ((WRRender) baseRender).setR1Color(color);
        }
        return this;
    }

    /**
     * ??????R??????
     *
     * @return {@link KChartView}
     */
    public KChartView setR2Color(int color) {
        BaseRender baseRender = indexRenders.get(Status.IndexStatus.WR.getStatu());
        if (baseRender instanceof WRRender) {
            ((WRRender) baseRender).setR2Color(color);
        }
        return this;
    }

    /**
     * ??????R??????
     *
     * @return {@link KChartView}
     */
    public KChartView setR3Color(int color) {
        BaseRender baseRender = indexRenders.get(Status.IndexStatus.WR.getStatu());
        if (baseRender instanceof WRRender) {
            ((WRRender) baseRender).setR3Color(color);
        }
        return this;
    }


    public KChartView setVolMa1Color(int color) {
        volumeRender.setMaOneColor(color);
        return this;
    }

    public KChartView setVolMa2Color(int color) {
        volumeRender.setMaTwoColor(color);
        return this;
    }

    /**
     * ??????ma5??????
     *
     * @param color color
     * @return {@link KChartView}
     */
    public KChartView setMa1Color(int color) {
        mainRender.setMaOneColor(color);
        return this;
    }

    /**
     * ??????ma10??????
     *
     * @param color color
     * @return {@link KChartView}
     */
    public KChartView setMa2Color(int color) {
        mainRender.setMaTwoColor(color);
        return this;
    }

    /**
     * ??????ma20??????
     *
     * @param color color
     * @return {@link KChartView}
     */
    public KChartView setMa3Color(int color) {
        mainRender.setMaThreeColor(color);
        return this;
    }

    /**
     * ???????????????????????????
     *
     * @param textSize textsize
     * @return {@link KChartView}
     */
    public KChartView setSelectedInfoTextSize(float textSize) {
        mainRender.setSelectorTextSize(textSize);
        return this;
    }

    /**
     * ???????????????????????????
     *
     * @param candleWidth candleWidth
     * @return {@link KChartView}
     */
    public KChartView setCandleWidth(float candleWidth) {
        mainRender.setCandleWidth(candleWidth);
        volumeRender.setBarWidth(candleWidth);
        return this;
    }

    /**
     * ????????????????????????(??????????????????)
     *
     * @param candleLineWidth candleLineWidth
     * @return {@link KChartView}
     */
    public KChartView setCandleLineWidth(float candleLineWidth) {
        mainRender.setCandleLineWidth(candleLineWidth);
        return this;
    }

    /**
     * ??????????????????
     *
     * @return {@link KChartView}
     */
    public KChartView setCandleSolid(Status.HollowModel candleStrokeModel) {
        mainRender.setStroke(candleStrokeModel);
        return this;
    }


    /**
     * ?????????????????????????????????/????????????????????????
     *
     * @param model {@link Status.CrossTouchModel} default  SHOW_CLOSE
     * @return {@link KChartView}
     */
    public KChartView setCrossFollowTouch(Status.CrossTouchModel model) {
        this.crossTouchModel = model;
        return this;
    }

    public KChartView setRSI1Color(int color) {
        BaseRender baseRender = indexRenders.get(Status.IndexStatus.RSI.getStatu());
        if (baseRender instanceof RSIRender) {
            ((RSIRender) baseRender).setRSI1Color(color);
        }
        return this;
    }

    public KChartView setRSI2Color(int color) {
        BaseRender baseRender = indexRenders.get(Status.IndexStatus.RSI.getStatu());
        if (baseRender instanceof RSIRender) {
            ((RSIRender) baseRender).setRSI2Color(color);
        }
        return this;
    }

    public KChartView setRSI3Color(int color) {
        BaseRender baseRender = indexRenders.get(Status.IndexStatus.RSI.getStatu());
        if (baseRender instanceof RSIRender) {
            ((RSIRender) baseRender).setRSI3Color(color);
        }
        return this;
    }


    /**
     * ??????????????????
     *
     * @param lineWidth ?????? defaut 0.8dp
     * @return {@link KChartView}
     */
    public KChartView setLineWidth(float lineWidth) {
        mainRender.setLineWidth(lineWidth);
        volumeRender.setLineWidth(lineWidth);
        if (null != indexRenders && indexRenders.size() > 0) {
            Set<String> strings = indexRenders.keySet();
            for (String string : strings) {
                BaseRender baseRender = indexRenders.get(string);
                if (null != baseRender) {
                    baseRender.setLineWidth(lineWidth);
                }
            }
        }

        return this;
    }


    @Override
    public void onSelectedChange(MotionEvent e) {
        if (!isShowLoading) {
            super.onSelectedChange(e);
        }
    }

    /**
     * ???????????????Y?????????Label??????????????????
     *
     * @param mainYMoveUpInterval default 5
     * @return {@link KChartView}
     */
    @SuppressWarnings("unused")
    public KChartView setMainYMoveUpInterval(int mainYMoveUpInterval) {
        this.mainYMoveUpInterval = mainYMoveUpInterval;
        return this;
    }

    /**
     * ??????y??????Label??????????????????
     *
     * @param yLabelMarginRight default 10
     * @return {@link KChartView}
     */
    public KChartView setyLabelMarginBorder(float yLabelMarginRight) {
        this.yLabelMarginBorder = yLabelMarginRight;
        return this;
    }


    public KChartView setMainPercent(float mainPercent) {
        this.mainPercent = mainPercent;
        return this;
    }

    public KChartView setVolPercent(float volPresent) {
        this.volPercent = volPresent;
        return this;
    }

    public KChartView setIndexPercent(float childPresent) {
        this.IndexPercent = childPresent;
        return this;
    }


    /**
     * ??????ValueFormatter
     *
     * @return IValueFormatter
     */
    public IValueFormatter getValueFormatter() {
        return super.getValueFormatter();
    }

    /**
     * ???????????????Formatter,Y????????????????????????
     *
     * @param valueFormatter value????????????
     * @return {@link KChartView}
     */
    public KChartView setValueFormatter(IValueFormatter valueFormatter) {
        this.valueFormatter = valueFormatter;
        mainRender.setValueFormatter(valueFormatter);
        return this;
    }

    /**
     * ???????????????ValueFormatter,Y????????????????????????
     *
     * @param valueFormatter value????????????
     * @return {@link KChartView}
     */
    public KChartView setIndexValueFormatter(IValueFormatter valueFormatter) {
        if (null != indexRenders && indexRenders.size() > 0) {
            Set<String> strings = indexRenders.keySet();
            for (String string : strings) {
                BaseRender baseRender = indexRenders.get(string);
                if (null != baseRender) {
                    baseRender.setValueFormatter(valueFormatter);
                }
            }
        }
        return this;
    }


    /**
     * ??????DatetimeFormatter
     *
     * @return ??????????????????
     */
    public IDateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    /**
     * ??????dateTimeFormatter,X??????label???????????????
     *
     * @param dateTimeFormatter ??????????????????
     * @return {@link KChartView}
     */
    public KChartView setDateTimeFormatter(IDateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
        return this;
    }

    /**
     * ??????K??????????????????
     *
     * @param klineState {@link Status.KlineStatus}
     * @return {@link KChartView}
     */
    public KChartView setKlineState(Status.KlineStatus klineState) {
        if (this.klineStatus != klineState) {
            this.klineStatus = klineState;
            switch (klineState) {
                case K_LINE:
                    stopFreshPage();
                    break;
                case TIME_LINE:
                    startFreshPage();
                    break;
            }
            setItemsCount(0);
            if (null != dataAdapter) {
                dataAdapter.setResetShowPosition(true);
            }
            invalidate();
        }
        return this;
    }


    /**
     * ???????????????????????????????????????
     *
     * @param priceBoxMarginRight priceLIneBoxMarginRight
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineMarginRight(float priceBoxMarginRight) {
        this.priceLineBoxMarginRight = priceBoxMarginRight;
        return this;
    }

    /**
     * ????????????????????????????????????
     *
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineBoxPadding(float padding) {
        this.priceLineBoxPadidng = padding;
        return this;
    }

    /**
     * ???????????????????????????
     *
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineShapeHeight(float priceLineShapeHeight) {
        this.priceShapeHeight = priceLineShapeHeight;
        return this;
    }

    /**
     * ???????????????????????????
     *
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineShapeWidth(float width) {
        this.priceShapeWidth = width;
        return this;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineShapeTextMargin(float margin) {
        this.priceBoxShapeTextMargin = margin;
        return this;
    }

    /**
     * ?????????????????????????????????
     *
     * @param priceLineBoxHeight priceLineBoxHeight
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineBoxHeight(float priceLineBoxHeight) {
        this.priceLabelInLineBoxHeight = priceLineBoxHeight;
        return this;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param radius priceLineBox radius
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineBoxRadius(float radius) {
        this.priceLabelInLineBoxRadius = radius;
        return this;
    }

    /**
     * ??????????????????????????????
     *
     * @param infoLabels ???????????? ?????????????????????
     * @return {@link KChartView}
     */
    public KChartView setSelectedInfoLabels(String[] infoLabels) {
        if (null != infoLabels) {
            if (infoLabels.length != 8) {
                throw new RuntimeException("????????????????????????????????????!??????????????????" +
                        "?????? " +
                        "???  " +
                        "???  " +
                        "???  " +
                        "???  " +
                        "?????????    " +
                        "?????????    " +
                        "?????????  ?????????!");
            }
            mainRender.setMarketInfoText(infoLabels);

        }
        return this;
    }

    /**
     * ????????????????????????
     *
     * @param color default black
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelInLineBoxBackgroundColor(int color) {
        priceLineBoxBgPaint.setColor(color);
        return this;
    }

    /**
     * ?????????????????????????????????
     *
     * @param color default wihte
     * @return {@link KChartView}
     */
    public KChartView setSelectedPointColor(int color) {
        selectedCrossPaint.setColor(color);
        return this;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param color default wihte
     * @return {@link KChartView}
     */
    public KChartView setSelectedCrossBigColor(int color) {
        selectedbigCrossPaint.setColor(color);
        return this;
    }

    /**
     * ?????????????????????????????????
     *
     * @param radius selected circle radius
     * @return {@link KChartView}
     */
    public KChartView setSelectedPointRadius(float radius) {
        selectedPointRadius = radius;
        return this;
    }


    /**
     * ???????????????Y?????????
     *
     * @param color select y color
     * @return {@link KChartView}
     */
    public KChartView setSelectedYColor(int color) {
        this.selectedYColor = color;
        return this;
    }

    /**
     * ???????????????????????????
     *
     * @param color backgroud linearGrident top
     * @return {@link KChartView}
     */
    public KChartView setBackGroundFillTopColor(int color) {
        this.backGroundFillTopColor = color;
        return this;
    }

    /**
     * ?????????????????????
     *
     * @param color backgroud linearGrident bottom
     * @return {@link KChartView}
     */
    public KChartView setBackGroundFillBottomColor(int color) {
        this.backGroundFillBottomColor = color;
        return this;
    }

    /**
     * ??????????????????
     *
     * @param color increase color
     * @return {@link KChartView}
     */
    public KChartView setIncreaseColor(int color) {
        mainRender.setIncreaseColor(color);
        volumeRender.setIncreaseColor(color);
        return this;
    }

    /**
     * ??????????????????
     *
     * @param color decrease color
     * @return {@link KChartView}
     */
    public KChartView setDecreaseColor(int color) {
        mainRender.setDecreaseColor(color);
        volumeRender.setDecreaseColor(color);
        return this;
    }

    /**
     * ?????????????????????
     *
     * @param color time line color
     * @return {@link KChartView}
     */
    public KChartView setTimeLineColor(int color) {
        mainRender.setMinuteLineColor(color);
        return this;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param color time line top fill color
     * @return {@link KChartView}
     */
    public KChartView setTimeLineFillTopColor(int color) {
        this.timeLineFillTopColor = color;
        return this;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param color time line bottom fill color
     * @return {@link KChartView}
     */
    public KChartView setTimeLineFillBottomColor(int color) {
        this.timeLineFillBottomColor = color;
        return this;
    }


    /**
     * ??????????????????????????????????????????????????????
     *
     * @param mainLegendMarginTop Legend margin top , default 10
     * @return {@link KChartView}
     */
    public KChartView setMainLegendMarginTop(float mainLegendMarginTop) {
        mainRender.setMainLegendMarginTop(mainLegendMarginTop);
        return this;
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param legendMarginLeft Legend margin left , default 0
     * @return {@link KChartView}
     */
    public KChartView setLegendMarginLeft(float legendMarginLeft) {
        this.legendMarginLeft = legendMarginLeft;
        return this;
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param volLegendMarginTop margin default 0
     * @return {@link KChartView}
     */
    public KChartView setVolLegendMarginTop(float volLegendMarginTop) {
        volumeRender.setVolLegendMarginTop(volLegendMarginTop);
        return this;
    }

    /**
     * ?????????????????????
     *
     * @param color color
     * @return {@link KChartView}
     */
    public KChartView setVolLegendColor(int color) {
        volumeRender.setVolLegendColor(color);
        return this;
    }

    /**
     * ?????????????????????X???????????????????????????,??????true
     *
     * @param betterSelectedX true??????????????????????????????label???????????????
     * @return {@link KChartView}
     */
    public KChartView setBetterSelectedX(boolean betterSelectedX) {
        this.betterSelectedX = betterSelectedX;
        return this;
    }

    /**
     * ?????????????????????X???????????????????????????,??????true
     *
     * @param betterX true??????????????????????????????label???????????????
     * @return {@link KChartView}
     */
    public KChartView setBetterX(boolean betterX) {
        this.betterX = betterX;
        return this;
    }

    /**
     * ??????setPriceLineRightLabelBackGroundColor
     *
     * @param priceRightBoxBackGroundColor backgroundFillPaint
     * @return {@link KChartView}
     */
    @Deprecated
    public KChartView setPriceBoxColor(int priceRightBoxBackGroundColor) {
        this.rightPriceBoxPaint.setColor(priceRightBoxBackGroundColor);
        return this;
    }

    /**
     * ????????????????????????
     *
     * @param priceLIneRightLabelBackGroundColor PriceLineRightLabelBackGroundColor
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelRightBackgroundColor(int priceLIneRightLabelBackGroundColor) {
        this.rightPriceBoxPaint.setColor(priceLIneRightLabelBackGroundColor);
        return this;
    }


    /**
     * ????????????????????????
     *
     * @param color price line right color
     * @return {@link KChartView}
     */
    public KChartView setPriceLineRightColor(int color) {
        priceLineRightPaint.setColor(color);
        return this;
    }

    /**
     * ?????? setPriceLineRightLabelTextColor
     *
     * @param color price line right color
     * @return {@link KChartView}
     */
    @Deprecated
    public KChartView setPriceLineRightTextColor(int color) {
        priceLineRightTextPaint.setColor(color);
        return this;
    }

    /**
     * ????????????????????????????????????
     *
     * @param color price line right color
     * @return {@link KChartView}
     */
    public KChartView setPriceLabelRightTextColor(int color) {
        priceLineRightTextPaint.setColor(color);
        return this;
    }


    /**
     * ????????????K????????????(???????????????)
     *
     * @return {@link KChartView}
     */
    public KChartView setChartItemWidth(float pointWidth) {
        chartItemWidth = pointWidth;
        return this;
    }

    /**
     * ??????K?????????
     *
     * @return chartWidth
     */
    public float getChartItemWidth() {
        return chartItemWidth;
    }


    /**
     * ???????????????????????????
     *
     * @param color pop color
     * @return {@link KChartView}
     */
    public KChartView setTimeLineEndColor(int color) {
        lineEndPointPaint.setColor(color);
        lineEndFillPointPaint.setColor(color);
        return this;
    }

    /**
     * ?????????????????????????????????
     *
     * @param width pop width
     * @return {@link KChartView}
     */
    public KChartView setTimeLineEndRadius(float width) {
        this.lineEndRadius = width;
        return this;
    }

    /**
     * ?????????????????????????????????
     *
     * @param multiply ??????
     * @return {@link KChartView}
     */
    public KChartView setTimeLineEndMultiply(float multiply) {
        this.lineEndMaxMultiply = multiply;
        return this;
    }

    /**
     * ????????????????????????
     *
     * @param lineWidth price line width
     * @return {@link KChartView}
     */
    public KChartView setPriceLineWidth(float lineWidth) {
        priceLinePaint.setStrokeWidth(lineWidth);
        priceLineRightPaint.setStrokeWidth(lineWidth);
        priceLinePaint.setStyle(Paint.Style.STROKE);
        priceLineRightPaint.setStyle(Paint.Style.STROKE);
        return this;
    }


    /**
     * ??????????????????????????????
     *
     * @return {@link KChartView}
     */
    public KChartView setCommonTextSize(float textSize) {
        commonTextPaint.setTextSize(textSize);
        mainRender.setTextSize(textSize);
        volumeRender.setTextSize(textSize);

        if (indexRenders.size() > 0) {
            Set<String> strings = indexRenders.keySet();
            for (String string : strings) {
                BaseRender baseRender = indexRenders.get(string);
                if (null != baseRender) {
                    baseRender.setTextSize(textSize);
                }
            }
        }
        Paint.FontMetrics fm = commonTextPaint.getFontMetrics();
        textHeight = fm.descent - fm.ascent;
        textDecent = fm.descent;
        baseLine = (textHeight - fm.bottom - fm.top) / 2;
        priceLineRightPaint.setTextSize(textSize);
        priceLineRightTextPaint.setTextSize(textSize);

        return this;
    }

    /**
     * ?????????????????????
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setPriceLineColor(int color) {
        priceLinePaint.setColor(color);
        return this;
    }


    /**
     * ????????????Y????????????
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setSelectedPriceBoxBackgroundColor(int color) {
        selectedPriceBoxBackgroundPaint.setColor(color);
        return this;
    }


    /**
     * ??????K?????????????????????
     *
     * @return {@link KChartView}
     */
    public KChartView setOverScrollRange(float overScrollRange) {
        if (overScrollRange < 0) {
            overScrollRange = 0;
        }
        this.overScrollRange = overScrollRange;
        setScrollX((int) -overScrollRange);
        return this;
    }


    /**
     * ????????????padding
     *
     * @param chartPaddingTop chatPaddingTop ???????????????K????????????????????????????????????
     */
    public KChartView setChartPaddingTop(float chartPaddingTop) {
        this.chartPaddingTop = (int) chartPaddingTop;
        return this;
    }

    /**
     * ????????????padding
     *
     * @param chartPaddingBottom chartPaddingBottom ?????????0???XLabel????????????
     * @return {@link KChartView}
     */
    public KChartView setChartPaddingBottom(float chartPaddingBottom) {
        this.chartPaddingBottom = (int) chartPaddingBottom;
        return this;
    }

    /**
     * ??????????????????
     *
     * @return {@link KChartView}
     */
    public KChartView setGridLineWidth(float width) {
        gridPaint.setStrokeWidth(width);
        return this;
    }

    /**
     * ?????????????????????
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setGridLineColor(int color) {
        gridPaint.setColor(color);
        return this;
    }

    /**
     * ????????????????????????
     *
     * @param width width
     * @return {@link KChartView}
     */
    public KChartView setSelectedXLineWidth(float width) {
        selectedXLinePaint.setStrokeWidth(width);
        return this;
    }

    /**
     * ???????????????????????????
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setSelectedXLineColor(int color) {
        selectedXLinePaint.setColor(color);
        return this;
    }

    /**
     * ???????????????????????????
     *
     * @param width width
     * @return {@link KChartView}
     */
    public KChartView setSelectedYLineWidth(float width) {
        selectedWidth = width;
        return this;
    }

    /**
     * ???????????????????????????
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setSelectedYLineColor(int color) {
        selectedYLinePaint.setColor(color);
        return this;
    }

    /**
     * ????????????????????????
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setCommonTextColor(int color) {
        commonTextPaint.setColor(color);
        return this;
    }

    public KChartView setYLabelTextSize(float textSize) {
        yLabelPaint.setTextSize(textSize);
        return this;
    }

    /**
     * ??????X?????????????????????
     *
     * @param textSize textSie
     * @return
     */
    public KChartView setXLabelTextSize(float textSize) {
        xLabelPaint.setTextSize(textSize);
        return this;
    }

    public KChartView setYLabelTextColor(int color) {
        yLabelPaint.setColor(color);
        return this;
    }

    /**
     * ??????Y??????????????????/??????
     *
     * @param alignLeft true??????????????? false ?????????
     * @return
     */
    public KChartView setYlabelAlign(boolean alignLeft) {
        if (alignLeft) {
            yLabelPaint.setTextAlign(Paint.Align.LEFT);
            yLabelX = yLabelMarginBorder;
        } else {
            yLabelPaint.setTextAlign(Paint.Align.RIGHT);
            postDelayed(() -> yLabelX = getViewWidth() - yLabelMarginBorder, 200);
        }
        return this;
    }


    /**
     * ??????X?????????????????????
     *
     * @param color {@link Color}
     * @return {@link KChartView}
     */
    public KChartView setXlabelTextColor(int color) {
        xLabelPaint.setColor(color);
        return this;
    }

    /**
     * ???????????????????????????????????? selected popupwindow text color
     *
     * @param textColor       ??????
     * @param borderColor     ??????
     * @param backgroundColor ??????
     * @return {@link KChartView}
     */
    public KChartView setSelectInfoBoxColors(int textColor, int borderColor, int backgroundColor) {
        mainRender.setSelectorColors(textColor, borderColor, backgroundColor);
        return this;
    }


    /**
     * ??????????????????
     *
     * @return {@link KChartView}
     */
    public KChartView setGridRows(int gridRows) {
        if (gridRows < 1) {
            gridRows = 1;
        }
        this.gridRows = gridRows;
        if (0 != displayHeight) {
            rowSpace = displayHeight / gridRows;
        }
        return this;
    }

    /**
     * ??????????????????
     *
     * @return {@link KChartView}
     */
    public KChartView setGridColumns(int gridColumns) {
        if (gridColumns < 1) {
            gridColumns = 1;
        }
        if (0 != viewWidth) {
            columnSpace = viewWidth / gridColumns;
        }
        this.gridColumns = gridColumns;
        return this;
    }

    /**
     * ????????????????????????IchartDraw
     *
     * @return {@link KChartView}
     */
    public KChartView setVolFormatter(IValueFormatter valueFormatter) {
        volumeRender.setValueFormatter(valueFormatter);
        return this;
    }


    /**
     * ?????????????????????
     *
     * @param adapter {@link KLineChartAdapter}
     * @return {@link KChartView}
     */
    public KChartView setAdapter(KLineChartAdapter adapter) {
        if (null != dataAdapter && null != dataSetObserver) {
            dataAdapter.unregisterDataSetObserver(dataSetObserver);
        }
        dataAdapter = adapter;
        if (null == dataAdapter || null == dataSetObserver) {
            itemsCount = 0;
        } else {
            dataAdapter.registerDataSetObserver(dataSetObserver);
            if (dataAdapter.getCount() > 0) {
                dataAdapter.notifyDataSetChanged();
            }
        }
        return this;
    }


    /**
     * ??????????????????
     *
     * @param l {@link OnSelectedChangedListener}
     * @return {@link KChartView}
     */
    @SuppressWarnings("unused")
    public KChartView setOnSelectedChangedListener(OnSelectedChangedListener l) {
        this.selectedChangedListener = l;
        return this;
    }


    /**
     * ????????????????????????
     *
     * @param position {@link Status.IndexStatus}
     * @return {@link KChartView}
     */
    public KChartView setIndexDraw(Status.IndexStatus position) {
        if (indexDrawPosition.getStatu().equals(position.getStatu())) {
            return this;
        }

        if (position == Status.IndexStatus.NONE) {
            indexRender = null;
            if (chartShowStatue == Status.ChildStatus.MAIN_INDEX) {
                chartShowStatue = Status.ChildStatus.MAIN_ONLY;
            } else if (chartShowStatue == Status.ChildStatus.MAIN_VOL_INDEX) {
                chartShowStatue = Status.ChildStatus.MAIN_VOL;
            }
        } else {
            indexRender = this.indexRenders.get(position.getStatu());
            if (chartShowStatue == Status.ChildStatus.MAIN_ONLY) {
                chartShowStatue = Status.ChildStatus.MAIN_INDEX;
            } else if (chartShowStatue == Status.ChildStatus.MAIN_VOL) {
                chartShowStatue = Status.ChildStatus.MAIN_VOL_INDEX;
            }
        }
        indexDrawPosition = position;
        initRect();
        invalidate();
        return this;
    }

    /**
     * ?????????MA/BOLL????????????
     *
     * @param status {@link Status.MainStatus}
     * @return {@link KChartView}
     */
    public KChartView changeMainDrawType(Status.MainStatus status) {
        if (this.status != status) {
            this.status = status;
            invalidate();
        }
        return this;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param withAnim true load data with anim ; default  true
     * @return {@link KChartView}
     */
    public KChartView setAnimLoadData(boolean withAnim) {
        loadDataWithAnim = withAnim;
        return this;
    }

    /**
     * ??????????????????????????????
     *
     * @return {@link Status.MainStatus}
     */
    public Status.MainStatus getStatus() {
        return super.getStatus();
    }


    /**
     * ??????k????????????loading
     *
     * @param progressBar loading View
     * @return {@link KChartView}
     */
    public KChartView setLoadingView(View progressBar) {
        this.progressBar = progressBar;
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        progressBar.setVisibility(INVISIBLE);
        addView(progressBar, layoutParams);
        return this;
    }

    /**
     * ??????K???????????????????????????
     *
     * @param slidListener ????????????
     * @return {@link KChartView}
     */
    public KChartView setSlidListener(SlidListener slidListener) {
        this.slidListener = slidListener;
        return this;
    }

    /**
     * ??????????????????????????????
     *
     * @param duration ??????????????????
     * @return {@link KChartView}
     */
    @SuppressWarnings("unused")
    public KChartView setAnimationDuration(long duration) {
        if (null != showAnim) {
            showAnim.setDuration(duration);
        }
        return this;
    }

    /**
     * ?????????????????????????????????
     *
     * @param clickable true?????????,????????????k?????????
     * @return {@link KChartView}
     */
    @SuppressWarnings("unused")
    public KChartView setPriceLabelInLineClickable(boolean clickable) {
        this.priceLabelInLineClickable = clickable;
        return this;
    }


    /**
     * ??????K????????????logo
     *
     * @param bitmap logo bitmap
     * @return {@link KChartView}
     */
    public KChartView setLogoBitmap(Bitmap bitmap) {
        if (null != bitmap) {
            this.logoBitmap = bitmap;
            initRect();
        }
        return this;
    }

    /**
     * ??????K????????????logo
     *
     * @param bitmapRes logo resource
     * @return {@link KChartView}
     */
    public KChartView setLogoResouce(int bitmapRes) {
        if (bitmapRes == 0) {
            logoBitmap = null;
        } else {
            setLogoBitmap(BitmapFactory.decodeResource(
                    getContext().getResources(), bitmapRes));
        }
        return this;
    }

    /**
     * ??????logo?????????
     *
     * @param alpha set the alpha component [0..255] of the paint's color.
     * @return {@link KChartView}
     */
    public KChartView setLogoAlpha(int alpha) {
        logoPaint.setAlpha(alpha);
        return this;
    }

    /**
     * ??????K???logo???????????????????????????,?????????
     *
     * @param leftMargin   logo left location default 0
     * @param bottomMargin logo top location default  -1 when top is -1 the logo will show in bottom
     * @return {@link KChartView}
     */
    public KChartView setLogoMargin(float leftMargin, float bottomMargin) {
        this.logoPaddingLeft = leftMargin;
        this.logoPaddingBottom = bottomMargin;
        initRect();
        return this;
    }


    /**
     * ??????K??????????????????????????????
     *
     * @param showCrossModel {@link Status.ShowCrossModel} default SELECT_BOTH
     * @return {@link KChartView}l
     */
    public KChartView setSelectedTouchModel(Status.ShowCrossModel showCrossModel) {
        this.modle = showCrossModel;
        return this;
    }


    /**
     * ?????????????????????????????????????????????
     *
     * @param alpha
     * @return
     */
    public KChartView setPriceLabelRightBackgroundAlpha(int alpha) {
        rightPriceBoxPaint.setAlpha(alpha);
        return this;
    }

    /**
     * ??????????????????????????????
     *
     * @return isLine
     */
    public boolean isLine() {
        return klineStatus.showLine();
    }

    /**
     * ???????????????label?????????padding
     *
     * @param padding padding, ?????????3??????????????? ???+???padding
     * @return {@link KChartView}
     */
    public KChartView setSelectedPriceBoxHorizentalPadding(float padding) {
        this.selectedPriceBoxHorizontalPadding = padding;
        return this;
    }

    /**
     * ???????????????label?????????padding
     *
     * @param padding padding ?????????3??????????????? ???+???padding
     * @return {@link KChartView}
     */
    public KChartView setSelectedPriceboxVerticalPadding(float padding) {
        this.selectedPriceBoxVerticalPadding = padding;
        return this;
    }

    /**
     * ????????????????????????????????????
     *
     * @param margin
     * @return {@link KChartView}
     */
    public KChartView setSelectInfoBoxMargin(float margin) {
        mainRender.setSelectInfoBoxMargin(margin);
        return this;
    }

    /**
     * ????????????????????????,???????????????*2
     *
     * @param padding float
     * @return {@link KChartView}
     */
    public KChartView setSelectInfoBoxPadding(float padding) {
        mainRender.setSelectorInfoBoxPadding(padding);
        return this;
    }

    /**
     * ?????????????????????????????????
     *
     * @param show true show vol view
     * @return {@link KChartView}
     */
    public KChartView setVolShowState(boolean show) {
        switch (chartShowStatue) {
            case MAIN_ONLY:
                if (show) {
                    chartShowStatue = Status.ChildStatus.MAIN_VOL;
                }
                break;
            case MAIN_INDEX:
                if (show) {
                    chartShowStatue = Status.ChildStatus.MAIN_VOL_INDEX;
                }
                break;
            case MAIN_VOL_INDEX:
                if (!show) {
                    chartShowStatue = Status.ChildStatus.MAIN_INDEX;
                }
                break;
            case MAIN_VOL:
                if (!show) {
                    chartShowStatue = Status.ChildStatus.MAIN_ONLY;
                }
                break;
        }
        initRect();
        return this;
    }


    /**
     * ??????????????????????????????????????????
     *
     * @return ??????true ??????false
     */
    public boolean getVolShowState() {
        return (chartShowStatue == Status.ChildStatus.MAIN_VOL || chartShowStatue == Status.ChildStatus.MAIN_VOL_INDEX);
    }


    /**
     * ????????????????????????????????????????????????
     *
     * @param color line color
     * @return {@link KChartView}
     */
    public KChartView setVolLineChartColor(int color) {
        volumeRender.setLineChartColor(color);
        return this;
    }


    /**
     * ??????????????????????????? ???/???
     *
     * @param volChartStatus defaul time line show line chart , K line show barChart
     * @return {@link KChartView}
     */
    public KChartView setVolChartStatues(Status.VolChartStatus volChartStatus) {
        this.volChartStatus = volChartStatus;
        return this;
    }

    /**
     * ?????????????????????
     */
    public KChartView hideSelectedInfoBox() {
        this.hideMarketInfo = true;
        animInvalidate();
        return this;
    }


    /**
     * ??????MainDraw  ?????????MainDraw??????
     *
     * @param t   MainDraw????????????
     * @param <T> ????????????
     * @return {@link KChartView}
     */
    public <T extends MainRender> KChartView resetMainDraw(T t) {
        this.mainRender = t;
        return this;
    }


    /**
     * ??????volDraw  ?????????volDraw??????
     *
     * @param t   VolumeDraw????????????
     * @param <T> ????????????
     * @return {@link KChartView}
     */
    public <T extends VolumeRender> KChartView resetVoDraw(T t) {
        this.volumeRender = t;
        return this;
    }

    /**
     * ???????????????X??????????????????????????????
     *
     * @param padding float
     * @return {@link KChartView}
     */
    public KChartView setSelectedDateBoxVerticalPadding(float padding) {
        dateBoxVerticalPadding = padding;
        return this;
    }

    /**
     * ???????????????X??????????????????????????????
     *
     * @param padding float
     * @return {@link KChartView}
     */
    public KChartView setSelectedDateBoxHorizontalPadding(float padding) {
        selectedDateBoxHorizontalPadding = padding;
        return this;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param model {@link Status.MaxMinCalcModel}
     * @return {@link KChartView}
     */
    public KChartView setMaxMinCalcModel(Status.MaxMinCalcModel model) {
        this.calcModel = model;
        return this;
    }

    /**
     * ??????Y?????????????????????
     *
     * @param model {@link Status.YLabelModel}  ??????????????????K?????????/?????????K?????????
     * @return {@link KChartView}
     */
    public KChartView setYLabelState(Status.YLabelModel model) {
        this.yLabelModel = model;
        initRect();
        return this;
    }

    /**
     * ?????????Y??????????????????????????????????????????Y??????????????????
     *
     * @param width width
     * @return {@link KChartView}
     */
    public KChartView setLabelSpace(float width) {
        labelSpace = width;
        return this;
    }

    /**
     * ??????Y???????????????????????????????????????
     *
     * @param color           -1??????
     * @param heightSameChart true??????????????? false ???????????????
     * @return {@link KChartView}
     */
    public KChartView setYLabelBackgroundColor(int color, boolean heightSameChart) {
        if (-1 != color) {
            this.yLabelBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            this.yLabelBackgroundPaint.setColor(color);
            this.yLabelBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.yLabelBackgroundHeightSameChart = heightSameChart;
        } else {
            this.yLabelBackgroundPaint = null;
        }
        return this;
    }

    /**
     * ???????????????
     *
     * @param t   IChartDraw????????????
     * @param <T> ????????????
     * @return {@link KChartView}
     */
    public <T extends BaseRender> KChartView resetIndexDraw(Status.IndexStatus status, T t) {
        this.indexRenders.put(status.getStatu(), t);
        return this;
    }
}
