package com.company;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class GUI {
    //Создадим окно и установим заголовок
    JFrame window = new JFrame("Сервер");
    //Создадим панель
    JPanel panel = new JPanel();
    //Создадим интерфейс
    JLabel lbIp = new JLabel("IP-адрес:");
    JTextField textFieldIp = new JTextField("127.0.0.1");
    JLabel lbPort = new JLabel("Порт:");
    JTextField textFieldPort = new JTextField("8888");
    JButton buttonStart = new JButton("Запустить сервер");

    ServerThread serv;
    Thread t;


    public GUI() {

        //Событие "закрыть" при нажатии по крестику окна
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Событие для запуска сервера
        buttonStart.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                serv = new ServerThread(textFieldIp.getText(), textFieldPort.getText());
                t = new Thread(serv);
                t.start();
                textFieldIp.setEditable(false);
                textFieldPort.setEditable(false);
                buttonStart.setEnabled(false);
            }
        });

        GridLayout grid = new GridLayout(5, 0, 5, 12);
        panel.setLayout(grid);



        //Добавим интерфейс на панель
        panel.add(lbIp);
        panel.add(textFieldIp);
        panel.add(lbPort);
        panel.add(textFieldPort);
        panel.add(buttonStart);


        //Добавим панель в окно
        window.getContentPane().add(panel);

        window.pack();

        //Разместим программу по центру
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
