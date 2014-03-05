package org.n3r.es.exception;

public class EsaoRuntimeException extends RuntimeException {

    public EsaoRuntimeException() {
        super();
    }

    public EsaoRuntimeException(String s) {
        super(s);
    }

    public EsaoRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public EsaoRuntimeException(Throwable cause) {
        super(cause);
    }

    private static final long serialVersionUID = 7071186278594213056L;

}
