package org.n3r.es.exception;

public class EsaoConfigException extends EsaoRuntimeException {

    public EsaoConfigException() {
        super();
    }

    public EsaoConfigException(String s) {
        super(s);
    }

    public EsaoConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public EsaoConfigException(Throwable cause) {
        super(cause);
    }

    private static final long serialVersionUID = -6742913124574974442L;

}
