package com.mrlou.write.sdcard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.List;

import android.content.Context;
import android.os.Environment;

public class InputUtil<T> {
	 /** 
     * ��ȡ���ض��� 
     * @param context 
     * @param fielName �ļ��� 
     * @return 
     */  
	   @SuppressWarnings("unchecked")  
	    public T readObjectFromLocal(Context context,String fielName){  
	        T bean;  
	        try {  
	            FileInputStream fis = context.openFileInput(fielName);//���������    
	            ObjectInputStream ois = new ObjectInputStream(fis);  
	            bean = (T) ois.readObject();  
	            fis.close();  
	            ois.close();  
	            return bean;  
	        } catch (StreamCorruptedException e) {    
	            //Toast.makeText(ShareTencentActivity.this,"�����쳣3",Toast.LENGTH_LONG).show();//����Toast��Ϣ    
	            e.printStackTrace();    
	            return null;  
	        } catch (OptionalDataException e) {    
	            //Toast.makeText(ShareTencentActivity.this,"�����쳣4",Toast.LENGTH_LONG).show();//����Toast��Ϣ    
	            e.printStackTrace();  
	            return null;  
	        } catch (FileNotFoundException e) {    
	            //Toast.makeText(ShareTencentActivity.this,"�����쳣5",Toast.LENGTH_LONG).show();//����Toast��Ϣ    
	            e.printStackTrace();    
	            return null;  
	        } catch (IOException e) {    
	            e.printStackTrace();    
	            return null;  
	        } catch (ClassNotFoundException e) {    
	            //Toast.makeText(ShareTencentActivity.this,"�����쳣6",Toast.LENGTH_LONG).show();//����Toast��Ϣ    
	            e.printStackTrace();    
	            return null;  
	        }    
	    }  
	    /** 
	     * ��ȡsd������ 
	     * @param fileName �ļ��� 
	     * @return 
	     */  
	    @SuppressWarnings("unchecked")  
	    public T readObjectFromSdCard(String fileName){  
	        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //���sd���Ƿ����  
	            T bean;  
	            File sdCardDir = Environment.getExternalStorageDirectory();  
	            File sdFile = new File(sdCardDir,fileName);  
	            try {  
	                FileInputStream fis = new FileInputStream(sdFile);  
	                ObjectInputStream ois = new ObjectInputStream(fis);  
	                bean = (T) ois.readObject();  
	                fis.close();  
	                ois.close();  
	                return bean;  
	            } catch (StreamCorruptedException e) {    
	                // TODO Auto-generated catch block    
	                e.printStackTrace();    
	                return null;  
	            } catch (OptionalDataException e) {    
	                // TODO Auto-generated catch block    
	                e.printStackTrace();    
	                return null;  
	            } catch (FileNotFoundException e) {    
	                // TODO Auto-generated catch block    
	                e.printStackTrace();   
	                return null;   
	            } catch (IOException e) {    
	                // TODO Auto-generated catch block    
	                e.printStackTrace();    
	                return null;  
	            } catch (ClassNotFoundException e) {    
	                // TODO Auto-generated catch block    
	                e.printStackTrace();    
	                return null;  
	            }    
	        }  
	        else {  
	            return null;  
	        }  
	    }  
	      
	    /** 
	     * ��ȡsd������ 
	     * @param fileName �ļ��� 
	     * @return 
	     */  
	    @SuppressWarnings("unchecked")  
	    public List<T> readListFromSdCard(String fileName){  
	        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //���sd���Ƿ����  
	            List<T> list;  
	            File sdCardDir = Environment.getExternalStorageDirectory();  
	            File sdFile = new File(sdCardDir,fileName);  
	            try {  
	                FileInputStream fis = new FileInputStream(sdFile);  
	                ObjectInputStream ois = new ObjectInputStream(fis);  
	                list = (List<T>) ois.readObject();  
	                fis.close();  
	                ois.close();  
	                return list;  
	            } catch (StreamCorruptedException e) {    
	                // TODO Auto-generated catch block    
	                e.printStackTrace();    
	                return null;  
	            } catch (OptionalDataException e) {    
	                // TODO Auto-generated catch block    
	                e.printStackTrace();    
	                return null;  
	            } catch (FileNotFoundException e) {    
	                // TODO Auto-generated catch block    
	                e.printStackTrace();   
	                return null;   
	            } catch (IOException e) {    
	                // TODO Auto-generated catch block    
	                e.printStackTrace();    
	                return null;  
	            } catch (ClassNotFoundException e) {    
	                // TODO Auto-generated catch block    
	                e.printStackTrace();    
	                return null;  
	            }    
	        }  
	        else {  
	            return null;  
	        }  
	    }  
	      
	}  