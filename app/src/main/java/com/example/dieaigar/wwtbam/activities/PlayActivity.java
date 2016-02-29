package com.example.dieaigar.wwtbam.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.example.dieaigar.wwtbam.R;
import com.example.dieaigar.wwtbam.databases.SQLHelper;
import com.example.dieaigar.wwtbam.pojo.Question;

import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class PlayActivity extends AppCompatActivity {

    TextView tvQuestion;
    Button bAnswer1, bAnswer2, bAnswer3, bAnswer4;
    int right, audience, phone, fifty1, fifty2, current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        bAnswer1 = (Button) findViewById(R.id.bAnswer1);
        bAnswer2 = (Button) findViewById(R.id.bAnswer2);
        bAnswer3 = (Button) findViewById(R.id.bAnswer3);
        bAnswer4 = (Button) findViewById(R.id.bAnswer4);

        current = 1;

        loadQuestion(current);


    }

    public void checkAnswer(View view){
        if(right == 1 && view.equals(bAnswer1))
            loadQuestion(++current);
        else if (right == 2 && view.equals(bAnswer2))
            loadQuestion(++current);
        else if (right == 3 && view.equals(bAnswer3))
            loadQuestion(++current);
        else if (right == 4 && view.equals(bAnswer4))
            loadQuestion(++current);
        else {
            tvQuestion.setText("You lose");
            SQLHelper.getInstance(this).addScore("dieaigar", "250");
        }

    }



    private void loadQuestion(int index){

        try{
//            FileInputStream fis = openFileInput(getResources().getXml(R.xml.questions));
//            InputStreamReader reader = new InputStreamReader(fis);
//            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            XmlPullParser parser = getResources().getXml(R.xml.questions);
//            parser.setInput(reader);
            int eventType = parser.getEventType();
            EditText target = null;

            while (XmlPullParser.END_DOCUMENT != eventType) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        System.out.println(parser.getName());
                        if(parser.getName().equals("question") && Integer.valueOf(parser.getAttributeValue(null, "number")) == index){
                            tvQuestion.setText(parser.getAttributeValue(null, "text"));
                            bAnswer1.setText(parser.getAttributeValue(null, "answer1"));
                            bAnswer2.setText(parser.getAttributeValue(null, "answer2"));
                            bAnswer3.setText(parser.getAttributeValue(null, "answer3"));
                            bAnswer4.setText(parser.getAttributeValue(null, "answer4"));

                            right = Integer.valueOf(parser.getAttributeValue(null, "right"));
                            audience = Integer.valueOf(parser.getAttributeValue(null, "audience"));
                            phone = Integer.valueOf(parser.getAttributeValue(null, "phone"));
                            fifty1 = Integer.valueOf(parser.getAttributeValue(null, "fifty1"));
                            fifty2 = Integer.valueOf(parser.getAttributeValue(null, "fifty2"));
                        }
                        break;
                    case XmlPullParser.TEXT:
                        System.out.println(parser.getText());
                        break;
                }
                parser.next();
                eventType = parser.getEventType();
            }
//            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        ;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch(item.getItemId()) {
            case R.id.menu_abanonar:
                Toast.makeText(PlayActivity.this, getString(R.string.menu_abandonar), Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_publico:
                Toast.makeText(PlayActivity.this, getString(R.string.menu_publico), Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_llamada:
                Toast.makeText(PlayActivity.this, getString(R.string.menu_llamada), Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_cincuenta:
                Toast.makeText(PlayActivity.this, getString(R.string.menu_cincuenta), Toast.LENGTH_SHORT).show();
                break;
        }

        if(intent != null)
            startActivity(intent);

        supportInvalidateOptionsMenu();
        return true;
    }

    public List<Question> generateQuestionList() {
        List<Question> list = new ArrayList<Question>();
        Question q = null;

        q = new Question(
                "1",
                "Which is the Sunshine State of the US?",
                "North Carolina",
                "Florida",
                "Texas",
                "Arizona",
                "2",
                "2",
                "2",
                "1",
                "4"
        );
        list.add(q);

        q = new Question(
                "2",
                "Which of these is not a U.S. state?",
                "New Hampshire",
                "Washington",
                "Wyoming",
                "Manitoba",
                "4",
                "4",
                "4",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "3",
                "What is Book 3 in the Pokemon book series?",
                "Charizard",
                "Island of the Giant Pokemon",
                "Attack of the Prehistoric Pokemon",
                "I Choose You!",
                "3",
                "2",
                "3",
                "1",
                "4"
        );
        list.add(q);

        q = new Question(
                "4",
                "Who was forced to sign the Magna Carta?",
                "King John",
                "King Henry VIII",
                "King Richard the Lion-Hearted",
                "King George III",
                "1",
                "3",
                "1",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "5",
                "Which ship was sunk in 1912 on its first voyage, although people said it would never sink?",
                "Monitor",
                "Royal Caribean",
                "Queen Elizabeth",
                "Titanic",
                "4",
                "4",
                "4",
                "1",
                "2"
        );
        list.add(q);

        q = new Question(
                "6",
                "Who was the third James Bond actor in the MGM films? (Do not include &apos;Casino Royale&apos;.)",
                "Roger Moore",
                "Pierce Brosnan",
                "Timothy Dalton",
                "Sean Connery",
                "1",
                "3",
                "3",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "7",
                "Which is the largest toothed whale?",
                "Humpback Whale",
                "Blue Whale",
                "Killer Whale",
                "Sperm Whale",
                "4",
                "2",
                "2",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "8",
                "In what year was George Washington born?",
                "1728",
                "1732",
                "1713",
                "1776",
                "2",
                "2",
                "2",
                "1",
                "4"
        );
        list.add(q);

        q = new Question(
                "9",
                "Which of these rooms is in the second floor of the White House?",
                "Red Room",
                "China Room",
                "State Dining Room",
                "East Room",
                "2",
                "2",
                "2",
                "3",
                "4"
        );
        list.add(q);

        q = new Question(
                "10",
                "Which Pope began his reign in 963?",
                "Innocent III",
                "Leo VIII",
                "Gregory VII",
                "Gregory I",
                "2",
                "1",
                "2",
                "3",
                "4"
        );
        list.add(q);

        q = new Question(
                "11",
                "What is the second longest river in South America?",
                "Parana River",
                "Xingu River",
                "Amazon River",
                "Rio Orinoco",
                "1",
                "1",
                "1",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "12",
                "What Ford replaced the Model T?",
                "Model U",
                "Model A",
                "Edsel",
                "Mustang",
                "2",
                "4",
                "4",
                "1",
                "3"
        );
        list.add(q);

        q = new Question(
                "13",
                "When was the first picture taken?",
                "1860",
                "1793",
                "1912",
                "1826",
                "4",
                "4",
                "4",
                "1",
                "3"
        );
        list.add(q);

        q = new Question(
                "14",
                "Where were the first Winter Olympics held?",
                "St. Moritz, Switzerland",
                "Stockholm, Sweden",
                "Oslo, Norway",
                "Chamonix, France",
                "4",
                "1",
                "4",
                "2",
                "3"
        );
        list.add(q);

        q = new Question(
                "15",
                "Which of these is not the name of a New York tunnel?",
                "Brooklyn-Battery",
                "Lincoln",
                "Queens Midtown",
                "Manhattan",
                "4",
                "4",
                "4",
                "1",
                "3"
        );
        list.add(q);

        return list;
    }
}
