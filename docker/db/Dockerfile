  FROM mariadb:11.3

  # Standard-Env für MariaDB
  ENV MYSQL_ROOT_PASSWORD=rootpass \
      MYSQL_DATABASE=orangehrm \
      MYSQL_USER=orangeuser \
      MYSQL_PASSWORD=orangepass

  # Unser vorbereiteter Dump – wird beim ersten Start automatisch importiert
  COPY seed.sql /docker-entrypoint-initdb.d/01-seed.sql