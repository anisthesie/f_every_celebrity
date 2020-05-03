package instabot;

import instabot.jobs.BotJob;
import org.apache.log4j.Logger;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class InstaBot {

    public static InstaBot BOT;
    public static Logger LOGGER = Logger.getLogger(InstaBot.class);

    private Instagram4j client;

    public List<String> getNames() {
        return names;
    }

    private List<String> names;


    public InstaBot(Instagram4j client) {
        this.client = client;
        loadNames();
    }

    private void loadNames() {
        this.names = new ArrayList<>();
        final File file = new File("shuffle.txt");
        final Scanner scanner;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();
                this.names.add(line.split(",")[1]);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void init() {


        try {
            final Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            final JobDetail job = newJob(BotJob.class)
                    .withIdentity("main_job", "instabot")
                    .build();

            final Trigger trigger = newTrigger()
                    .withIdentity("main_job", "instabot")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInMinutes(20)
                            .repeatForever())
                    .build();

            scheduler.scheduleJob(job, trigger);

        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    public Instagram4j getClient() {
        return client;
    }
}
