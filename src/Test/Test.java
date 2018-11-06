package Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Test {

    static String cryptString(String string){
        //do here something
        return string;
    }

    static String decryptString(String string){
        //do here something
        return string;
    }

    public static void main(String[] args) {
        String string ="Официальная публикация запланирована на 5 ноября, тогда же новые меры вступят в силу. Это произойдет вслед за решением президента США Дональда Трампа от 8 мая о выходе страны из Совместного всеобъемлющего плана действий с Ираном";
        System.out.println("Before");
        System.out.println(string);

        string = cryptString(string);//krypt here

        System.out.println("After crypt");
        System.out.println(string);

        string = decryptString(string);

        System.out.println("After decrypt");
        System.out.println(string);



    }
}