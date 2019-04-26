package vn.five9.data.exception;


public class DatabaseException extends Exception {

    public DatabaseException( String msg ) {
        super( msg );
    }

    public DatabaseException( String msg, Throwable inner ) {
        super( msg, inner );
    }
}
