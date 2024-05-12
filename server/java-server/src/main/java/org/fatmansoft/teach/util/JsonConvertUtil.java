package org.fatmansoft.teach.util;

import org.fatmansoft.teach.payload.response.DataResponse;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class JsonConvertUtil {
    public static boolean isBaseType(Class fc) {
        if(fc == String.class || fc == Integer.class)
            return true;
        else
            return false;
    }
    public static String toString(Object o) {
        if(o == null)
            return "";
        return o.toString();
    }
    public static String objectToJsonBody(Object o) {
        String str ="";
        try {
            Class fc;
            int i;
            Method[] methods;
            Class c = o.getClass();
            Object value, sValue;
            String mName;
            if(c == ArrayList.class) {
                ArrayList aList = (ArrayList)o;
                for(i = 0; i <aList.size();i++) {
                    sValue = aList.get(i);
                    if(isBaseType(sValue.getClass())) {
                        if(i == 0) {
                            str += toString(sValue);
                        }else {
                            str +="," + toString(sValue);
                        }
                    }else {
                        if(i == 0) {
                            str = "{"+ objectToJsonBody(sValue) + "}";
                        }else {
                            str +=",{"+ objectToJsonBody(sValue) + "}";
                        }
                    }
                }
            }else {
                str += "\"classType\":\"" + c.getName() + "\"";
                methods = c.getMethods();
                for (Method m : methods) {
                    mName = m.getName();
                    if (!mName.startsWith("get") || mName.equals("getClass"))
                        continue;
                    mName = mName.substring(3, 4).toLowerCase() + mName.substring(4);
                    value = m.invoke(o);
                    if (value == null)
                        continue;
                    fc = value.getClass();
                    if (isBaseType(fc)) {
                        str += ",\"" + mName + "\":\"" + toString(value) + "\"";
                    }else if(fc == ArrayList.class) {
                        str += ",\"" + mName + "\":["  + objectToJsonBody(value) + "]";
                    }else {
                        str += ",\"" + mName + "\":{"  + objectToJsonBody(value) + "}";
                    }
                }
            }
            return str;
        }catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String[] getAttributeValue(String str) {
        String[] kv = str.split(":");
        kv[0] = kv[0].substring(1, kv[0].length() - 1);
        kv[1] = kv[1].substring(1, kv[1].length() - 1);
        return kv;
    }

    public static int getBaseEnd(String str, int s) {
        int e = s + 1;
        while (e < str.length()) {
            if (str.charAt(e) == '"')
                return e;
            e++;
        }
        return str.length();
    }
    public static int getObjectEnd(String str, int s) {
        int e = s + 1;
        int count=1;
        char c;
        while(e < str.length()) {
            c = str.charAt(e);
            if (c == '}') {
                count--;
                if (count == 0)
                    return e;
            } else if (c == '{') {
                count++;
            }
            e++;
        }
        return e;
    }
    public static int getArrayEnd(String str, int s) {
        int e = s + 1;
        int count=1;
        char c;
        while(e < str.length()) {
            c = str.charAt(e);
            if (c == ']') {
                count--;
                if (count == 0)
                    return e;
            } else if (c == '[') {
                count++;
            }
            e++;
        }
        return e;
    }
    public static Object  jsonToObject(String json){
        try {
            StringTokenizer sz;
            List list;
            String[] kv;
            Object o= null;
            Class c=null, fc;
            String mName;
            Method m;
            Object value;
            String str = json;
            int len = str.length();
            int s= 0,e=0,index=0;
            String aStr,vStr,tStr;
            char cc;
            while(s<len) {
                index = str.indexOf(':', s);
                aStr = str.substring(s + 1, index - 1);
                cc = str.charAt(index + 1);
                if (cc == '{') {
                    e = getObjectEnd(str, index + 2);
                } else if (cc == '[') {
                    e = getArrayEnd(str, index + 2);
                } else {
                    e = getBaseEnd(str, index + 2);
                }
                vStr = str.substring(index + 2, e);
                s = e + 2;
                if (aStr.equals("classType")) {
                    c = Class.forName(vStr);
                    o = c.getDeclaredConstructor().newInstance();
                } else {
                    mName = aStr.substring(0, 1).toUpperCase() + aStr.substring(1);
                    m = c.getMethod("get" + mName);
                    fc = m.getReturnType();
                    if(cc == '['){
                        List aList = new ArrayList();
                        int ss= 0;
                        while(ss<vStr.length()) {
                            e = getObjectEnd(vStr, ss+1);
                            tStr = vStr.substring(ss, e);
                            ss = e + 2;
                            aList.add(jsonToObject(tStr.substring(1,tStr.length()-1)));
                        }
                        value= aList;
                    }else if (fc == Integer.class) {
                        value = Integer.parseInt(vStr);
                    } else if (fc == String.class) {
                        value = vStr;
                    } else {
                        value = jsonToObject(vStr);
                    }
                    m = c.getMethod("set" + mName, fc);
                    m.invoke(o, value);
                }
            }
            return o;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDataResponseJson(Object obj){
        DataResponse response = new DataResponse(1,obj,null);
        String str = "{"+objectToJsonBody(response) + "}";
        return str;
    }
    public static String getDataListJson(List list ){
        String str;
        if(list == null || list.size()== 0)
            str ="[]";
        else {
            str = "[{" + objectToJsonBody(list.get(0)) + "}";
            for(int i = 1; i < list.size();i++) {
                str += ",{" +  objectToJsonBody(list.get(i)) + "}";
            }
            str += "]";
        }
        return str;
    }
    public static String getDataObjectJson(Object obj){
        if(obj == null) {
            return "";
        }
        String str = "{"+objectToJsonBody(obj) + "}";
        return str;
    }
}
