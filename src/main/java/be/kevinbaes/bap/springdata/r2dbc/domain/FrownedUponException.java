package be.kevinbaes.bap.springdata.r2dbc.domain;

public class FrownedUponException extends Exception {
  public FrownedUponException(String message) {
    super(message);
  }

  public FrownedUponException(String message, Throwable cause) {
    super(message, cause);
  }
}
