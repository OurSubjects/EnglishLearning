package com.englishlearning.android.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * 文件存储在SD卡的相关类
 */

public class SDUtil {

    private String mSDPath;//SD卡的路径

    private String mRecordVoiceAhead;//音频记录的绝对路径头

    private String mWordBookVoiceAhead;//单词本音频文件的绝对路径头

    private String mDailySentenceImgAhead;//每日一句图片文件的绝对路径头

    private File mRecordCatalog;//存放音频记录的目录

    private File mWordBookCatalog;//存放单词本音频文件的目录

    private File mDailySentenceCatalog;//存放每日一句图片的目录


    private SDUtil() {
        //若用户插入了SD卡且可以读写
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d("SDUtil","SD inserted");
            //获取SD卡的目录
//           mSDPath= Context.getExternalFilesDir("");
             mSDPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            Log.d("mSDPath",mSDPath);
            //创建相关的目录
            mRecordCatalog = createCatalog(mSDPath, "RecordVoice");
            mWordBookCatalog = createCatalog(mSDPath, "WordBookVoice");
            mDailySentenceCatalog = createCatalog(mSDPath, "DailySentenceImg");
            //获取相关文件的绝对路径头
            mRecordVoiceAhead = mRecordCatalog.getAbsolutePath() + "/";
            mWordBookVoiceAhead = mWordBookCatalog.getAbsolutePath() + "/";
            mDailySentenceImgAhead = mDailySentenceCatalog.getAbsolutePath() + "/";
        }
    }

    /**
     * 获取HandlerSD实例的工具方法
     *
     * @return
     */
    public static SDUtil getHanderSD() {
        return new SDUtil();
    }


    /**
     * 创建目录
     *
     * @param SDPath      SD卡的路径
     * @param catalogName 目录名
     * @return 存放文件的目录
     */
    private File createCatalog(String SDPath, String catalogName) {
        File catalog = new File(SDPath, catalogName);
        //若本地存在此目录名的文件夹
        if (catalog.exists() && catalog.isDirectory()) {
            //存在，则直接返回该文件夹
            return catalog;
        } else {
            //否则以此目录名创建新的文件夹
            catalog.mkdir();
            return catalog;
        }
    }

    /**
     * 创建文件
     *
     * @param pathHead 文件的绝对路径头
     * @param fileName 文件名
     * @return 音频文件
     */
    public File creatFile(String pathHead, String fileName) {
        File file = new File(pathHead, fileName);
        //判断文件是否已经存在
        if (file.exists()) {
            //若已经存在，则直接使用它
            return file;
        } else {
            //否则，创建一个新的文件
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        }
    }


    /**
     * 获取音频记录文件的绝对路径
     *
     * @param wordName 音频的文件名
     * @return 音频的地址，若音频不存在，则返回空
     */
    public String getRecordVoiceAddress(String wordName) {
        File file = new File(mRecordVoiceAhead + wordName);
        if (file.exists()) {
            //若音频存在
            return file.getAbsolutePath();
        } else {
            //音频不存在，返回空
            return null;
        }
    }


    /**
     * 获取单词本音频文件的绝对路径
     *
     * @param wordName 音频的文件名
     * @return 音频的地址，若音频不存在，则返回空
     */
    public String getWordBookVoiceAddress(String wordName) {
        File file = new File(mWordBookVoiceAhead + wordName);
        if (file.exists()) {
            //若音频存在
            return file.getAbsolutePath();
        } else {
            //音频不存在，返回空
            return null;
        }
    }

    /**
     * 获取每日一句图片文件的绝对路径
     *
     * @param fileName
     * @return
     */
    public String getDailySentenceImgAddress(String fileName) {
        File file = new File(mDailySentenceImgAhead + fileName);
        if (file.exists()) {
            //若图片存在,返回路径
            return file.getAbsolutePath();
        } else {
            //图片不存在，返回空
            return null;
        }
    }

    /**
     * 删除指定的图片
     *
     * @param fileName
     */
    public void deleteImg(String fileName) {
        File file = new File(mDailySentenceCatalog, fileName);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    /**
     * 删除RecordVoice目录下全部的音频的文件
     */
    public void deleteAllRecordVoice() {
        File[] files = mRecordCatalog.listFiles();
        for (File file : files) {
            file.delete();
        }
    }

    /**
     * 删除WordBook目录下指定的音频的文件
     *
     * @param wordName 音频文件名
     */
    public void deleteWordBookVoice(String wordName) {
        File file = new File(mWordBookVoiceAhead, wordName);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    /**
     * 删除全部图片
     */
    public void deleteAllImg() {
        File[] files = mDailySentenceCatalog.listFiles();
        for (File file : files) {
            file.delete();
        }
    }

    /**
     * SD卡保存的图片数量
     *
     * @return
     */
    public int numberOfImg() {
        File[] files = mDailySentenceCatalog.listFiles();
        if (files == null) {
            return 0;
        }
        return files.length;
    }

    /**
     * 复制文件到单词本中
     *
     * @param oldPath   原文件的路径
     * @param voiceName 文件名
     * @return
     */
    public boolean copyToWordBook(String oldPath, String voiceName) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String newPath = creatFile(mWordBookVoiceAhead, voiceName).getAbsolutePath();
            FileInputStream in = null;
            FileOutputStream out = null;
            try {
                in = new FileInputStream(oldPath);
                out = new FileOutputStream(newPath);
                byte[] bt = new byte[1024 * 2];
                int length;
                while ((length = in.read(bt)) != -1) {
                    out.write(bt, 0, length);
                }
                out.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }


    /**
     * 复制文件到记录中
     *
     * @param oldPath   原文件的路径
     * @param voiceName 文件名
     * @return
     */
    public boolean copyToRecord(String oldPath, String voiceName) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String newPath = creatFile(mRecordVoiceAhead, voiceName).getAbsolutePath();
            FileInputStream in = null;
            FileOutputStream out = null;
            try {
                in = new FileInputStream(oldPath);
                out = new FileOutputStream(newPath);
                byte[] bt = new byte[1024 * 2];
                int length;
                while ((length = in.read(bt)) != -1) {
                    out.write(bt, 0, length);
                }
                out.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }


    /**
     * 保存一个音频到记录中
     *
     * @param wordName 单词
     * @param in       音频的输入流
     */
    public void saveToRecord(String wordName, InputStream in) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            FileOutputStream out = null;
            File wordVoice = creatFile(mRecordVoiceAhead, wordName);
            try {
                out = new FileOutputStream(wordVoice);
                byte[] bt = new byte[2 * 1024];
                int length;
                while ((length = in.read(bt)) != -1) {
                    out.write(bt, 0, length);
                }
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    }

    /**
     * 保存图片到SD卡中
     *
     * @param fileName 文件名
     * @param bitmap   图片
     */
    public void saveImg(String fileName, Bitmap bitmap) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) && bitmap != null) {
            File file = creatFile(mDailySentenceImgAhead, fileName);
            BufferedOutputStream buffOS = null;
            try {
                buffOS = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, buffOS);
                buffOS.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (buffOS != null) {
                        buffOS.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
