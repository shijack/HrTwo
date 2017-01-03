package com.zerostudio.hrtwo.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by shijack on 2017/1/3.
 */
public class AssetsUtil {
    /**
     * 将assets文件下分割的数据库文件组装到数据库文件夹下。assets文件夹下最大文件为1M
     * @param context
     * @param finalDbFileName 最终数据库文件名。
     * @throws IOException
     */

    public static void copyDatabase(Context context, String  finalDbFileName) throws IOException {

            AssetManager assets = context.getAssets();

        File finalDbFile = context.getDatabasePath(finalDbFileName);
        if (!finalDbFile.exists()){

            OutputStream outstream = new FileOutputStream(finalDbFile);
            finalDbFile.createNewFile();
            byte[] b = new byte[1024];
            int i, r;
            String[] assetfiles = assets.list("");
            Arrays.sort(assetfiles);
            for (i = 1; i < 10; i++) //I have definitely less than 10 files; you might have more
            {
                String partname = String.format(finalDbFileName + ".%d", i);
                if (Arrays.binarySearch(assetfiles, partname) < 0) //No such file in assets - time to quit the loop
                {
                    break;
                }
                InputStream instream = assets.open(partname);
                while ((r = instream.read(b)) != -1) {
                    outstream.write(b, 0, r);
                }
                instream.close();
            }
            outstream.close();
        }
    }
}
