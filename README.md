# BookStore_24162094

Java 17, Jakarta Servlet 6, JSP, Hibernate, and MySQL bookstore application.

## Database configuration

The default database URL and username are configured in `persistence.xml`. Set
`BOOKSTORE_DB_PASSWORD` in the environment of the process that starts Tomcat.
The URL and username can also be overridden with `BOOKSTORE_DB_URL` and
`BOOKSTORE_DB_USER`. The same values may be supplied as Java system properties.

On PowerShell, configure the password for the current session before starting
the server:

```powershell
$env:BOOKSTORE_DB_PASSWORD = '<local database password>'
```

Do not commit local passwords or `.env` files.

## Build and tests

```powershell
mvn test
mvn package
```