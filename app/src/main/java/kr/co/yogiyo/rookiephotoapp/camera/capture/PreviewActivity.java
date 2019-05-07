package kr.co.yogiyo.rookiephotoapp.camera.capture;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import kr.co.yogiyo.rookiephotoapp.BaseActivity;
import kr.co.yogiyo.rookiephotoapp.R;

public class PreviewActivity extends BaseActivity implements View.OnClickListener {


    private ImageView previewImageView;
    private Button backButton;
    private Button editPhotoButton;

    private Bitmap capturedImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        initImageView();
        initView();

    }

    private void initImageView() {
        previewImageView = findViewById(R.id.preview_image);

        capturedImageBitmap = ResultHolder.getBitmap();

        previewImageView.setVisibility(View.VISIBLE);
        previewImageView.setImageBitmap(capturedImageBitmap);

        showToast(String.format("width: %d, height: %d \nsize : %f MB",
                capturedImageBitmap.getWidth(), capturedImageBitmap.getHeight(), getApproximateFileMegabytes(capturedImageBitmap)));

    }

    private void initView() {
        backButton = findViewById(R.id.btn_back_capture);
        editPhotoButton = findViewById(R.id.btn_edit);
        backButton.setOnClickListener(this);
        editPhotoButton.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (capturedImageBitmap != null && !capturedImageBitmap.isRecycled()) {
            capturedImageBitmap.recycle();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back_capture:
                onBackPressed();
                break;
            case R.id.btn_edit:
                // 편집화면으로 이동
                break;
        }

    }

    private static float getApproximateFileMegabytes(Bitmap bitmap) {
        return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024 / 1024;

    }

}
