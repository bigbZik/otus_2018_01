package ru.otus.hw02;

import java.lang.reflect.Constructor;

public class Main {

    public static void main(String[] args) {

        //MemResults("new Object()", new Object(), new Object[]{} );
        MemResults("Empty String", new String(new char[0]), new Object[]{new char[0] } );
        //MemResults("Empty Example", new Example(5, 6), new Object[]{ new Integer(5), new Integer(6)} );

    }

    private static void MemResults(String desc, Object obj, Object[] val) {

        System.out.println(" Object type:  " + desc);

        String result;

        try {
            long mem = getObjectSizeClassCalc(obj, val);
            result = "" + mem + " byte";
        } catch (Exception e) {
            result =  e.getMessage();
        }
        System.out.println(" - size via Class: " + result);

        try {
            long mem = getObjectSizeCalc();
            result = "" + mem + " byte";
        } catch (Exception e) {
            result =  e.getMessage();
        }
        System.out.println(" - size via new: " + result);

        try {
            long mem = getObjectSizeInstr(obj);
            result = "" + mem + " byte";
        } catch (Exception e) {
            result =  e.getMessage();
        }
        System.out.println(" - size via Instrumentation: " + result);

    }

    private static long getObjectSizeInstr(Object obj) {
        return AgentMemoryCounter.getObjectSize(obj);
    }

    private static long getObjectSizeCalc() throws InterruptedException {

        int size = 20_000_000;
        Runtime rt = Runtime.getRuntime();

        System.gc();

        Thread.sleep(100);

        Object[] array = new Object[size];

        long mem2 = rt.totalMemory() - rt.freeMemory();

        for (int i = 0; i < size; i++) {
            array[i] = new String(new char[0]);
        }

        long mem3 = rt.totalMemory() - rt.freeMemory();

        return  (mem3 - mem2) / size;

    }

    private static long getObjectSizeClassCalc(Object obj, Object val[]) throws InterruptedException {

        //Получаем объект Class
        Class cl = obj.getClass();

        //Найдем нужный конструктор
        Constructor<?> cn = null;
        boolean found_con = false;

        for (Constructor<?> con : cl.getConstructors()) {

            Class<?>[] arg_con = con.getParameterTypes();

            boolean found_arg = true;

            if (val.length ==  arg_con.length) {

                for (int i = 0; i < val.length; i++) {
                    if (!val[i].getClass().equals(arg_con[i])) {
                        found_arg = false;
                        break;
                    }
                }
            }
            else
                found_arg = false;

            if (found_arg) {
                found_con = true;
                cn = con;
                break;
            }
        }

        if (!found_con) {
            throw new InterruptedException("Error. Counstructor was not found.");
        }

        int size = 20_000_000;
        Runtime rt = Runtime.getRuntime();

        System.gc();

        Thread.sleep(100);

        Object[] array = new Object[size];

        long mem2 = rt.totalMemory() - rt.freeMemory();

        for (int i = 0; i < size; i++) {
            try {
                if (val.length == 0)
                    array[i] = cn.newInstance();
                else
                    array[i] = cn.newInstance(val);

            } catch (Exception e) {
                throw new InterruptedException("Error at object creation");
            }

        }

        long mem3 = rt.totalMemory() - rt.freeMemory();

        return (mem3 - mem2) / size;

    }

}
