package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerThread implements Runnable {
    ExecutorService executeIt = Executors.newFixedThreadPool(2);
    // Сервер
    ServerSocket server;
    // IP
    String ip;
    // Порт
    String port;

    public boolean isRunning = false;


    public ServerThread(String ip, String port){
        this.ip = ip;
        this.port = port;
    }

    public void SetStopSignal()
    {
        try {
            server.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void run() {
        // стартуем сервер на порту и инициализируем переменную для обработки консольных команд с самого сервера
        try (ServerSocket server = new ServerSocket(Integer.valueOf(port));

             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Server socket created, command console reader for listen to server commands");

            isRunning = true;

            // стартуем цикл при условии что серверный сокет не закрыт
            while (!server.isClosed()) {

                // проверяем поступившие комманды из консоли сервера если такие
                // были
                if (br.ready()) {
                    System.out.println("Main Server found any messages in channel, let's look at them.");

                    // Получаем команду
                    String serverCommand = br.readLine();

                    // если команда - quit то инициализируем закрытие сервера и
                    // выход из цикла раздачии нитей монопоточных серверов
                    if (serverCommand.equalsIgnoreCase("quit") || !isRunning) {
                        System.out.println("Main Server initiate exiting...");
                        server.close();
                        System.out.println("Сервер успешно остановлен");
                        break;
                    }
                }

                // если комманд от сервера нет то становимся в ожидание
                // подключения к сокету общения под именем - "clientDialog" на
                // серверной стороне
                Socket client = server.accept();

                // после получения запроса на подключение сервер создаёт сокет
                // для общения с клиентом и отправляет его в отдельную нить
                // в Runnable(при необходимости можно создать Callable)
                // монопоточную нить = сервер - MonoThreadClientHandler и тот
                // продолжает общение от лица сервера
                executeIt.execute(new MonoThreadClientHandler(client));
                System.out.print("Connection accepted.");
            }

            // закрытие пула нитей после завершения работы всех нитей
            executeIt.shutdown();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
