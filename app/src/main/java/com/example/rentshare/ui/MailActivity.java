package com.example.rentshare.ui;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentshare.R;

import java.lang.ref.WeakReference;
import java.util.Base64;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailActivity extends AppCompatActivity {
    static String clientName;
    static String adOwner;
    static String token;
    static String adName;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        textView= findViewById(R.id.textViewMailReservation);

        Intent intent = getIntent();
        clientName = intent.getExtras().getString("username");
        token = intent.getExtras().getString("token");
        adOwner = intent.getExtras().getString("adowner");
        adName = intent.getExtras().getString("advertname");

        textView.setText("\n\nDank voor uw reservering van: "+adName+ ".\n\nWe hebben u een email gestuurd met de gegevens van de verhuurder: \n"+adOwner+"\n");

        Toast.makeText(this, clientName+" en "+adOwner, Toast.LENGTH_SHORT).show();

        new MailTask(this.getApplication()).execute();

        Toast.makeText(this, "Mail gestuurd", Toast.LENGTH_LONG).show();
    }

    private static class MailTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<Application> reference;

        MailTask(Application context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... params) {

            // Username en password van smtp-server (versturend)
            final String username = "apprentshare@gmail.com";
            byte[] passwordBytes = Base64.getDecoder().decode("cmVudHNoYXJlMTIz");
            final String password = new String(passwordBytes);

            // Ontvanger(s). Bij meerdere ontvangers de adressen scheiden met komma, in dezelfde string.
            final String recipients = clientName+","+adOwner;

            Properties prop = new Properties();
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "465");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.socketFactory.port", "465");
            prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session session = Session.getInstance(prop,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            try {

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("apprentshare@gmail.com"));
                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(recipients)
                );
                message.setSubject("Bevestiging van uw reservering: "+ adName );
                message.setText("Hoi, \n\nDit is een bevestiging van een reservering van "+adName+". \nU kunt contact opnemen met de verhuurder op "+adOwner+".");

                Transport.send(message);

            } catch (Exception e) {
                System.out.println("Mail sturen niet gelukt");
            }
           return null;
        }
    }
}


