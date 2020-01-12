package com.englishlearning.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.englishlearning.android.MainActivity;
import com.englishlearning.android.R;
import com.englishlearning.android.jsonBean.EnglishScore;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OralCardActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {
    private ImageView back_oralCard;
    private MediaRecorder soundRecorder; //语音录制对象
    private MediaRecorder videoRecorder;  //录制视频对象
    private MediaPlayer videoPlayer;//播放视频
    private File soundFile;  //临时存储声音文件
    private File videoFile;  //临时存储视频文件
    private Camera camera;
    private SurfaceView surfaceView;//显示视频控件
    private ImageView startRecord;  //开始录制按钮
    private TextView english;
    private TextView score;
    private TextView integrity;
    private TextView fluency;
    private ImageView nextWord;
    private SurfaceHolder holder;
    private static final String[] data={"[ɪ]","[i]","[i:]","[e]","[ɜː]","[ə]","[ɑ:]","[ʌ]","[ɔ:]","[ɔ]","[u:]","[u]","[ai]","[ei]","[au]","[əu]","[iə]","[εə]","[uə]","[ɔi]","[p]","[b]","[t]",
            "[d]","[k]","[g]","[s]","[z]","[f]","[v]","[ʍ]","[w]","[∫]","[ʒ]","[h]","[j]","[l]","[r]","[m]","[n]","[ŋ]","[θ]","[ð]","[t∫]","[dʒ]","[ts]","[dz]","[tr]","[dr]"};

    private int wordIndex=0;


    private Button bt_pause;
    //private RelativeLayout rLayout_video;
    private MediaPlayer mediaPlayer;//多媒体播放器
    private SurfaceView sv;//****************SurfaceView是一个在其他线程中显示、更新画面的组件，专门用来完成在单位时间内大量画面变化的需求
    private SurfaceHolder myholder;//****************SurfaceHolder接口为一个显示界面内容的容器
    private SeekBar seekBar;//===============进度条
    private int currentPosition;//===============记录当前播放文件播放的进度
    private String savedFilepath;//===============记录当前播放文件的位置

    private boolean isPlaying;  //记录是否正在播放
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() !=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_oral_card);
        initControls();
        wordUpdate(wordIndex);
        initEvents();
        holder = surfaceView.getHolder();//取得holder
        holder.addCallback(this);//添加surfaceView的回调
        // setType必须设置，要不出错.
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    //控件初始化
    private void initControls(){
        startRecord = findViewById(R.id.startRecord);
        surfaceView=findViewById(R.id.videoRecord);
        english = findViewById(R.id.english);
        score = findViewById(R.id.score);
        nextWord=findViewById(R.id.nextWord);
        integrity=findViewById(R.id.integrity);
        fluency=findViewById(R.id.fluency);
        back_oralCard=findViewById(R.id.back_oralCard);
        bt_pause = (Button) findViewById(R.id.bt_pause);
        sv = (SurfaceView) findViewById(R.id.sv);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
    }
    private void wordUpdate(int index){
        english.setText(data[index]);
        bt_pause.setTag("开始");
        String videoName="video"+index;
        savedFilepath="android.resource://" + getPackageName() + "/" + getResources().getIdentifier(videoName, "raw", getPackageName());
        score.setVisibility(View.INVISIBLE);
        integrity.setVisibility(View.INVISIBLE);
        fluency.setVisibility(View.INVISIBLE);
        nextWord.setVisibility(View.INVISIBLE);
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
        // 开启相机
        if (camera == null) {
            camera = Camera.open(1);
        }
        doChange(holder);
    }
    private void scoreUpdate(final EnglishScore englishScore){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                score.setText("总分："+englishScore.getScore());
                integrity.setText("完整度："+englishScore.getDetail_score().getIntegrity());
                fluency.setText("流利度："+englishScore.getDetail_score().getFluency());
                score.setVisibility(View.VISIBLE);
                integrity.setVisibility(View.VISIBLE);
                fluency.setVisibility(View.VISIBLE);
                nextWord.setVisibility(View.VISIBLE);
            }
        });
    }
    private void scoreUpdate(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                score.setText("总分："+8);
                integrity.setText("完整度："+7.5);
                fluency.setText("流利度："+8.1);
                score.setVisibility(View.VISIBLE);
                integrity.setVisibility(View.VISIBLE);
                fluency.setVisibility(View.VISIBLE);
                nextWord.setVisibility(View.VISIBLE);
            }
        });
    }

    //视频配置初始化
    private void prepareVideoRecorder() throws IOException {
        if (videoRecorder == null) {
            videoRecorder = new MediaRecorder();
            videoRecorder.reset();
        }
        camera.unlock();
        /*videoRecorder设置部分*/
        // Step 1: Unlock and set camera to MediaRecorder
        videoRecorder.setCamera(camera);
        videoRecorder.setOrientationHint(270);
        // Step 2: Set sources
        videoRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        videoRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        videoRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));  //设置输出视频文件的格式和编码,利用MediaRecorder.setProfile方法进行设置，利用CamcorderProfile.get()可以获取CamcorderProfile对象，内部包含了系统封装好的一些配置。
        // Step 4: Set output file
        videoFile = File.createTempFile("video",".mp4");
        videoRecorder.setOutputFile(videoFile.getAbsolutePath());
        // Step 5: Set the preview output
        videoRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
        // Step 6: Prepare configured MediaRecorder
    }
    //事件初始化
    @SuppressLint("ClickableViewAccessibility")
    private void initEvents(){
        bt_pause.setOnClickListener(this);
        myholder = sv.getHolder();//****************得到显示界面内容的容器
        myholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //设置surfaceView自己不管理缓存区。虽然提示过时，但最好还是设置下
        myholder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {//holder被销毁时回调。最小化时都会回调
                // 销毁SurfaceHolder的时候记录当前的播放位置并停止播放
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    currentPosition = mediaPlayer.getCurrentPosition();
                    mediaPlayer.stop();
                }
            }
            @Override
            public void surfaceCreated(SurfaceHolder myholder) {//holder被创建时回调
                if (currentPosition > 0) {
                    // 创建SurfaceHolder的时候，如果存在上次播放的位置，则按照上次播放位置进行播放
                    play(currentPosition);
                    currentPosition = 0;
                }
            }
            //holder宽高发生变化（横竖屏切换）时回调
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                //Toast.makeText(MainVideoActivity.this, "横竖屏切换", Toast.LENGTH_SHORT).show();
            }
        });
        // 为进度条添加进度更改事件
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 当进度条停止修改的时候触发
                // 取得当前进度条的刻度
                int progress = seekBar.getProgress();
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    // 设置当前播放的位置
                    mediaPlayer.seekTo(progress);
                }
            }
        });

        back_oralCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        startRecord.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try {
                    pause();
                    startRecord.setImageResource(R.drawable.recording);
                    startRecording();//初始化录音
                    prepareVideoRecorder();//
                    //准备录音
                    soundRecorder.prepare();
                    //开始录制
                    soundRecorder.start();
                    try {
                        videoRecorder.prepare();
                        videoRecorder.start();
                    } catch (IllegalStateException e) {
                        Log.d("videoPrepare", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
                    } catch (IOException e) {
                        Log.d("videoPrepare", "IOException preparing MediaRecorder: " + e.getMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        //手向上抬起的按键
        startRecord.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    //stopRecording();
                    videoRecorder.stop();
                    soundRecorder.stop();
                    releaseMediaRecorder();
                    startRecord.setImageResource(R.drawable.record);
                    OkHttpClient okHttpClient=new OkHttpClient();
                    RequestBody fileBody = RequestBody.create(MediaType.parse("audio/mpeg"), soundFile);
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("myWavfile", soundFile.getName(), fileBody)
                            .addFormDataPart("word_name",data[wordIndex]).build();
                    Request request = new Request.Builder()
                            .url("https://t02.io.speechx.cn:8443/MDD_Server/mdd_v18")
                            .addHeader("authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzcGVlY2h4X21kZCIsIlNpZ25lZEJ5IjoianN6aG9uZyIsIkVuZ2xpc2hMZXZlbCI6MywiaXNzIjoiYXV0aDAiLCJuR0JfVVMiOjAsImF1ZCI6Imd1ZXN0IiwiaXNGb3JDaGlsZCI6ZmFsc2UsIm5DbGllbnRJRCI6MTU3NTQ0ODczMCwibk1heENvb" +
                                    "mN1cnJlbnRVc2VyIjowLCJQdWJsaXNoZXJOYW1lIjoiYmVpamluZ2RheHVlXzIwMTkxMjA0MTYzNzA2MzUzIiwiRmVlZEJhY2tUeXBlIjo2LCJleHAiOjE2MDc3ODg4NTAsImlhdCI6MTU3NTQ0ODczMH0.N45sdy7f-hORM8CLbl62YEJZsXj43h97UzrgWuMYLpg")
                            .post(requestBody)
                            .build();
                    if (videoPlayer == null) {
                        videoPlayer = new MediaPlayer();
                    }
                    videoPlayer.reset();
                    Uri uri = Uri.parse(videoFile.getAbsolutePath());
                    videoPlayer = MediaPlayer.create(OralCardActivity.this, uri);
                    videoPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    videoPlayer.setDisplay(holder);
                    try{
                        if (videoPlayer != null) {
                            videoPlayer.stop();
                        }
                        videoPlayer.prepare();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    videoPlayer.start();
                    play(0);
                    videoPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.stop();
                            mp.release();
                            videoPlayer=null;
                            videoFile.delete();
                        }
                    });
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.d("okhttp", "onFailure: " + e.getMessage());
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String responseBack= response.body().string();
                            System.out.println(responseBack);
                            //EnglishScore englishScore=new Gson().fromJson(responseBack,EnglishScore.class);
                           // scoreUpdate(englishScore);
                            scoreUpdate();
                            soundFile.delete();
                        }
                    });
                }
                return false;
            }
        });
        nextWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordIndex++;
                if(wordIndex<data.length){
                    wordUpdate(wordIndex);
                }else {
                    //跳转到主界面
                    startActivity(new Intent(OralCardActivity.this, MainActivity.class));
                }
            }
        });
    }

    //启动录音
    private void startRecording(){
        try{
            //创建录音对象
            soundRecorder = new MediaRecorder();
            //设置声音来源  MIC 系统麦克风
            soundRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置声音格式  MPEG_4 音频、视频的标准格式
            soundRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            //设置声音的编码格式
            soundRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            soundRecorder.setAudioSamplingRate(22050); //采样频率
            soundRecorder.setAudioChannels(AudioFormat.CHANNEL_CONFIGURATION_MONO); //单声道
            //准备临时文件用于承载录制的声音
            soundFile = File.createTempFile("voice"," .mp3");
            //将录制好的声音输出到创建好的临时文件中
            soundRecorder.setOutputFile(soundFile.getAbsolutePath());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 录制视频结束时释放相机资源
     * */
    private void  releaseMediaRecorder() {
        Log.d("tag", "录制结束后释放资源 ");
        if(soundRecorder!=null){
            //释放资源
            soundRecorder.release();
        }
        if (videoRecorder != null) {
            videoRecorder.reset();   // clear recorder configuration
            videoRecorder.release(); // release the recorder object
            videoRecorder = null;
            if(camera!=null){
                camera.release();
                camera=null;
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //将holder ，这个holder为开始在initView里面取得的holder，将它赋给Holder
        this.holder = holder;
        // 开启相机
        if (camera == null) {
            camera = Camera.open(1);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //将holder ，这个holder为开始在initView里面取得的holder，将它赋给Holder
        this.holder  = holder;
        // 设置参数并开始预览
        doChange(this.holder);
    }
    private void doChange(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            //设置surfaceView旋转的角度，系统默认的录制是横向的画面，把这句话注释掉运行你就会发现这行代码的作用
            camera.setDisplayOrientation(getDegree());
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getDegree() {
        //获取当前屏幕旋转的角度
        int rotating = this.getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        //根据手机旋转的角度，来设置surfaceView的显示的角度
        switch (rotating) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        surfaceView = null;
        this.holder = null;
        videoRecorder = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_pause:
                if(bt_pause.getTag().toString().trim().equals("开始")){
                    play(0);
                }else if(bt_pause.getTag().toString().trim().equals("重播")){
                    replay();
                } else {
                    pause();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 开始播放
     *
     * @param msec 播放初始位置
     */
    protected void play(final int msec) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置播放的视频源
            Uri uri = Uri.parse(savedFilepath);
            mediaPlayer = MediaPlayer.create(OralCardActivity.this, uri);
            // 设置显示视频的SurfaceHolder
            mediaPlayer.setDisplay(myholder);
            if (mediaPlayer != null) {
                mediaPlayer.stop();

            }
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    // 按照初始位置播放
                    mediaPlayer.seekTo(msec);
                    // 设置进度条的最大进度为视频流的最大播放时长
                    seekBar.setMax(mediaPlayer.getDuration());
                    // 开始线程，更新进度条的刻度
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                isPlaying = true;
                                while (isPlaying) {
                                    int current = mediaPlayer
                                            .getCurrentPosition();
                                    seekBar.setProgress(current);
                                    sleep(500);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                    bt_pause.setTag("暂停");
                    bt_pause.setBackgroundResource(R.drawable.videoplayer_play);
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 在播放完毕被回调
                    bt_pause.setTag("重播");
                    bt_pause.setBackgroundResource(R.drawable.videoplayer_pause);
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // 发生错误重新播放
                    play(0);
                    isPlaying = false;
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 暂停或继续
     */
    protected void pause() {
        if (bt_pause.getTag().toString().trim().equals("继续")) {
            bt_pause.setTag("暂停");
            mediaPlayer.start();
            bt_pause.setBackgroundResource(R.drawable.videoplayer_play);
            return;
        }
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            bt_pause.setTag("继续");
            bt_pause.setBackgroundResource(R.drawable.videoplayer_pause);
        }

    }
    /**
     * 重播
     */
    public void replay() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            mediaPlayer.seekTo(0);
        }
        bt_pause.setTag("暂停");
    }
}
