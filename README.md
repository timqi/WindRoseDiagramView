# WindRoseDiagramView

# Demo

![demo gif](https://github.com/timqi/WindRoseDiagramView/raw/master/art/WindRoseDiagram.gif)

[Download the APK Demo](https://github.com/timqi/WindRoseDiagramView/raw/master/art/example-debug.apk)

# How To Use

See the example code in project is the simplest way to konw how to use WindRoseDiagramView. And here to provide a number of parameters to explain.

- Define views in xml

```xml
<com.timqi.windrosediagram.WindRoseDiagramView
  xmlns:app="http://schemas.android.com/apk/res-auto"
  android:id="@+id/windRoseDiagramView"
  android:layout_width="250dp"
  android:layout_height="250dp"

  app:anchorColor="#fff"
  app:anchorWidth="4dp"
  app:backgroundColor="#00000000"
  app:foregroundColor="#881888ec"
  app:outlineColor="#ccffffff"
  app:outlineWidth="1dp"
  app:startAngle="90"
  app:textColor="#ccffffff"
  app:textSize="15sp"
  app:touchSlop="24dp"
  />
```

- parameters description

![parameters description](https://github.com/timqi/WindRoseDiagramView/raw/master/art/parameters-description.png)

- You can custom view using java code

```java
// value is a proper value.

windRoseDiagramView.setAnchorColor(value);
windRoseDiagramView.setAnchorWidth(value);
windRoseDiagramView.setBackgroundColor(value);
windRoseDiagramView.setForegroundColor(value);
windRoseDiagramView.setOutlineColor(value);
windRoseDiagramView.setOutlineWidth(value);
windRoseDiagramView.setStartAngle(value);
windRoseDiagramView.setTextColor(value);
windRoseDiagramView.setTextSize(value);
windRoseDiagramView.setTouchSlop(value);
```

# About Click Event

- `setWindRoseClickListener` will detect the distance between touch point and each center point of Text. If the distance is shortter than `touchSlop`, will callback an event onItemClick(int position), the `position` is the position of Text.
- `setOnClickListener` trigger the click event in the view but out of `setWindRoseClickListener` area.

See the demo app for more information.

# Integration

- Using gradle. Add the dependency in your app.gradle

```groovy
dependencies {
    compile 'com.timqi.windrosediagram:library:1.0.5'
}
```

- Or you can Clone the repo from github. If you are using Android Studio. Select `File -> New -> Import Module` and navigate the source directory to the `library` folder is OK!

Author
========

- [mail](mailto://i@timqi.com)
- [weibo](http://weibo.com/timqi)
- [twitter](https://twitter.com/timqi_cn)

License
=======

    Copyright 2016 Tim Qi

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
