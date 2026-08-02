package hr.algebra.humanitarnaorganizacija.exception;

public class AppException extends RuntimeException {
    public AppException(String message) {
        super(message);
    }
    public AppException(String message, Throwable cause) { super(message, cause); }
    /*
    Throwable je parent svih excepitona - super ih proslijeđuje ropditeljstkoj klasi RuntimeExceptioon
    * */
}
