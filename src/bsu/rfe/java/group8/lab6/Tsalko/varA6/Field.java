package bsu.rfe.java.group8.lab6.Tsalko.varA6;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Field extends JPanel {
    private boolean paused; // Флаг приостановленности движения
    private boolean pausedFast;
    private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);  // Динамический список скачущих мячей

    // Класс таймер отвечает за регулярную генерацию событий ActionEvent
    // При создании его экземпляра используется анонимный класс,
    // реализующий интерфейс ActionListener

    private Timer repaintTimer = new Timer(10, new ActionListener()
    {
        public void actionPerformed(ActionEvent ev)
        {
            repaint(); // Задача обработчика события ActionEvent - перерисовка окна
        }
    });

    // Конструктор класса BouncingBall
    public Field() {
        setBackground(Color.WHITE); // Установить цвет заднего фона белым
        repaintTimer.start(); // Запустить таймер
    }
    // Унаследованный от JPanel метод перерисовки компонента
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Вызвать версию метода, унаследованную от предка
        Graphics2D canvas = (Graphics2D) g;
        // Последовательно запросить прорисовку от всех мячей из списка
        for (BouncingBall ball: balls) {
            ball.paint(canvas);
        }
    }
    // Метод добавления нового мяча в список
    public void addBall() {
    //Заключается в добавлении в список нового экземпляра BouncingBall
    // Всю инициализацию положения, скорости, размера, цвета
    // BouncingBall выполняет сам в конструкторе
        balls.add(new BouncingBall(this));
    }
    // Метод синхронизированный, т.е. только один поток может
// одновременно быть внутри
    public synchronized void pause() {
// Включить режим паузы
        paused = true;
    }

    public synchronized void pauseSome(){
        pausedFast = true;
    }
    // Метод синхронизированный, т.е. только один поток может
// одновременно быть внутри
    public synchronized void resume() {
// Выключить режим паузы
        paused = false;
        pausedFast = false;
// Будим все ожидающие продолжения потоки
        notifyAll();
    }
    // Синхронизированный метод проверки, может ли мяч двигаться
// (не включен ли режим паузы?)
    public synchronized void canMove(BouncingBall ball) throws
            InterruptedException {
        if (paused) {
// Если режим паузы включен, то поток, зашедший
// внутрь данного метода, засыпает
            wait();
        }else if (pausedFast && ball.getSpeed() > 8){
            wait();
        }
    }
}