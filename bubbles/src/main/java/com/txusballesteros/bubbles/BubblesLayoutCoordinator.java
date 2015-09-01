/*
 * Copyright Txus Ballesteros 2015 (@txusballesteros)
 *
 * This file is part of some open source application.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * Contact: Txus Ballesteros <txus.ballesteros@gmail.com>
 */
package com.txusballesteros.bubbles;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;

final class BubblesLayoutCoordinator {
    private static BubblesLayoutCoordinator INSTANCE;
    private BubbleTrashLayout trashView;
    private WindowManager windowManager;
    private BubblesService bubblesService;

    private static BubblesLayoutCoordinator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BubblesLayoutCoordinator();
        }
        return INSTANCE;
    }

    private BubblesLayoutCoordinator() { }

    public void notifyBubblePositionChanged(BubbleLayout bubble, int x, int y) {
        if (trashView != null) {
            trashView.setVisibility(View.VISIBLE);
            if (checkIfBubbleIsOverTrash(bubble)) {
                trashView.applyMagnetism();
                trashView.vibrate();
                applyTrashMagnetismToBubble(bubble);
            } else {
                trashView.releaseMagnetism();
            }
        }
    }

    private void applyTrashMagnetismToBubble(BubbleLayout bubble) {
        View trashContentView = getTrashContent();
        int trashCenterX = (trashContentView.getLeft() + (trashContentView.getMeasuredWidth() / 2));
        int trashCenterY = (trashContentView.getTop() + (trashContentView.getMeasuredHeight() / 2));
        int x = (trashCenterX - (bubble.getMeasuredWidth() / 2));
        int y = (trashCenterY - (bubble.getMeasuredHeight() / 2));
        bubble.getViewParams().x = x;
        bubble.getViewParams().y = y;
        windowManager.updateViewLayout(bubble, bubble.getViewParams());
    }

    private boolean checkIfBubbleIsOverTrash(BubbleLayout bubble) {
        boolean result = false;
        if (trashView.getVisibility() == View.VISIBLE) {
            View trashContentView = getTrashContent();
            int trashWidth = trashContentView.getMeasuredWidth();
            int trashHeight = trashContentView.getMeasuredHeight();
            int trashLeft = (trashContentView.getLeft() - (trashWidth / 2));
            int trashRight = (trashContentView.getLeft() + trashWidth + (trashWidth / 2));
            int trashTop = (trashContentView.getTop() - (trashHeight / 2));
            int trashBottom = (trashContentView.getTop() + trashHeight + (trashHeight / 2));
            int bubbleWidth = bubble.getMeasuredWidth();
            int bubbleHeight = bubble.getMeasuredHeight();
            int bubbleLeft = bubble.getViewParams().x;
            int bubbleRight = bubbleLeft + bubbleWidth;
            int bubbleTop = bubble.getViewParams().y;
            int bubbleBottom = bubbleTop + bubbleHeight;
            if (bubbleLeft >= trashLeft && bubbleRight <= trashRight) {
                if (bubbleTop >= trashTop && bubbleBottom <= trashBottom) {
                    result = true;
                }
            }
        }
        return result;
    }

    public void notifyBubbleRelease(BubbleLayout bubble) {
        if (trashView != null) {
            if (checkIfBubbleIsOverTrash(bubble)) {
                bubblesService.removeBubble(bubble);
            }
            trashView.setVisibility(View.GONE);
        }
    }

    public static class Builder {
        private BubblesLayoutCoordinator layoutCoordinator;

        public Builder(BubblesService service) {
            layoutCoordinator = getInstance();
            layoutCoordinator.bubblesService = service;
        }

        public Builder setTrashView(BubbleTrashLayout trashView) {
            layoutCoordinator.trashView = trashView;
            return this;
        }

        public Builder setWindowManager(WindowManager windowManager) {
            layoutCoordinator.windowManager = windowManager;
            return this;
        }

        public BubblesLayoutCoordinator build() {
            return layoutCoordinator;
        }
    }

    private View getTrashContent() {
        return trashView.getChildAt(0);
    }
}
