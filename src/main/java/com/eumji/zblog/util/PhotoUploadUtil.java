package com.eumji.zblog.util;

import com.eumji.zblog.vo.PhotoResult;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import java.io.File;
import java.util.Calendar;

/**
 * FILE: com.eumji.zblog.util.PhotoUploadUtil.java
 * MOTTO:  不积跬步无以至千里,不积小流无以至千里
 * AUTHOR: EumJi
 * DATE: 2017/4/21
 * TIME: 22:08
 */
public class PhotoUploadUtil {
    //设置好账号的ACCESS_KEY和SECRET_KEY
    private static String ACCESS_KEY = "canKgBzqR_AN_EviuOuWYUT4Vko3gi5dTNZFiQ4C";
    private static String SECRET_KEY = "feAWGHn_UmhGU2ckkHd9F2q2qFpC7lPqlemvEOCH";
    //要上传的空间
    private static String bucketname = "eumji025";
    public static String basePath = "http://of8rkrh1w.bkt.clouddn.com/";

    //上传到七牛后保存的文件名
    public static String getFilePath(String fileName){
        Calendar instance = Calendar.getInstance();
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH)+1;
        int day = instance.get(Calendar.DATE);
        return year+"/"+month+"/"+day+"/"+fileName;
    }

    public static Auth getAuth(){
        return Auth.create(ACCESS_KEY,SECRET_KEY);
    }

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public static String getUpToken(){
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        return auth.uploadToken(bucketname);
    }

    /**
     * 长传图片
     * @param realName 图片路径名
     * @param filename 图片名
     * @return
     */
    public static PhotoResult uploadPhoto(String realName, String filename){
        PhotoResult result = new PhotoResult();
        try {
            Configuration cfg = new Configuration(Zone.zone2());
            Response response = new UploadManager(cfg).put(realName, getFilePath(filename), getUpToken());
            if (response.isOK()){
                result.setSuccess(1);
                result.setUrl(basePath+getFilePath(filename));
                return result;
            }
        } catch (QiniuException e) {
            result.setSuccess(0);
            result.setMessage(e.getMessage());
            return result;
        }
        return result;
    }

    /**
     * 删除图片
     * @param fileNames
     * @return
     */
    public static int deletePhoto(String[] fileNames){
        int result = 0;
        Configuration cfg = new Configuration(Zone.zone2());
        BucketManager bucketManager = new BucketManager(getAuth(),cfg);
        for (String filename : fileNames) {
            try {
                bucketManager.delete(bucketname,filename);
                result++;
            } catch (QiniuException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return result;
    }

}
