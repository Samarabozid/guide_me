package mona.project.guideme.view.fragment.splashCycle;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mona.project.guideme.BaseFragment;
import mona.project.guideme.R;
import mona.project.guideme.adapter.SlideAdapter;

import static mona.project.guideme.helper.HelperMethod.replaceFragment;

public class SliderFragment extends BaseFragment {

    private static final int MAX_STEP = 3;

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.img_first)
    ImageView imgFirst;
    @BindView(R.id.layout_dots)
    LinearLayout layoutDots;

    public SliderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        intiFragment();
        View view =  inflater.inflate(R.layout.fragment_slider, container, false);
        ButterKnife.bind(this,view);
        bottomProgressDots(0);
        viewPager.setAdapter(new SlideAdapter(getContext()));
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        return view;
    }

    @OnClick({R.id.img_first})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_first:
                int current = viewPager.getCurrentItem() + 1;
                if (current < MAX_STEP) {
                    viewPager.setCurrentItem(current);
                } else {
                    replaceFragment(getActivity().getSupportFragmentManager(), R.id.container, new LoginFragment());
                }
        }
    }

    private void bottomProgressDots(int current_index) {
        ImageView[] dots = new ImageView[MAX_STEP];
        layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(getContext());
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(40, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(R.color.background, PorterDuff.Mode.SRC_IN);
            layoutDots.addView(dots[i]);
        }

        if (dots.length > 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(20, 15));
            params.setMargins(10, 10, 10, 10);
            dots[current_index].setLayoutParams(params);

            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        }
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            bottomProgressDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    @Override
    public void onBack() {
        getActivity().finish();
    }
}