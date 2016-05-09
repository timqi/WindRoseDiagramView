package com.timqi.windrosediagram;

/**
 * Created by qiqi on 16/5/9.
 */
public abstract class WindRoseDiagramAdapter {

  private WindRoseDiagramView view;

  public abstract int getCount();

  public abstract String getName(int position);

  public abstract float getPercent(int position);

  public void notifyDataSetChange() {
    if (view != null) {
      view.onDataSetChange();
    }
  }

  public void setView(WindRoseDiagramView windRoseDiagramView) {
    this.view = windRoseDiagramView;
  }
}
