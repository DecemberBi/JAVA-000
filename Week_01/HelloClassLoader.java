package jvm;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args) {
        try {
            Object hello = new HelloClassLoader().findClass("Hello").getDeclaredConstructor().newInstance();
            hello.getClass().getDeclaredMethod("hello").invoke(hello);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            File file = new File("D:\\JavaStudy\\课件\\Hello\\Hello.xlass");
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = fileInputStream.readAllBytes();
            byte[] originBytes = new byte[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                originBytes[i] = (byte) (255 - bytes[i]);
            }
            return defineClass(name, originBytes, 0, originBytes.length);
        } catch (IOException ffe) {
            ffe.printStackTrace();
        }
        return null;
    }

}
