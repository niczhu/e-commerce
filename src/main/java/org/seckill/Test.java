package org.seckill;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCommitStatement;
import org.springframework.transaction.annotation.Transactional;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.beans.Transient;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.nio.channels.ServerSocketChannel;
import java.sql.Connection;

public class Test {

//    Lock lock =new ReentrantLock();
//    Condition condition = lock.newCondition();
    // CacheManager
//        CacheInterceptor
//
//        CacheAspectSupport
//        AbstractQueuedSynchronizer
//        BlockingQueue
    //ConcurrentLinkedQueue

//        Channel
//        RandomAccessFile
//        ByteBuffer
//        Selector.open()
//        ClassLoader
//        Launcher

//    ServerSocketChannel
//    ObjectOutputStream
    public static void main(String[] args) throws Exception {
        System.out.println("hello world");

        Main main = (Main) Class.forName("org.seckill.Main").newInstance();
        System.out.println(main.getClass());
        Constructor<Main> c  = Main.class.getConstructor();
        System.out.println(c.newInstance().getClass());

        Main main1 = new Main();

//        loadClassTest();

//        readClassFile("Main");




    }

    public static void compilerClass(File file) throws Exception{
        JavaCompiler systemJavaCompiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager standardFileManager = systemJavaCompiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> javaFileObjects = standardFileManager.getJavaFileObjects(file);

       JavaCompiler.CompilationTask task = systemJavaCompiler.getTask(null, standardFileManager, null, null, null, javaFileObjects);
        task.call();
        standardFileManager.close();

    }

    public static byte[] readClassFile(String name){

        try {
            // package -> file folder
            name = name.replace(".", "//");
            FileInputStream fis = new FileInputStream(new File( name + ".class"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = -1;
            byte[] b = new byte[2048];
            while ((len = fis.read(b)) != -1) {
                baos.write(b, 0, len);
            }
            fis.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void loadClassTest(){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class clazz = null;
        try {
            clazz = classLoader.loadClass("org.seckill.Main");

            Main m = (Main)clazz.newInstance();
            m.test();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    /**
     public void conditionTest(){
     ReentrantLock lock = new ReentrantLock();
     Condition condition = lock.newCondition();
     List list = new ArrayList<>(2);
     int size = 2;

     Thread thread1 = new Thread(new Runnable() {
    @Override public void run() {


    for (int i = 0; i < 6; i++) {

    lock.lock();

    try {
    Thread.sleep(200);
    } catch (InterruptedException e) {
    e.printStackTrace();
    }

    if (list.size() >= size) {
    try {
    condition.await();
    System.out.println("thread1 await ...");
    } catch (InterruptedException e) {
    e.printStackTrace();
    }
    }
    list.add(i);
    System.out.println("list add i=" + i);
    lock.unlock();
    }
    System.out.println("unlock");
    }

    });

     Thread thread = new Thread(new Runnable() {
    @Override public void run() {
    int count = 0;
    while (list.size() <= size && count < 7) {
    try {
    Thread.sleep(1000);
    } catch (InterruptedException e) {
    e.printStackTrace();
    }

    lock.lock();
    if (!list.isEmpty()) {
    list.remove(0);
    condition.signal();
    System.out.println(" remove list and signal ");
    }
    lock.unlock();
    count++;
    }

    }
    });
     thread1.start();
     thread.start();
     }
     **/


}
