package com.example.native_activity;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AssetHelper {
    public static void copyDirorfileFromAssetManager(File dataPath, AssetManager assetManager, String assetDir, String destinationDir) throws IOException
    {
        String destDirPath = dataPath + addLeadingSlash(destinationDir);
        File destDir = new File(destDirPath);

        Log.println(Log.INFO, "ASSET", String.format("Create dir %s", destDirPath));
        createDir(destDir);

        String[] files = assetManager.list(assetDir);

        for (int i = 0; i < files.length; i++)
        {

            String absAssetFilePath = addTrailingSlash(assetDir) + files[i];
            String subFiles[] = assetManager.list(absAssetFilePath);

            if (subFiles.length == 0)
            {
                // It is a file
                String destFilePath = addTrailingSlash(destDirPath) + files[i];
                copyAssetFile(assetManager, absAssetFilePath, destFilePath);
            } else
            {
                // It is a sub directory
                copyDirorfileFromAssetManager(dataPath, assetManager, absAssetFilePath, addTrailingSlash(destinationDir) + files[i]);
            }
        }
    }

    private static void copyAssetFile(AssetManager assetManager, String assetFilePath, String destinationFilePath) throws IOException
    {
        Log.println(Log.INFO, "ASSET", String.format("Copy asset %s --> %s", assetFilePath, destinationFilePath));
        InputStream in = assetManager.open(assetFilePath);
        OutputStream out = new FileOutputStream(destinationFilePath);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0)
            out.write(buf, 0, len);
        in.close();
        out.close();
    }

    private static String addTrailingSlash(String path)
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

    private static String addLeadingSlash(String path)
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

    private static void createDir(File dir) throws IOException
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
