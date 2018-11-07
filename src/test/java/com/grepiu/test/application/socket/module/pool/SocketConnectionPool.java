package com.grepiu.test.application.socket.module.pool;

/**
 *
 * 커넥션 Pool 관리
 *
 */
public interface SocketConnectionPool {
  public SocketConnection getConnection();

  public void init();

  public void restart();

  public void destroy();
}
