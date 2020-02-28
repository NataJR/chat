package sample;


import java.awt.TrayIcon.MessageType;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static out.Message.*;

public class ClientHandler<TimeoutExcetion> {
    private static final Pattern MESSAGE_PATTERN = Pattern.compile("^/w (\\w+) (.+)", Pattern.MULTILINE);
    private static final String MESSAGE_SEND_PATTERN = "/w %s %s";


    private final Thread handleThread;
    private final DataInputStream inp;
    private final DataOutputStream out;
    private final ChatServer server;
    private final String username;
    private final Socket socket;
    private String clientMessage;
    private Object msg;

    public ClientHandler(String username, Socket socket, ChatServer server) throws IOException {
        this.username = username;
        this.socket = socket;
        this.server = server;
        this.inp = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.handleThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        String msg = inp.readUTF();
                        System.out.printf("Message from user %s: %s%n", username, msg);
                        Matcher matcher = MESSAGE_PATTERN.matcher(msg);
                        if (matcher.matches()) {
                            String userTo = matcher.group(1);
                            String message = matcher.group(2);
                            server.sendMessage(userTo, username, message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    System.out.printf("Client %s disconnected%n", username);
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    server.unsubscribeClient(ClientHandler.this);
                }
            }
        });
        handleThread.start();
    }

    public void sendMessage(String userTo) throws IOException {
        out.writeUTF(String.format(MESSAGE_SEND_PATTERN, userTo, msg));
    }

    public String getUsername() {
        return username;
    }


    /**
     * 1. Разобраться с кодом.
     * 2. Добавить отключение неавторизованных пользователей по таймауту
     * (120 сек. ждем после подключения клиента.
     * Если он не авторизовался за это время, закрываем соединение).
     *
     * @param clientMessage
     * @param msg
     */


    // Первый вариант решения:
    public void run(String clientMessage, Object msg) throws TimeoutExcetion, InterruptedException, ExecutionException, TimeoutException {
        this.clientMessage = clientMessage;
        this.msg = msg;
        MessageType inMessage;
        inMessage = null;
        try {
            while (true) {
                server.toString("New customer joined us");
                break;
            }
            try {
                //если от клиента не пришло смс
                if (String clientMessage = inMessage(msg))
                // клиент выходит из чата
                if (clientMessage.equalsIgnoreCase("the session is finished")) {
                    break;
                }
                //останавливаем выполнение потока на 120 секунд
                Thread.sleep(120);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (InterruptException ex) {
            ex.printStackTrace();
        } finally {
            this.close();

            //отправляем смс
            public void sendMsg (String msg){

                try {
                    inMessage(msg);
                    flush();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            //клиент выходит из чата
            public void close () {
                //удаляем клиента
                server.removeClient(this);
                Object client_count;
                server.sendMessage("There are  = +clients_count + cliemts in chat");
            }



            private String inMessage () {
                return inMessage();
            }

            
        }
    }

    private void close() {
    }
}