package instabot.jobs;

import instabot.InstaBot;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUploadPhotoRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static instabot.InstaBot.BOT;

public class BotJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        InstaBot.LOGGER.info("\n=====================");


        if (BOT == null) {
            InstaBot.LOGGER.error("InstaBot.BOT == null");
            return;
        }
        final Instagram4j client = BOT.getClient();
        try {

            InstagramSearchUsernameResult me = client.sendRequest(new InstagramSearchUsernameRequest(client.getUsername()));
            final String[] name = InstaBot.BOT.getNames().get(me.getUser().getMedia_count()).split(" ");

            writeOnImage(name);

            client.sendRequest(new InstagramUploadPhotoRequest(
                    new File("test.jpg"),
                    "Damn, love that guy"));
            System.gc();

        } catch (Exception e) {
            e.printStackTrace();
        }


        InstaBot.LOGGER.info("=====================\n");
    }

    public static String writeOnImage(final String... arr) {

        if (arr.length == 0)
            throw new IllegalArgumentException("Array length 0");

        Stream.of(arr).forEach(System.out::println);

        String s1 = "hey";
        final String s2;
        StringBuilder s3 = new StringBuilder("");

        if (arr.length < 2)
            s2 = arr[0];
        else {
            s2 = arr[1];
            Stream.of(arr)
                    .filter(s -> !s.equals(s1) && !s.equals(s2))
                    .forEach(s -> s3.append(s).append(" "));

        }

        final BufferedImage image;
        try {
            image = ImageIO.read(new File("background.jpg"));

            final Graphics g = image.getGraphics();
            final Font font = Font.createFont(Font.TRUETYPE_FONT, new File("font.ttf")).deriveFont(150f);

            FontMetrics metrics = g.getFontMetrics(font);
            int x1 = (image.getWidth() - metrics.stringWidth(s1)) / 2;
            int y1 = ((image.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent() - (metrics.getHeight());

            int x2 = (image.getWidth() - metrics.stringWidth(s2)) / 2;
            int y2 = ((image.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

            int x3 = (image.getWidth() - metrics.stringWidth(s3.toString())) / 2;
            int y3 = ((image.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent() + (metrics.getHeight());

            g.setFont(font);

            g.setColor(new Color(128, 128, 128, 255));
            g.drawString(s1, x1, y1);
            g.drawString(s3.toString(), x2, y2);
            g.drawString(s2, x3, y3);

            g.setColor(Color.black);
            g.drawString(s1, x1 - 10, y1 - 10);
            g.drawString(s3.toString(), x2 - 10, y2 - 10);
            g.drawString(s2, x3 - 10, y3 - 10);


            final File out = new File("test.jpg");
            Files.deleteIfExists(out.toPath());

            ImageIO.write(image, "jpg", out);

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        return s2 + " " + s3;

    }


}
