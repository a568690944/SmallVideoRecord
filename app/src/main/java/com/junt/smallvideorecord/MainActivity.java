package com.junt.smallvideorecord;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.junt.videorecorderlib.RecordConfig;
import com.junt.videorecorderlib.RecordVideoActivity;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoulPermission.getInstance().checkAndRequestPermissions(Permissions.build(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO), new CheckRequestPermissionsListener() {
                    @Override
                    public void onAllPermissionOk(Permission[] allPermissions) {
                        toRecordVideo();
                    }

                    @Override
                    public void onPermissionDenied(Permission[] refusedPermissions) {

                    }
                });
            }
        });
    }

    private final int REQUEST_CODE_VIDEO=0;

    // 录制视频
    private void toRecordVideo() {
        RecordConfig
                .getInstance()
                .with(this)
                .setQuality(RecordConfig.Quality.QUALITY_480P)
                .setEncodingBitRate(5*1280*720)
                .setFrameRate(30)      //请设置>=30
                .setMaxDuration(6*1000)
                .setFocusMode(RecordConfig.FocusMode.FOCUS_MODE_CONTINUOUS_VIDEO)
                .setOutputPath("/smallvideo/")
                .obtainVideo(MainActivity.this,REQUEST_CODE_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_VIDEO&&resultCode==RESULT_OK){
            String videoPath=RecordConfig.obtainVideoPath(data);
            Log.i(this.getClass().getSimpleName(),"obtainVideoPath="+videoPath);
        }
    }
}
