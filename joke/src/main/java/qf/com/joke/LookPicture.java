package qf.com.joke;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Administrator on 2016/10/24 0024.
 */
public class LookPicture extends Activity {
    PhotoView pv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
        pv = (PhotoView) findViewById(R.id.pv);
        if (MainApplication.drawable != null) {
            pv.setImageDrawable(MainApplication.drawable);
        } else {
            finish();
        }
        pv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainApplication.drawable = null;
    }
}
