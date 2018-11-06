package Client;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * создание клиента со всеми необходимыми утилитами, точка входа в программу в классе Client
 */

class ClientSomething extends JFrame {

    private Socket socket;
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток чтения в сокет
    private BufferedReader inputUser; // поток чтения с консоли
    private String addr; // ip адрес клиента
    private int port; // порт соединения
    private String nickname; // имя клиента
    private Date time;
    private String dtime;
    private SimpleDateFormat dt1;
    public String message;
    WriteMsg Writer;
    /////////////////////////////////////JFrame stuff//////////////////////////////////////////
    private JLabel countLabel;
    private JButton addCrow;
    private JButton removeCrow;

    /**
     * для создания необходимо принять адрес и номер порта
     *
     * @param addr
     * @param port
     */

    public ClientSomething(String addr, int port) {
        super("Crow calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        /* Подготавливаем компоненты объекта  */
        countLabel = new JLabel("Crows:");
        addCrow = new JButton("Add Crow");
        removeCrow = new JButton("Remove Crow");
        /* Подготавливаем временные компоненты  */
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        /* Расставляем компоненты по местам  */
        add(countLabel, BorderLayout.NORTH); /* О размещении компонент поговорим позже  */
        buttonsPanel.add(addCrow);
        buttonsPanel.add(removeCrow);
        add(buttonsPanel, BorderLayout.SOUTH);

        ////////////////////////////////////////////JFrame stuff//////////////////////////////////////////









        this.addr = addr;
        this.port = port;
        try {
            this.socket = new Socket(addr, port);
        } catch (IOException e) {
            System.err.println("Socket failed");
        }
        try {
            // потоки чтения из сокета / записи в сокет, и чтения с консоли
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.pressNickname(); // перед началом необходимо спросит имя
            new ReadMsg().start(); // нить читающая сообщения из сокета в бесконечном цикле
            //////////////////////////////////////////////////////////////////////////////////////////////////////// TODO!
            Writer = new WriteMsg();
            Writer.start(); // нить пишущая сообщения в сокет приходящие с консоли в бесконечном цикле
            ////////////////////////////////////////////////////////////////////////////////////////////////////////

        } catch (IOException e) {
            // Сокет должен быть закрыт при любой
            // ошибке, кроме ошибки конструктора сокета:
            ClientSomething.this.downService();
        }
        // В противном случае сокет будет закрыт
        // в методе run() нити.
    }

    /**
     * просьба ввести имя,
     * и отсылка эхо с приветсвием на сервер
     */

    private void pressNickname() {
        //System.out.print("Press your nick: "); TODO: Убрать
        try {
//            nickname = inputUser.readLine();// TODO: Убрать
            nickname = "Test-user";// TODO: Временно
            out.write("Hello " + nickname + "\n");
            out.flush();
        } catch (IOException ignored) {
        }

    }

    /**
     * закрытие сокета
     */
    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {
        }
    }

    // нить чтения сообщений с сервера
    private class ReadMsg extends Thread {
        @Override
        public void run() {

            String str;
            try {
                while (true) {
                    str = in.readLine(); // ждем сообщения с сервера
                    if (str.equals("stop")) {
                        ClientSomething.this.downService(); // харакири
                        break; // выходим из цикла если пришло "stop"
                    }
                    System.out.println(str); // пишем сообщение с сервера на консоль
                }
            } catch (IOException e) {
                ClientSomething.this.downService();
            }
        }
    }

    // нить отправляющая сообщения приходящие с консоли на сервер
    public class WriteMsg extends Thread {

        @Override
        public void run() {
            while (true) {
                String userWord;
                try {
                    time = new Date(); // текущая дата
                    dt1 = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
                    dtime = dt1.format(time); // время
                    userWord = inputUser.readLine(); // сообщения с консоли TODO: отсюда читается вход сообщения
                    if (userWord.equals("stop")) {
                        out.write("stop" + "\n");
                        ClientSomething.this.downService(); // харакири
                        break; // выходим из цикла если пришло "stop"
                    } else {
                        out.write("(" + dtime + ") " + nickname + ": " + userWord + "\n"); // отправляем на сервер TODO: или отсюда
                        message = "(" + dtime + ") " + nickname + ": " + userWord + "\n";
                    }
                    out.flush(); // чистим
                } catch (IOException e) {
                    ClientSomething.this.downService(); // в случае исключения тоже харакири

                }

            }
        }
    }
}

/**
 * создание клиент-соединения с узананными адресом и номером порта
 */
public class Client {

    public static String ipAddr = "localhost";
    public static int port = 8080;

    public static void main(String[] args) {
        ClientSomething clientSomething = new ClientSomething(ipAddr, port);
        clientSomething.setVisible(true);
        clientSomething.pack(); //Эта команда подбирает оптимальный размер в зависимости от содержимого окна
    }

}