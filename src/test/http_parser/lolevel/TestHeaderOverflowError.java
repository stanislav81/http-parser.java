package http_parser.lolevel;

import java.nio.*;


public class TestHeaderOverflowError {

  public static void test (http_parser.ParserType type) {
    HTTPParser parser = new HTTPParser(type);
    ByteBuffer buf    = getBytes(type);
    
    int numbytes = buf.limit();

    parser.execute(settingsNull(), buf);

    check(numbytes == buf.position());

    buf      = buffer("header-key: header-value\r\n");
    numbytes = buf.limit();
    for (int i = 0; i!= 1000; ++i) {
      parser.execute(settingsNull(), buf);
      check(numbytes == buf.position());

      buf.rewind();

    }
  }

  static ByteBuffer getBytes (http_parser.ParserType type) {
    if (http_parser.ParserType.HTTP_BOTH == type) {
      throw new RuntimeException("only HTTP_REQUEST and HTTP_RESPONSE");
    }

    if (http_parser.ParserType.HTTP_REQUEST == type) {
      return buffer("GET / HTTP/1.1\r\n"); 
    }
    return buffer("HTTP/1.0 200 OK\r\n");
  }

  public static void test () {
    test(http_parser.ParserType.HTTP_REQUEST);
    test(http_parser.ParserType.HTTP_RESPONSE);
  }

  static void check(boolean betterBtrue) {
    if (!betterBtrue) {
      throw new RuntimeException("!");
    }
  }
  static void p(Object o) {
    System.out.println(o);
  }

  static ByteBuffer buffer(String str) {
    return ByteBuffer.wrap(str.getBytes());
  }
  
  static ParserSettings settingsNull () {
    return new ParserSettings();
  }

}
