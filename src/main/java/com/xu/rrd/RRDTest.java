package com.xu.rrd;

import java.io.IOException;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javafx.util.Pair;
import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author CharleyXu Created on 2018/8/19.
 */
public class RRDTest {

  private static final Logger logger = LoggerFactory.getLogger(RRDTest.class);

  public static void main(String[] args) throws InterruptedException, IOException {
    final Random random = new Random();
    long START = System.currentTimeMillis() / 1000;
    RRDUtils rrdObj = new RRDUtils("demo.rrd", START - 1, 2);
    rrdObj.addDataSource("users", DsType.GAUGE, 4, 0, Double.NaN);
    rrdObj.addDataSource("devices", DsType.GAUGE, 4, 0, Double.NaN);
    rrdObj.addArchive(ConsolFun.AVERAGE, 0.5, 1, 1 * 2 * 60 * 60);
    rrdObj.addArchive(ConsolFun.AVERAGE, 0.5, 5, 5 * 2 * 60 * 60);
    rrdObj.build();

    ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(1);
    poolExecutor.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        try {
          rrdObj.rrdUpdate(new Pair<>("users", 1.1 + random.nextInt(1000)),
              new Pair<>("devices", 2.2 + random.nextInt(1000)));
        } catch (IOException e) {
          logger.error(e.getMessage(), e);
        }
      }
    }, 100, 2000, TimeUnit.MILLISECONDS);

    while (true) {
      rrdObj.rrdQuery(ConsolFun.AVERAGE, START, System.currentTimeMillis() / 1000, 2);
      Thread.sleep(2000);
    }
  }
}
