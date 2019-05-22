package com.example.sorena.wanandroidapp.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.util.BaseFragment;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.util.MyApplication;
import com.example.sorena.wanandroidapp.util.PictureUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author sorena
 * 轮播图适配器
 */
public class LooperPagerAdapter extends PagerAdapter
{

    private List<String> URLs = null;
    private List<String> messages = null;
    private Vector<Bitmap> bitmaps = null;

    public LooperPagerAdapter(NotifyDataLoadingFinish notifyDataLoadingFinish) {
        super();
        this.notifyDataLoadingFinish = notifyDataLoadingFinish;
    }


    @Override
    public int getCount() {
        if (URLs != null){
            return Integer.MAX_VALUE;
        }else {
            return 0;
        }
    }

    @NonNull
    @Override
    public synchronized Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView;
        int realPosition = position % URLs.size();
        imageView = new ImageView(container.getContext());
        if (bitmaps == null){
            imageView.setImageBitmap(PictureUtil.getBitmaps(URLs.get(realPosition)));
        }else {
            //这里会抛异常2019/5/8
            //好了现在不会了2019/5/9
            //好了还是会2019/5/9
            //好了vector没有用2019/5/9
            //好了把setURLs方法里的notifyDataSetChange注释掉就好了,wtf??   2019/5/10
            //好了我现在在想要不要把退回桌面可以加载更快的提示去掉,但我觉得这个问题还没完全解决.....2019/5/10
//            if (bitmaps.size() <= realPosition){
////                container.removeAllViews();
//                LogUtil.d("日志:","removeAll执行:empty");
//                imageView.setImageBitmap(PictureUtil.getBitmaps(URLs.get(realPosition)));
//                shouldClear = true;
//            }
//            else {
//                if (shouldClear){
//                    LogUtil.d("日志:","removeAll执行:fail");
//                    imageView.setImageBitmap(PictureUtil.getBitmaps(URLs.get(realPosition)));
////                    container.removeAllViews();
//                    shouldClear = false;
//                }
//                else {
//                    LogUtil.d("日志:","普通状态");
//                    imageView.setImageBitmap(bitmaps.get(realPosition));
//                }
            //快速滑动时会突然出现bitmaps的大小突然改变甚至变成0的情况,暂时不知道原因,先返回一张空白图处理 2019/5/8
            //下面的判断条件如果是<=就会自动显示图片，否则需要手动切换到后台再切回来才能正常显示图片       2019/5/9
            if (realPosition < bitmaps.size()){
                imageView.setImageBitmap(bitmaps.get(realPosition));
            }
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


    public void setURLs(List<String> URLs) {
        this.URLs = URLs;
        LogUtil.i("日志","设置数据");
        bitmaps = new Vector<>();
        new Thread(() ->
        {
            {
                for (String s : LooperPagerAdapter.this.URLs)
                {
                    bitmaps.add(PictureUtil.getBitmaps(s));
                }
                while (true)
                {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (bitmaps.size() >= URLs.size()) {
                        //this.notifyDataSetChanged();
                        notifyDataLoadingFinish.doOnFinish();
                        break;
                    }
                    LogUtil.d("日志:bitmaps.size() ", "" + bitmaps.size());
                }
            }
        }).start();

    }

    public List<String> getURLs() {
        return URLs;
    }

    public void setMessages(final List<String> messages) {
        this.messages = messages;

    }


    public List<String> getMessages() {
        return messages;
    }

    private NotifyDataLoadingFinish notifyDataLoadingFinish;
    public interface NotifyDataLoadingFinish {
        void doOnFinish();
    }



}
