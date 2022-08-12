package com.example.native_activity;

import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

public class AssetHelper {
    public static String copyDirorfileFromAssetManager(File data_path, AssetManager asset_manager, String arg_assetDir, String arg_destinationDir) throws IOException
    {
        //File data_path = Environment.getExternalStorageDirectory();
        String dest_dir_path = data_path + addLeadingSlash(arg_destinationDir);
        File dest_dir = new File(dest_dir_path);

        Log.println(Log.INFO, "ASSET", String.format("Create dir %s", dest_dir_path));
        createDir(dest_dir);

        String[] files = asset_manager.list(arg_assetDir);

        for (int i = 0; i < files.length; i++)
        {

            String abs_asset_file_path = addTrailingSlash(arg_assetDir) + files[i];
            String sub_files[] = asset_manager.list(abs_asset_file_path);

            if (sub_files.length == 0)
            {
                // It is a file
                String dest_file_path = addTrailingSlash(dest_dir_path) + files[i];
                copyAssetFile(asset_manager, abs_asset_file_path, dest_file_path);
            } else
            {
                // It is a sub directory
                copyDirorfileFromAssetManager(data_path, asset_manager, abs_asset_file_path, addTrailingSlash(arg_destinationDir) + files[i]);
            }
        }

        return dest_dir_path;
    }


    public static void copyAssetFile(AssetManager asset_manager, String assetFilePath, String destinationFilePath) throws IOException
    {
        Log.println(Log.INFO, "ASSET", String.format("Copy asset %s --> %s", assetFilePath, destinationFilePath));
        InputStream in = asset_manager.open(assetFilePath);
        OutputStream out = new FileOutputStream(destinationFilePath);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0)
            out.write(buf, 0, len);
        in.close();
        out.close();
    }

    public static String addTrailingSlash(String path)
    {
        if (path.length() == 0)
        {
            return "";
        }

        if (path.charAt(path.length() - 1) != '/')
        {
            path += "/";
        }
        return path;
    }

    public static String addLeadingSlash(String path)
    {
        if (path.length() == 0)
        {
            return "";
        }

        if (path.charAt(0) != '/')
        {
            path = "/" + path;
        }
        return path;
    }

    public static void createDir(File dir) throws IOException
    {
        if (dir.exists())
        {
            if (!dir.isDirectory())
            {
                throw new IOException("Can't create directory, a file is in the way");
            }
        } else
        {
            dir.mkdirs();
            if (!dir.isDirectory())
            {
                throw new IOException("Unable to create directory");
            }
        }
    }
}
