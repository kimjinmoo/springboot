package com.grepiu.www.process.sample.util.socket.module.model;

import com.grepiu.www.process.sample.util.socket.module.domain.WatchGradeBody;
import com.grepiu.www.process.sample.util.socket.module.domain.WatchGradeVO;
import com.grepiu.www.process.sample.util.socket.module.pool.SocketHelper;
import org.assertj.core.util.Lists;

import java.util.List;

/**
 * 관람 등급
 */
public class WatchGrade<T1 extends WatchGradeBody, T2 extends  WatchGradeVO> extends SejongSocket<T1, T2> {

  public WatchGrade(String code) {
    super(code);
  }

  @Override
  public void send(T1 data) throws Exception {
    StringBuilder sb = new StringBuilder();
    String sample = sb.append(header)
        .append(data.getCost())
        .append(data.getType())
        .toString();
    this.response = SocketHelper.sendDataStream(sample.getBytes("KSC5601"));
  }

  @Override
  public List<T2> response(Class<T2> type) throws Exception {
    // 헤더 Set
    this.header = response.substring(0, response.indexOf(delimiter) + 1);
    // 데이터 Set
    String d = response.substring(header.length(), response.length()) + delimiter;

    this.data = Lists.newArrayList();

    int pos = 0, end, index = 1, route = 3;
    T2 j = type.getDeclaredConstructor().newInstance();

    while ((end = d.indexOf(delimiter, pos)) >= 0) {
      switch (index) {
        case 1:
          j = type.getDeclaredConstructor().newInstance();
          j.setCode(d.substring(pos, end));
          break;
        case 2:
          j.setName(d.substring(pos, end));
          break;
        case 3:
          j.setCost(d.substring(pos, end));
          this.data.add(j);
          index = 0;
          break;
      }
      index++;
      pos = end + 1;
    }
    return this.data;
  }
}
