package com.test.topquizz.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.test.topquizz.R;
import com.test.topquizz.model.Question;
import com.test.topquizz.model.QuestionBank;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mQuestionTextView;
    private Button mReponse1_1;
    private Button mReponse1_2;
    private Button mReponse1_3;
    private Button mReponse1_4;

    private QuestionBank mQuestionBank;
    private Question mQuestionCourante;

    private int mScore;
    private int mNumberOfQuestions;

    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";

    private boolean mEnabledTouchEvents;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("questionCourante", mQuestionCourante);
        outState.putInt("numberQuestion", mNumberOfQuestions);
        outState.putInt("score", mScore);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        System.out.println("GameActivity::onCreate()");

        mQuestionBank = this.generateQuestions();

        mScore = 0;
        mNumberOfQuestions = 4;
        mEnabledTouchEvents = true;

        mQuestionTextView = findViewById(R.id.game_activity_question1);
        mReponse1_1 = findViewById(R.id.game_activity_reponse1_1);
        mReponse1_2 = findViewById(R.id.game_activity_reponse1_2);
        mReponse1_3 = findViewById(R.id.game_activity_reponse1_3);
        mReponse1_4 = findViewById(R.id.game_activity_reponse1_4);

        mReponse1_1.setTag(0);
        mReponse1_2.setTag(1);
        mReponse1_3.setTag(2);
        mReponse1_4.setTag(3);

        mReponse1_1.setOnClickListener(this);
        mReponse1_2.setOnClickListener(this);
        mReponse1_3.setOnClickListener(this);
        mReponse1_4.setOnClickListener(this);

        mQuestionCourante = mQuestionBank.getQuestion();

        if(savedInstanceState != null){
            mQuestionCourante = savedInstanceState.getParcelable("questionCourante");
            mScore = savedInstanceState.getInt("score");
            mNumberOfQuestions = savedInstanceState.getInt("numberQuestion");
        }
        this.displayQuestion(mQuestionCourante);
    }

    @Override
    public void onClick(View v) {
        int reponseIndex = (int) v.getTag();

        if(reponseIndex == mQuestionCourante.getAnswerIndex()){
            Toast.makeText(this, "Bien joué !", Toast.LENGTH_SHORT).show();
            mScore++;
        }else{
            Toast.makeText(this, "Mauvaise réponse..", Toast.LENGTH_SHORT).show();
        }

        mEnabledTouchEvents = false;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mEnabledTouchEvents = true;

                if(--mNumberOfQuestions == 0){
                    endGame();
                }else{
                    mQuestionCourante = mQuestionBank.getQuestion();
                    displayQuestion(mQuestionCourante);
                }
            }
        },2000);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnabledTouchEvents && super.dispatchTouchEvent(ev);
    }

    private void endGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Et voilà, c'est fini !").setMessage("Votre score est " + mScore)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }).create().show();

    }

    private void displayQuestion(final Question question){
        mQuestionTextView.setText(question.getQuestion());
        mReponse1_1.setText(question.getChoiceList().get(0));
        mReponse1_2.setText(question.getChoiceList().get(1));
        mReponse1_3.setText(question.getChoiceList().get(2));
        mReponse1_4.setText(question.getChoiceList().get(3));
    }

    private QuestionBank generateQuestions(){
        Question question1 = new Question(getString(R.string.question1),
                Arrays.asList(getString(R.string.reponse1_1), getString(R.string.reponse1_2), getString(R.string.reponse1_3), getString(R.string.reponse1_4)),
                1);

        Question question2 = new Question("Qu'a de particulier la rue des Degrés à Paris dans le deuxième arrondissement ?",
                Arrays.asList("Elle n'a que des numéros pairs.", "C'est l'une des rues les plus froides de la ville.",
                        "Elle est si étroite qu'un adulte ne peut y pénétrer de face.", "C'est la rue la plus courte de la capitale."),
                3);

        Question question3 = new Question("En 1610, le mathématicien Johannes Kepler est gêné de ne pouvoir apporter un cadeau à son mécène pour le Nouvel An. Il lui offre finalement « presque rien », soit :",
                Arrays.asList("Du vide en bocal.", "Un flocon de neige.", "Un point sur une feuille.", "Un cerveau de moustique."),
                1);

        Question question4 = new Question("Pourquoi chante-t-on que le roi Dagobert a mis sa culotte à l'envers ?",
                Arrays.asList("Il pensait à l'envers et tenait des raisonnements abscons.", "La chanson se moque de Louis XVI mais évoque Dagobert pour échapper à la censure.",
                        "La culotte désignait une armure au Moyen Age, et la chanson fut écrite après la défaite de Dagobert à la bataille de Tertry en 687.", "C'est sa femme, Nanthilde, qui portait la culotte."),
                1);

        Question question5 = new Question("Les camions à pizza sont apparus à Marseille au début des années 1960. Jusqu'à cette date, en Italie, il n'était pas très bien vu de manger des pizzas, car elles :",
                Arrays.asList("Étaient un plat de pauvres.","Étaient décorées avec des motifs grivois.",
                        "Remplaçaient les pâtes.", "Étaient prescrites en cas de dépression."),
                0);

        Question question6 = new Question("En 2009, le groupe nationaliste hindou Rashtriya Swayamsevak Sangh lance un soda à base d'urine de vache pour contrer l'influence de Coca-Cola et Pepsi en Inde. " +
                "Qui d'autre est connu pour boire son urine ?",
                Arrays.asList("Les rois de France.", "Les astronautes.", "Les cyclistes participant au tour de France.", "Les Aborigènes d'Australie."),
                1);

        Question question7 = new Question("La garde suisse pontificale forme la plus petite armée du monde. Pour l'intégrer, il faut :",
                Arrays.asList("Mesurer moins d'1,80 m.", "Être célibataire.", "Être diplômé en histoire de l'art.", "Appartenir à la famille d'un cardinal."),
                1);

        Question question8 = new Question("À la fin du XVIIIe siècle, un automate, dit le Turc mécanique, bat les meilleurs joueurs d'échecs. " +
                "Il s'agit en fait d'un canular. Comment un complice peut-il se cacher dans la machine ?",
                Arrays.asList("C'est un cul-de-jatte.", "C'est un enfant.", "C'est un nain.", "Il est caché par un ensemble d'engrenages sophistiqués."),
                3);

        Question question9 = new Question("Une étude a montré que se regarder les yeux dans les yeux pendant longtemps peut avoir des effets indésirables, en particulier entraîner :",
                Arrays.asList("Une cécité partielle.", "Des maux de tête.", "Des hallucinations.", "Des flatulences."),
                2);

        Question question10 = new Question("En 1953, le psychologue anglais Colin Cherry montre que les personnes concentrées sur une tâche filtrent les stimuli extérieurs mais réagissent toujours à :",
                Arrays.asList("Un mot à connotation sexuelle.", "Un bruit d'accident.", "L'appel de leur nom.", "Leur tube préféré."),
                0);

        return new QuestionBank(Arrays.asList(
                question1,
                question2,
                question3,
                question4,
                question5,
                question6,
                question7,
                question8,
                question9,
                question10));
    }

    protected void onStart() {
        super.onStart();

        System.out.println("GameActivity::onStart()");
    }

    protected void onResume() {
        super.onResume();

        System.out.println("GameActivity::onResume()");
    }

    protected void onPause() {
        super.onPause();

        System.out.println("GameActivity::onPause()");
    }

    protected void onStop() {
        super.onStop();

        System.out.println("GameActivity::onStop()");
    }

    protected void onDestroy() {
        super.onDestroy();

        System.out.println("GameActivity::onDestroy()");
    }
}
