package feicui.edu.safeapp.ui;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import feicui.edu.safeapp.R;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final String TAG = "SplashActivity";

    private ViewPager mviewPager;
    private ArrayList<View> mList;

    int[] pics = {R.mipmap.adware_style_creditswall, R.mipmap.adware_style_banner,
            R.mipmap.adware_style_applist};

    private Button mBtnSkip;

//    private ImageView icon1, icon2, icon3;
//    ImageView icons[] = {icon1, icon2, icon3};
    private ImageView icons[]=new ImageView[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initview();
    }

    private void initview() {
        icons[0] = (ImageView) findViewById(R.id.icon1);
        icons[1] = (ImageView) findViewById(R.id.icon2);
        icons[2] = (ImageView) findViewById(R.id.icon3);

        icons[0].setImageResource(R.drawable.adware_style_selected);

        mBtnSkip = (Button) findViewById(R.id.btn_skip);

        mBtnSkip.setOnClickListener(this);

        mList = new ArrayList<>();

        mviewPager = (ViewPager) findViewById(R.id.vp_guids);

        for (int i = 0; i < pics.length; i++) {

            ImageView iv = new ImageView(this);

            iv.setImageResource(pics[i]);
            mList.add(iv);
        }
        mviewPager.setAdapter(new myPagerAdapter(mList));

        mviewPager.addOnPageChangeListener(this);
         // mviewPager.setPageTransformer(true,new DepthPageTransformer());

          mviewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        Log.d(TAG, "onPageScrolled: start" + "position:" + position + "offset" + positionOffset + "pisels" + positionOffsetPixels);

    }

    @Override
    public void onPageSelected(int position) {
        if (position  == 2) {
            mBtnSkip.setVisibility(View.VISIBLE);
        } else {
            mBtnSkip.setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i <icons.length ; i++) {
            icons[i].setImageResource(R.drawable.adware_style_default);

        }
        icons[position].setImageResource(R.drawable.adware_style_selected);

        Log.d(TAG, "onPageSelected:start, position: " + position );

    }

    @Override
    public void onPageScrollStateChanged(int state) {

        Log.d(TAG, "onPageScrollStateChanged: start,state:" + state);
    }

    private class myPagerAdapter extends PagerAdapter {
        private ArrayList<View> mList;

        public myPagerAdapter(ArrayList<View> list) {

            mList = list;
        }


        //初始化里面的视图
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            container.addView(mList.get(position), 0);
            return mList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));
        }

        @Override
        public int getCount() {
            if (mList != null) {
                return mList.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view == object;

        }
    }

    //官方动画1

//    private class DepthPageTransformer implements ViewPager.PageTransformer {
//
//        private static final float MIN_SCALE = 0.75f;
//
//        public void transformPage(View view, float position) {
//            int pageWidth = view.getWidth();
//
//            if (position < -1) { // [-Infinity,-1)
//                // This page is way off-screen to the left.
//                view.setAlpha(0);
//
//            } else if (position <= 0) { // [-1,0]
//                // Use the default slide transition when moving to the left page
//                view.setAlpha(1);
//                view.setTranslationX(0);
//                view.setScaleX(1);
//                view.setScaleY(1);
//
//            } else if (position <= 1) { // (0,1]
//                // Fade the page out.
//                view.setAlpha(1 - position);
//
//                // Counteract the default slide transition
//                view.setTranslationX(pageWidth * -position);
//
//                // Scale the page down (between MIN_SCALE and 1)
//                float scaleFactor = MIN_SCALE
//                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
//                view.setScaleX(scaleFactor);
//                view.setScaleY(scaleFactor);
//
//            } else { // (1,+Infinity]
//                // This page is way off-screen to the right.
//                view.setAlpha(0);
//            }
//        }
//    }

    //官方动画2

    private class ZoomOutPageTransformer implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
