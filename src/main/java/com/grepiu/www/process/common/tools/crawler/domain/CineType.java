package com.grepiu.www.process.common.tools.crawler.domain;

public enum CineType {
  LOTTE("lotte"), CGV("cgv"), ALL("all");

  CineType(String code) {
    this.code = code;
  }

  String code;

  public String getCode() {
    return code;
  }
}
