import instabot.InstaBot;
import instabot.jobs.BotJob;
import org.brunocvcunha.instagram4j.Instagram4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static instabot.InstaBot.BOT;

public class Main {


    public static void main(String[] args) throws Exception {

        final Instagram4j instagram4j = Instagram4j.builder()
                .username("USERNAME")
                .password("PASSWORD")
                .build();
        instagram4j.setup();
        try {
            instagram4j.login();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BOT = new InstaBot(instagram4j);
        BOT.init();


    }


}
