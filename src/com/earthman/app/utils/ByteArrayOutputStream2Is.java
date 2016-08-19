package com.earthman.app.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 继承了 java.io.ByteArrayOutputStream，可以把 ByteArrayOutputStream 内部的 buf 直接作为输入流的 buf。避免把输出流转成输入流时，调用 toByteArray()
 * 方法发生数组拷贝 Arrays.copyOf(buf, count)，消耗不必要的内存
 * 
 * @author lp.chen
 * @time 2015年2月3日 上午10:38:17
 */
public class ByteArrayOutputStream2Is extends ByteArrayOutputStream {
    public ByteArrayOutputStream2Is() {
        super(1024);
    }
 
    public ByteArrayOutputStream2Is(int size) {
        super(size);
    }
 
    public synchronized InputStream asInputStream() {
        return new ByteArrayInputStream(buf, 0, count);
    }
 
    public synchronized byte[] getBuf() {
        return buf;
    }
}