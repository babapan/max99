package com.migme.beautyfilter;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import com.oney.WebRTCModule.videoEffects.ProcessorProvider;

/**
 * React Native module that:
 * 1. Registers BeautyFrameProcessor into ProcessorProvider on startup (so WebRTC picks it up)
 * 2. Exposes setBeautyParams() for JS to update filter values in real-time
 */
public class BeautyFilterModule extends ReactContextBaseJavaModule {

    static final String NAME = "BeautyFilter";
    private static BeautyFrameProcessor processor;

    public BeautyFilterModule(ReactApplicationContext context) {
        super(context);
        ensureRegistered();
    }

    private static synchronized void ensureRegistered() {
        if (processor == null) {
            processor = new BeautyFrameProcessor();
            ProcessorProvider.addProcessor("beauty", () -> processor);
        }
    }

    @Override
    public String getName() { return NAME; }

    @ReactMethod
    public void setBeautyParams(ReadableMap params) {
        if (processor == null) return;
        float smooth   = params.hasKey("smooth")   ? (float) params.getDouble("smooth")   : 0f;
        float bright   = params.hasKey("bright")   ? (float) params.getDouble("bright")   : 0f;
        float rosiness = params.hasKey("rosiness") ? (float) params.getDouble("rosiness") : 0f;
        boolean enabled = params.hasKey("enabled") && params.getBoolean("enabled");
        processor.setParams(smooth, bright, rosiness, enabled);
    }
}
