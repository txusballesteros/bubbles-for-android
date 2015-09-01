Bubbles for Android
=====================

Bubbles for Android is an Android library to provide chat heads capabilities on your apps. With a fast way to integrate with your development.

![Logo](assets/bubbles_demo.gif)

## Latest Version

[![Download](https://api.bintray.com/packages/txusballesteros/maven/bubbles-for-android/images/download.svg) ](https://bintray.com/txusballesteros/maven/bubbles-for-android/_latestVersion) ![](https://img.shields.io/badge/platform-android-green.svg) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Bubbles%20for%20Android-green.svg?style=flat)](https://android-arsenal.com/details/1/2113)

## How to use

### Configuring your project dependencies

Add the library dependency in your build.gradle file.

```groovy
dependencies {
    ...
    compile 'com.txusballesteros:bubbles:1.2.1'
}
```

### Adding your first Bubble

Compose your Bubble layout, for example using a Xml layout file. Remember that the first view of your Bubble layout has to be a BubbleLayout view.

```xml
<com.txusballesteros.bubbles.BubbleLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content">

    <ImageView
        android:id="@+id/avatar"
        android:layout_width="70dp"
        android:layout_height="70dp"
        android:layout_gravity="center"
        android:background="@drawable/profile_decorator"
        android:src="@drawable/profile"
        android:scaleType="centerCrop"/>

</com.txusballesteros.bubbles.BubbleLayout>
```

Create your BubblesManager instance.

```java
private BubblesManager bubblesManager;

@Override
protected void onCreate(Bundle savedInstanceState) {
     bubblesManager = new BubblesManager.Builder(this)
                                        .build();
     bubblesManager.initialize();
    ...
}

@Override
protected void onDestroy() {
    bubblesManager.recycle();
    ...
}
```

Attach your Bubble to the window.

```java
BubbleLayout bubbleView = (BubbleLayout)LayoutInflater
                                    .from(MainActivity.this).inflate(R.layout.bubble_layout, null);
bubblesManager.addBubble(bubbleView, 60, 20);
```

### Configuring your Bubbles Trash

If you want to have a trash to remove on screen bubbles, you can configure the
layout of that.

Define your trash layout Xml.

```xml
<ImageView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_marginBottom="20dp"
    android:src="@mipmap/bubble_trash_background"
    android:layout_gravity="bottom|center_horizontal" />
```

Configure the trash layout with your BubblesManager builder.

```java
private BubblesManager bubblesManager;

@Override
protected void onCreate(Bundle savedInstanceState) {
     bubblesManager = new BubblesManager.Builder(this)
                                        .setTrashLayout(R.layout.bubble_trash_layout)
                                        .build();
     bubblesManager.initialize();
    ...
}
```

## License

Copyright Txus Ballesteros 2015 (@txusballesteros)

This file is part of some open source application.

Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

Contact: Txus Ballesteros <txus.ballesteros@gmail.com>
