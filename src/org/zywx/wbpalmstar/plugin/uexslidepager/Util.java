package org.zywx.wbpalmstar.plugin.uexslidepager;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.zywx.wbpalmstar.base.BDebug;
import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.EBrowserView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.webkit.CookieManager;

final class Util
{
  static final Charset US_ASCII = Charset.forName("US-ASCII");
  static final Charset UTF_8 = Charset.forName("UTF-8");

  static void deleteContents(File paramFile)
    throws IOException
  {
    File[] arrayOfFile = paramFile.listFiles();
    if (arrayOfFile == null)
      throw new IOException("not a readable directory: " + paramFile);
    int i = arrayOfFile.length;
    for (int j = 0; j < i; j++)
    {
      File localFile = arrayOfFile[j];
      if (localFile.isDirectory())
        deleteContents(localFile);
      if (!localFile.delete())
        throw new IOException("failed to delete file: " + localFile);
    }
  }

  static String readFully(Reader paramReader)
    throws IOException
  {
    StringWriter localStringWriter;
    try
    {
      localStringWriter = new StringWriter();
      char[] arrayOfChar = new char[1024];
      while (true)
      {
        int i = paramReader.read(arrayOfChar);
        if (i == -1)
          break;
        localStringWriter.write(arrayOfChar, 0, i);
      }
    }
    finally
    {
      paramReader.close();
    }
    String str = localStringWriter.toString();
    paramReader.close();
    return str;
  }
  
  public static Bitmap downloadImageFromNetwork(String url) {
      InputStream is = null;
      Bitmap bitmap = null;
      int responseCode = 0;
      HttpEntity httpEntity = null;
      HttpGet httpGet = null;
      try {
          httpGet = new HttpGet(url);
          setCookieForHttpRequest(url, httpGet);
          BasicHttpParams httpParams = new BasicHttpParams();
          HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
          HttpConnectionParams.setSoTimeout(httpParams, 30000);
          HttpClient httpClient = new DefaultHttpClient(httpParams);
          HttpResponse httpResponse = httpClient.execute(httpGet);
          responseCode = httpResponse.getStatusLine().getStatusCode();
          if (responseCode == HttpStatus.SC_OK) {
              httpEntity = httpResponse.getEntity();
              if (httpEntity != null) {
                  is = httpResponse.getEntity().getContent();
                  bitmap = BitmapFactory.decodeStream(is);
              }
          }
      } catch (Exception e) {
          if (httpGet != null) {
              httpGet.abort();
          }
          e.printStackTrace();
      } catch (OutOfMemoryError e) {
          e.printStackTrace();
      } finally {
          if (is != null) {
              try {
                  is.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
          if (httpEntity != null) {
              try {
                  httpEntity.consumeContent();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return bitmap;
  }
  
  /**
   * 为Http请求设置请求头
   * 
   * @param requestUrl
   * @param request
   */
  public static void setCookieForHttpRequest(String requestUrl, HttpRequestBase request) {
      String cookie = CookieManager.getInstance().getCookie(requestUrl);
      if (cookie != null) {
          request.setHeader("Cookie", cookie);
      }
  }
  
  /**
   * 获取图片的缩略图
   * 
   * @param destSize
   * @param filePath
   * @return
   * @throws OutOfMemoryError
   */
  public static Bitmap getPictureThumbnail(int maxSize, String filePath) {
      if (filePath == null || filePath.length() == 0) {
          return null;
      }
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      Bitmap source = BitmapFactory.decodeFile(filePath, options);
      final int srcHeight = options.outHeight;
      final int srcWidth = options.outWidth;
      if (srcHeight <= 0 || srcWidth <= 0) {
          return null;
      }
      float scaleRate = 1;
      if (srcHeight > srcWidth) {
          scaleRate = srcHeight / maxSize;
      } else {
          scaleRate = srcWidth / maxSize;
      }
      scaleRate = scaleRate > 1 ? scaleRate : 1;
      options.inJustDecodeBounds = false;
      options.inSampleSize = (int) scaleRate;
      options.inDither = false;
      options.inPreferredConfig = Config.ARGB_8888;
      FileInputStream fis = null;
      try {
          fis = new FileInputStream(new File(filePath));
          source = BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
          if (source != null) {
              int width = source.getWidth();
              int height = source.getHeight();
              // 最大边长度
              int currentMax = Math.max(width, height);
              // 当前图片尺寸的最长边小于最大尺寸限制，直接返回
              if (currentMax <= maxSize) {
                  return source;
              } else {
                  // 超出最大尺寸，需要裁减
                  float dstScale = (float) maxSize / currentMax;
                  int dstWidth = (int) (width * dstScale);
                  int dstHeight = (int) (height * dstScale);
                  source = Bitmap.createScaledBitmap(source, dstWidth, dstHeight, false);
              }
          }
      } catch (Exception e) {
          e.printStackTrace();
      } catch (OutOfMemoryError e) {
          e.printStackTrace();
      } finally {
          if (fis != null) {
              try {
                  fis.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      if (source != null) {
          BDebug.d("DecodeBitmap--->",
                  "maxSize:" + maxSize + "  width:" + source.getWidth() + "  height:" + source.getHeight());
      }
      return source;
  }
  
  public static Bitmap getImage(Context ctx, String imgUrl) {
      if (imgUrl == null || imgUrl.length() == 0) {
          return null;
      }
      Bitmap bitmap = null;
      InputStream is = null;
      try {
          if (imgUrl.startsWith(BUtility.F_Widget_RES_SCHEMA)) {
              is = BUtility.getInputStreamByResPath(ctx, imgUrl);
              bitmap = BitmapFactory.decodeStream(is);
          } else if (imgUrl.startsWith(BUtility.F_FILE_SCHEMA)) {
              imgUrl = imgUrl.replace(BUtility.F_FILE_SCHEMA, "");
              bitmap = BitmapFactory.decodeFile(imgUrl);
          } else if (imgUrl.startsWith(BUtility.F_Widget_RES_path)) {
              try {
                  is = ctx.getAssets().open(imgUrl);
                  if (is != null) {
                      bitmap = BitmapFactory.decodeStream(is);
                  }
              } catch (IOException e) {
                  e.printStackTrace();
              }
          } else if (imgUrl.startsWith("/")) {
              bitmap = BitmapFactory.decodeFile(imgUrl);
          }
      } catch (OutOfMemoryError e) {
          e.printStackTrace();
      } finally {
          Log.i("fzy", "is:" + is);
          if (is != null) {
              try {
                  is.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      Log.i("fzy", "bitmap:" + bitmap);
      return bitmap;
  }
  
  public static String getRealUrlPath(EBrowserView brw, String itemPath){
      String fullPath = BUtility.getFullPath(brw.getCurrentUrl(),
              itemPath);
      if(fullPath.startsWith("http")){
          return fullPath;
      }
      if(fullPath.startsWith(BUtility.F_Widget_RES_SCHEMA)){//res:

          if (BUtility.isDes) {

              fullPath = BUtility.g_desPath + BUtility.F_Widget_RES_path + fullPath.substring(BUtility.F_Widget_RES_SCHEMA.length());

          } else {
              fullPath = "file:///android_asset/widget/wgtRes/" + fullPath.replace(BUtility.F_Widget_RES_SCHEMA, "");
          }


      }else{ 
          fullPath = BUtility.getSDRealPath(fullPath,
                  brw.getCurrentWidget().m_widgetPath,
                  brw.getCurrentWidget().m_wgtType);
      }
      return fullPath;
  }

}