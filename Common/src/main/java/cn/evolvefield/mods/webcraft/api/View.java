package cn.evolvefield.mods.webcraft.api;


import cn.evolvefield.mods.webcraft.api.math.Vec2i;
import cn.evolvefield.mods.webcraft.api.math.Vec4i;
import cn.evolvefield.mods.webcraft.util.FileUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.mojang.blaze3d.shaders.ProgramManager.glUseProgram;

public class View implements AutoCloseable {
    private Vec4i bounds;
    private final long viewPointer;
    private final boolean isTransparent;
    private IResizeCallback resizeCallback;
    private final double scale = 1.0f;
    private final List<Runnable> domReadyCallbakList = new LinkedList<>();
    private final Map<String, IJSFuncCallback> jsCallbackMap = new HashMap<>();

    public View() {
        this(0, 0, 100, 100);
    }

    public View(int x, int y, int width, int height) {
        this(new Vec4i(x, y, width, height), true);
    }

    @Override
    public void close() throws Exception {
        destroyView(viewPointer);
    }


    /**
     * @param vec           一个表示坐标和长宽的向量
     * @param isTransparent 表示view的背景是否透明
     */
    public View(Vec4i vec, boolean isTransparent/*, float scale*/) {
        bounds = vec;
        //this.scale = Minecraft.getInstance().getMainWindow().getGuiScaleFactor();
        this.isTransparent = isTransparent;
        viewPointer = WebRenderer.INSTANCE.createView((int) (vec.w * scale), (int) (vec.h * scale), isTransparent, this);
    }

    /**
     * @return 这个view的背景是否透明（默认为true且一旦已实例化则不能修改）
     */
    public boolean isTransparent() {
        return isTransparent;
    }

    /**
     * @return view的位置信息
     */
    public Vec4i getBounds() {
        return bounds;
    }

    /**
     * 设置view的坐标以及长宽
     */
    public View setBounds(Vec4i bounds) {
        this.bounds = bounds;
        //this.scale = Minecraft.getInstance().getMainWindow().getGuiScaleFactor();
        //float scale = this.scale;
        //if (scale > Minecraft.getInstance().getMainWindow().getGuiScaleFactor())
        //    scale = (float) Minecraft.getInstance().getMainWindow().getGuiScaleFactor();
        resize(viewPointer, (int) (bounds.w * scale), (int) (bounds.h * scale));
        return this;
    }

    /**
     * 设置WebScreen窗口大小改变时的回调函数
     */
    public View setResizeCallback(IResizeCallback callback) {
        resizeCallback = callback;
        return this;
    }

    void onResize(Vec2i vec) {
        if (resizeCallback != null) setBounds(resizeCallback.onResize(vec));
    }

