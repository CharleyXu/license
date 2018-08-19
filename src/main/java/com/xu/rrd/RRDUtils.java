package com.xu.rrd;

import java.io.IOException;
import java.util.List;
import javafx.util.Pair;
import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.ArcDef;
import org.rrd4j.core.DsDef;
import org.rrd4j.core.FetchData;
import org.rrd4j.core.FetchRequest;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Sample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author CharleyXu Created on 2018/8/19.
 */
public class RRDUtils {

  private String rrdName = "";
  private long rrdStart = 0;
  private long rrdStep = 0;
  private RrdDef rrdDef = null;
  private RrdDb rrdDb = null;
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * 1、定义RRD数据库
   */
  public RRDUtils(String rrdName, long start, long step) {
    this.rrdName = rrdName;
    this.rrdStart = start;
    this.rrdStep = step;
    this.rrdDb = null;
    this.rrdDef = new RrdDef(this.rrdName, this.rrdStart, this.rrdStep);
    this.rrdDef.setVersion(2);
  }

  /**
   * 2、定义RRD数据源
   */
  public void addDataSource(String dsName, DsType dsType, long heartbeat, double minValue,
      double maxValue) {
    if (this.rrdDef == null || dsName == null || dsName.equals("")) {
      return;
    }
    this.rrdDef.addDatasource(dsName, dsType, heartbeat, minValue, maxValue);
  }

  public void addDataSource(DsDef dsDef) {
    if (this.rrdDef == null || dsDef == null) {
      return;
    }
    this.rrdDef.addDatasource(dsDef);
  }

  public void addDataSources(List<DsDef> dsDefList) {
    if (this.rrdDef == null || dsDefList == null) {
      return;
    }
    for (DsDef dsDef : dsDefList) {
      this.rrdDef.addDatasource(dsDef);
    }
  }

  /**
   * 定义RRD存档
   */
  public void addArchive(ConsolFun consolFun, double xff, int steps, int rows) {
    this.rrdDef.addArchive(consolFun, xff, steps, rows);
  }

  public void addArchive(ArcDef arcDef) {
    if (this.rrdDef == null || arcDef == null) {
      return;
    }
    this.rrdDef.addArchive(arcDef);
  }

  public void addArchives(List<ArcDef> arcDefsList) {
    if (this.rrdDef == null || arcDefsList == null) {
      return;
    }
    for (ArcDef arcDef : arcDefsList) {
      this.rrdDef.addArchive(arcDef);
    }
  }

  /**
   * 构建,关闭数据库
   */
  public RRDUtils build() throws IOException {
    this.rrdDb = new RrdDb(this.rrdDef);
    return this;
  }

  public void close() throws IOException {
    this.rrdDb.close();
  }

  /**
   * 插入数据
   */
  public boolean rrdUpdate(Pair<String, Double>... values) throws IOException {
    Sample sample = this.rrdDb.createSample();
    long time = System.currentTimeMillis() / 1000;
    sample.setTime(time);
    for (Pair<String, Double> val : values) {
      sample.setValue(val.getKey(), val.getValue());
    }
    sample.update();

    return true;
  }

  public void rrdUpdate(String dsName, double value) throws IOException {
    Sample sample = this.rrdDb.createSample();
    long time = System.currentTimeMillis() / 1000;
    sample.setTime(time);
    sample.setValue(dsName, value);
    sample.update();
  }

  /**
   * 查询
   */
  public FetchData rrdQuery(ConsolFun fun, long start, long end, long resolution)
      throws IOException {
    FetchRequest request = this.rrdDb.createFetchRequest(fun, start, end, resolution);
    FetchData fetchData = request.fetchData();
    logger.info("== Data fetched. " + fetchData.getRowCount() + " points obtained");
    logger.info(fetchData.toString());
    return fetchData;
  }

  /**
   * 导出
   */
  public void exportToXml(String xmlPath) throws IOException {
    rrdDb.exportXml(xmlPath);
  }

  /**
   * 恢复
   */
  public static RrdDb restoreFromXml(String restoredPath, String xmlPath) throws IOException {
    RrdDb rrdDb = null;
    rrdDb = new RrdDb(restoredPath, xmlPath);
    return rrdDb;
  }

}
