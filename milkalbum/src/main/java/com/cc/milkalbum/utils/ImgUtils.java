package com.cc.milkalbum.utils;

/**
 * Created by Administrator on 2017/2/9.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

public class ImgUtils {
    private LruCache<String, Bitmap> lruCache;
    private File cacheDir;
    private ExecutorService newFixedThreadPool;
    private Handler handler;
    public static final int SUCCESS = 100;
    public static final int FAIL = 101;
    //当我们获取图片的时候,分三步
    //1.从缓存中获取图片
    //2.从本地的缓存目录中获取图片,并且获取到之后,放到缓存中
    //3.从网络去下载图片,下载完成之后,保存到本地缓存目录和放到缓存中
    public ImgUtils(Context context,Handler handler){
        //获取缓存的大小
        int maxsize	= (int) (Runtime.getRuntime().maxMemory()/8);
        //maxSize : 设置缓存的最大空间
        lruCache = new LruCache<String, Bitmap>(maxsize){
            //获取移出的图片所占用的空间,当图片移出的时候,需要将图片占用的缓存空间从缓存中移出
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //计算图片所占用的缓存大小
                //getRowBytes : 获取图片一行所占用的大小
                //getHeight :　获取图片所占用行数
                return value.getRowBytes()*value.getHeight();
            }
        };
        //获取缓存目录
        cacheDir = context.getCacheDir();
        //获取线程池
        //nThreads : 线程池中的线程数量
        newFixedThreadPool = Executors.newFixedThreadPool(5);
        this.handler = handler;
    }
    /**
     * 获取图片的方法
     */
    public Bitmap getBitmap(String url,int position){
        Bitmap bitmap = null;
        //1.从缓存中获取图片  (LRUCache<k,v>)  k:key 保存图片的标示,一般都是图片的url地址,v:value 图片
        bitmap = lruCache.get(url);//根据key从缓存中获取相应的图片
        //lruCache.put(url, bitmap):保存图片到缓存中
        if (bitmap!=null) {
            return bitmap;
        }
        //2.从本地的缓存目录中获取图片,并且获取到之后,放到缓存中
        bitmap = getFromLocal(url);
        if (bitmap!=null) {
            return bitmap;
        }
        //3.从网络去下载图片,下载完成之后,保存到本地缓存目录和放到缓存中
        getFromNet(url,position);
        return null;
    }
    /**
     * 从网络下载图片,异步方式,线程池
     * @param url
     * @param position
     */
    private void getFromNet(String url, int position) {
        newFixedThreadPool.execute(new RunnableTask(url,position));
    }
    class RunnableTask implements Runnable{
        private String imageUrl;
        private int position;
        public RunnableTask(String url,int position){
            this.imageUrl = url;
            this.position = position;
        }

        @Override
        public void run() {
            Message message = Message.obtain();
            //下载图片
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(3000);
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                //保存到本地缓存中
                wirteToLocal(imageUrl, bitmap);
                //保存到系统缓冲中
                lruCache.put(imageUrl, bitmap);
                //显示图片,给handler发送消息
                message.what = SUCCESS;
                message.obj = bitmap;
                message.arg1 = position;
                handler.sendMessage(message);
                Log.d("cbx", "getFromNet over, position: " + position + ", url: " + imageUrl);
                return;
            } catch (Exception e) {
                Log.d("cbx", "getFromNet error!!! url: " + imageUrl);
                e.printStackTrace();
            }
            message.what = FAIL;
            handler.sendMessage(message);
        }
    }
    /**
     * 从本地缓存目录获取图片
     * @param url
     */
    private Bitmap getFromLocal(String url) {
        //根据图片的名称获取图片
        try {
            String fileName = MD5Encoder.encode(url).substring(10);
            File file = new File(cacheDir, fileName);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            //放到缓存当中
            lruCache.put(url, bitmap);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将图片保存到本地缓存目录中
     */
    public void wirteToLocal(String url,Bitmap bitmap){
        //url名称,通过MD5加密,并且截取前10位作为名称
        try {
            String fileName = MD5Encoder.encode(url).substring(10);
            File file = new File(cacheDir, fileName);
            FileOutputStream out = new FileOutputStream(file);
            //format :图片的格式（android中用的png多，因为png质量是不会改变的）
            //quality : 压缩比例
            //stream : 流信息
            bitmap.compress(CompressFormat.JPEG, 100, out);//将图片保存到那个位置
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