    @SuppressWarnings("UnusedReturnValue")
    public View loadHTML(String html) {
        nloadHTML(viewPointer, html);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public View loadHTML(ResourceLocation location) throws IOException {
        String path = "/assets/" + location.getNamespace() + "/web/" + location.getPath();
        FileUtils.upzipIfNeeded(path);
        String url = "file:///" + "mods/webcraft" + path;
        loadURL(url);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public View loadHTML(Class<?> c, ResourceLocation location) throws IOException {
        String path = "/assets/" + location.getNamespace() + "/web/" + location.getPath();
        FileUtils.upzipIfNeeded(c, path);
        String url = "file:///" + "mods/webcraft" + path;
        loadURL(url);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public View loadURL(URL url) {
        loadURL(url.toString());
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public View loadURL(String url) {
        //System.out.println(url);
        nloadURL(viewPointer, url);
        return this;
    }

    /**
     * 绘制view，注意本方法必须在离屏渲染之后执行
     */
    public void draw() {
        long rtt = getRTT(viewPointer);
        int textureID = getRTTTextureID(rtt);
        float t = getRTTTop(rtt), b = getRTTBottom(rtt), l = getRTTLeft(rtt), r = getRTTRight(rtt);
        //int pID = WCShaders.getSRGBToLinearProgramID();
        //glUseProgram(pID);
        RenderSystem.setShaderTexture(0, textureID);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(bounds.x, bounds.y, 0.0).uv(l, t).endVertex();
        bufferbuilder.vertex(bounds.x + bounds.w, bounds.y, 0.0).uv(r, t).endVertex();
        bufferbuilder.vertex(bounds.x + bounds.w, bounds.y + bounds.h, 0.0).uv(r, b).endVertex();
        bufferbuilder.vertex(bounds.x, bounds.y + bounds.h, 0.0).uv(l, b).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        destroyRTT(rtt);
    }

    public void fireMouseEvent(int type, int buttonType, int x, int y) {
        nfireMouseEvent(viewPointer, type, buttonType, (int) (x * scale), (int) (y * scale));
    }

    public void fireScrollEvent(int amount) {
        nfireScrollEvent(viewPointer, amount);
    }

    public void fireKeyEvent(int type, int modifiers, String text, int scanCode, int keyCode) {
        nfireKeyEvent(viewPointer, type, modifiers, text, scanCode, keyCode);
    }

    public View addJSFuncWithCallback(String name, IJSFuncCallback callback) {
        jsCallbackMap.put(name, callback);
        return this;
    }

    public void addDOMReadyListener(Runnable runnable) {
        domReadyCallbakList.add(runnable);
    }

    public void onDOMReady() {
        jsCallbackMap.keySet().forEach(name -> addJSFuncWithCallback(viewPointer, name));
        domReadyCallbakList.forEach(Runnable::run);
    }

    public void enable() {
        enable(viewPointer);
    }

    public void disable() {
        disable(viewPointer);
    }

    public String jsFuncCallback(String funcName) {
        IJSFuncCallback callback = jsCallbackMap.get(funcName);
        JsonElement obj = callback.callback(null);
        return obj == null ? null : obj.toString();
    }

    public String jsFuncCallback(String funcName, String jsonStr) {
        IJSFuncCallback callback = jsCallbackMap.get(funcName);
        JsonElement obj = callback.callback(JsonParser.parseString(jsonStr));
        return obj == null ? null : obj.toString();
    }

    /**
     * 执行js
     *
     * @param js js代码
     * @return 返回json
     */
    public JsonElement evaluteJS(String js) {
        String result = evaluateScript(viewPointer, js);
        if (result == null) return null;
        return JsonParser.parseString(result);
    }

    public JsonElement evaluteJSFunc(String jsFuncName, JsonObject arg) {
        String js = jsFuncName + "(" + arg.toString() + ")";
        String result = evaluateScript(viewPointer, js);
        if (result == null) return null;
        return JsonParser.parseString(result);
    }



    /*public static void test(int x)
    {
        WebCraft.LOGGER.info(x);
    }*/

    /**
     * 当WebScreen的尺寸发生改变时会调用的回调函数
     */
    public interface IResizeCallback {
        /**
         * @param vec 用一个二维向量表示WebScreen的尺寸，其中x为长，y为宽
         * @return 用一个四维向量表示的view的坐标和长宽
         */
        Vec4i onResize(Vec2i vec);
    }

    public interface IJSFuncCallback {
        JsonElement callback(JsonElement obj);
    }

    private native void nloadURL(long pointer, String url);

    private native void nloadHTML(long pointer, String html);

    private native void resize(long pointer, int width, int height);

    private native long getRTT(long pointer);

    private native int getRTTTextureID(long rttPointer);

    private native float getRTTTop(long rttPointer);

    private native float getRTTBottom(long rttPointer);

    private native float getRTTRight(long rttPointer);

    private native float getRTTLeft(long rttPointer);

    private native void nfireScrollEvent(long pointer, int amount);

    private native void nfireMouseEvent(long pointer, int type, int buttonType, int x, int y);

    private native void destroyRTT(long pointer);

    private native void destroyView(long pointer);

    private native void nfireKeyEvent(long pointer, int type, int modifiers, String text, int scanCode, int keyCode);

    private native void addJSFuncWithCallback(long pointer, String funcName);

    private native void enable(long pointer);

    private native void disable(long pointer);

    private native String evaluateScript(long pointer, String script);
}
