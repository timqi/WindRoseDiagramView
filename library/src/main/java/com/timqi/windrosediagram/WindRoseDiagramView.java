package com.timqi.windrosediagram;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by qiqi on 16/5/7.
 */
public class WindRoseDiagramView extends View {

  private Context mCtx;

  // for background
  private Paint mOvalPaint;

  private RectF mOvalRectF;

  private int mOvalBackgroundColor = 0xff00ff00;

  private Paint mOutlinePaint;

  private int mOutlineColor = 0xffff5534;

  private int mOutlineWidth;

  private float mRadius;

  private Point mCenter;

  private float mStartAngle = 0;

  // for foreground
  private int mForegroundColor = 0xff0000ff;

  // for text
  private Paint mTextPaint;

  private int mTextSize;

  private int mTextColor = 0xffffffff;

  // data
  private WindRoseDiagramAdapter mAdapter;

  private Point[] mDestPoints;

  private Point[] mForegroundDestPoints;

  private Point[] mTextDestPoints;

  private MotionEvent mLastTouchMotionEvent;

  // widget
  private WindRoseClickListener windRoseClickListener;

  public void setWindRoseClickListener(WindRoseClickListener listener) {
    this.windRoseClickListener = listener;
  }


  public WindRoseDiagramView(Context context) {
    super(context);
    this.mCtx = context;

    init();
  }

  public WindRoseDiagramView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.mCtx = context;

    TypedArray attributes = context.getTheme()
        .obtainStyledAttributes(attrs, R.styleable.WindRoseDiagramView, 0, 0);
    initAttrs(attributes);

    init();
  }

  public WindRoseDiagramView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.mCtx = context;

    TypedArray attributes = context.getTheme()
        .obtainStyledAttributes(attrs, R.styleable.WindRoseDiagramView, defStyleAttr, 0);
    initAttrs(attributes);

    init();
  }

  private void initAttrs(TypedArray attributes) {

    try {
      mTextColor = attributes.getColor(R.styleable.WindRoseDiagramView_textColor, 0xffffffff);
      mForegroundColor = attributes.getColor(R.styleable.WindRoseDiagramView_foregroundColor, 0xffffffff);
      mOvalBackgroundColor = attributes.getColor(R.styleable.WindRoseDiagramView_backgroundColor, 0xff00ff00);
      mOutlineColor = attributes.getColor(R.styleable.WindRoseDiagramView_outlineColor, 0xffff5534);
      mTextSize = attributes.getDimensionPixelOffset(R.styleable.WindRoseDiagramView_textSize, dp2px(12));
      mOutlineWidth = attributes.getDimensionPixelOffset(R.styleable.WindRoseDiagramView_outlineWidth, dp2px(12));
      mStartAngle = attributes.getFloat(R.styleable.WindRoseDiagramView_startAngle, 0);
    } finally {
      attributes.recycle();
    }
  }

  private Paint mMarkPaint;

  private void init() {
    setOnClickListener(onClickListener);

    mOvalPaint = new Paint();
    mOvalPaint.setAntiAlias(true);
    mOvalPaint.setColor(mOvalBackgroundColor);

    mOutlinePaint = new Paint();
    mOutlinePaint.setAntiAlias(true);
    mOutlinePaint.setStyle(Paint.Style.STROKE);
    mOutlinePaint.setStrokeWidth(10);
    mOutlinePaint.setColor(mOutlineColor);

    mTextPaint = new Paint();
    mTextPaint.setAntiAlias(true);
    mTextPaint.setColor(mTextColor);
    mTextPaint.setStyle(Paint.Style.FILL);
    mTextPaint.setTextSize(mTextSize);

    mMarkPaint = new Paint();
    mMarkPaint.setAntiAlias(true);
    mMarkPaint.setStyle(Paint.Style.STROKE);
    mMarkPaint.setStrokeWidth(6);
    mMarkPaint.setColor(0xff00ff00);
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

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    mOutlinePaint.setColor(mOutlineColor);
    mOutlinePaint.setStrokeWidth(mOutlineWidth);
    canvas.drawOval(mOvalRectF, mOutlinePaint);

    mOvalPaint.setColor(mOvalBackgroundColor);
    canvas.drawOval(mOvalRectF, mOvalPaint);

    canvas.drawPoint(mOvalRectF.centerX(), mOvalRectF.centerY(), mOutlinePaint);

    if (mAdapter == null) return;
    for (int i = 0, count = mAdapter.getCount(); i < count; i++) {
      canvas.drawPoint(mDestPoints[i].x, mDestPoints[i].y, mOutlinePaint);
      canvas.drawPoint(mForegroundDestPoints[i].x, mForegroundDestPoints[i].y, mOutlinePaint);
      canvas.drawLine(mCenter.x, mCenter.y, mDestPoints[i].x, mDestPoints[i].y, mOutlinePaint);
      canvas.drawLine(mForegroundDestPoints[i].x, mForegroundDestPoints[i].y,
          mForegroundDestPoints[i+1].x, mForegroundDestPoints[i+1].y, mOutlinePaint);

      mTextPaint.setTextAlign(Paint.Align.CENTER);
      canvas.drawText(mAdapter.getName(i), mTextDestPoints[i].x, mTextDestPoints[i].y+(float)mTextSize/2, mTextPaint);


      float d = (float)dp2px(48) / 2;
      RectF rectF = new RectF(mTextDestPoints[i].x-d, mTextDestPoints[i].y-d, mTextDestPoints[i].x+d, mTextDestPoints[i].y+d);
      canvas.drawOval(rectF, mMarkPaint);
      if (mLastTouchMotionEvent != null) {
        int[] parentLocation = new int[2];
        getLocationOnScreen(parentLocation);
        canvas.drawPoint(mLastTouchMotionEvent.getRawX()-parentLocation[0], mLastTouchMotionEvent.getRawY()-parentLocation[1], mMarkPaint);
      }
    }
  }


  public void onDataSetChange() {
    if (mAdapter == null || mCenter == null) return;

    int count = mAdapter.getCount();
    mDestPoints = new Point[count];

    mTextDestPoints = new Point[count];
    int realWidth = getWidth() - getPaddingLeft() - getPaddingRight();
    int realHeight = getHeight() - getPaddingTop() - getPaddingBottom();
    int diameter = realWidth > realHeight ? realHeight : realWidth;
    float textRadius = (float)diameter/2 - (float)mTextSize;

    mForegroundDestPoints = new Point[count+1];
    float angleFraction = (float) (2*Math.PI) / count;

    for (int i = 0; i < count; i++) {
      float angle = mStartAngle + i * angleFraction;
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
      if (mTextDestPoints == null || mLastTouchMotionEvent == null || windRoseClickListener == null) return;
      int slop = dp2px(48);

      int[] parentLocation = new int[2];
      getLocationOnScreen(parentLocation);
      Point touchPoint = new Point((int) mLastTouchMotionEvent.getRawX()-parentLocation[0], (int) mLastTouchMotionEvent.getRawY()-parentLocation[1]);

      System.out.println("#########  slop:"+slop);
      for (int i = 0, count = mTextDestPoints.length; i < count; i++) {
        double distance = getDistance(touchPoint, mTextDestPoints[i]);
        System.out.println("####### i:"+i+"   distance:"+distance);
        if (distance <= slop) {
          windRoseClickListener.onItemClick(i);
          break;
        }
      }
    }
  };

  private double getDistance(Point p1, Point p2) {
    return Math.floor((p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y));
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
}
