package mona.project.guideme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import mona.project.guideme.R;

public class SlideAdapter extends PagerAdapter {
    private  Context context;
    private int[] GalImages = new int[]{R.drawable.firebase, R.drawable.qr,R.drawable.transportation};
    private String[] texts = new String[]{"Available realtime database, can access to any driver easily and quickly."
            , "Generating QR code after reservation, To maintain your security and safety."
            , "It's easy to find and book a trip anytime to go anywhere."};

    LayoutInflater mLayoutInflater;

    public SlideAdapter(Context context) {
        this.context = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return GalImages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        try {

            View itemView = mLayoutInflater.inflate(R.layout.item_on_boarding, container, false);

            ImageView imageView = itemView.findViewById(R.id.imageview);
            imageView.setImageResource(GalImages[position]);
            TextView textView = itemView.findViewById(R.id.textview);
            textView.setText(texts[position]);

            container.addView(itemView);

            return itemView;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}