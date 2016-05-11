package com.timqi.windrosediagram;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class WindRoseDiagramView extends View {

  private Context mCtx;

  // for background
  private Paint mOvalPaint;

  private RectF mOvalRectF;

  private int mOvalBackgroundColor = 0;

  private Paint mOutlinePaint;

  private int mOutlineColor = 0xccffffff;

  private int mOutlineWidth;

  private float mRadius;

  private Point mCenter;

  private float mStartAngle = 0;

  // for foreground
  private Paint mForegroundPaint;

  private int mForegroundColor = 0x881888ec;

  private Paint mAnchorPaint;

  private int mAnchorWidth;

  private int mAnchorColor = 0xffffffff;

  // for text
  private Paint mTextPaint;

  private int mTextSize;

  private int mTextColor = 0xccffffff;

  // data
  private WindRoseDiagramAdapter mAdapter;

  private Point[] mDestPoints;

  private Point[] mForegroundDestPoints;

  private Point[] mTextDestPoints;

  private MotionEvent mLastTouchMotionEvent;

  // widget
  private WindRoseClickListener windRoseClickListener;

  private int mTouchSlop;

  private OnClickListener mClickListenerNormal;

  public void setWindRoseClickListener(WindRoseClickListener listener) {
    this.windRoseClickListener = listener;
  }

  @Override
  public void setOnClickListener(OnClickListener l) {
    super.setOnClickListener(onClickListener);
    this.mClickListenerNormal = l;
  }

  public WindRoseDiagramView(Context context) {
    super(context);
    init(context);
  }

  public WindRoseDiagramView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);

    TypedArray attributes = context.getTheme()
        .obtainStyledAttributes(attrs, R.styleable.WindRoseDiagramView, 0, 0);
    initAttrs(attributes);
  }

  public WindRoseDiagramView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);

    TypedArray attributes = context.getTheme()
        .obtainStyledAttributes(attrs, R.styleable.WindRoseDiagramView, defStyleAttr, 0);
    initAttrs(attributes);
  }

  private void initAttrs(TypedArray attributes) {

    try {
      mOvalBackgroundColor = attributes.getColor(R.styleable.WindRoseDiagramView_backgroundColor, 0);
      mOutlineWidth = attributes.getDimensionPixelOffset(R.styleable.WindRoseDiagramView_outlineWidth, dp2px(1));
      mOutlineColor = attributes.getColor(R.styleable.WindRoseDiagramView_outlineColor, 0xccffffff);
      mTextColor = attributes.getColor(R.styleable.WindRoseDiagramView_textColor, 0xccffffff);
      mTextSize = attributes.getDimensionPixelOffset(R.styleable.WindRoseDiagramView_textSize, dp2px(15));
      mAnchorWidth = attributes.getDimensionPixelOffset(R.styleable.WindRoseDiagramView_anchorWidth, dp2px(4));
      mAnchorColor = attributes.getColor(R.styleable.WindRoseDiagramView_anchorColor, 0xffffffff);
      mForegroundColor = attributes.getColor(R.styleable.WindRoseDiagramView_foregroundColor, 0x881888ec);

      mTouchSlop = attributes.getDimensionPixelOffset(R.styleable.WindRoseDiagramView_touchSlop, dp2px(24));
      mStartAngle = attributes.getFloat(R.styleable.WindRoseDiagramView_startAngle, 0);
    } finally {
      attributes.recycle();
    }
  }

  private void init(Context context) {
    this.mCtx = context;
    setOnClickListener(onClickListener);

    mOvalPaint = new Paint();
    mOvalPaint.setAntiAlias(true);
    mOvalPaint.setStyle(Paint.Style.FILL);
    mOvalBackgroundColor = 0;

    mOutlinePaint = new Paint();
    mOutlinePaint.setAntiAlias(true);
    mOutlinePaint.setStyle(Paint.Style.STROKE);
    mOutlineWidth = dp2px(1);
    mOutlineColor = 0xccffffff;

    mTextPaint = new Paint();
    mTextPaint.setAntiAlias(true);
    mTextPaint.setStyle(Paint.Style.FILL);
    mTextColor = 0xccffffff;
    mTextSize = dp2px(15);

    mAnchorPaint = new Paint();
    mAnchorPaint.setAntiAlias(true);
    mAnchorPaint.setStyle(Paint.Style.FILL);
    mAnchorColor = 0xffffffff;
    mAnchorWidth = dp2px(4);

    mForegroundPaint = new Paint();
    mForegroundPaint.setAntiAlias(true);
    mForegroundColor = 0x881888ec;

    mTouchSlop = dp2px(24);
    mStartAngle = 0;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    // background
    mOvalPaint.setColor(mOvalBackgroundColor);
    canvas.drawOval(mOvalRectF, mOvalPaint);

    // outline
    mOutlinePaint.setColor(mOutlineColor);
    mOutlinePaint.setStrokeWidth(mOutlineWidth);
    canvas.drawOval(mOvalRectF, mOutlinePaint);


    if (mAdapter == null) return;

    mAnchorPaint.setColor(mAnchorColor);
    mTextPaint.setColor(mTextColor);
    mTextPaint.setTextSize(mTextSize);

    Path path = new Path();
    path.moveTo(mForegroundDestPoints[0].x, mForegroundDestPoints[0].y);
    for (int i = 0, count = mAdapter.getCount(); i < count; i++) {

      canvas.drawOval(new RectF(mForegroundDestPoints[i].x - mAnchorWidth,
          mForegroundDestPoints[i].y - mAnchorWidth,
          mForegroundDestPoints[i].x + mAnchorWidth,
          mForegroundDestPoints[i].y + mAnchorWidth), mAnchorPaint);

      canvas.drawLine(mCenter.x, mCenter.y, mDestPoints[i].x, mDestPoints[i].y, mOutlinePaint);
      canvas.drawLine(mForegroundDestPoints[i].x, mForegroundDestPoints[i].y,
          mForegroundDestPoints[i + 1].x, mForegroundDestPoints[i + 1].y, mOutlinePaint);

      mTextPaint.setTextAlign(Paint.Align.CENTER);
      canvas.drawText(mAdapter.getName(i), mTextDestPoints[i].x, mTextDestPoints[i].y + (float) mTextSize / 2, mTextPaint);

      if (i > 0) path.lineTo(mForegroundDestPoints[i].x, mForegroundDestPoints[i].y);
    }

    path.close();
    mForegroundPaint.setColor(mForegroundColor);
    canvas.drawPath(path, mForegroundPaint);
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    updateOval();
    onDataSetChange();
  }

  private void updateOval() {
    int realWidth = getWidth() - getPaddingLeft() - getPaddingRight();
    int realHeight = getHeight() - getPaddingTop() - getPaddingBottom();

    int diameter = realWidth > realHeight ? realHeight : realWidth;
    float textPadding = (mTextSize + 5) * 2;
    mOvalRectF = new RectF(getPaddingLeft(), getPaddingTop(),
        getPaddingLeft() + diameter, getPaddingTop() + diameter);
    mCenter = new Point((int) mOvalRectF.centerX(), (int) mOvalRectF.centerY());
    mOvalRectF.inset(textPadding + mOutlineWidth, textPadding + mOutlineWidth);
    mRadius = mOvalRectF.width() / 2;
  }


  public void onDataSetChange() {
    if (mAdapter == null || mCenter == null) return;

    int count = mAdapter.getCount();
    mDestPoints = new Point[count];

    mTextDestPoints = new Point[count];
    int realWidth = getWidth() - getPaddingLeft() - getPaddingRight();
    int realHeight = getHeight() - getPaddingTop() - getPaddingBottom();
    int diameter = realWidth > realHeight ? realHeight : realWidth;
    float textRadius = (float) diameter / 2 - (float) mTextSize;

    mForegroundDestPoints = new Point[count + 1];
    float angleFraction = (float) (2 * Math.PI) / count;

    for (int i = 0; i < count; i++) {
      float angle = (float) (mStartAngle * Math.PI / 180 + i * angleFraction);
      mDestPoints[i] = new Point((int) (mCenter.x + mRadius * Math.sin(angle)),
          (int) (mCenter.y - mRadius * Math.cos(angle)));
      mTextDestPoints[i] = new Point((int) (mCenter.x + textRadius * Math.sin(angle)),
          (int) (mCenter.y - textRadius * Math.cos(angle)));
      mForegroundDestPoints[i] = new Point((int) (mCenter.x + mRadius * mAdapter.getPercent(i) * Math.sin(angle)),
          (int) (mCenter.y - mRadius * mAdapter.getPercent(i) * Math.cos(angle)));
    }
    mForegroundDestPoints[count] = mForegroundDestPoints[0];

    refreshLayout();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_UP) {
      this.mLastTouchMotionEvent = event;
    }
    refreshLayout();
    return super.onTouchEvent(event);
  }

  private OnClickListener onClickListener = new OnClickListener() {
    @Override
    public void onClick(View v) {
      if (mTextDestPoints != null && mLastTouchMotionEvent != null && windRoseClickListener != null) {
        int[] parentLocation = new int[2];
        getLocationOnScreen(parentLocation);
        Point touchPoint = new Point((int) mLastTouchMotionEvent.getRawX() - parentLocation[0],
            (int) mLastTouchMotionEvent.getRawY() - parentLocation[1]);

        for (int i = 0, count = mTextDestPoints.length; i < count; i++) {
          double distance = getDistance(touchPoint, mTextDestPoints[i]);
          if (distance <= mTouchSlop) {
            windRoseClickListener.onItemClick(i);
            return;
          }
        }
      }
      if (mClickListenerNormal != null) {
        mClickListenerNormal.onClick(WindRoseDiagramView.this);
      }
    }
  };

  private double getDistance(Point p1, Point p2) {
    return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
  }

  private int dp2px(float dp) {
    return (int) (mCtx.getResources().getDisplayMetrics().density * dp + 0.5f);
  }

  public void refreshLayout() {
    invalidate();
    requestLayout();
  }

  public void setAdapter(WindRoseDiagramAdapter adapter) {
    this.mAdapter = adapter;
    this.mAdapter.setView(this);
    onDataSetChange();
  }

  public int getBackgroundColor() {
    return mOvalBackgroundColor;
  }

  public void setBackgroundColor(int mOvalBackgroundColor) {
    this.mOvalBackgroundColor = mOvalBackgroundColor;
    refreshLayout();
  }

  public int getOutlineColor() {
    return mOutlineColor;
  }

  public void setOutlineColor(int mOutlineColor) {
    this.mOutlineColor = mOutlineColor;
    refreshLayout();
  }

  public int getOutlineWidth() {
    return mOutlineWidth;
  }

  public void setOutlineWidth(int mOutlineWidth) {
    this.mOutlineWidth = mOutlineWidth;
    refreshLayout();
  }

  public float getStartAngle() {
    return mStartAngle;
  }

  public void setStartAngle(float mStartAngle) {
    this.mStartAngle = mStartAngle;
    onDataSetChange();
    refreshLayout();
  }

  public int getForegroundColor() {
    return mForegroundColor;
  }

  public void setForegroundColor(int mForegroundColor) {
    this.mForegroundColor = mForegroundColor;
    refreshLayout();
  }

  public int getAnchorWidth() {
    return mAnchorWidth;
  }

  public void setAnchorWidth(int mAnchorWidth) {
    this.mAnchorWidth = mAnchorWidth;
    refreshLayout();
  }

  public int getAnchorColor() {
    return mAnchorColor;
  }

  public void setAnchorColor(int mAnchorColor) {
    this.mAnchorColor = mAnchorColor;
    refreshLayout();
  }

  public int getTextSize() {
    return mTextSize;
  }

  public void setTextSize(int mTextSize) {
    this.mTextSize = mTextSize;
    updateOval();
    onDataSetChange();
    refreshLayout();
  }

  public int getTextColor() {
    return mTextColor;
  }

  public void setTextColor(int mTextColor) {
    this.mTextColor = mTextColor;
    refreshLayout();
  }

  public int getTouchSlop() {
    return mTouchSlop;
  }

  public void setTouchSlop(int mTouchSlop) {
    this.mTouchSlop = mTouchSlop;
    refreshLayout();
  }
}
