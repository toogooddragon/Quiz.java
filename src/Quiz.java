import java.awt.event.ActionEvent;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;


public class Quiz extends JFrame implements ActionListener {
    final String url = "https://travelswithelle.com/other/multiple-choice-trivia-questions/";

    JLabel label;
    JLabel labelResult;
    JLabel labelScores;
    JRadioButton[] radioButtons = new JRadioButton[5];
    JButton buttonNext, buttonSubmit, buttonResult;
    ButtonGroup bg;
    int rightAnswer = 0, currentQuestion = 1, currentLine = 0, scores = 0;

    Quiz(String s) {
        super(s);
        bg = new ButtonGroup();

        for (int i = 0; i < 5; i++) {
            radioButtons[i] = new JRadioButton();
            add(radioButtons[i]);
            bg.add(radioButtons[i]);
        }

        label = new JLabel();
        add(label);

        labelResult = new JLabel();
        add(labelResult);
        labelResult.setVisible(false);

        labelScores = new JLabel();
        add(labelScores);

        buttonNext = new JButton("Next");
        add(buttonNext);

        buttonSubmit = new JButton("Submit");
        add(buttonSubmit);

        buttonResult = new JButton("Result");
        add(buttonSubmit);

        buttonSubmit.addActionListener(this);
        buttonNext.addActionListener(this);
        buttonResult.addActionListener(this);


        label.setBounds(30, 30, 450, 20);
        labelResult.setBounds(50, 280, 450, 20);
        labelScores.setBounds(50, 330, 450, 20);

        radioButtons[0].setBounds(50, 80, 450, 20);
        radioButtons[1].setBounds(50, 130, 450, 20);
        radioButtons[2].setBounds(50, 180, 450, 20);
        radioButtons[3].setBounds(50, 230, 450, 20);

        buttonNext.setBounds(600, 330, 100, 30);
        buttonNext.setVisible(false);

        buttonSubmit.setBounds(600, 330, 100, 30);

        buttonResult.setBounds(100, 330, 100, 30);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocation(250, 100);
        setVisible(true);
        setSize(800, 450);

        setData(currentQuestion, currentLine);
    }

    public static void main(String[] args) {
        new Quiz("Quiz App");
    }

    public void setData(int currentQuestion, int currentLine) {
        radioButtons[4].setSelected(true);

        try {
            Document document = Jsoup.connect(url).timeout(6000).get();

            Elements body = document.select("div.entry-content ol li");

            String answer = body.get(currentLine).select("strong").text();

            label.setText(currentQuestion + ". " + body.get(currentLine).text().substring(0, body.get(currentLine).text().indexOf("?") + 1));
            labelScores.setText("Score: " + scores);

            for (int i = currentLine + 1; i <= currentLine + 4; i++) {

                if (body.get(i).text().equals(answer)) rightAnswer = i - currentLine - 1;

                switch (i % 5) {
                    case 1 -> {
                        radioButtons[0].setText("A. " + body.get(i).text());
                        radioButtons[0].setBounds(50, 80, 200, 20);
                    }
                    case 2 -> {
                        radioButtons[1].setText("B. " + body.get(i).text());
                        radioButtons[1].setBounds(50, 130, 200, 20);
                    }
                    case 3 -> {
                        radioButtons[2].setText("C. " + body.get(i).text());
                        radioButtons[2].setBounds(50, 180, 200, 20);
                    }
                    case 4 -> {
                        radioButtons[3].setText("D. " + body.get(i).text());
                        radioButtons[3].setBounds(50, 230, 200, 20);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Next")) {
            currentQuestion++;
            currentLine += 5;

            labelScores.setText("Score: " + scores);
            labelResult.setVisible(false);
            buttonSubmit.setVisible(true);
            buttonNext.setVisible(false);

            setData(currentQuestion, currentLine);
        }

        if (e.getActionCommand().equals("Submit")) {
            int selectedOption = 0;
            for (int i = 0; i < 4; i++) {
                if (radioButtons[i].isSelected()) {
                    selectedOption = i;
                    break;
                }
            }

            boolean isCorrect = selectedOption == rightAnswer;

            if (isCorrect) {
                labelResult.setText("Correct!");
                scores++;
            } else {
                labelResult.setText("Wrong! The answer is " + radioButtons[rightAnswer].getText());
            }

            labelScores.setText("Score: " + scores);
            labelResult.setVisible(true);
            buttonSubmit.setVisible(false);
            buttonNext.setVisible(true);
        }

    }

}