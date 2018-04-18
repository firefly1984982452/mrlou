package com.mrlou.write.sdcard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

@SuppressLint("WorldReadableFiles")
public class OutputUtil<T> {
	 /** 
     * �����󱣴浽���� 
     * @param context 
     * @param fileName �ļ��� 
     * @param bean  ���� 
     * @return true ����ɹ� 
     */  
    public boolean writeObjectIntoLocal(Context context,String fileName,T bean){  
        try {  
            // ͨ��openFileOutput�����õ�һ�����������������Ϊ�������ļ�����������б�ܣ�������ģʽ  
            @SuppressWarnings("deprecation")  
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_WORLD_READABLE);  
            ObjectOutputStream oos = new ObjectOutputStream(fos);  
            oos.writeObject(bean);//д��  
            fos.close();//�ر�������  
            oos.close();  
            return true;  
        } catch (FileNotFoundException e) {    
            e.printStackTrace();    
            //Toast.makeText(WebviewTencentActivity.this, "�����쳣1",Toast.LENGTH_LONG).show();    
            return false;  
        } catch (IOException e) {    
            e.printStackTrace();    
            //Toast.makeText(WebviewTencentActivity.this, "�����쳣2",Toast.LENGTH_LONG).show();    
            return false;  
        }   
    }  
    /** 
     * ������д��sd�� 
     * @param fileName �ļ��� 
     * @param bean  ���� 
     * @return true ����ɹ� 
     */  
    public boolean writObjectIntoSDcard(String fileName,T bean){  
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){    
            File sdCardDir = Environment.getExternalStorageDirectory();//��ȡsd��Ŀ¼  
            File sdFile  = new File(sdCardDir, fileName);  
            try {  
                FileOutputStream fos = new FileOutputStream(sdFile);  
                ObjectOutputStream oos = new ObjectOutputStream(fos);  
                oos.writeObject(bean);//д��  
                fos.close();  
                oos.close();  
                return true;  
            } catch (FileNotFoundException e) {    
                // TODO Auto-generated catch block    
                e.printStackTrace();    
                return false;  
            } catch (IOException e) {    
                // TODO Auto-generated catch block    
                e.printStackTrace();    
                return false;  
            }  
        }  
        else {  
            return false;  
        }  
    }  
    /** 
     * ������д��sd�� 
     * @param fileName �ļ��� 
     * @param list  ���� 
     * @return true ����ɹ� 
     */  
    public boolean writeListIntoSDcard(String fileName,List<T> list){  
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){    
            File sdCardDir = Environment.getExternalStorageDirectory();//��ȡsd��Ŀ¼  
            File sdFile  = new File(sdCardDir, fileName);  
            try {  
                FileOutputStream fos = new FileOutputStream(sdFile);  
                ObjectOutputStream oos = new ObjectOutputStream(fos);  
                oos.writeObject(list);//д��  
                fos.close();  
                oos.close();  
                return true;  
            } catch (FileNotFoundException e) {    
                // TODO Auto-generated catch block    
                e.printStackTrace();    
                return false;  
            } catch (IOException e) {    
                // TODO Auto-generated catch block    
                e.printStackTrace();    
                return false;  
            }  
        }  
        else {  
            return false;  
        }  
    }  
}  