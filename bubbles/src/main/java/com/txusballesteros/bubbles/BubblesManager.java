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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class BubblesManager {
    private static BubblesManager INSTANCE;
    private Context context;
    private boolean bounded;
    private BubblesService bubblesService;
    private int trashLayoutResourceId;
    private OnInitializedCallback listener;

    private static BubblesManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new BubblesManager(context);
        }
        return INSTANCE;
    }

    private ServiceConnection bubbleServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BubblesService.BubblesServiceBinder binder = (BubblesService.BubblesServiceBinder)service;
            BubblesManager.this.bubblesService = binder.getService();
            configureBubblesService();
            bounded = true;
            if (listener != null) {
                listener.onInitialized();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bounded = false;
        }
    };

    private BubblesManager(Context context) {
        this.context = context;
    }

    private void configureBubblesService() {
        bubblesService.addTrash(trashLayoutResourceId);
    }

    public void initialize() {
        context.bindService(new Intent(context, BubblesService.class),
                bubbleServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    public void recycle() {
        context.unbindService(bubbleServiceConnection);
    }

    public void addBubble(BubbleLayout bubble, int x, int y) {
        if (bounded) {
            bubblesService.addBubble(bubble, x, y);
        }
    }

    public void removeBubble(BubbleLayout bubble) {
        if (bounded) {
            bubblesService.removeBubble(bubble);
        }
    }

    public static class Builder {
        private BubblesManager bubblesManager;

        public Builder(Context context) {
            this.bubblesManager = getInstance(context);
        }

        public Builder setInitializationCallback(OnInitializedCallback listener) {
            bubblesManager.listener = listener;
            return this;
        }

        public Builder setTrashLayout(int trashLayoutResourceId) {
            bubblesManager.trashLayoutResourceId =trashLayoutResourceId;
            return this;
        }

        public BubblesManager build() {
            return bubblesManager;
        }
    }
}
