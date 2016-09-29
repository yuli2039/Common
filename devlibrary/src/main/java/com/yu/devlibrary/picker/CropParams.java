package com.yu.devlibrary.picker;

/**
 * 调用系统裁剪的参数
 *
 * @author yu
 *         Create on 16/4/20.
 */
public class CropParams {

    public int aspectX;
    public int aspectY;
    public int outputX;
    public int outputY;

    public CropParams() {
        this(1, 1, 300, 300);// 默认 1:1 300*300
    }

    public CropParams(int aspectX, int aspectY, int outputX, int outputY) {
        this.aspectX = aspectX;
        this.aspectY = aspectY;
        this.outputX = outputX;
        this.outputY = outputY;
    }

}
