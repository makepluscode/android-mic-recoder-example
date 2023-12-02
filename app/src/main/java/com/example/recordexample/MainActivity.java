package com.example.recordexample;

import android.os.ParcelFileDescriptor;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 메인 액티비티의 레이아웃을 설정합니다.
        setContentView(R.layout.activity_main);

        // 녹음 버튼을 초기화하고 클릭 리스너를 설정합니다.
        initializeRecordingButtons();
    }

    // 녹음 시작 및 중지 버튼을 초기화하고 이벤트 리스너를 설정하는 메소드입니다.
    private void initializeRecordingButtons() {
        Button recordButton = findViewById(R.id.recordButton);
        Button stopButton = findViewById(R.id.stopButton);

        // 녹음 시작 버튼에 클릭 리스너를 설정합니다.
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording) {
                    startRecording();
                }
            }
        });

        // 녹음 중지 버튼에 클릭 리스너를 설정합니다.
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    stopRecording();
                }
            }
        });
    }

    // 녹음을 시작하는 메소드입니다.
    private void startRecording() {
        try {
            fileUri = createFileUri(); // 녹음 파일의 URI를 생성합니다.
            setupMediaRecorder(); // MediaRecorder를 설정합니다.

            // 녹음을 준비하고 시작합니다.
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true; // 녹음 상태를 true로 변경합니다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // MediaRecorder를 설정하는 메소드입니다.
    private void setupMediaRecorder() throws IOException {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 오디오 소스 설정
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // 출력 형식 설정
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // 오디오 인코더 설정

        // ParcelFileDescriptor를 통해 얻은 FileDescriptor를 사용
        ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(fileUri, "w");
        if (pfd == null) {
            throw new IOException("Cannot open file descriptor for URI: " + fileUri);
        }
        mediaRecorder.setOutputFile(pfd.getFileDescriptor());
    }


    // MediaStore를 사용하여 녹음 파일의 URI를 생성하는 메소드입니다.
    private Uri createFileUri() throws IOException {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, createFileName()); // 파일의 이름을 설정합니다.
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/3gpp"); // 파일의 MIME 타입을 설정합니다.
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MUSIC + "/RecordExample"); // 파일의 저장 경로를 설정합니다.

        // 생성된 정보를 바탕으로 파일 URI를 MediaStore에 추가합니다.
        Uri uri = getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
        if (uri == null) {
            throw new IOException("Failed to create new MediaStore record.");
        }
        return uri;
    }

    // 현재 시간을 기반으로 파일 이름을 생성하는 메소드입니다.
    private String createFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        return "REC_" + timeStamp + ".3gpp";
    }

    // 파일 URI에 대한 파일 디스크립터를 반환하는 메소드입니다.
    private ParcelFileDescriptor getFileDescriptor(Uri uri) throws IOException {
        ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "w");
        if (pfd == null) {
            throw new IOException("Cannot open file descriptor for URI: " + uri);
        }
        return pfd;
    }

    // 녹음을 중지하고 MediaRecorder 리소스를 해제하는 메소드입니다.
    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop(); // 녹음을 중지합니다.
            mediaRecorder.release(); // MediaRecorder 리소스를 해제합니다.
            mediaRecorder = null; // MediaRecorder 객체를 null로 설정합니다.
            isRecording = false; // 녹음 상태를 false로 변경합니다.
        }
    }
}
